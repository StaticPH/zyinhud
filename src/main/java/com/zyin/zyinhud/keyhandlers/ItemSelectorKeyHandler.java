package com.zyin.zyinhud.keyhandlers;

//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
import com.zyin.zyinhud.modules.ItemSelector;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;

public class ItemSelectorKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String hotkeyDescription = "key.zyinhud.itemselector";

	public static void onMouseWheelScroll(MouseScrollEvent event) { //should event be .Post?
		if (!mc.mouseHelper.isMouseGrabbed() || !ItemSelector.isEnabled) { return; }
        ItemSelector.scroll(event.getScrollDelta() > 0 ? ItemSelector.WHEEL_UP : ItemSelector.WHEEL_DOWN);
		event.setCanceled(true);
	}

	public static void onMouseSideButton(MouseInputEvent event) { //should event be .Post?
		if (!mc.mouseHelper.isMouseGrabbed() || !ItemSelector.isEnabled || !ItemSelector.shouldUseMouseSideButtons()) {
			return;
		}
		int direction = event.getButton() == 3 ? ItemSelector.WHEEL_UP : ItemSelector.WHEEL_DOWN;

		ItemSelector.sideButton(direction);
		event.setCanceled(true);
	}

	public static void onPressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI or has a modifier pressed
		if (mc.currentScreen == null) { ItemSelector.onHotkeyPressed(); }
	}

	public static void onReleased(KeyInputEvent event) {
		ItemSelector.onHotkeyReleased();
	}
}