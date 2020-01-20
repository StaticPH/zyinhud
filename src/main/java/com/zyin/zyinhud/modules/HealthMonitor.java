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
	public static boolean Enabled = ZyinHUDConfig.EnableHealthMonitor.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		ZyinHUDConfig.EnableHealthMonitor.set(!Enabled);
		ZyinHUDConfig.EnableHealthMonitor.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return Enabled = !Enabled;
	}

	/**
	 * The current mode for this module
	 */
	public static HealthMonitorOptions.HealthMonitorModes Mode = ZyinHUDConfig.HealthMonitorMode.get();

	private static Timer timer = new Timer();

	private static int LowHealthSoundThreshold = ZyinHUDConfig.LowHealthSoundThreshold.get();
	private static float Volume = ZyinHUDConfig.HealthMonitorVolume.get().floatValue();
	public static boolean PlayFasterNearDeath = ZyinHUDConfig.PlayFasterNearDeath.get();

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
	public void ClientTickEvent(ClientTickEvent event) {
		//only play the sound if it's not playing already
		if (HealthMonitor.Enabled && !isPlayingLowHealthSound) {
			PlayLowHealthSoundIfHurt();
		}
	}


	/**
	 * Checks to see if the player has less health than the set threshold, and will play a
	 * warning sound on a 1 second loop until they heal up.
	 */
	protected static void PlayLowHealthSoundIfHurt() {
		//Don't play any sounds or do anything with any timers while in creative mode
		if (HealthMonitor.Enabled && mc.player != null && !mc.playerController.isInCreativeMode()) {
			int playerHealth = (int) mc.player.getHealth();
			if (playerHealth < LowHealthSoundThreshold && playerHealth > 0) {
				//don't play the sound if the user is looking at a screen
				if (!mc.isGamePaused() && mc.isGameFocused()) {
					PlayLowHealthSound();
				}

				isPlayingLowHealthSound = true;

				int soundDelay = repeatDelay;

				if (PlayFasterNearDeath) {
					soundDelay = repeatDelay / 2 + (int) ((float) repeatDelay / 2 * ((float) playerHealth / (float) LowHealthSoundThreshold));
				}

				TimerTask t = new PlayLowHealthSoundTimerTask();
				timer.schedule(t, soundDelay);

				return;
			}
		}

		isPlayingLowHealthSound = false;
	}


	/**
	 * Gets the name of the sound resource associated with the current mode.
	 * Sound resouce names are declared in assets/zyinhud/sounds.json.
	 *
	 * @return string
	 */
	protected static String GetSoundNameFromMode() {
		return Mode.soundName;
	}

	/**
	 * Plays the low health warning sound right now.
	 */
	public static void PlayLowHealthSound() {
		ZyinHUDSound.play(GetSoundNameFromMode(), Volume);
	}

	private static class PlayLowHealthSoundTimerTask extends TimerTask {
		/**
		 * Instantiates a new Play low health sound timer task.
		 */
		PlayLowHealthSoundTimerTask() {}

		@Override
		public void run() {
			PlayLowHealthSoundIfHurt();
		}
	}

	/**
	 * Set low health sound threshold.
	 *
	 * @param lowHealthSoundThreshold the low health sound threshold
	 */
	public static void SetLowHealthSoundThreshold(int lowHealthSoundThreshold) {
		LowHealthSoundThreshold = MathHelper.clamp(lowHealthSoundThreshold, 1, 20);
	}

	/**
	 * Get low health sound threshold int.
	 *
	 * @return the int
	 */
	public static int GetLowHealthSoundThreshold() {
		return LowHealthSoundThreshold;
	}

	/**
	 * Set volume.
	 *
	 * @param volume the volume
	 */
	public static void SetVolume(float volume) {
		Volume = MathHelper.clamp(volume, 0, 1);
	}

	/**
	 * Get volume float.
	 *
	 * @return the float
	 */
	public static float GetVolume() {
		return Volume;
	}


	/**
	 * Toggles making the sound play quicker when close to dieing
	 *
	 * @return boolean
	 */
	public static boolean TogglePlayFasterNearDeath() {
		return PlayFasterNearDeath = !PlayFasterNearDeath;
	}
}
