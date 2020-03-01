package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.config.ZyinHUDConfig;
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
	public static boolean isEnabled;

	/**
	 * The current mode for this module
	 */
	public static ClockOptions.ClockModes mode;

	//TODO: consider adding config option for using 24-hour time instead of 12-hour

	private static final long mobSpawningStartTime = 13187;//in ticks

	//mobs stop spawning at: 22813
	//mobs start to burn at: 23600
	private static final long mobSpawningStopTime = 23460;

	//mc.world.isDaytime() always returns true on client, so it cant be used to determine bedtime
	private static final long bedTime = 12540;

	static { loadFromConfig(); }

	public static void loadFromConfig() {
		isEnabled = ZyinHUDConfig.enableClock.get();
		mode = ZyinHUDConfig.clockMode.get();
	}

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
	 * Calculates time
	 *
	 * @param infoLineMessageUpToThisPoint the info line message up to this point
	 * @return time if the Clock is enabled, otherwise "".
	 */
	public static String calculateMessageForInfoLine(String infoLineMessageUpToThisPoint) {
		if (Clock.isEnabled) {
			if (Clock.mode == ClockOptions.ClockModes.STANDARD) {
				return getTimeOfDay();
			}
			else if (Clock.mode == ClockOptions.ClockModes.COUNTDOWN) {
				long time = mc.player.world.getDayTime() % 24000; // Does not necessarily match the sun/moon positions
				if (isNight(time)) {
					//night time
					long secondsTillDay = (mobSpawningStopTime - time) / 20;
					long minutes = secondsTillDay / 60;
					long seconds = secondsTillDay - minutes * 60;

					return getColorForTime(time) + String.format("%02d:%02d", minutes, seconds);
				}
				else {
					//day time
					long secondsTillNight = time > mobSpawningStopTime
					                        ? (24000 - time + mobSpawningStartTime) / 20
					                        : (mobSpawningStartTime - time) / 20;
					long minutes = secondsTillNight / 60;
					long seconds = secondsTillNight - minutes * 60;

					return getColorForTime(time) + String.format("%02d:%02d", minutes, seconds);
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

	private static long getTimeFromGameTime() {
		return (mc.player.world.getGameTime()) % 24000;
	}

	private static int getCurrentMinute(final long time) {
		return (int) (time % 1000) * 60 / 1000;
	}

	private static String get12HTime(final long time) {
		final int hour = (time >= 6000 && time < 7000) ? 12 : ((int) (time / 1000 + 6) % 24 % 12);
		return String.format("%02d:%02d", hour, getCurrentMinute(time));
	}

	private static String get24HTime(final long time) {
		final int hour = (time >= 6000 && time < 7000) ? 12 : ((int) (time / 1000 + 6) % 24);
		return String.format("%02d:%02d", hour, getCurrentMinute(time));
	}

	/**
	 * @return true if it is currently night in-game, false otherwise
	 */
	public static boolean isNight() {
		long time = (mc.player.world.getDayTime()) % 24000;
		return time >= mobSpawningStartTime && time < mobSpawningStopTime;
	}

	public static boolean isNight(final long time) {
		return time >= mobSpawningStartTime && time < mobSpawningStopTime;
	}

	private static TextFormatting getColorForTime(final long time) {
		return isNight(time) ? TextFormatting.GRAY :
		       time < bedTime ? TextFormatting.YELLOW : TextFormatting.GOLD;
	}

	private static String getTimeOfDay() {
		/*
		 Unlike World.getGameTime(), the return value of World.getDayTime() does not change if
		 the doDaylightCycle GameRule is false AND it's per dimension
			time 24000 or 0 is ~6am
			day is 1000
			noon is 6000
			night is 13000
			midnight is 18000
		*/
		long now = mc.player.world.getDayTime();
		return getColorForTime(now) + get12HTime(now);
	}
}
