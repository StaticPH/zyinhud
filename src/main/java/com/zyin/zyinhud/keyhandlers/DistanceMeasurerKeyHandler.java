package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.modules.DistanceMeasurer;

import com.zyin.zyinhud.modules.ZyinHUDModuleModes;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.DistanceMeasurerOptions.DistanceMeasurerModes;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class DistanceMeasurerKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.distancemeasurer";

	public static void Pressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI
		if (mc.currentScreen == null && DistanceMeasurer.Enabled) {
			DistanceMeasurer.Mode = (DistanceMeasurerModes) DistanceMeasurer.Mode.next();
			ZyinHUDSound.PlayButtonPress();
		}
	}
}