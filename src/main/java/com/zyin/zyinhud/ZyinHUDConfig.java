package com.zyin.zyinhud;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;

import static net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import static net.minecraftforge.common.ForgeConfigSpec.Builder;
import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import static net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import static net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import static net.minecraftforge.common.ForgeConfigSpec.IntValue;

/**
 * This class is responsible for interacting with the configuration file.
 */
public class ZyinHUDConfig {

	private static ForgeConfigSpec SPEC; // Will hold the built config when done
	public static Builder BUILDER;
	public static ForgeConfigSpec SPEC_OVERRIDE;
	public static Builder BUILDER_OVERRIDE;

	public static final String CATEGORY_ANIMALINFO = "animalinfo";
	public static final String CATEGORY_CLOCK = "clock";
	public static final String CATEGORY_COMPASS = "compass";
	public static final String CATEGORY_COORDINATES = "coordinates";
	public static final String CATEGORY_DISTANCEMEASURER = "distancemeasurer";
	public static final String CATEGORY_DURABILITYINFO = "durabilityinfo";
	//    public static final String CATEGORY_EATINGAID = "eatingaid";
	public static final String CATEGORY_ENDERPEARLAID = "enderpearlaid";
	public static final String CATEGORY_FPS = "fps";
	public static final String CATEGORY_HEALTHMONITOR = "healthmonitor";
	public static final String CATEGORY_INFOLINE = "infoline";
	public static final String CATEGORY_ITEMSELECTOR = "itemselector";
	public static final String CATEGORY_MISCELLANEOUS = "miscellaneous";
	public static final String CATEGORY_PLAYERLOCATOR = "playerlocator";
	public static final String CATEGORY_POTIONAID = "potionaid";
	//    public static final String CATEGORY_POTIONTIMERS = "potiontimers";
	public static final String CATEGORY_QUICKDEPOSIT = "quickdeposit";
	public static final String CATEGORY_SAFEOVERLAY = "safeoverlay";
	public static final String CATEGORY_TORCHAID = "torchaid";
	public static final String CATEGORY_WEAPONSWAP = "weaponswap";

	// ######################################################################
	// Animal(Mainly Horse) Info Module
	// ######################################################################
	// Enable/Disable Animal Info
	public static BooleanValue EnableAnimalInfo;
	// How far away animal info will be rendered on the screen (distance measured in blocks). Range [1, 64]
	public static IntValue AnimalInfoMaxViewDistance;
	// Sets the Animal Info mode. Valid modes are ON and OFF
	public static EnumValue<AnimalInfoOptions.AnimalInfoModes> AnimalInfoMode;
	// How many decimal places will be used when displaying horse stats
	public static IntValue AnimalInfoNumberOfDecimalsDisplayed;
	// Enable/Disable showing an icon if the animal is ready to breed
	public static BooleanValue ShowBreedingIcons;
	// Enable/Disable showing the stats of the horse you're riding on the F3 screen
	public static BooleanValue ShowHorseStatsOnF3Menu;
	// Enable/Disable showing the stats of horses on screen
	public static BooleanValue ShowHorseStatsOverlay;
	// Enable/Disable showing a black background behind text
	public static BooleanValue ShowTextBackgrounds;

	// ######################################################################
	// Clock Module
	// ######################################################################
	// Enable/Disable showing the clock
	public static BooleanValue EnableClock;
	// Sets the clock mode. Valid modes are STANDARD, COUNTDOWN, and GRAPHIC
	public static EnumValue<ClockOptions.ClockModes> ClockMode;

	// ######################################################################
	//Compass Module
	// ######################################################################
	// Enable/Disable showing the compass
	public static BooleanValue EnableCompass;

	// ######################################################################
	// Coordinates Module
	// ######################################################################
	// Enable/Disable showing your coordinates
	public static BooleanValue EnableCoordinates;
	// The format used when sending your coordinates in a chat message by pressing the keybind
	// {x}{y}{z} are replaced with actual coordinates
	public static ConfigValue<String> CoordinatesChatStringFormat;
	// Sets the coordinates mode. Valid modes are XYZ and XZY
	public static EnumValue<CoordinateOptions.CoordinateModes> CoordinatesMode;
	// Shows how far into the 16x16 chunk you're in
	public static BooleanValue ShowChunkCoordinates;
	// Color code the Y (height) coordinate based on what ores can spawn at that level
	public static BooleanValue UseYCoordinateColors;

	// ######################################################################
	// Distance Measurer Module
	// ######################################################################
	// Enable/Disable the distance measurer
	public static BooleanValue EnableDistanceMeasurer;
	// Sets the Distance Measurer mode. Valid modes are OFF, SIMPLE, and COORDINATE
	public static EnumValue<DistanceMeasurerOptions.DistanceMeasurerModes> DistanceMeasurerMode;

	// ######################################################################
	// Durability Info Module
	// ######################################################################
	// Enable/Disable showing all durability info
	public static BooleanValue EnableDurabilityInfo;
	// Enable/Disable automatically unequipping armor before it breaks.
	public static BooleanValue AutoUnequipArmor;
	// Enable/Disable automatically unequipping tools before they breaks
	public static BooleanValue AutoUnequipTools;
	// Display when armor gets damaged less than this fraction of its durability
	public static DoubleValue DurabilityDisplayThresholdForArmor;
	// Display when an item gets damaged less than this fraction of its durability
	public static DoubleValue DurabilityDisplayThresholdForItem;
	// Sets Durability Info's number display mode. Valid modes are NONE, TEXT, and PERCENTAGE
	public static EnumValue<DurabilityInfoOptions.DurabilityInfoTextModes> DurabilityInfoTextMode;
	// The horizontal position of the durability icons. 0 is left, 400 is far right
	public static IntValue DurabilityLocationHorizontal;
	// The vertical position of the durability icons. 0 is top, 200 is very bottom
	public static IntValue DurabilityLocationVertical;
	// How large the durability icons are rendered, 1.0 being the normal size
	public static DoubleValue DurabilityScale;
	// Enable/Disable showing breaking armor
	public static BooleanValue ShowArmorDurability;
	// Enable/Disable showing armor peices instead of the big broken armor icon
	public static BooleanValue ShowIndividualArmorIcons;
	// Enable/Disable showing breaking items
	public static BooleanValue ShowItemDurability;
	// Toggle using colored numbering
	public static BooleanValue UseColoredNumbers;

	//HideDurabilityInfoInChat, boolean, default true

	// ######################################################################
	// Eating Aid Module
	// ######################################################################
	// Enables pressing the eating aid keybind to eat food even if it is  in your inventory and not your hotbar
//	public static BooleanValue EnableEatingAid;
	// Enable/Disable using golden apples and golden carrots as food
//	public static BooleanValue EatGoldenFood;
	// Enable/Disable eating raw chicken, beef, and porkchops
//	public static BooleanValue EatRawFood;
	// Sets the Eating Aid mode. Valid modes are BASIC and INTELLIGENT
//	public static EnumValue<EatingAid.DistanceMeasurerModes> EatingAidMode;
	// Use food that is in your hotbar before looking for food in your main inventory
//	public static BooleanValue PrioritizeFoodInHotbar;
	// If you are connected to a Bukkit server that uses PvP Soup or Fast Soup (mushroom stew) with this enabled,
	// Eating Aid will use it instead of other foods
//	public static BooleanValue UsePvPSoup;

	// ######################################################################
	// Ender Pearl Aid Module
	// ######################################################################
	// Enables pressing the Ender Pearl Aid hotkey to use an ender pearl even
	// if it is in your main inventory and not your hotbar
	public static BooleanValue EnableEnderPearlAid;

	// ######################################################################
	// FPS Module
	// ######################################################################
	// Enable/Disable showing your FPS at the end of the Info Line
	public static BooleanValue EnableFPS;

	// ######################################################################
	// Health Monitor Module
	// ######################################################################
	// Enable/Disable using the Health Monitor.
	public static BooleanValue EnableHealthMonitor;
	// Sets the Health Monitor sound mode. Valid modes are OOT, LTTP, ORACLE, LA, LOZ, and AOL
	public static EnumValue<HealthMonitorOptions.HealthMonitorModes> HealthMonitorMode;
	// Set the volume of the beeps. Range [0.0, 1.0]
	public static DoubleValue HealthMonitorVolume;
	// A sound will start playing when you have less than this much health left
	public static IntValue LowHealthSoundThreshold;
	// Play the warning sounds quicker the closer you get to dieing
	public static BooleanValue PlayFasterNearDeath;

	// ######################################################################
	// Info Line Module
	// ######################################################################
	// Enable/Disable the entire info line in the top left part of the screen.
	// This includes the clock, coordinates, compass, mod status, etc
	public static BooleanValue EnableInfoLine;
	// The horizontal position of the info line. 1 is left, 400 is far right
	public static IntValue InfoLineLocationHorizontal;
	// The vertical position of the info line. 1 is top, 200 is very bottom
	public static IntValue InfoLineLocationVertical;
	// Enable/Disable showing what biome you are in on the Info Line
	public static BooleanValue ShowBiome;
	// Enable/Disable showing if it can snow at the player's feet on the Info Line
	public static BooleanValue ShowCanSnow;
	// Enable/Disable showing the player's ping while on a multiplayer server
	public static BooleanValue ShowPing;

	// ######################################################################
	// Item Selector Module
	// ######################################################################
	// Enables/Disable using mouse wheel scrolling whilst holding the 
	// Item Selector hotkey to swap the selected item with an inventory item
	public static BooleanValue EnableItemSelector;
	// Sets the Item Selector mode. Valid modes are ALL and SAME_COLUMN
	public static EnumValue<ItemSelectorOptions.ItemSelectorModes> ItemSelectorMode;
	// Enable/disable use of side buttons for item selection
	public static BooleanValue ItemSelectorSideButtons;
	// Specifies how many ticks until the item selector confirms your choice and performs the item swap
	public static IntValue ItemSelectorTimeout;

	// ######################################################################
	// Miscellaneous Module
	// ######################################################################
	// Enable/Disable showing the repair count on items while using the anvil
	public static BooleanValue ShowAnvilRepairs;
	// Enable/Disable being able to place a sign with no text by sneaking while placing a sign
	public static BooleanValue UseQuickPlaceSign;
	// Enable/Disable overriding the default sprint behavior and run forever
	public static BooleanValue UseUnlimitedSprintingSP;

	// ######################################################################
	// Player Locator Module
	// ######################################################################
	// Enable/Disable the Player Locator.
	public static BooleanValue EnablePlayerLocator;
	// Stop showing player names when they are this close (distance measured in blocks).
	public static IntValue PlayerLocatorMinViewDistance;
	// Sets the Player Locator mode. Valid modes are OFF and ON
	public static EnumValue<LocatorOptions.LocatorModes> PlayerLocatorMode;
	// Show how far away you are from the other players next to their name.
	public static BooleanValue ShowDistanceToPlayers;
	// Show how much health players have by their name.
	public static BooleanValue ShowPlayerHealth;
	// Show wither skeletons in addition to other players.
	public static BooleanValue ShowWitherSkeletons;
	// Show your tamed wolves in addition to other players.
	public static BooleanValue ShowWolves;
	// Use the color of your wolf's collar to colorize their name.
	public static BooleanValue UseWolfColors;

	// ######################################################################
	// Potion Aid Module
	// ######################################################################
	// Enables pressing potion aid keybind to drink a potion even if it is in 
	// your main inventory and not your hotbar.
	public static BooleanValue EnablePotionAid;

	// ######################################################################
	// Potion Timers Module
	// ######################################################################
	// Enable/Disable showing the time remaining on potions.
//    public static BooleanValue EnablePotionTimers;
	// Sets Potion Timer's text display mode. Valid modes are WHITE, COLORED, and NONE
//    public static EnumValue<PotionTimers.PotionTimerTextModes> PotionTimerTextMode;
	// Enable/Disable hiding the default potion effects when you open your inventory.
//    public static BooleanValue HidePotionEffectsInInventory;
	// How large the potion timers are rendered, 1.0 being the normal size. Range [0.25, 5.0]
//    public static DoubleValue PotionScale;
	// The horizontal position of the potion timers. 0 is left, 400 is far right.
//    public static IntValue PotionTimersLocationHorizontal;
	// The vertical position of the potion timers. 0 is top, 200 is very bottom.
//    public static IntValue PotionTimersLocationVertical;
	// Enable/Disable showing the status effect of potions next to the timers.
//    public static BooleanValue ShowPotionIcons;

	//HideBeaconPotionEffects, boolean, default false
	//ShowVanillaStatusEffectHUD, boolean, default true
	//ShowEffectName, boolean, default true
	//ShowEffectLevel, boolean, default true

	// ######################################################################
	// Quick Deposit Module
	// ######################################################################
	// Enables Quick Deposit.
	public static BooleanValue EnableQuickDeposit;
	// Stop Quick Deposit from putting arrows in chests?
	public static BooleanValue BlacklistArrow;
	// Stop Quick Deposit from putting clocks and compasses in chests?
	public static BooleanValue BlacklistClockCompass;
	// Stop Quick Deposit from putting ender pearls in chests?
	public static BooleanValue BlacklistEnderPearl;
	// Stop Quick Deposit from putting food in chests?
	public static BooleanValue BlacklistFood;
	// Stop Quick Deposit from putting tools (picks, axes, shovels, shears) in chests?
	public static BooleanValue BlacklistTools;
	// Stop Quick Deposit from putting torches in chests?
	public static BooleanValue BlacklistTorch;
	// Stop Quick Deposit from putting water buckets in chests?
	public static BooleanValue BlacklistWaterBucket;
	// Stop Quick Deposit from putting swords and bows in chests?
	public static BooleanValue BlacklistWeapons;
	// Closes the chest GUI after you deposit your items in it. 
	// Allows quick and easy depositing of all your items into multiple chests.
	public static BooleanValue CloseChestAfterDepositing;
	// Determines if items in your hotbar will be deposited into chests when 
	// the Quick Deposit hotkey is pressed.
	public static BooleanValue IgnoreItemsInHotbar;

	// ######################################################################
	// Safe Overlay Module
	// ######################################################################
	// Enable/Disable the Safe Overlay.
	public static BooleanValue EnableSafeOverlay;
	// Enable/Disable showing unsafe areas in the Nether.
	public static BooleanValue SafeOverlayDisplayInNether;
	// How far away unsafe spots should be rendered around the player measured in blocks. 
	// This can be changed in game with - + <hotkey> and + + <hotkey>.
	public static IntValue SafeOverlayDrawDistance;
	// Sets the Safe Overlay mode. Valid modes are OFF and On
	public static EnumValue<SafeOverlayOptions.SafeOverlayModes> SafeOverlayMode;
	// Enable/Disable showing unsafe areas through walls. Toggle in game with Ctrl + <hotkey>.
	public static BooleanValue SafeOverlaySeeThroughWalls;
	// The transparency of the unsafe marks. Range [0.11, 1.0].
	public static DoubleValue SafeOverlayTransparency;

	// ######################################################################
	// Torch Aid Module
	// ######################################################################
	// Enable/Disable using Torch Aid to help you place torches more easily.
	public static BooleanValue EnableTorchAid;

	// ######################################################################
	// Weapon Swap Module
	// ######################################################################
	// Enables pressing your weapon-swap keybind to swap between your sword and bow.
	public static BooleanValue EnableWeaponSwap;


	static {
		BUILDER = new Builder();
		BUILDER_OVERRIDE = new Builder();
		configure(BUILDER, BUILDER_OVERRIDE);
		SPEC = BUILDER.build();
		SPEC_OVERRIDE = BUILDER_OVERRIDE.build();
	}

	//TODO: custom implementation of some .define* methods that allow for placement of a comment
	// after ranges/accepted values, but before the configuration option itself.

	private static void configure(Builder builder, Builder builder_override) {
		// ######################################################################
		// Animal(Mainly Horse) Info Module
		// ######################################################################
		builder.comment(
			"Animal Info gives you information about horse(also mule, donkey, and llama) stats, such as speed and jump height."
		).push(CATEGORY_ANIMALINFO);
		{
			EnableAnimalInfo = builder
				.comment("Enable/Disable Animal Info")
				.define("EnableAnimalInfo", AnimalInfoOptions.defaultEnabled);
			AnimalInfoMaxViewDistance = builder
				.comment("How far away animal info will be rendered on the screen (distance measured in blocks).")
				.defineInRange(
					"AnimalInfoMaxViewDistance", AnimalInfoOptions.defaultViewDistanceCutoff,
					AnimalInfoOptions.minViewDistanceCutoff, AnimalInfoOptions.maxViewDistanceCutoff
				);
			AnimalInfoMode = builder
				.comment("Sets the Animal Info mode.")
				.defineEnum("AnimalInfoMode", AnimalInfoOptions.AnimalInfoModes.OFF, EnumGetMethod.NAME_IGNORECASE);
			AnimalInfoNumberOfDecimalsDisplayed = builder
				.comment("How many decimal places will be used when displaying horse stats.")
				.defineInRange(
					"AnimalInfoNumberOfDecimalsDisplayed", AnimalInfoOptions.defautNumberOfDecimalsDisplayed,
					AnimalInfoOptions.minDecimalsDisplayed, AnimalInfoOptions.maxDecimalsDisplayed
				);
			ShowBreedingIcons = builder
				.comment("Enable/Disable showing an icon if the animal is ready to breed")
				.define("ShowBreedingIcons", AnimalInfoOptions.defaultShowBreedingIcons);
			ShowHorseStatsOnF3Menu = builder
				.comment("Enable/Disable showing the stats of the horse you're riding on the F3 screen")
				.define("ShowHorseStatsOnF3Menu", AnimalInfoOptions.defaultShowHorseStatsOnF3Menu);
			ShowHorseStatsOverlay = builder
				.comment("Enable/Disable showing the stats of horses on screen")
				.define("ShowHorseStatsOverlay", AnimalInfoOptions.defaultShowHorseStatsOverlay);
			ShowTextBackgrounds = builder
				.comment("Enable/Disable showing a black background behind text")
				.define("ShowTextBackgrounds", AnimalInfoOptions.defaultShowTextBackgrounds);
		}
		builder.pop();

		// ######################################################################
		// Clock Module
		// ######################################################################
		builder.comment(
			"Clock shows you time relevant to Minecraft time."
		).push(CATEGORY_CLOCK);
		{
			EnableClock = builder
				.comment("Enable/Disable showing the clock")
				.define("EnableClock", ClockOptions.defaultEnabled);
			ClockMode = builder
				.comment("Sets the clock mode.")
				.defineEnum("ClockMode", ClockOptions.ClockModes.STANDARD, EnumGetMethod.NAME_IGNORECASE);
		}
		builder.pop();

		// ######################################################################
		//Compass Module
		// ######################################################################
		builder.comment(
			"Compass displays a text compass."
		).push(CATEGORY_COMPASS);
		{
			EnableCompass = builder
				.comment("Enable/Disable showing the compass")
				.define("EnableCompass", CompassOptions.defaultEnabled);
		}
		builder.pop();

		// ######################################################################
		// Coordinates Module
		// ######################################################################
		builder.comment(
			"Coordinates displays your coordinates. Nuff said."
		).push(CATEGORY_COORDINATES);
		{
			EnableCoordinates = builder
				.comment("Enable/Disable showing your coordinates")
				.define("EnableCoordinates", CoordinateOptions.defaultEnabled);
			CoordinatesChatStringFormat = builder
				.comment(
					"The format used when sending your coordinates in a chat message by pressing the " +
					CATEGORY_COORDINATES + " keybind (default: 'F4')."
				)
				.comment("{x}{y}{z} are replaced with actual coordinates")
				.define("CoordinatesChatStringFormat", CoordinateOptions.defaultChatStringFormat);
			CoordinatesMode = builder
				.comment("Sets the coordinates mode.")
				.defineEnum("CoordinatesMode", CoordinateOptions.CoordinateModes.XYZ, EnumGetMethod.NAME_IGNORECASE);
			ShowChunkCoordinates = builder
				.comment("Shows how far into the 16x16 chunk you're in")
				.define("ShowChunkCoordinates", CoordinateOptions.defaultShowChunkCoordinates);
			UseYCoordinateColors = builder
				.comment("Color code the Y (height) coordinate based on what ores can spawn at that level")
				.define("UseYCoordinateColors", CoordinateOptions.defaultUseYCoordinateColors);
		}
		builder.pop();

		// ######################################################################
		// Distance Measurer Module
		// ######################################################################
		builder.comment(
			"Distance Measurer can calculate distances between you and blocks that you aim at."
		).push(CATEGORY_DISTANCEMEASURER);
		{
			EnableDistanceMeasurer = builder
				.comment("Enable/Disable the distance measurer")
				.define("EnableDistanceMeasurer", DistanceMeasurerOptions.defaultEnabled);
			DistanceMeasurerMode = builder
				.comment("Sets the Distance Measurer mode.")
				.defineEnum(
					"DistanceMeasurerMode",
					DistanceMeasurerOptions.DistanceMeasurerModes.OFF,
					EnumGetMethod.NAME_IGNORECASE
				);
		}
		builder.pop();

		// ######################################################################
		// Durability Info Module
		// ######################################################################
		builder.comment(
			"Durability Info will display your breaking armor and equipment."
		).push(CATEGORY_DURABILITYINFO);
		{
			EnableDurabilityInfo = builder
				.comment("Enable/Disable showing all durability info")
				.define("EnableDurabilityInfo", DurabilityInfoOptions.defaultEnabled);
			AutoUnequipArmor = builder
				.comment("Enable/Disable automatically unequipping armor before it breaks")
				.define("AutoUneqipArmor", DurabilityInfoOptions.defaultAutoUnequipArmor);
			AutoUnequipTools = builder
				.comment("Enable/Disable automatically unequipping tools before they breaks")
				.define("AutoUnequipTools", DurabilityInfoOptions.defaultAutoUnequipTools);
			DurabilityDisplayThresholdForArmor = builder
				.comment("Display when armor has less than this fraction of its durability")
				.defineInRange(
					"DurabilityDisplayThresholdForArmor",
					DurabilityInfoOptions.defaultDurabilityDisplayThreshold,
					DurabilityInfoOptions.minDurabilityDisplayThreshold,
					DurabilityInfoOptions.maxDurabilityDisplayThreshold
				);
			DurabilityDisplayThresholdForItem = builder
				.comment("Display when an item has less than this fraction of its durability")
				.defineInRange(
					"DurabilityDisplayThresholdForItem",
					DurabilityInfoOptions.defaultDurabilityDisplayThreshold,
					DurabilityInfoOptions.minDurabilityDisplayThreshold,
					DurabilityInfoOptions.maxDurabilityDisplayThreshold
				);
			DurabilityInfoTextMode = builder
				.comment("Sets Durability Info's number display mode.")
				.defineEnum(
					"DurabilityInfoTextMode", DurabilityInfoOptions.DurabilityInfoTextModes.NONE,
					EnumGetMethod.NAME_IGNORECASE
				);
			DurabilityLocationHorizontal = builder
				.comment(
					"The horizontal position of the durability icons. The far left is at " +
					DurabilityInfoOptions.minArmorPosXY + ", and " +
					DurabilityInfoOptions.maxArmorIconPosX + " is the far right."
				)
				.defineInRange(
					"DurabilityLocationHorizontal", DurabilityInfoOptions.defaultArmorIconPosX,
					DurabilityInfoOptions.minArmorPosXY, DurabilityInfoOptions.maxArmorIconPosX
				);
			DurabilityLocationVertical = builder
				.comment(
					"The vertical position of the durability icons. The top is at " +
					DurabilityInfoOptions.minArmorPosXY + ", and " +
					DurabilityInfoOptions.maxArmorIconPosY + " is the very bottom."
				)
				.defineInRange(
					"DurabilityLocationVertical", DurabilityInfoOptions.defaultArmorIconPosY,
					DurabilityInfoOptions.minArmorPosXY, DurabilityInfoOptions.maxArmorIconPosY
				);
			DurabilityScale = builder
				.comment("How large the durability icons are rendered, 1.0 being the normal size")
				.defineInRange(
					"DurabilityScale", DurabilityInfoOptions.defaultDurabilityIconScale,
					DurabilityInfoOptions.minIconScale, DurabilityInfoOptions.maxIconScale
				);
			ShowArmorDurability = builder
				.comment("Enable/Disable showing breaking armor")
				.define("ShowArmorDurability", DurabilityInfoOptions.defaultShowArmorDurability);
			ShowIndividualArmorIcons = builder
				.comment("Enable/Disable showing armor peices instead of the big broken armor icon")
				.define("ShowIndividualArmorIcons", DurabilityInfoOptions.defaultShowIndividualArmorIcons);
			ShowItemDurability = builder
				.comment("Enable/Disable showing breaking items")
				.define("ShowItemDurability", DurabilityInfoOptions.defaultShowItemDurability);
			UseColoredNumbers = builder
				.comment("Toggle using colored numbering")
				.define("UseColoredNumbers", DurabilityInfoOptions.defaultUseColoredNumbers);
		}
		builder.pop();

		// ######################################################################
		// Eating Aid Module
		// ######################################################################
//		builder.comment(
//			"Eating Aid makes eating food quick and easy."
//		).push(CATEGORY_EATINGAID);
//		{
//			EnableEatingAid = builder
//				.comment(
//					"Enables pressing the " CATEGORY_EATINGAID + " keybind (default: 'G')" +
//					" to eat food even if it is in your main inventory and not your hotbar"
//				)
//				.define("EnableEatingAid", true);
//			EatGoldenFood = builder
//				.comment("Enable/Disable using golden apples and golden carrots as food")
//				.define("EatGoldenFood", false);
//			EatRawFood = builder
//				.comment("Enable/Disable eating raw chicken, beef, and porkchops")
//				.define("EatRawFood", false);
//			EatingAidMode = builder
//				.comment("Sets the Eating Aid mode.")
//				.defineEnum("EatingAidMode", EatingAid.Modes.INTELLIGENT, EnumGetMethod.NAME_IGNORECASE);
//			PrioritizeFoodInHotbar = builder
//				.comment("Use food that is in your hotbar before looking for food in your main inventory")
//				.define("PrioritizeFoodInHotbar", false);
//			UsePvPSoup = builder
//				.comment(
//					"If you are connected to a Bukkit server that uses PvP Soup or Fast Soup (mushroom stew) with this enabled, " +
//					"Eating Aid will use it instead of other foods"
//				)
//				.define("UsePvPSoup", false);
//		}
//		builder.pop();

		// ######################################################################
		// Ender Pearl Aid Module
		// ######################################################################
		builder.comment(
			"Ender Pearl Aid makes it easier to quickly throw ender pearls."
		).push(CATEGORY_ENDERPEARLAID);
		{
			EnableEnderPearlAid = builder
				.comment(
					"Enables pressing the " + CATEGORY_ENDERPEARLAID + " keybind (default: 'C')" +
					" to use an enderpearl even if it is in your main inventory and not your hotbar"
				)
				.define("EnableEnderPearlAid", EnderPearlAidOptions.defaultEnabled);
		}
		builder.pop();

		// ######################################################################
		// FPS Module
		// ######################################################################
		builder.comment(
			"FPS shows your frames per second without having to go into the F3 menu."
		).push(CATEGORY_FPS);
		{
			EnableFPS = builder
				.comment("Enable/Disable showing your FPS at the end of the Info Line")
				.define("EnableFPS", FpsOptions.defaultEnabled);
		}
		builder.pop();

		// ######################################################################
		// Health Monitor Module
		// ######################################################################
		builder.comment(
			"Plays warning beeps when you are low on health."
		).push(CATEGORY_HEALTHMONITOR);
		{
			EnableHealthMonitor = builder
				.comment("Enable/Disable using the Health Monitor")
				.define("EnableHealthMonitor", HealthMonitorOptions.defaultEnabled);
			HealthMonitorMode = builder
				.comment("Sets the Health Monitor sound.")
				.defineEnum(
					"HealthMonitorMode", HealthMonitorOptions.HealthMonitorModes.OOT,
					EnumGetMethod.NAME_IGNORECASE
				);
			HealthMonitorVolume = builder
				.comment("Set the volume of the beeps")
				.defineInRange(
					"HealthMonitorVolume", (float) HealthMonitorOptions.defaultSoundVolume,
					HealthMonitorOptions.minSoundVolume, HealthMonitorOptions.maxSoundVolume
				);
			LowHealthSoundThreshold = builder
				.comment("A sound will start playing when you have less than this much health left")
				// Apparently the max number of regular (non-absorption) hearts is 1024. Don't know why you'd ever want to
				// be warned when your health is 1 less than max, but I'll allow it
				// 4 health (2 hearts) is when the health bar starts to shake
				.defineInRange(
					"LowHealthSoundThreshold", HealthMonitorOptions.defaultLowHealthSoundThreshold,
					HealthMonitorOptions.minLowHealthSoundThreshold, HealthMonitorOptions.maxLowHealthSoundThreshold
				);
			PlayFasterNearDeath = builder
				.comment("Play the warning sounds quicker the closer you get to dieing")
				.define("PlayFasterNearDeath", HealthMonitorOptions.defaultPlayFasterNearDeath);
		}
		builder.pop();

		// ######################################################################
		// Info Line Module
		// ######################################################################
		builder.comment(
			"Info Line displays the status of other features in the top left corner of the screen."
		).push(CATEGORY_INFOLINE);
		{
			EnableInfoLine = builder
				.comment(
					"Enable/Disable the entire info line in the top left part of the screen. " +
					"This includes the clock, coordinates, compass, mod status, etc"
				)
				.define("EnableInfoLine", InfoLineOptions.defaultEnabled);
			InfoLineLocationHorizontal = builder
				.comment(
					"The horizontal position of the info line. The far left is at " +
					InfoLineOptions.minLeftOffset + ", and " + InfoLineOptions.maxLeftOffset + " is the far right"
				)
				.defineInRange(
					"InfoLineLocationHorizontal", InfoLineOptions.defaultLeftOffset,
					InfoLineOptions.minLeftOffset, InfoLineOptions.maxLeftOffset
				);
			InfoLineLocationVertical = builder
				.comment(
					"The vertical position of the info line. The top is at " +
					InfoLineOptions.minTopOffset + ", and the bottom is at " + InfoLineOptions.maxTopOffset
				)
				.defineInRange(
					"InfoLineLocationVertical", InfoLineOptions.defaultTopOffset,
					InfoLineOptions.minTopOffset, InfoLineOptions.maxTopOffset
				);
			ShowBiome = builder
				.comment("Enable/Disable showing what biome you are in on the Info Line")
				.define("ShowBiome", InfoLineOptions.defaultShowBiome);
			ShowCanSnow = builder
				.comment("Enable/Disable showing if it can snow at the player's feet on the Info Line")
				.define("ShowCanSnow", InfoLineOptions.defaultShowCanSnow);
			ShowPing = builder
				.comment("Enable/Disable showing the player's ping while on a multiplayer server")
				.define("ShowPing", InfoLineOptions.defaultShowPing);
		}
		builder.pop();

		// ######################################################################
		// Item Selector Module
		// ######################################################################
		builder.comment(
			"Item Selector allows you to conveniently swap your currently selected hotbar item with something in your inventory."
		).push(CATEGORY_ITEMSELECTOR);
		{
			EnableItemSelector = builder
				.comment(
					"Enables/Disable using mouse wheel scrolling whilst holding the " + CATEGORY_ITEMSELECTOR +
					" keybind (default: Left Alt) to swap the selected item with an inventory item"
				)
				.define("EnableItemSelector", ItemSelectorOptions.defaultEnabled);
			ItemSelectorMode = builder
				.comment("Sets the Item Selector mode.")
				.defineEnum(
					"ItemSelectorMode", ItemSelectorOptions.ItemSelectorModes.ALL, EnumGetMethod.NAME_IGNORECASE
				);
			ItemSelectorSideButtons = builder
				.comment("Enable/disable use of side buttons for item selection")
				.define("ItemSelectorSideButtons", ItemSelectorOptions.defaultUseMouseSideButtons);
			ItemSelectorTimeout = builder
				.comment(
					"Specifies how many ticks until the item selector confirms your choice and performs the item swap"
				)
				.defineInRange(
					"ItemSelectorTimeout", ItemSelectorOptions.defaultTimeout,
					ItemSelectorOptions.minTimeout, ItemSelectorOptions.maxTimeout
				);
		}
		builder.pop();

		// ######################################################################
		// Miscellaneous Module
		// ######################################################################
		builder.comment(
			"Other settings not related to any specific functionality."
		).push(CATEGORY_MISCELLANEOUS);
		{
			ShowAnvilRepairs = builder
				.comment("Enable/Disable showing the repair count on items while using the anvil")
				.define("ShowAnvilRepairs", MiscOptions.ShowAnvilRepairs);
			UseQuickPlaceSign = builder
				.comment("Enable/Disable being able to place a sign with no text by sneaking while placing a sign")
				.define("UseQuickPlaceSign", MiscOptions.UseQuickPlaceSign);
			UseUnlimitedSprintingSP = builder
				.comment("Enable/Disable overriding the default sprint behavior and run forever. SINGLEPLAYER ONLY.")
				.define("UseUnlimitedSprintingSP", MiscOptions.UseUnlimitedSprintingSP);
		}
		builder.pop();

		// ######################################################################
		// Player Locator Module
		// ######################################################################
		builder.comment(
			"Player Locator gives you a radar-like ability to easily see where other people are."
		).push(CATEGORY_PLAYERLOCATOR);
		{
			EnablePlayerLocator = builder
				.comment("Enable/Disable the Player Locator")
				.define("EnablePlayerLocator", LocatorOptions.defaultEnabled);
			PlayerLocatorMinViewDistance = builder
				.comment("Stop showing player names when they are this close (distance measured in blocks)")
				.defineInRange(
					"PlayerLocatorMinViewDistance", LocatorOptions.defaultViewDistanceCutoff,
					LocatorOptions.minViewDistanceCutoff, LocatorOptions.maxViewDistanceCutoff
				);
			PlayerLocatorMode = builder
				.comment("Sets the Player Locator mode.")
				.defineEnum(
					"PlayerLocatorMode", LocatorOptions.LocatorModes.OFF, EnumGetMethod.NAME_IGNORECASE
				);
			ShowDistanceToPlayers = builder
				.comment("Show how far away you are from the other players next to their name")
				.define("ShowDistanceToPlayers", LocatorOptions.defaultShowDistanceToPlayers);
			ShowPlayerHealth = builder
				.comment("Show how much health players have by their name")
				.define("ShowPlayerHealth", LocatorOptions.defaultShowPlayerHealth);
			ShowWitherSkeletons = builder
				.comment("Show your tamed wolves in addition to other players")
				.define("ShowWitherSkeletons", LocatorOptions.defaultShowWitherSkeletons);
			ShowWolves = builder
				.comment("Show your tamed wolves in addition to other players")
				.define("ShowWolves", LocatorOptions.defaultShowWolves);
			UseWolfColors = builder
				.comment("Use the color of your wolf's collar to colorize their name")
				.define("UseWolfColors", LocatorOptions.defaultUseWolfColors);
		}
		builder.pop();

		// ######################################################################
		// Potion Aid Module
		// ######################################################################
		builder.comment(
			"Potion Aid helps you quickly drink potions based on your circumstance."
		).push(CATEGORY_POTIONAID);
		{
			EnablePotionAid = builder
				.comment(
					"Enables pressing " + CATEGORY_POTIONAID + " keybind (default: 'V')" +
					" to drink a potion even if it is in your main inventory and not your hotbar"
				)
				.define("EnablePotionAid", PotionAidOptions.defaultEnabled);
		}
		builder.pop();

		// ######################################################################
		// Potion Timers Module
		// ######################################################################
//		builder.comment(
//			"Potion Timers shows the duration remaining on potions that you drink."
//		).push(CATEGORY_POTIONTIMERS);
//		{
//			EnablePotionTimers = builder
//				.comment("Enable/Disable showing the time remaining on potions")
//				.define("EnablePotionTimers", true);
//			PotionTimerTextMode = builder
//				.comment("Sets Potion Timer's text display mode.")
//				.defineEnum("PotionTimerTextMode", PotionTimers.PotionTimerTextModes.COLORED, EnumGetMethod.NAME_IGNORECASE);
//			HidePotionEffectsInInventory = builder
//				.comment("Enable/Disable hiding the default potion effects when you open your inventory")
//				.define("HidePotionEffectsInInventory", false);
//			PotionScale = builder
//				.comment("How large the potion timers are rendered, 1.0 being the normal size")
//				.defineInRange("PotionScale", 1.0d, 0.25d, 5.0d);
//			PotionTimersLocationHorizontal = builder
//				.comment("The horizontal position of the potion timers. 0 is left, 400 is far right")
//				.defineInRange("PotionTimersLocationHorizontal", 1, 0, 400);
//			PotionTimersLocationVertical = builder
//				.comment("The vertical position of the potion timers. 0 is top, 200 is very bottom")
//				.defineInRange("PotionTimersLocationVertical", 16, 0, 200);
//			ShowPotionIcons = builder
//				.comment("Enable/Disable showing the status effect of potions next to the timers")
//				.define("ShowPotionIcons", true);
//		}
//		builder.pop();

		// ######################################################################
		// Quick Deposit Module
		// ######################################################################
		builder.comment(
			"Quick Stack allows you to intelligently deposit every item in your inventory quickly into a chest."
		).push(CATEGORY_QUICKDEPOSIT);
		{
			EnableQuickDeposit = builder
				.comment("Enables Quick Deposit.")
				.define("EnableQuickDeposit", QuickDepositOptions.defaultEnabled);
			BlacklistArrow = builder
				.comment("Stop Quick Deposit from putting arrows in chests?")
				.define("BlacklistArrows", QuickDepositOptions.BlacklistArrow);
			BlacklistClockCompass = builder
				.comment("Stop Quick Deposit from putting clocks and compasses in chests?")
				.define("BlacklistClockCompass", QuickDepositOptions.BlacklistClockCompass);
			BlacklistEnderPearl = builder
				.comment("Stop Quick Deposit from putting ender pearls in chests?")
				.define("BlacklistEnderPearl", QuickDepositOptions.BlacklistEnderPearl);
			BlacklistFood = builder
				.comment("Stop Quick Deposit from putting food in chests?")
				.define("BlacklistFood", QuickDepositOptions.BlacklistFood);
			BlacklistTools = builder
				.comment("Stop Quick Deposit from putting tools (picks, axes, shovels, shears) in chests?")
				.define("BlacklistTools", QuickDepositOptions.BlacklistTools);
			BlacklistTorch = builder
				.comment("Stop Quick Deposit from putting torches in chests?")
				.define("BlacklistTorches", QuickDepositOptions.BlacklistTorch);
			BlacklistWaterBucket = builder
				.comment("Stop Quick Deposit from putting water buckets in chests?")
				.define("BlacklistWaterBucket", QuickDepositOptions.BlacklistWaterBucket);
			BlacklistWeapons = builder
				.comment("Stop Quick Deposit from putting swords and bows in chests?")
				.define("BlacklistWeapons", QuickDepositOptions.BlacklistWeapons);
			CloseChestAfterDepositing = builder
				.comment(
					"Closes the chest GUI after you deposit your items in it. " +
					"Allows quick and easy depositing of all your items into multiple chests"
				)
				.define("CloseChestAfterDepositing", QuickDepositOptions.CloseChestAfterDepositing);
			IgnoreItemsInHotbar = builder
				.comment(
					"Determines if items in your hotbar will be deposited into chests when the " +
					CATEGORY_QUICKDEPOSIT + " keybind (default: 'X') is pressed"
				)
				.define("IgnoreItemsInHotbar", QuickDepositOptions.IgnoreItemsInHotbar);
		}
		builder.pop();

		// ######################################################################
		// Safe Overlay Module
		// ######################################################################
		builder.comment(
			"Safe Overlay shows you which blocks are dark enough to spawn mobs."
		).push(CATEGORY_SAFEOVERLAY);
		{
			EnableSafeOverlay = builder
				.comment("Enable/Disable the Safe Overlay")
				.define("EnableSafeOverlay", SafeOverlayOptions.defaultEnabled);
			SafeOverlayDisplayInNether = builder
				.comment("Enable/Disable showing unsafe areas in the Nether")
				.define("SafeOverlayDisplayInNether", SafeOverlayOptions.defaultDisplayInNether);
			SafeOverlayDrawDistance = builder
				.comment(
					"How far away unsafe spots should be rendered around the player measured in blocks. " +
					"This can be changed in game by pressing the " + CATEGORY_SAFEOVERLAY + " keybind (default: 'L') " +
					"in combination with the '-' and '+' keys"
				)
				.defineInRange(
					"SafeOverlayDrawDistance", SafeOverlayOptions.defaultDrawDistance,
					SafeOverlayOptions.minDrawDistance, SafeOverlayOptions.maxDrawDistance
				);
			SafeOverlayMode = builder
				.comment("Sets the Safe Overlay mode.")
				.defineEnum(
					"SafeOverlayMode", SafeOverlayOptions.SafeOverlayModes.OFF,
					EnumGetMethod.NAME_IGNORECASE
				);
			SafeOverlaySeeThroughWalls = builder
				.comment(
					"Enable/Disable showing unsafe areas through walls. Toggle in game with 'Ctrl' + the " +
					CATEGORY_SAFEOVERLAY + " keybind (default: 'L')"
				)
				.define("SafeOverlaySeeThroughWalls", SafeOverlayOptions.defaultRenderThroughWalls);
			SafeOverlayTransparency = builder
				.comment("The transparency of the unsafe marks")
				.defineInRange(
					"SafeOverlayTransparency", SafeOverlayOptions.defaultUnsafeOverlayTransparency,
					SafeOverlayOptions.minUnsafeOverlayTransparency,
					SafeOverlayOptions.maxUnsafeOverlayTransparency
				);
		}
		builder.pop();

		// ######################################################################
		// Torch Aid Module
		// ######################################################################
		builder.comment(
			"Torch Aid lets you right click while holding an axe, pickaxe, shovel, or when you have nothing in your hand to place a torch."
		).push(CATEGORY_TORCHAID);
		{
			EnableTorchAid = builder
				.comment("Enable/Disable using Torch Aid to help you place torches more easily")
				.define("EnableTorchAid", TorchAidOptions.defaultEnabled);
		}
		builder.pop();

		// ######################################################################
		// Weapon Swap Module
		// ######################################################################

		builder.comment(
			"Weapon Swap allows you to quickly select your sword and bow."
		).push(CATEGORY_WEAPONSWAP);
		{
			EnableWeaponSwap = builder
				.comment(
					"Enables pressing the " + CATEGORY_WEAPONSWAP + " keybind (default: 'F') to swap between your sword and bow"
				)
				.define("EnableWeaponSwap", WeaponSwapOptions.defaultEnabled);
		}
		builder.pop();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		ZyinHUD.ZyinLogger.debug("Loaded Zyin's HUD config file {}", configEvent.getConfig().getFileName());
	}


	@SubscribeEvent
	public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
		ZyinHUD.ZyinLogger.fatal("Zyin's HUD config just got changed on the file system!");
	}

	public static ForgeConfigSpec getConfigSpec() {
		return SPEC;
	}

	public Type getConfigType() {
		return Type.CLIENT;
	}
}
