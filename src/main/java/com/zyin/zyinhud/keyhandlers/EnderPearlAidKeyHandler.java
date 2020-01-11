package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.mods.EnderPearlAid;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class EnderPearlAidKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.enderpearlaid";

	public static void Pressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI
		if (mc.currentScreen == null && EnderPearlAid.Enabled) { EnderPearlAid.UseEnderPearl(); }
	}
}