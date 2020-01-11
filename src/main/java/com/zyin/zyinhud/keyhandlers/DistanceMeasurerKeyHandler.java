package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.mods.DistanceMeasurer;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class DistanceMeasurerKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.distancemeasurer";

	public static void Pressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI
		if (mc.currentScreen == null && DistanceMeasurer.Enabled) {
			DistanceMeasurer.Modes.ToggleMode();
			ZyinHUDSound.PlayButtonPress();
		}
	}
}