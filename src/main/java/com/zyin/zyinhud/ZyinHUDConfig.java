package com.zyin.zyinhud;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.zyin.zyinhud.mods.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

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
	
	//categories
	/**
	 * The constant CATEGORY_CLOCK.
	 */
	public static final String CATEGORY_CLOCK = "clock";
	/**
	 * The constant CATEGORY_COMPASS.
	 */
	public static final String CATEGORY_COMPASS = "compass";
	/**
	 * The constant CATEGORY_COORDINATES.
	 */
	public static final String CATEGORY_COORDINATES = "coordinates";
	/**
	 * The constant CATEGORY_DISTANCEMEASURER.
	 */
	public static final String CATEGORY_DISTANCEMEASURER = "distancemeasurer";
	/**
	 * The constant CATEGORY_DURABILITYINFO.
	 */
	public static final String CATEGORY_DURABILITYINFO = "durabilityinfo";
	/**
	 * The constant CATEGORY_EATINGAID.
	 */
//    public static final String CATEGORY_EATINGAID = "eatingaid";
	/**
	 * The constant CATEGORY_ENDERPEARLAID.
	 */
	public static final String CATEGORY_ENDERPEARLAID = "enderpearlaid";
	/**
	 * The constant CATEGORY_FPS.
	 */
	public static final String CATEGORY_FPS = "fps";
	/**
	 * The constant CATEGORY_HEALTHMONITOR.
	 */
	public static final String CATEGORY_HEALTHMONITOR = "healthmonitor";
	/**
	 * The constant CATEGORY_ANIMALINFO.
	 */
	public static final String CATEGORY_ANIMALINFO = "horseinfo";
	/**
	 * The constant CATEGORY_INFOLINE.
	 */
	public static final String CATEGORY_INFOLINE = "infoline";
	/**
	 * The constant CATEGORY_ITEMSELECTOR.
	 */
	public static final String CATEGORY_ITEMSELECTOR = "itemselector";
	/**
	 * The constant CATEGORY_MISCELLANEOUS.
	 */
	public static final String CATEGORY_MISCELLANEOUS = "miscellaneous";
	/**
	 * The constant CATEGORY_PLAYERLOCATOR.
	 */
	public static final String CATEGORY_PLAYERLOCATOR = "playerlocator";
	/**
	 * The constant CATEGORY_POTIONAID.
	 */
	public static final String CATEGORY_POTIONAID = "potionaid";
	/**
	 * The constant CATEGORY_POTIONTIMERS.
	 */
//    public static final String CATEGORY_POTIONTIMERS = "potiontimers";
	/**
	 * The constant CATEGORY_QUICKDEPOSIT.
	 */
	public static final String CATEGORY_QUICKDEPOSIT = "quickdeposit";
	/**
	 * The constant CATEGORY_SAFEOVERLAY.
	 */
	public static final String CATEGORY_SAFEOVERLAY = "safeoverlay";
	/**
	 * The constant CATEGORY_TORCHAID.
	 */
	public static final String CATEGORY_TORCHAID = "torchaid";
	/**
	 * The constant CATEGORY_WEAPONSWAP.
	 */
	public static final String CATEGORY_WEAPONSWAP = "weaponswap";

	// ######################################################################
	// Clock Module
	// ######################################################################
	// Sets the clock mode. Valid modes are STANDARD, COUNTDOWN, and GRAPHIC
	public static EnumValue<Clock.Modes> ClockMode;
	// Enable/Disable showing the clock
	public static BooleanValue EnableClock;

	// ######################################################################
	//Compass Module
	// ######################################################################
	// Enable/Disable showing the compass
	public static BooleanValue EnableCompass;

	// ######################################################################
	// Coordinates Module
	// ######################################################################
	// The format used when sending your coordinates in a chat message by pressing the keybind
	// {x}{y}{z} are replaced with actual coordinates
	public static ConfigValue<String> CoordinatesChatStringFormat;
	// Sets the coordinates mode. Valid modes are XYZ and XZY
	public static EnumValue<Coordinates.Modes> CoordinatesMode;
	// Enable/Disable showing your coordinates
	public static BooleanValue EnableCoordinates;
	// Shows how far into the 16x16 chunk you're in
	public static BooleanValue ShowChunkCoordinates;
	// Color code the Y (height) coordinate based on what ores can spawn at that level
	public static BooleanValue UseYCoordinateColors;

	// ######################################################################
	// Distance Measurer Module
	// ######################################################################
	// Sets the Distance Measurer mode. Valid modes are OFF, SIMPLE, and COORDINATE
	public static EnumValue<DistanceMeasurer.Modes> DistanceMeasurerMode;
	// Enable/Disable the distance measurer
	public static BooleanValue EnableDistanceMeasurer;

	// ######################################################################
	// Durability Info Module
	// ######################################################################
	// Enable/Disable automatically unequipping armor before it breaks.
	public static BooleanValue AutoUnequipArmor;
	// Enable/Disable automatically unequipping tools before they breaks
	public static BooleanValue AutoUnequipTools;
	// Display when armor gets damaged less than this fraction of its durability
	public static DoubleValue DurabilityDisplayThresholdForArmor;
	// Display when an item gets damaged less than this fraction of its durability
	public static DoubleValue DurabilityDisplayThresholdForItem;
	// Sets Durability Info's number display mode. Valid modes are NONE, TEXT, and PERCENTAGE
	public static EnumValue<DurabilityInfo.TextModes> DurabilityInfoTextMode;
	// The horizontal position of the durability icons. 0 is left, 400 is far right
	public static IntValue DurabilityLocationHorizontal;
	// The vertical position of the durability icons. 0 is top, 200 is very bottom
	public static IntValue DurabilityLocationVertical;
	// How large the durability icons are rendered, 1.0 being the normal size
	public static DoubleValue DurabilityScale;
	// Enable/Disable showing all durability info
	public static BooleanValue EnableDurabilityInfo;
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
	// Enable/Disable using golden apples and golden carrots as food
//	public static BooleanValue EatGoldenFood;
	// Enable/Disable eating raw chicken, beef, and porkchops
//	public static BooleanValue EatRawFood;
	// Sets the Eating Aid mode. Valid modes are BASIC and INTELLIGENT
//	public static EnumValue<EatingAid.Modes> EatingAidMode;
	// Enables pressing the eating aid keybind to eat food even if it is  in your inventory and not your hotbar
//	public static BooleanValue EnableEatingAid;
	// Use food that is in your hotbar before looking for food in your main inventory
//	public static BooleanValue PrioritizeFoodInHotbar;
	// If you are connected to a Bukkit server that uses PvP Soup or Fast Soup (mushroom stew) with this enabled,
	// Eating Aid will use it instead of other foods
//	public static BooleanValue UsePvPSoup;

	// ######################################################################
	// Ender Pearl Aid Module
	// ######################################################################
	// Enables pressing the ender pearl aid keybind to use an enderpearl even if it is  in your inventory and not your hotbar
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
	// Sets the Health Monitor mode. Valid modes are OOT, LTTP, ORACLE, LA, LOZ, and AOL
	public static EnumValue<HealthMonitor.Modes> HealthMonitorMode;
	// Set the volume of the beeps [0..1]
	public static DoubleValue HealthMonitorVolume;
	// A sound will start playing when you have less than this much health left
	public static IntValue LowHealthSoundThreshold;
	// Play the warning sounds quicker the closer you get to dieing
	public static BooleanValue PlayFasterNearDeath;

	// ######################################################################
	// Horse Info Module
	// ######################################################################
	// How far away animal info will be rendered on the screen (distance measured in blocks)
	public static IntValue AnimalInfoMaxViewDistance;
	// Sets the Animal Info mode. Valid modes are ON and OFF
	public static EnumValue<AnimalInfo.Modes> AnimalInfoMode;
	// Enable/Disable Animal Info
	public static BooleanValue EnableAnimalInfo;
	// How many decimal places will be used when displaying horse stats
	public static IntValue HorseInfoNumberOfDecimalsDisplayed;
	// Enable/Disable showing an icon if the animal is ready to breed
	public static BooleanValue ShowBreedingIcons;
	// Enable/Disable showing the stats of the horse you're riding on the F3 screen
	public static BooleanValue ShowHorseStatsOnF3Menu;
	// Enable/Disable showing the stats of horses on screen
	public static BooleanValue ShowHorseStatsOverlay;
	// Enable/Disable showing a black background behind text
	public static BooleanValue ShowTextBackgrounds;

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
	// Enable/Disable showing what biome you are in on the info line
	public static BooleanValue ShowBiome;
	// Enable/Disable showing if it can snow at the player's feet on the info line
	public static BooleanValue ShowCanSnow;

	//ShowPing, boolean, default true

	// ######################################################################
	// Item Selector Module
	// ######################################################################
	// Enables/Disable using mouse wheel scrolling whilst holding LMENU to swap the selected item with an inventory item
	public static BooleanValue EnableItemSelector;
	// Sets the Item Selector mode. Valid modes are ALL and SAME_COLUMN
	public static EnumValue<ItemSelector.Modes> ItemSelectorMode;
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
	public static BooleanValue UseUnlimitedSprinting;

	// ######################################################################
	// Player Locator Module
	// ######################################################################
	// Enable/Disable the Player Locator.
	public static BooleanValue EnablePlayerLocator;
	// Stop showing player names when they are this close (distance measured in blocks).
	public static IntValue PlayerLocatorMinViewDistance;
	// Sets the Player Locator mode. Valid modes are OFF and ON
	public static EnumValue<PlayerLocator.Modes> PlayerLocatorMode;
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
	// Enables pressing potion aid keybind to drink a potion even if it is  in your inventory and not your hotbar.
	public static BooleanValue EnablePotionAid;

	// ######################################################################
	// Potion Timers Module
	// ######################################################################
	// Sets Potion Timer's text display mode. Valid modes are WHITE, COLORED, and NONE
//    public static EnumValue<PotionTimers.TextModes> DurabilityInfoTextMode;
	// Enable/Disable showing the time remaining on potions.
//    public static BooleanValue EnablePotionTimers;
	// Enable/Disable hiding the default potion effects when you open your inventory.
//    public static BooleanValue HidePotionEffectsInInventory;
	// How large the potion timers are rendered, 1.0 being the normal size.
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
	// Stop Quick Deposit from putting arrows in chests?
	public static BooleanValue BlacklistArrow;
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
	// Closes the chest GUI after you deposit your items in it. Allows quick and easy depositing of all your items into multiple chests.
	public static BooleanValue CloseChestAfterDepositing;
	// Enables Quick Deposit.
	public static BooleanValue EnableQuickDeposit;
	// Determines if items in your hotbar will be deposited into chests when 'X' is pressed.
	public static BooleanValue IgnoreItemsInHotbar;

	// ######################################################################
	// Safe Overlay Module
	// ######################################################################
	// Enable/Disable the Safe Overlay.
	public static BooleanValue EnableSafeOverlay;
	// Enable/Disable showing unsafe areas in the Nether.
	public static BooleanValue SafeOverlayDisplayInNether;
	// How far away unsafe spots should be rendered around the player measured in blocks. This can be changed in game with - + L and + + L.
	public static IntValue SafeOverlayDrawDistance;
	// Sets the Safe Overlay mode. Valid modes are OFF and On
	public static EnumValue<SafeOverlay.Modes> SafeOverlayMode;
	// Enable/Disable showing unsafe areas through walls. Toggle in game with Ctrl + L.
	public static BooleanValue SafeOverlaySeeThroughWalls;
	// The transparency of the unsafe marks. Must be between greater than 0.1 and less than or equal to 1.
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

	private static void configure(Builder builder, Builder builder_override) {
		// ######################################################################
		// Clock Module
		// ######################################################################
		builder.comment(
			"Clock shows you time relevant to Minecraft time."
		).push(CATEGORY_CLOCK);
		{
			ClockMode = builder
				.comment("Sets the clock mode. Valid modes are STANDARD, COUNTDOWN, and GRAPHIC")
				.defineEnum("ClockMode", Clock.Modes.STANDARD, EnumGetMethod.NAME_IGNORECASE);
			EnableClock = builder
				.comment("Enable/Disable showing the clock")
				.define("EnableClock", true);
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
				.define("EnableCompass", true);
		}
		builder.pop();

		// ######################################################################
		// Coordinates Module
		// ######################################################################
		builder.comment(
			"Coordinates displays your coordinates. Nuff said."
		).push(CATEGORY_COORDINATES);
		{
			CoordinatesChatStringFormat = builder
				.comment(
					"The format used when sending your coordinates in a chat message by pressing '" +
					bindingToKeyName(ZyinHUDKeyHandlers.KEY_BINDINGS[1]) + "'. " +
					"{x}{y}{z} are replaced with actual coordinates"
				)
				.define("CoordinatesChatStringFormat", "X={x}, Y={y}, Z={z}");
			CoordinatesMode = builder
				.comment("Sets the coordinates mode. Valid modes are XYZ and XZY")
				.defineEnum("CoordinatesMode", Coordinates.Modes.XYZ, EnumGetMethod.NAME_IGNORECASE);
			EnableCoordinates = builder
				.comment("Enable/Disable showing your coordinates")
				.define("EnableCoordinates", true);
			ShowChunkCoordinates = builder
				.comment("Shows how far into the 16x16 chunk you're in")
				.define("ShowChunkCoordinates", false);
			UseYCoordinateColors = builder
				.comment("Color code the Y (height) coordinate based on what ores can spawn at that level")
				.define("UseYCoordinateColors", true);
		}
		builder.pop();

		// ######################################################################
		// Distance Measurer Module
		// ######################################################################
		builder.comment(
			"Distance Measurer can calculate distances between you and blocks that you aim at."
		).push(CATEGORY_DISTANCEMEASURER);
		{
			DistanceMeasurerMode = builder
				.comment("Sets the Distance Measurer mode. Valid modes are OFF, SIMPLE, and COORDINATE")
				.defineEnum("DistanceMeasurerMode", DistanceMeasurer.Modes.OFF, EnumGetMethod.NAME_IGNORECASE);
			EnableDistanceMeasurer = builder
				.comment("Enable/Disable the distance measurer")
				.define("EnableDistanceMeasurer", true);
		}
		builder.pop();

		// ######################################################################
		// Durability Info Module
		// ######################################################################
		builder.comment(
			"Durability Info will display your breaking armor and equipment."
		).push(CATEGORY_DURABILITYINFO);
		{
			AutoUnequipArmor = builder
				.comment("Enable/Disable automatically unequipping armor before it breaks.")
				.define("AutoUneqipArmor", false);
			AutoUnequipTools = builder
				.comment("Enable/Disable automatically unequipping tools before they breaks")
				.define("AutoUnequipTools", false);
			DurabilityDisplayThresholdForArmor = builder
				.comment("Display when armor gets damaged less than this fraction of its durability")
				.defineInRange("DurabilityDisplayThresholdForArmor", 0.1d, 0.0d, 1.0d);
			DurabilityDisplayThresholdForItem = builder
				.comment("Display when an item gets damaged less than this fraction of its durability")
				.defineInRange("DurabilityDisplayThresholdForItem", 0.1d, 0.0d, 1.0d);
			DurabilityInfoTextMode = builder
				.comment("Sets Durability Info's number display mode. Valid modes are NONE, TEXT, and PERCENTAGE")
				.defineEnum("DurabilityInfoTextMode", DurabilityInfo.TextModes.NONE, EnumGetMethod.NAME_IGNORECASE);
			DurabilityLocationHorizontal = builder
				.comment("The horizontal position of the durability icons. 0 is left, 400 is far right")
				.defineInRange("DurabilityLocationHorizontal", 30, 0, 400);
			DurabilityLocationVertical = builder
				.comment("The vertical position of the durability icons. 0 is top, 200 is very bottom")
				.defineInRange("DurabilityLocationVertical", 20, 0, 200);
			DurabilityScale = builder
				.comment("How large the durability icons are rendered, 1.0 being the normal size. Max is 5.0")
				.defineInRange("DurabilityScale", 1.0d, 0.25d, 5.0d);
			EnableDurabilityInfo = builder
				.comment("Enable/Disable showing all durability info")
				.define("EnableDurabilityInfo", true);
			ShowArmorDurability = builder
				.comment("Enable/Disable showing breaking armor")
				.define("ShowArmorDurability", true);
			ShowIndividualArmorIcons = builder
				.comment("Enable/Disable showing armor peices instead of the big broken armor icon")
				.define("ShowIndividualArmorIcons", true);
			ShowItemDurability = builder
				.comment("Enable/Disable showing breaking items")
				.define("ShowItemDurability", true);
			UseColoredNumbers = builder
				.comment("Toggle using colored numbering")
				.define("UseColoredNumbers", true);
		}
		builder.pop();

		// ######################################################################
		// Eating Aid Module
		// ######################################################################
//		builder.comment(
//			"Eating Aid makes eating food quick and easy."
//		).push(CATEGORY_EATINGAID);
//		{
//			EatGoldenFood = builder
//				.comment("Enable/Disable using golden apples and golden carrots as food")
//				.define("EatGoldenFood", false);
//			EatRawFood = builder
//				.comment("Enable/Disable eating raw chicken, beef, and porkchops")
//				.define("EatRawFood", false);
//			EatingAidMode = builder
//				.comment("Sets the Eating Aid mode. Valid modes are BASIC and INTELLIGENT")
//				.defineEnum("EatingAidMode", EatingAid.Modes.INTELLIGENT, EnumGetMethod.NAME_IGNORECASE);
//			EnableEatingAid = builder
//				.comment(
//					"Enables pressing " +
//					bindingToKeyName(ZyinHUDKeyHandlers.KEY_BINDINGS[3]) +
//					" to eat food even if it is in your inventory and not your hotbar")
//				.define("EnableEatingAid", true);
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
				.comment("Enables pressing " +
				         bindingToKeyName(ZyinHUDKeyHandlers.KEY_BINDINGS[4]) +
				         " to use an enderpearl even if it is in your inventory and not your hotbar")
				.define("EnableEnderPearlAid", true);
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
				.define("EnableFPS", false);
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
				.define("EnableHealthMonitor", false);
			HealthMonitorMode = builder
				.comment("Sets the Health Monitor mode. Valid modes are OOT, LTTP, ORACLE, LA, LOZ, and AOL")
				.defineEnum("HealthMonitorMode", HealthMonitor.Modes.OOT, EnumGetMethod.NAME_IGNORECASE);
			HealthMonitorVolume = builder
				.comment("Set the volume of the beeps [0..1]")
				.defineInRange("HealthMonitorVolume", 1.0d, 0.0d, 1.0d);
			LowHealthSoundThreshold = builder
				.comment("A sound will start playing when you have less than this much health left")
				.defineInRange("LowHealthSoundThreshold", 6, 1, 10);
			PlayFasterNearDeath = builder
				.comment("Play the warning sounds quicker the closer you get to dieing")
				.define("PlayFasterNearDeath", false);
		}
		builder.pop();

		// ######################################################################
		// Horse Info Module
		// ######################################################################
		builder.comment(
			"Animal Info gives you information about horse stats, such as speed and jump height."
		).push(CATEGORY_ANIMALINFO);
		{
			AnimalInfoMaxViewDistance = builder
				.comment("How far away animal info will be rendered on the screen (distance measured in blocks)")
				.defineInRange("AnimalInfoMaxViewDistance", 8, 1, 64);
			AnimalInfoMode = builder
				.comment("Sets the Animal Info mode. Valid modes are ON and OFF")
				.defineEnum("AnimalInfoMode", AnimalInfo.Modes.OFF, EnumGetMethod.NAME_IGNORECASE);
			EnableAnimalInfo = builder
				.comment("Enable/Disable Animal Info")
				.define("EnableAnimalInfo", true);
			HorseInfoNumberOfDecimalsDisplayed = builder
				.comment("How many decimal places will be used when displaying horse stats. Range 0-6")
				.defineInRange("HorseInfoNumberOfDecimalsDisplayed", 1, 0, 6);
			ShowBreedingIcons = builder
				.comment("Enable/Disable showing an icon if the animal is ready to breed")
				.define("ShowBreedingIcons", true);
			ShowHorseStatsOnF3Menu = builder
				.comment("Enable/Disable showing the stats of the horse you're riding on the F3 screen")
				.define("ShowHorseStatsOnF3Menu", true);
			ShowHorseStatsOverlay = builder
				.comment("Enable/Disable showing the stats of horses on screen")
				.define("ShowHorseStatsOverlay", true);
			ShowTextBackgrounds = builder
				.comment("Enable/Disable showing a black background behind text")
				.define("ShowTextBackgrounds", true);
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
				.define("EnableInfoLine", true);
			InfoLineLocationHorizontal = builder
				.comment("The horizontal position of the info line. 1 is left, 400 is far right")
				.defineInRange("InfoLineLocationHorizontal", 1, 1, 400);
			InfoLineLocationVertical = builder
				.comment("The vertical position of the info line. 1 is top, 200 is very bottom")
				.defineInRange("InfoLineLocationVertical", 1, 1, 200);
			ShowBiome = builder
				.comment("Enable/Disable showing what biome you are in on the info line")
				.define("ShowBiome", false);
			ShowCanSnow = builder
				.comment("Enable/Disable showing if it can snow at the player's feet on the info line")
				.define("ShowCanSnow", false);
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
					"Enables/Disable using mouse wheel scrolling whilst holding LMENU to swap the selected item with an inventory item")
				.define("EnableItemSelector", true);
			ItemSelectorMode = builder
				.comment("Sets the Item Selector mode. Valid modes are ALL and SAME_COLUMN")
				.defineEnum("ItemSelectorMode", ItemSelector.Modes.ALL, EnumGetMethod.NAME_IGNORECASE);
			ItemSelectorSideButtons = builder
				.comment("Enable/disable use of side buttons for item selection")
				.define("ItemSelectorSideButtons", false);
			ItemSelectorTimeout = builder
				.comment(
					"Specifies how many ticks until the item selector confirms your choice and performs the item swap")
				.defineInRange(
					"ItemSelectorTimeout", ItemSelector.defaultTimeout, ItemSelector.minTimeout,
					ItemSelector.maxTimeout
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
				.define("ShowAnvilRepairs", true);
			UseQuickPlaceSign = builder
				.comment("Enable/Disable being able to place a sign with no text by sneaking while placing a sign")
				.define("UseQuickPlaceSign", false);
			UseUnlimitedSprinting = builder
				.comment("Enable/Disable overriding the default sprint behavior and run forever")
				.define("UseUnlimitedSprinting", false);
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
				.define("EnablePlayerLocator", true);
			PlayerLocatorMinViewDistance = builder
				.comment("Stop showing player names when they are this close (distance measured in blocks).")
				.defineInRange(
					"PlayerLocatorMinViewDistance", 0, PlayerLocator.minViewDistanceCutoff,
					PlayerLocator.maxViewDistanceCutoff
				);
			PlayerLocatorMode = builder
				.comment("Sets the Player Locator mode. Valid modes are OFF and ON")
				.defineEnum("PlayerLocatorMode", PlayerLocator.Modes.OFF, EnumGetMethod.NAME_IGNORECASE);
			ShowDistanceToPlayers = builder
				.comment("Show how far away you are from the other players next to their name")
				.define("ShowDistanceToPlayers", false);
			ShowPlayerHealth = builder
				.comment("Show how much health players have by their name")
				.define("ShowPlayerHealth", false);
			ShowWitherSkeletons = builder
				.comment("Show your tamed wolves in addition to other players.")
				.define("ShowWitherSkeletons", false);
			ShowWolves = builder
				.comment("Show your tamed wolves in addition to other players")
				.define("ShowWolves", true);
			UseWolfColors = builder
				.comment("Use the color of your wolf's collar to colorize their name.")
				.define("UseWolfColors", true);
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
					"Enables pressing " +
					bindingToKeyName(ZyinHUDKeyHandlers.KEY_BINDINGS[6]) +
					" to drink a potion even if it is  in your inventory and not your hotbar.")
				.define("EnablePotionAid", true);
		}
		builder.pop();

		// ######################################################################
		// Potion Timers Module
		// ######################################################################
//		builder.comment(
//			"Potion Timers shows the duration remaining on potions that you drink."
//		).push(CATEGORY_POTIONTIMERS);
//		{
//			DurabilityInfoTextMode = builder
//				.comment("Sets Potion Timer's text display mode. Valid modes are WHITE, COLORED, and NONE")
//				.defineEnum("DurabilityInfoTextMode", PotionTimers.TextModes.COLORED, EnumGetMethod.NAME_IGNORECASE);
//			EnablePotionTimers = builder
//				.comment("Enable/Disable showing the time remaining on potions")
//				.define("EnablePotionTimers", true);
//			HidePotionEffectsInInventory = builder
//				.comment("Enable/Disable hiding the default potion effects when you open your inventory.")
//				.define("HidePotionEffectsInInventory", false);
//			PotionScale = builder
//				.comment("How large the potion timers are rendered, 1.0 being the normal size. Range 0.25-5.0")
//				.defineInRange("PotionScale", 1.0d, 0.25d, 5.0d);
//			PotionTimersLocationHorizontal = builder
//				.comment("The horizontal position of the potion timers. 0 is left, 400 is far right")
//				.defineInRange("PotionTimersLocationHorizontal", 1, 0, 400);
//			PotionTimersLocationVertical = builder
//				.comment("The vertical position of the potion timers. 0 is top, 200 is very bottom")
//				.defineInRange("PotionTimersLocationVertical", 16, 0, 200);
//			ShowPotionIcons = builder
//				.comment("Enable/Disable showing the status effect of potions next to the timers.")
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
			BlacklistArrow = builder
				.comment("Stop Quick Deposit from putting arrows in chests?")
				.define("BlacklistArrows", false);
			BlacklistEnderPearl = builder
				.comment("Stop Quick Deposit from putting ender pearls in chests?")
				.define("BlacklistEnderPearl", false);
			BlacklistFood = builder
				.comment("Stop Quick Deposit from putting food in chests?")
				.define("BlacklistFood", false);
			BlacklistTools = builder
				.comment("Stop Quick Deposit from putting tools (picks, axes, shovels, shears) in chests?")
				.define("BlacklistTools", true);
			BlacklistTorch = builder
				.comment("Stop Quick Deposit from putting torches in chests?")
				.define("BlacklistTorches", false);
			BlacklistWaterBucket = builder
				.comment("Stop Quick Deposit from putting water buckets in chests?")
				.define("BlacklistWaterBucket", false);
			BlacklistWeapons = builder
				.comment("Stop Quick Deposit from putting swords and bows in chests?")
				.define("BlacklistWeapons", true);
			CloseChestAfterDepositing = builder
				.comment(
					"Closes the chest GUI after you deposit your items in it. Allows quick and easy depositing of all your items into multiple chests.")
				.define("CloseChestAfterDepositing", false);
			EnableQuickDeposit = builder
				.comment("Enables Quick Deposit.")
				.define("EnableQuickDeposit", true);
			IgnoreItemsInHotbar = builder
				.comment("Determines if items in your hotbar will be deposited into chests when 'X' is pressed.")
				.define("IgnoreItemsInHotbar", false);
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
				.define("EnableSafeOverlay", true);
			SafeOverlayDisplayInNether = builder
				.comment("Enable/Disable showing unsafe areas in the Nether")
				.define("SafeOverlayDisplayInNether", false);
			SafeOverlayDrawDistance = builder
				.comment(
					"How far away unsafe spots should be rendered around the player measured in blocks. This can be changed in game with - + L and + + L.")
				.defineInRange(
					"SafeOverlayDrawDistance", SafeOverlay.defaultDrawDistance, SafeOverlay.minDrawDistance,
					SafeOverlay.maxDrawDistance
				);
			SafeOverlayMode = builder
				.comment("Sets the Safe Overlay mode. Valid modes are OFF and On")
				.defineEnum("SafeOverlayMode", SafeOverlay.Modes.OFF, EnumGetMethod.NAME_IGNORECASE);
			SafeOverlaySeeThroughWalls = builder
				.comment("Enable/Disable showing unsafe areas through walls. Toggle in game with Ctrl + L.")
				.define("SafeOverlaySeeThroughWalls", false);
			SafeOverlayTransparency = builder
				.comment(
					"The transparency of the unsafe marks. Range [0.11, 1.0]")
				.defineInRange("SafeOverlayTransparency", 0.3d, 0.11d, 1.0d);
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
				.comment("Enable/Disable using Torch Aid to help you place torches more easily.")
				.define("EnableTorchAid", false);
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
				.comment("Enables pressing " +
				         bindingToKeyName(ZyinHUDKeyHandlers.KEY_BINDINGS[9]) +
				         " to swap between your sword and bow")
				.define("EnableWeaponSwap", true);
		}
		builder.pop();
	}
	private static String bindingToKeyName(KeyBinding key){
		String s = key.getTranslationKey();
		return s.substring(1+s.lastIndexOf('.')).toUpperCase();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		ZyinHUD.ZyinLogger.debug("Loaded Zyin's HUD config file {}", configEvent.getConfig().getFileName());
	}


	@SubscribeEvent
	public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
		ZyinHUD.ZyinLogger.fatal("Zyin's HUD config just got changed on the file system!");
	}

	public static ForgeConfigSpec getConfigSpec() { return SPEC; }
}
