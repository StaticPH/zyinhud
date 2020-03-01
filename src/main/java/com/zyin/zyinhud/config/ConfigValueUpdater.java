package com.zyin.zyinhud.config;

import com.zyin.zyinhud.compat.GeneralCompat;
import com.zyin.zyinhud.modules.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ConfigValueUpdater {
	private static final Logger logger = LogManager.getLogger(ConfigValueUpdater.class);
	private static boolean doPropagateUpdates = false;

	@SubscribeEvent
	public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
		if (!doPropagateUpdates){
			logger.fatal("Zyin's HUD config just got changed on the file system! Skipping reload");
			doPropagateUpdates = true;
			return;
		}
		logger.warn("Zyin's HUD config was just updated. Propagating changes.");

		GeneralCompat.ItemLike.loadFromConfig();

		AnimalInfo.loadFromConfig();
		Clock.loadFromConfig();
		Compass.loadFromConfig();
		Coordinates.loadFromConfig();
		DistanceMeasurer.loadFromConfig();
		DurabilityInfo.loadFromConfig();
//		EatingAid.loadFromConfig();
		EnderPearlAid.loadFromConfig();
		Fps.loadFromConfig();
		HealthMonitor.loadFromConfig();
		InfoLine.loadFromConfig();
		ItemSelector.loadFromConfig();
		Miscellaneous.loadFromConfig();
		PlayerLocator.loadFromConfig();
		PotionAid.loadFromConfig();
//		PotionTimers.loadFromConfig();
		QuickDeposit.loadFromConfig();
		SafeOverlay.loadFromConfig();
		TorchAid.loadFromConfig();
		WeaponSwapper.loadFromConfig();
	}
}
