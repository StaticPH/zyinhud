package com.zyin.zyinhud.keyhandlers;

import net.minecraftforge.client.event.InputEvent.MouseInputEvent;

//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
import com.zyin.zyinhud.modules.ItemSelector;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class ItemSelectorKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.itemselector";

	public static void OnMouseWheelScroll(MouseInputEvent event) {
		if (!mc.mouseHelper.isMouseGrabbed() || !ItemSelector.Enabled) { return; }
//        TODO: Mouse wheel event
//        ItemSelector.Scroll(event.getDwheel() > 0 ? ItemSelector.WHEEL_UP : ItemSelector.WHEEL_DOWN);
		event.setCanceled(true);
	}

	public static void OnMouseSideButton(MouseInputEvent event) {
		if (!mc.mouseHelper.isMouseGrabbed() || !ItemSelector.Enabled || !ItemSelector.shouldUseMouseSideButtons()) {
			return;
		}

		int direction = event.getButton() == 3 ? ItemSelector.WHEEL_UP : ItemSelector.WHEEL_DOWN;

		ItemSelector.SideButton(direction);
		event.setCanceled(true);
	}

	public static void Pressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI or has a modifier pressed
		if (mc.currentScreen == null) { ItemSelector.OnHotkeyPressed(); }
	}

	public static void Released(KeyInputEvent event) {
		ItemSelector.OnHotkeyReleased();
	}
}