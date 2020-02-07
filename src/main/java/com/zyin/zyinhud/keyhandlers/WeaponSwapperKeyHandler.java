package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.modules.WeaponSwapper;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class WeaponSwapperKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String hotkeyDescription = "key.zyinhud.weaponswapper";

	public static void onPressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI
		if (mc.currentScreen == null && WeaponSwapper.isEnabled) { WeaponSwapper.swapWeapons(); }
	}
}