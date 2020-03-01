package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.config.ZyinHUDConfig;
import com.zyin.zyinhud.util.Localization;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

//TODO: consider merging into InfoLine
public class Fps extends ZyinHUDModuleBase {

	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled;

	// The assignment here seems unnecessary, but surely there was a reason at one point
	public static String currentFps = "0";

	static { loadFromConfig(); }

	public static void loadFromConfig() {
		isEnabled = ZyinHUDConfig.enableFPS.get();
	}

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableFPS.set(!isEnabled);
		ZyinHUDConfig.enableFPS.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * Calculate message for info line string.
	 *
	 * @return the string
	 */
	public static String calculateMessageForInfoLine() {
		if (Fps.isEnabled) {
			currentFps = String.valueOf(Minecraft.getDebugFPS());
			return TextFormatting.WHITE + currentFps + ' ' + Localization.get("fps.infoline");
		}
		else { return ""; }
	}
}
