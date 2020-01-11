package com.zyin.zyinhud.mods;

import com.zyin.zyinhud.util.Localization;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

public class Fps extends ZyinHUDModBase {

	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled;

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		return Enabled = !Enabled;
	}

	public static String currentFps = "0";

	/**
	 * Calculate message for info line string.
	 *
	 * @return the string
	 */
	public static String CalculateMessageForInfoLine() {
		if (Fps.Enabled) {
			currentFps = String.valueOf(Minecraft.getDebugFPS());
			return TextFormatting.WHITE + currentFps + ' ' + Localization.get("fps.infoline");
		}
		else { return ""; }
	}
}
