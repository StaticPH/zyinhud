package com.zyin.zyinhud.keyhandlers;

import net.minecraftforge.client.event.InputEvent.MouseInputEvent;

//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
import com.zyin.zyinhud.mods.ItemSelector;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

/**
 * The type Item selector key handler.
 */
public class ItemSelectorKeyHandler implements ZyinHUDKeyHandlerBase
{
    /**
     * The constant HotkeyDescription.
     */
    public static final String HotkeyDescription = "key.zyinhud.itemselector";

    /**
     * On mouse wheel scroll.
     *
     * @param event the event
     */
    public static void OnMouseWheelScroll(MouseInputEvent event)
    {
        if (!mc.mouseHelper.isMouseGrabbed() || !ItemSelector.Enabled)
            return;
//        TODO: Mouse wheel event
//        ItemSelector.Scroll(event.getDwheel() > 0 ? ItemSelector.WHEEL_UP : ItemSelector.WHEEL_DOWN);
        event.setCanceled(true);
    }

    /**
     * On mouse side button.
     *
     * @param event the event
     */
    public static void OnMouseSideButton(MouseInputEvent event)
    {
        if (!mc.mouseHelper.isMouseGrabbed() || !ItemSelector.Enabled || !ItemSelector.UseMouseSideButtons)
            return;

        int direction = event.getButton() == 3 ? ItemSelector.WHEEL_UP : ItemSelector.WHEEL_DOWN;

        ItemSelector.SideButton(direction);
        event.setCanceled(true);
    }

    /**
     * Pressed.
     *
     * @param event the event
     */
    public static void Pressed(KeyInputEvent event) {
        if (mc.currentScreen != null)
        {
            return;    //don't activate if the user is looking at a GUI or has a modifier pressed
        }
        
		ItemSelector.OnHotkeyPressed();
	}

    /**
     * Released.
     *
     * @param event the event
     */
    public static void Released(KeyInputEvent event) {
        ItemSelector.OnHotkeyReleased();
	}
}