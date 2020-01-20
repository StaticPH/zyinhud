package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.util.Localization;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

//TODO: consider merging into InfoLine
public class Fps extends ZyinHUDModuleBase {

	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled = ZyinHUDConfig.EnableFPS.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		ZyinHUDConfig.EnableFPS.set(!Enabled);
		ZyinHUDConfig.EnableFPS.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
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
