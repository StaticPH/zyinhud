package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.modules.PlayerLocator;

import com.zyin.zyinhud.modules.ZyinHUDModuleModes;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.LocatorOptions.LocatorModes;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class PlayerLocatorKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.playerlocator";

	public static void Pressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI
		if (mc.currentScreen == null && PlayerLocator.Enabled) {
			PlayerLocator.Mode = (LocatorModes) PlayerLocator.Mode.next();
			ZyinHUDSound.PlayButtonPress();
		}
	}
}