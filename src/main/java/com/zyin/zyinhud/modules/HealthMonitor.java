package com.zyin.zyinhud.modules;

import java.util.Timer;
import java.util.TimerTask;

import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.HealthMonitorOptions;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.TickEvent.ClientTickEvent;

import com.zyin.zyinhud.ZyinHUDSound;

import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Plays a warning sound when the player is low on health.
 */
public class HealthMonitor extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled = ZyinHUDConfig.enableHealthMonitor.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableHealthMonitor.set(!isEnabled);
		ZyinHUDConfig.enableHealthMonitor.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * The current mode for this module
	 */
	public static HealthMonitorOptions.HealthMonitorModes mode = ZyinHUDConfig.healthMonitorMode.get();

	private static Timer timer = new Timer();

	private static int lowHealthSoundThreshold = ZyinHUDConfig.lowHealthSoundThreshold.get();
	private static float volume = ZyinHUDConfig.healthMonitorVolume.get().floatValue();
	public static boolean playFasterNearDeath = ZyinHUDConfig.playFasterNearDeath.get();

	private static boolean isPlayingLowHealthSound = false;
	private static final int repeatDelay = 1000;

	public static final HealthMonitor instance = new HealthMonitor();

	/**
	 * Instantiates a new Health monitor.
	 */
	public HealthMonitor() {}

	/**
	 * We use a ClientTickEvent instead of a LivingHurtEvent because a LivingHurtEvent will only
	 * fire in single player, whereas a ClientTickEvent fires in both single and multi player.
	 * PlayerTickEvent ticks for every player rendered.
	 * WorldTickEvent doesn't work on servers.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void onClientTickEvent(ClientTickEvent event) {
		//only play the sound if it's not playing already
		if (HealthMonitor.isEnabled && !isPlayingLowHealthSound) {
			playLowHealthSoundIfHurt();
		}
	}


	/**
	 * Checks to see if the player has less health than the set threshold, and will play a
	 * warning sound on a 1 second loop until they heal up.
	 */
	protected static void playLowHealthSoundIfHurt() {
		//Don't play any sounds or do anything with any timers while in creative mode
		if (HealthMonitor.isEnabled && mc.player != null && !mc.playerController.isInCreativeMode()) {
			int playerHealth = (int) mc.player.getHealth();
			if (playerHealth < lowHealthSoundThreshold && playerHealth > 0) {
				//don't play the sound if the user is looking at a screen
				if (!mc.isGamePaused() && mc.isGameFocused()) {
					playLowHealthSound();
				}

				isPlayingLowHealthSound = true;

				int soundDelay = repeatDelay;

				if (playFasterNearDeath) {
					soundDelay = repeatDelay / 2 + (int) ((float) repeatDelay / 2 * ((float) playerHealth / (float) lowHealthSoundThreshold));
				}

				TimerTask t = new PlayLowHealthSoundTimerTask();
				timer.schedule(t, soundDelay);

				return;
			}
		}

		isPlayingLowHealthSound = false;
	}

	/**
	 * Plays the low health warning sound right now.
	 */
	public static void playLowHealthSound() {
		ZyinHUDSound.play(mode.getSoundName(), volume);
	}

	private static class PlayLowHealthSoundTimerTask extends TimerTask {
		/**
		 * Instantiates a new Play low health sound timer task.
		 */
		PlayLowHealthSoundTimerTask() {}

		@Override
		public void run() {
			playLowHealthSoundIfHurt();
		}
	}

	/**
	 * Set low health sound threshold.
	 *
	 * @param newThreshold the low health sound threshold
	 */
	public static void setLowHealthSoundThreshold(int newThreshold) {
		lowHealthSoundThreshold = MathHelper.clamp(lowHealthSoundThreshold, 1, 20);
	}

	/**
	 * Get low health sound threshold int.
	 *
	 * @return the int
	 */
	public static int getLowHealthSoundThreshold() {
		return lowHealthSoundThreshold;
	}

	/**
	 * Set volume.
	 *
	 * @param volume the volume
	 */
	public static void setVolume(float volume) {
		HealthMonitor.volume = MathHelper.clamp(volume, 0, 1);
	}

	/**
	 * Get volume float.
	 *
	 * @return the float
	 */
	public static float getVolume() {
		return volume;
	}


	/**
	 * Toggles making the sound play quicker when close to dieing
	 *
	 * @return boolean
	 */
	public static boolean togglePlayFasterNearDeath() {
		return playFasterNearDeath = !playFasterNearDeath;
	}
}
