package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.modules.AnimalInfo;

import com.zyin.zyinhud.modules.ZyinHUDModuleModes.AnimalInfoOptions.AnimalInfoModes;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class AnimalInfoKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.animalinfo";

	public static void Pressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI
		if (mc.currentScreen == null && AnimalInfo.Enabled) {
			AnimalInfo.Mode = (AnimalInfoModes) AnimalInfo.Mode.next();
			ZyinHUDSound.PlayButtonPress();
		}
	}
}