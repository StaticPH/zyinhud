package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.modules.EnderPearlAid;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class EnderPearlAidKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String hotkeyDescription = "key.zyinhud.enderpearlaid";

	public static void onPressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI
		if (mc.currentScreen == null && EnderPearlAid.isEnabled) { EnderPearlAid.useEnderPearl(); }
	}
}