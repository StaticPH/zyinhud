package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.modules.PotionAid;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class PotionAidKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.potionaid";

	public static void Pressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI
		if (mc.currentScreen == null && PotionAid.Enabled) { PotionAid.instance.Drink(); }
	}
}