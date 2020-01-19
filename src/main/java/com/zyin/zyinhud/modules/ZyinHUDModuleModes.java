package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.util.IAdvancingEnum;
import com.zyin.zyinhud.util.Localization;

//TODO: give this a more appropriate name
//TODO: ?add setter methods for all modes so that the config code can control them without this class importing from it?
public class ZyinHUDModuleModes {
	//module imports from this class and the config class
	//  where possible, sets its internal default values according to their equivalent config
	//  otherwise, fall back to the copy declared here
	//config imports from this class, NEVER MODULES
	//this class NEVER imports from modules or the config class

	//TODO: ToggleEnabled FOR EVERYTHING
	//Can probably replace enums that are simply ON and OFF with regular booleans

	public static class AnimalInfoOptions {
		public static final boolean defaultEnabled = true;
		public static AnimalInfoModes animalInfoMode;   //TODO:default?
		/* How far away (in blocks) will the overlay by visible from by default*/
		public static final int defaultViewDistanceCutoff = 8;
		/* Do you REALLY want a bunch of nonsense in your face when you're in the same block space?
		 * I sure wouldn't; minimum increased from 0->1 */
		public static final int minViewDistanceCutoff = 1;
		/* Honestly, I think 120 is already a bit ridiculous, but we'll see */
		public static final int maxViewDistanceCutoff = 120;

		/* The default number of decimal places to include when displaying animal stats */
		public static final int defautNumberOfDecimalsDisplayed = 2;
		public static final int minDecimalsDisplayed = 0;
		/* The max COUlD be 20, but why would you need such precision? Just 6 is already overkill. */
		public static final int maxDecimalsDisplayed = 6;

		public static final boolean defaultShowBreedingIcons = true;
		//public static final boolean defaultShowBreedingTimers;
		public static final boolean defaultShowHorseStatsOnF3Menu = true;
		public static final boolean defaultShowHorseStatsOverlay = true;
		public static final boolean defaultShowTextBackgrounds = true;

		/**
		 * The enum for the different modes available to the AnimalInfo module
		 */
		public static enum AnimalInfoModes implements IAdvancingEnum {
			OFF("safeoverlay.mode.0"),
			ON("safeoverlay.mode.1");

			private String unfriendlyName;

			private AnimalInfoModes(String unfriendlyName) {
				this.unfriendlyName = unfriendlyName;
			}

			/**
			 * Gets the mode based on its internal name as written in the enum declaration
			 *
			 * @param modeName the mode name
			 * @return modes
			 */
			public static AnimalInfoModes GetMode(String modeName) {
				try { return AnimalInfoModes.valueOf(modeName); }
				catch (IllegalArgumentException e) { return values()[0]; }
			}

			/**
			 * Get friendly name string.
			 *
			 * @return the string
			 */
			public String GetFriendlyName() {
				return Localization.get(unfriendlyName);
			}
		}
	}

	public static class ClockOptions {
		public static final boolean defaultEnabled = true;
		public static ClockModes clockMode;

		/**
		 * The enum for the different modes available to the Clock module
		 */
		public static enum ClockModes implements IAdvancingEnum {
			STANDARD("clock.mode.standard"),
			COUNTDOWN("clock.mode.countdown"),
			GRAPHIC("clock.mode.graphic");

			private String unfriendlyName;

			//NOTE: In all likelihood, the next() and prev() methods will likely be called by a gui button, if at all

			private ClockModes(String unfriendlyName) {
				this.unfriendlyName = unfriendlyName;
			}

			/**
			 * Gets the mode based on its internal name as written in the enum declaration
			 *
			 * @param modeName the mode name
			 * @return modes
			 */
			public static ClockModes GetMode(String modeName) {
				try { return ClockModes.valueOf(modeName); }
				catch (IllegalArgumentException e) { return STANDARD; }
			}

			/**
			 * Get friendly name string.
			 *
			 * @return the string
			 */
			public String GetFriendlyName() {
				return Localization.get(unfriendlyName);
			}
		}
	}

	public static class CompassOptions{
		public static final boolean defaultEnabled = true;
	}

	public static class CoordinateOptions {
		public static final boolean defaultEnabled = true;
		public static CoordinateModes coordinateMode;
		/**
		 * The default chat format String which replaces "{x}", "{y}", and "{z}" with coordinates
		 */
		public static final String defaultChatStringFormat = "[{x}, {y}, {z}]";
		public static final boolean defaultShowChunkCoordinates = false;
		/**
		 * Use colors to show what ores spawn at the elevation level
		 */
		public static final boolean defaultUseYCoordinateColors = true;

		/**
		 * The enum for the different modes available to the Coordinates module
		 */
		public static enum CoordinateModes implements IAdvancingEnum {
			XZY("coordinates.mode.xzy"),
			XYZ("coordinates.mode.xyz");

			private String unfriendlyName;

			//NOTE: In all likelihood, the next() and prev() methods will likely be called by a gui button, if at all

			private CoordinateModes(String unfriendlyName) {
				this.unfriendlyName = unfriendlyName;
			}

			/**
			 * Gets the mode based on its internal name as written in the enum declaration
			 *
			 * @param modeName the mode name
			 * @return modes
			 */
			public static CoordinateModes GetMode(String modeName) {
				try {return CoordinateModes.valueOf(modeName);}
				catch (IllegalArgumentException e) {return XZY;}
			}

			/**
			 * Get friendly name string.
			 *
			 * @return the string
			 */
			public String GetFriendlyName() {
				return Localization.get(unfriendlyName);
			}
		}
	}

	public static class DistanceMeasurerOptions {
		public static final boolean defaultEnabled = true;
		public static DistanceMeasurerModes distanceMeasurerMode;

		/**
		 * The enum for the different modes available to the DistanceMeasurer module
		 */
		public static enum DistanceMeasurerModes implements IAdvancingEnum {
			OFF("distancemeasurer.mode.off"),
			SIMPLE("distancemeasurer.mode.simple"),
			COORDINATE("distancemeasurer.mode.complex");

			private String unfriendlyName;

			private DistanceMeasurerModes(String unfriendlyName) {
				this.unfriendlyName = unfriendlyName;
			}

			/**
			 * Gets the mode based on its internal name as written in the enum declaration
			 *
			 * @param modeName the mode name
			 * @return modes
			 */
			public static DistanceMeasurerModes GetMode(String modeName) {
				try { return DistanceMeasurerModes.valueOf(modeName); }
				catch (IllegalArgumentException e) { return OFF; }
			}

			/**
			 * Get friendly name string.
			 *
			 * @return the string
			 */
			public String GetFriendlyName() {
				return Localization.get(unfriendlyName);
			}
		}
	}

	public static class DurabilityInfoOptions {
		public static final boolean defaultEnabled = true;
		public static DurabilityInfoTextModes durabilityInfoTextMode;

		public static final boolean defaultShowArmorDurability = true;
		public static final boolean defaultShowItemDurability = true;
		public static final boolean defaultShowIndividualArmorIcons = true;
		public static final boolean defaultAutoUnequipArmor = false;
		public static final boolean defaultAutoUnequipTools = false;
		public static final boolean defaultUseColoredNumbers = true;

		//the height/width of the tools being rendered;
		// NOTE: being hardcoded to 16, this may do weird things with larger textures
		public static final int toolIconWidth = 16; // 16 horizontal
		public static final int toolIconHeight = 16; // 16 vertical

		//where the armor icon is rendered
		public static final int defaultArmorIconPosX = 30;
		public static final int defaultArmorIconPosY = 20;
		public static final int minArmorPosXY = 0;
		public static final int maxArmorIconPosX = 400;
		public static final int maxArmorIconPosY = 200;

		public static final double defaultDurabilityDisplayThreshold = 0.1d;
		public static final double minDurabilityDisplayThreshold = 0.0d;
		public static final double maxDurabilityDisplayThreshold = 1.0d;

		public static final double defaultDurabilityIconScale = 1.0d;
		public static final double minIconScale = 0.25d;
		public static final double maxIconScale = 3.0d;

		/**
		 * The enum for the different modes available to the DurabilityInfo module
		 */
		public static enum DurabilityInfoTextModes implements IAdvancingEnum {
			NONE("durabilityinfo.textmode.none"),
			TEXT("durabilityinfo.textmode.text"),
			PERCENTAGE("durabilityinfo.textmode.percentage");

			private String unfriendlyName;

			//NOTE: In all likelihood, the next() and prev() methods will likely be called by a gui button, if at all

			private DurabilityInfoTextModes(String unfriendlyName) {
				this.unfriendlyName = unfriendlyName;
			}

			/**
			 * Gets the mode based on its internal name as written in the enum declaration
			 *
			 * @param modeName the mode name
			 * @return text modes
			 */
			public static DurabilityInfoTextModes GetMode(String modeName) {
				try {return DurabilityInfoTextModes.valueOf(modeName);}
				catch (IllegalArgumentException e) {return values()[1];}
			}

			/**
			 * Get friendly name string.
			 *
			 * @return the string
			 */
			public String GetFriendlyName() {
				return Localization.get(unfriendlyName);
			}
		}
	}

	//TODO
//	public static class EatingAidOptions {
//		public static final boolean defaultEnabled = true;
//	}

	public static class EnderPearlAidOptions{
		public static final boolean defaultEnabled = true;
	}

	public static class FpsOptions{
		public static final boolean defaultEnabled = false;
	}

	public static class HealthMonitorOptions {
		public static final boolean defaultEnabled = false;
		//		public static HealthMonitorModes healthMonitorMode;
		public static final boolean defaultPlayFasterNearDeath = false;
		public static final int defaultSoundVolume = 1;
		public static final int minSoundVolume = 0;
		public static final int maxSoundVolume = 1;
		public static final int defaultLowHealthSoundThreshold = 4;
		public static final int minLowHealthSoundThreshold = 1;
		public static final int maxLowHealthSoundThreshold = 1023;

		/**
		 * The enum for the different modes available to the HealthMonitor module
		 */
		public static enum HealthMonitorModes {
			OOT("healthmonitor.mode.oot", "lowhealth_oot"),
			LTTP("healthmonitor.mode.lttp", "lowhealth_lttp"),
			ORACLE("healthmonitor.mode.oracle", "lowhealth_oracle"),
			LA("healthmonitor.mode.la", "lowhealth_la"),
			LOZ("healthmonitor.mode.loz", "lowhealth_loz"),
			AOL("healthmonitor.mode.aol", "lowhealth_aol");

			private String unfriendlyName;
			public String soundName;

			//NOTE: In all likelihood, the next() and prev() methods will likely be called by a gui button, if at all

			private HealthMonitorModes(String unfriendlyName, String soundName) {
				this.unfriendlyName = unfriendlyName;
				this.soundName = soundName;
			}

			/**
			 * Gets the mode based on its internal name as written in the enum declaration
			 *
			 * @param modeName the mode name
			 * @return modes
			 */
			public static HealthMonitorModes GetMode(String modeName) {
				try { return HealthMonitorModes.valueOf(modeName); }
				catch (IllegalArgumentException e) { return OOT; }
			}

			/**
			 * Get friendly name string.
			 *
			 * @return the string
			 */
			public String GetFriendlyName() {
				return Localization.get(unfriendlyName);
			}
		}
	}

	public static class InfoLineOptions {
		public static final boolean defaultEnabled = true;
		public static final boolean defaultShowBiome = false;
		public static final boolean defaultShowCanSnow = false;
		public static final boolean defaultShowPing = false;
		public static final int defaultLeftOffset = 1;
		public static final int minLeftOffset = 1;
		public static final int maxLeftOffset = 400;
		public static final int defaultTopOffset = 1;
		public static final int minTopOffset = 1;
		public static final int maxTopOffset = 200;
	}

	public static class ItemSelectorOptions {
		public static final boolean defaultEnabled = true;
		public static ItemSelectorModes itemSelectorMode; //Uses?

		public static final boolean defaultUseMouseSideButtons = false;
		public static final int defaultTimeout = 200;
		public static final int minTimeout = 50;
		public static final int maxTimeout = 500;

		/**
		 * The enum for the different modes available to the ItemSelector module
		 */
		public static enum ItemSelectorModes implements IAdvancingEnum {
			ALL("itemselector.mode.all"),
			SAME_COLUMN("itemselector.mode.column");

			private String unfriendlyName;

			//NOTE: In all likelihood, the next() and prev() methods will likely be called by a gui button, if at all

			private ItemSelectorModes(String unfriendlyName) {
				this.unfriendlyName = unfriendlyName;
			}

			/**
			 * Gets the mode based on its internal name as written in the enum declaration
			 *
			 * @param modeName the mode name
			 * @return modes
			 */
			public static ItemSelectorModes GetMode(String modeName) {
				try { return ItemSelectorModes.valueOf(modeName); }
				catch (IllegalArgumentException e) { return values()[0]; }
			}

			/**
			 * Get friendly name string.
			 *
			 * @return the string
			 */
			public String GetFriendlyName() {
				return Localization.get(unfriendlyName);
			}
		}
	}

	public static class LocatorOptions {
		public static final boolean defaultEnabled = true;
		public static LocatorModes locatorMode;

		public static final int minViewDistanceCutoff = 0;
		public static final int maxViewDistanceCutoff = 130;    //realistic max distance the game will render entities: up to ~115 blocks away
		public static final int defaultViewDistanceCutoff = 0;
		public static final boolean defaultShowDistanceToPlayers = false;
		public static final boolean defaultShowPlayerHealth = false;
		public static final boolean defaultShowWitherSkeletons = false;
		public static final boolean defaultShowWolves = true;
		public static final boolean defaultUseWolfColors = true;

		/**
		 * The enum for the different modes available to the PlayerLocator module
		 */
		public static enum LocatorModes implements IAdvancingEnum {
			OFF("playerlocator.mode.off"),
			ON("playerlocator.mode.on");

			private String unfriendlyName;

			private LocatorModes(String unfriendlyName) {
				this.unfriendlyName = unfriendlyName;
			}

			/**
			 * Gets the mode based on its internal name as written in the enum declaration
			 *
			 * @param modeName the mode name
			 * @return modes
			 */
			public static LocatorModes GetMode(String modeName) {
				try { return LocatorModes.valueOf(modeName);}
				catch (IllegalArgumentException e) { return OFF;}
			}

			/**
			 * Get friendly name string.
			 *
			 * @return the string
			 */
			public String GetFriendlyName() {
				return Localization.get(unfriendlyName);
			}
		}
	}

	public static class MiscOptions{
		public static final boolean ShowAnvilRepairs = true;
		public static final boolean UseQuickPlaceSign = false;
		public static final boolean UseUnlimitedSprintingSP = false;
	}

	public static class PotionAidOptions{
		public static final boolean defaultEnabled = true;
	}

	//TODO
//	public static class PotionTimerOptions{
//		public static final boolean defaultEnabled = true;
//	}

	public static class QuickDepositOptions {
		public static final boolean defaultEnabled = true;
		public static final boolean BlacklistArrow = false;
		public static final boolean BlacklistEnderPearl = false;
		public static final boolean BlacklistFood = false;
		public static final boolean BlacklistTools = true;
		public static final boolean BlacklistTorch = false;
		public static final boolean BlacklistWaterBucket = false;
		public static final boolean BlacklistWeapons = true;
		public static final boolean BlacklistClockCompass = false;
		public static final boolean CloseChestAfterDepositing = false;
		public static final boolean IgnoreItemsInHotbar = false;
	}

	public static class SafeOverlayOptions {
		public static final boolean defaultEnabled = true;
		public static SafeOverlayModes safeOverlayMode;

		public static final boolean defaultDisplayInNether = false;
		public static final int defaultDrawDistance = 20;
		public static final int minDrawDistance = 2;    //can't go lower than 2. setting this to 1 displays nothing
		public static final int maxDrawDistance = 175;    //175 is the edge of the visible map on far
		public static final float defaultUnsafeOverlayTransparency = 0.3f;
		public static final float minUnsafeOverlayTransparency = 0.11f;
		public static final float maxUnsafeOverlayTransparency = 1f;
		public static final boolean defaultRenderThroughWalls = false;

		/**
		 * The enum for the different modes available to the SafeOverlay module
		 */
		public static enum SafeOverlayModes implements IAdvancingEnum {
			OFF("safeoverlay.mode.off"),
			ON("safeoverlay.mode.on");

			private String unfriendlyName;

			private SafeOverlayModes(String unfriendlyName) {
				this.unfriendlyName = unfriendlyName;
			}

			/**
			 * Gets the mode based on its internal name as written in the enum declaration
			 *
			 * @param modeName the mode name
			 * @return modes
			 */
			public static SafeOverlayModes GetMode(String modeName) {
				try { return SafeOverlayModes.valueOf(modeName); }
				catch (IllegalArgumentException e) { return OFF; }
			}

			/**
			 * Get friendly name string.
			 *
			 * @return the string
			 */
			public String GetFriendlyName() {
				return Localization.get(unfriendlyName);
			}
		}
	}

	public static class TorchAidOptions{
		public static final boolean defaultEnabled = false;
	}

	public static class WeaponSwapOptions{
		public static final boolean defaultEnabled = true;
	}
}