package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.ClockOptions;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

/**
 * Calculates time.
 *
 * @See {@link "http://www.minecraftwiki.net/wiki/Day-night_cycle"}
 */
public class Clock extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(Clock.class);

	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled = ZyinHUDConfig.enableClock.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableClock.set(!isEnabled);
		ZyinHUDConfig.enableClock.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * The current mode for this module
	 */
	public static ClockOptions.ClockModes mode = ZyinHUDConfig.clockMode.get();

	private static final long mobSpawningStartTime = 13187;

	//mobs stop spawning at: 22813
	//mobs start to burn at: 23600
	private static final long mobSpawningStopTime = 23600;

	//mc.world.isDaytime() always returns true on client, so it cant be used to determine bedtime
	private static final long bedTime = 12540;

	//TODO: consider adjusting time display to be constant if mc.gameSettings.doDaylightCycle == false

	/**
	 * Calculates time
	 *
	 * @param infoLineMessageUpToThisPoint the info line message up to this point
	 * @return time if the Clock is enabled, otherwise "".
	 */
	public static String calculateMessageForInfoLine(String infoLineMessageUpToThisPoint) {
		if (Clock.isEnabled) {
			if (Clock.mode == ClockOptions.ClockModes.STANDARD) {
				long time = (mc.world.getGameTime()) % 24000;

				//0 game time is 6am, so add 6000
				long hours = (time + 6000) / 1000;
				if (hours >= 24) { hours = hours - 24; }
				long seconds = (long) (((time + 6000) % 1000) * (60.0 / 1000.0));

				if (isNight()) {
					//night time
					return TextFormatting.GRAY + String.format("%02d:%02d", hours, seconds);
				}
				else {
					//day time
					return (time < bedTime ? TextFormatting.YELLOW : TextFormatting.GOLD) +
					       String.format("%02d:%02d", hours, seconds);
				}
			}
			else if (Clock.mode == ClockOptions.ClockModes.COUNTDOWN) {
				long time = (mc.world.getGameTime()) % 24000;

				if (isNight()) {
					//night time
					long secondsTillDay = (mobSpawningStopTime - time) / 20;
					long minutes = secondsTillDay / 60;
					long seconds = secondsTillDay - minutes * 60;

					return TextFormatting.GRAY + String.format("%02d:%02d", minutes, seconds);
				}
				else {
					//day time
					long secondsTillNight = time > mobSpawningStopTime
					                        ? (24000 - time + mobSpawningStartTime) / 20
					                        : (mobSpawningStartTime - time) / 20;
					long minutes = secondsTillNight / 60;
					long seconds = secondsTillNight - minutes * 60;

					return (time < bedTime ? TextFormatting.YELLOW : TextFormatting.GOLD) +
					       String.format("%02d:%02d", minutes, seconds);
				}
			}
			else if (Clock.mode == ClockOptions.ClockModes.GRAPHIC) {
				int infoLineWidth = mc.fontRenderer.getStringWidth(infoLineMessageUpToThisPoint);

				itemRenderer.getValue().renderItemAndEffectIntoGUI(
					new ItemStack(Items.CLOCK),
					infoLineWidth + InfoLine.getHorizontalLocation(),
					InfoLine.getVerticalLocation()
				);

				//this is needed because the RenderItem.renderItem() methods enable lighting
				GL11.glDisable(GL11.GL_LIGHTING);

				return "    ";    //about the length of the clock graphic
			}
		}

		return "";
	}

	/**
	 * @return true if it is currently night in-game, false otherwise
	 */
	public static boolean isNight() {
		long time = (mc.world.getGameTime()) % 24000;
		return time >= mobSpawningStartTime && time < mobSpawningStopTime;
	}
}
