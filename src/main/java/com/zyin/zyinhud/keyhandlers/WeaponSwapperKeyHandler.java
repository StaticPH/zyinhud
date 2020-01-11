package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.mods.WeaponSwapper;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class WeaponSwapperKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.weaponswapper";

	public static void Pressed(KeyInputEvent event) {
		//don't activate if the user is looking at a GUI
		if (mc.currentScreen == null && WeaponSwapper.Enabled) { WeaponSwapper.SwapWeapons(); }
	}
}