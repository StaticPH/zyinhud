package com.zyin.zyinhud;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ZyinHUDConfig {

	private static ForgeConfigSpec SPEC; // Will hold the built config when done
	public static Builder BUILDER;
	public static ForgeConfigSpec SPEC_OVERRIDE;
	public static Builder BUILDER_OVERRIDE;

	public static final String CATEGORY_DEBUG = "debug options";

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
	// Mod Debugging Options
	// ######################################################################
	// Enable/Disable logging every entity found by any module that deals with entities in the world.
	public static BooleanValue enableLoggingAllEntitiesFound;
	// Enable/Disable logging whenever any of the mod's keybindings receive input, and what key they are bound to
	public static BooleanValue enableLoggingKeybindInputs;
	// Enable/Disable logging when a tool or armor piece has been automatically unequipped to prevent it from breaking
	public static BooleanValue enableLoggingUnequip;

	// ######################################################################
	// Animal(Mainly Horse) Info Module
	// ######################################################################
	// Enable/Disable Animal Info
	public static BooleanValue enableAnimalInfo;
	// How far away animal info will be rendered on the screen (distance measured in blocks). Range [1, 64]
	public static IntValue animalInfoMaxViewDistance;
	// Sets the Animal Info mode. Valid modes are ON and OFF
	public static EnumValue<AnimalInfoOptions.AnimalInfoModes> animalInfoMode;
	// How many decimal places will be used when displaying horse stats
	public static IntValue animalInfoNumberOfDecimalsDisplayed;
	// Enable/Disable showing an icon if the animal is ready to breed
	public static BooleanValue showBreedingIcons;
	// Enable/Disable showing the stats of the horse you're riding on the F3 screen
	public static BooleanValue showHorseStatsOnF3Menu;
	// Enable/Disable showing the stats of horses on screen
	public static BooleanValue showHorseStatsOverlay;
	// Enable/Disable showing a black background behind text
	public static BooleanValue showTextBackgrounds;

	// ######################################################################
	// Clock Module
	// ######################################################################
	// Enable/Disable showing the clock
	public static BooleanValue enableClock;
	// Sets the clock mode. Valid modes are STANDARD, COUNTDOWN, and GRAPHIC
	public static EnumValue<ClockOptions.ClockModes> clockMode;

	// ######################################################################
	//Compass Module
	// ######################################################################
	// Enable/Disable showing the compass
	public static BooleanValue enableCompass;
	// Renders the compass text in the center of the screen instead of on the side
	public static BooleanValue renderCompassTextInMiddle;

	// ######################################################################
	// Coordinates Module
	// ######################################################################
	// Enable/Disable showing your coordinates
	public static BooleanValue enableCoordinates;
	// The format used when sending your coordinates in a chat message by pressing the keybind
	// {x}{y}{z} are replaced with actual coordinates
	public static ConfigValue<String> coordinatesChatStringFormat;
	// Sets the coordinates mode. Valid modes are XYZ and XZY
	public static EnumValue<CoordinateOptions.CoordinateModes> coordinatesMode;
	// Shows how far into the 16x16 chunk you're in
	public static BooleanValue showChunkCoordinates;
	// Shows the coordinates and dimension where you died
	public static BooleanValue showDeathLocation;
	// Color code the Y (height) coordinate based on what ores can spawn at that level
	public static BooleanValue useYCoordinateColors;

	// ######################################################################
	// Distance Measurer Module
	// ######################################################################
	// Enable/Disable the distance measurer
	public static BooleanValue enableDistanceMeasurer;
	// Sets the Distance Measurer mode. Valid modes are OFF, SIMPLE, and COORDINATE
	public static EnumValue<DistanceMeasurerOptions.DistanceMeasurerModes> distanceMeasurerMode;

	// ######################################################################
	// Durability Info Module
	// ######################################################################
	// Enable/Disable showing all durability info
	public static BooleanValue enableDurabilityInfo;
	// Display when armor gets damaged less than this fraction of its durability
	public static DoubleValue armorDurabilityDisplayThreshold;
	// Enable/Disable automatically unequipping armor before it breaks.
	public static BooleanValue autoUnequipArmor;
	// Enable/Disable automatically unequipping tools before they breaks
	public static BooleanValue autoUnequipTools;
	// The horizontal position of the durability icons. 0 is left, 400 is far right
	public static IntValue durabilityHorizontalPos;
	// Sets Durability Info's number display mode. Valid modes are NONE, TEXT, and PERCENTAGE
	public static EnumValue<DurabilityInfoOptions.DurabilityInfoTextModes> durabilityInfoTextMode;
	// How large the durability icons are rendered, 1.0 being the normal size
	public static DoubleValue durabilityScale;
	// The vertical position of the durability icons. 0 is top, 200 is very bottom
	public static IntValue durabilityVerticalPos;
	// Hide/Show durability info while chat is open.
	public static BooleanValue hideDurabilityInfoInChat;
	// Display when an item gets damaged less than this fraction of its durability
	public static DoubleValue itemDurabilityDisplayThreshold;
	// Enable/Disable showing breaking armor
	public static BooleanValue showArmorDurability;
	// Enable/Disable showing armor peices instead of the big broken armor icon
	public static BooleanValue showIndividualArmorIcons;
	// Enable/Disable showing breaking items
	public static BooleanValue showItemDurability;
	// Toggle using colored numbering
	public static BooleanValue useColoredNumbers;

	// ######################################################################
	// Eating Aid Module
	// ######################################################################
	// Enables pressing the eating aid keybind to eat food even if it is  in your inventory and not your hotbar
//	public static BooleanValue enableEatingAid;
	// Enable/Disable using golden apples and golden carrots as food
//	public static BooleanValue eatGoldenFood;
	// Enable/Disable eating raw chicken, beef, and porkchops
//	public static BooleanValue eatRawFood;
	// Sets the Eating Aid mode. Valid modes are BASIC and INTELLIGENT
//	public static EnumValue<EatingAidOptions.EatingAidModes> eatingAidMode;
	// Use food that is in your hotbar before looking for food in your main inventory
//	public static BooleanValue prioritizeFoodInHotbar;
	// If you are connected to a Bukkit server that uses PvP Soup or Fast Soup (mushroom stew) with this enabled,
	// Eating Aid will use it instead of other foods
//	public static BooleanValue usePvPSoup;

	// ######################################################################
	// Ender Pearl Aid Module
	// ######################################################################
	// Enables pressing the Ender Pearl Aid hotkey to use an ender pearl even
	// if it is in your main inventory and not your hotbar
	public static BooleanValue enableEnderPearlAid;

	// ######################################################################
	// FPS Module
	// ######################################################################
	// Enable/Disable showing your FPS at the end of the Info Line
	public static BooleanValue enableFPS;

	// ######################################################################
	// Health Monitor Module
	// ######################################################################
	// Enable/Disable using the Health Monitor.
	public static BooleanValue enableHealthMonitor;
	// Sets the Health Monitor sound mode. Valid modes are OOT, LTTP, ORACLE, LA, LOZ, and AOL
	public static EnumValue<HealthMonitorOptions.HealthMonitorModes> healthMonitorMode;
	// Set the volume of the beeps. Range [0.0, 1.0]
	public static DoubleValue healthMonitorVolume;
	// A sound will start playing when you have less than this much health left
	public static IntValue lowHealthSoundThreshold;
	// Play the warning sounds quicker the closer you get to dieing
	public static BooleanValue playFasterNearDeath;

	// ######################################################################
	// Info Line Module
	// ######################################################################
	// Enable/Disable the entire info line in the top left part of the screen.
	// This includes the clock, coordinates, compass, module status, etc
	public static BooleanValue enableInfoLine;
	// The horizontal position of the info line. 1 is left, 400 is far right
	public static IntValue infoLineHorizontalPos;
	// The vertical position of the info line. 1 is top, 200 is very bottom
	public static IntValue infoLineVerticalPos;
	// Enable/Disable showing what biome you are in on the Info Line
	public static BooleanValue showBiome;
	// Enable/Disable showing if it can snow at the player's feet on the Info Line
	public static BooleanValue showCanSnow;
	// Enable/Disable showing the player's ping while on a multiplayer server
	public static BooleanValue showPing;

	// ######################################################################
	// Item Selector Module
	// ######################################################################
	// Enables/Disable using mouse wheel scrolling whilst holding the 
	// Item Selector hotkey to swap the selected item with an inventory item
	public static BooleanValue enableItemSelector;
	// Sets the Item Selector mode. Valid modes are ALL and SAME_COLUMN
	public static EnumValue<ItemSelectorOptions.ItemSelectorModes> itemSelectorMode;
	// Enable/disable use of side buttons for item selection
	public static BooleanValue itemSelectorSideButtons;
	// Specifies how many ticks until the item selector confirms your choice and performs the item swap
	public static IntValue itemSelectorTimeout;

	// ######################################################################
	// Miscellaneous Module
	// ######################################################################
	// Enable/Disable showing the repair count on items while using the anvil
	public static BooleanValue showAnvilRepairs;
	// Enable/Disable being able to place a sign with no text by sneaking while placing a sign
	public static BooleanValue useQuickPlaceSign;
	// Enable/Disable overriding the default sprint behavior and run forever
	public static BooleanValue useUnlimitedSprintingSP;

	// ######################################################################
	// Player Locator Module
	// ######################################################################
	// Enable/Disable the Player Locator.
	public static BooleanValue enablePlayerLocator;
	// Stop showing player names when they are this close (distance measured in blocks).
	public static IntValue playerLocatorMinViewDistance;
	// Sets the Player Locator mode. Valid modes are OFF and ON
	public static EnumValue<LocatorOptions.LocatorModes> playerLocatorMode;
	// Show how far away you are from the other players next to their name.
	public static BooleanValue showDistanceToPlayers;
	// Show how much health players have by their name.
	public static BooleanValue showPlayerHealth;
	// Show wither skeletons in addition to other players.
	public static BooleanValue showWitherSkeletons;
	// Show your tamed wolves in addition to other players.
	public static BooleanValue showWolves;
	// Use the color of your wolf's collar to colorize their name.
	public static BooleanValue useWolfColors;

	// ######################################################################
	// Potion Aid Module
	// ######################################################################
	// Enables pressing potion aid keybind to drink a potion even if it is in 
	// your main inventory and not your hotbar.
	public static BooleanValue enablePotionAid;

	// ######################################################################
	// Potion Timers Module
	// ######################################################################
	// Enable/Disable showing the time remaining on potions.
//	public static BooleanValue enablePotionTimers;
	// Sets Potion Timer's text display mode. Valid modes are WHITE, COLORED, and NONE
//	public static EnumValue<PotionTimerOptions.PotionTimerModes> potionTimerMode;
	// Enable/Disable hiding the default potion effects when you open your inventory.
//	public static BooleanValue hidePotionEffectsInInventory;
	// How large the potion timers are rendered, 1.0 being the normal size. Range [0.25, 5.0]
//	public static DoubleValue potionScale;
	// The horizontal position of the potion timers. 0 is left, 400 is far right.
//	public static IntValue potionTimersHorizontalPos;
	// The vertical position of the potion timers. 0 is top, 200 is very bottom.
//	public static IntValue potionTimersVerticalPos;
	// Enable/Disable showing the status effect of potions next to the timers.
//	public static BooleanValue showPotionIcons;
	//TODO?
	// hideBeaconPotionEffects, boolean, default false
	// showVanillaStatusEffectHUD, boolean, default true
	// showEffectName, boolean, default true
	// showEffectLevel, boolean, default true

	// ######################################################################
	// Quick Deposit Module
	// ######################################################################
	// Enables Quick Deposit.
	public static BooleanValue enableQuickDeposit;
	// Stop Quick Deposit from putting arrows in chests?
	public static BooleanValue blacklistArrow;
	// Stop Quick Deposit from putting clocks and compasses in chests?
	public static BooleanValue blacklistClockCompass;
	// Stop Quick Deposit from putting ender pearls in chests?
	public static BooleanValue blacklistEnderPearl;
	// Stop Quick Deposit from putting food in chests?
	public static BooleanValue blacklistFood;
	// Stop Quick Deposit from putting tools (picks, axes, shovels, shears) in chests?
	public static BooleanValue blacklistTools;
	// Stop Quick Deposit from putting torches in chests?
	public static BooleanValue blacklistTorch;
	// Stop Quick Deposit from putting water buckets in chests?
	public static BooleanValue blacklistWaterBucket;
	// Stop Quick Deposit from putting swords and bows in chests?
	public static BooleanValue blacklistWeapons;
	// Closes the chest GUI after you deposit your items in it. 
	// Allows quick and easy depositing of all your items into multiple chests.
	public static BooleanValue closeChestAfterDepositing;
	// Determines if items in your hotbar will be deposited into chests when 
	// the Quick Deposit hotkey is pressed.
	public static BooleanValue ignoreItemsInHotbar;

	// ######################################################################
	// Safe Overlay Module
	// ######################################################################
	// Enable/Disable the Safe Overlay.
	public static BooleanValue enableSafeOverlay;
	// Enable/Disable showing unsafe areas in the Nether.
	public static BooleanValue safeOverlayDisplayInNether;
	// How far away unsafe spots should be rendered around the player measured in blocks. 
	// This can be changed in game with - + <hotkey> and + + <hotkey>.
	public static IntValue safeOverlayDrawDistance;
	// Sets the Safe Overlay mode. Valid modes are OFF and On
	public static EnumValue<SafeOverlayOptions.safeOverlayModes> safeOverlayMode;
	// Enable/Disable showing unsafe areas through walls. Toggle in game with Ctrl + <hotkey>.
	public static BooleanValue safeOverlaySeeThroughWalls;
	// The transparency of the unsafe marks. Range [0.11, 1.0].
	public static DoubleValue safeOverlayTransparency;

	// ######################################################################
	// Torch Aid Module
	// ######################################################################
	// Enable/Disable using Torch Aid to help you place torches more easily.
	public static BooleanValue enableTorchAid;

	// ######################################################################
	// Weapon Swap Module
	// ######################################################################
	// Enables pressing your weapon-swap keybind to swap between your sword and bow.
	public static BooleanValue enableWeaponSwap;


	static {
		BUILDER = new Builder();
		BUILDER_OVERRIDE = new Builder();
		configure(BUILDER, BUILDER_OVERRIDE);
		SPEC = BUILDER.build();
		SPEC_OVERRIDE = BUILDER_OVERRIDE.build();
	}

	//TODO: custom implementation of some .define* methods that allow for placement of a comment
	// after ranges/accepted values, but before the configuration option itself.

	//TODO: config option for quickdeposit not to deposit (item)"minecraft:tipped_arrow" and (item)"minecraft:spectral_arrow"
	//			maybe I should add an option that holds a list of namespaced items(/?tags?) that should be handled the same way as regular arrows are?
	//      add configurable lists of namespaced items(/?tags?) for quickdeposit to treat as tools, weapons, torches, or arrows
	//          or just provide a data Tag in the zyinhud namespace for each, and let the end user deal with it?
	private static void configure(Builder builder, Builder builder_override) {
		// ######################################################################
		// Mod Debugging Options
		// ######################################################################
		builder.comment(
			"These options control certain logging/debugging code for this mod.",
			"NOTE: these options may result in decreased performance, very large log files, and/or console spam.",
			"You probably shouldn't change these unless you know what you're doing."
		).push(CATEGORY_DEBUG);
		{
			enableLoggingKeybindInputs = builder
				.comment(
					"Enable/Disable logging whenever any of the mod's keybindings receive input, and what key they are bound to"
				)
				.define("enableLoggingKeybindInputs", false);
			enableLoggingAllEntitiesFound = builder
				.comment(
					"Enable/Disable logging every entity found by any module that deals with entities in the world."
				)
				.define("enableLoggingAllEntitiesFound", false);
			enableLoggingUnequip = builder
				.comment(
					"Enable/Disable logging when a tool or armor piece has been automatically unequipped to prevent it from breaking."
				)
				.define("enableLoggingUnequip", false);
		}
		builder.pop();

		// ######################################################################
		// Animal(Mainly Horse) Info Module
		// ######################################################################
		builder.comment(
			"Animal Info gives you information about horse(also mule, donkey, and llama) stats, such as speed and jump height."
		).push(CATEGORY_ANIMALINFO);
		{
			enableAnimalInfo = builder
				.comment("Enable/Disable Animal Info")
				.define("enableAnimalInfo", AnimalInfoOptions.defaultEnabled);
			animalInfoMaxViewDistance = builder
				.comment("How far away animal info will be rendered on the screen (distance measured in blocks).")
				.defineInRange(
					"animalInfoMaxViewDistance", AnimalInfoOptions.defaultViewDistanceCutoff,
					AnimalInfoOptions.minViewDistanceCutoff, AnimalInfoOptions.maxViewDistanceCutoff
				);
			animalInfoMode = builder
				.comment("Sets the Animal Info mode.")
				.defineEnum("animalInfoMode", AnimalInfoOptions.AnimalInfoModes.OFF, EnumGetMethod.NAME_IGNORECASE);
			animalInfoNumberOfDecimalsDisplayed = builder
				.comment("How many decimal places will be used when displaying horse stats.")
				.defineInRange(
					"animalInfoNumberOfDecimalsDisplayed", AnimalInfoOptions.defautNumberOfDecimalsDisplayed,
					AnimalInfoOptions.minDecimalsDisplayed, AnimalInfoOptions.maxDecimalsDisplayed
				);
			showBreedingIcons = builder
				.comment("Enable/Disable showing an icon if the animal is ready to breed")
				.define("showBreedingIcons", AnimalInfoOptions.defaultShowBreedingIcons);
			showHorseStatsOnF3Menu = builder
				.comment("Enable/Disable showing the stats of the horse you're riding on the F3 screen")
				.define("showHorseStatsOnF3Menu", AnimalInfoOptions.defaultShowHorseStatsOnF3Menu);
			showHorseStatsOverlay = builder
				.comment("Enable/Disable showing the stats of horses on screen")
				.define("showHorseStatsOverlay", AnimalInfoOptions.defaultShowHorseStatsOverlay);
			showTextBackgrounds = builder
				.comment("Enable/Disable showing a black background behind text")
				.define("showTextBackgrounds", AnimalInfoOptions.defaultShowTextBackgrounds);
		}
		builder.pop();

		// ######################################################################
		// Clock Module
		// ######################################################################
		builder.comment(
			"Clock shows you time relevant to Minecraft time."
		).push(CATEGORY_CLOCK);
		{
			enableClock = builder
				.comment("Enable/Disable showing the clock")
				.define("enableClock", ClockOptions.defaultEnabled);
			clockMode = builder
				.comment("Sets the clock mode.")
				.defineEnum("clockMode", ClockOptions.ClockModes.STANDARD, EnumGetMethod.NAME_IGNORECASE);
		}
		builder.pop();

		// ######################################################################
		//Compass Module
		// ######################################################################
		builder.comment(
			"Compass displays a text compass."
		).push(CATEGORY_COMPASS);
		{
			enableCompass = builder
				.comment("Enable/Disable showing the compass")
				.define("enableCompass", CompassOptions.defaultEnabled);
			renderCompassTextInMiddle = builder
				.comment("Renders the compass text in the center of the screen instead of on the side")
				.define("renderCompassTextInMiddle", CompassOptions.defaultRenderCompassTextInMiddle);
		}
		builder.pop();

		// ######################################################################
		// Coordinates Module
		// ######################################################################
		builder.comment(
			"Coordinates displays your coordinates. Nuff said."
		).push(CATEGORY_COORDINATES);
		{
			enableCoordinates = builder
				.comment("Enable/Disable showing your coordinates")
				.define("enableCoordinates", CoordinateOptions.defaultEnabled);
			coordinatesChatStringFormat = builder
				.comment(
					"The format used when sending your coordinates in a chat message by pressing the " +
					CATEGORY_COORDINATES + " keybind (default: 'F4')."
				)
				.comment("{x}{y}{z} are replaced with actual coordinates")
				.define("coordinatesChatStringFormat", CoordinateOptions.defaultChatStringFormat);
			coordinatesMode = builder
				.comment("Sets the coordinates mode.")
				.defineEnum("coordinatesMode", CoordinateOptions.CoordinateModes.XYZ, EnumGetMethod.NAME_IGNORECASE);
			// Shows the coordinates and dimension where you died
			showDeathLocation = builder
				.comment("Print to the player's chat the coordinates and dimension at the moment of their death.")
				.define("showDeathLocation", CoordinateOptions.defaultShowDeathLocation);
			showChunkCoordinates = builder
				.comment("Shows how far into the 16x16 chunk you're in")
				.define("showChunkCoordinates", CoordinateOptions.defaultShowChunkCoordinates);
			useYCoordinateColors = builder
				.comment("Color code the Y (height) coordinate based on what ores can spawn at that level")
				.define("useYCoordinateColors", CoordinateOptions.defaultUseYCoordinateColors);
		}
		builder.pop();

		// ######################################################################
		// Distance Measurer Module
		// ######################################################################
		builder.comment(
			"Distance Measurer can calculate distances between you and blocks that you aim at."
		).push(CATEGORY_DISTANCEMEASURER);
		{
			enableDistanceMeasurer = builder
				.comment("Enable/Disable the distance measurer")
				.define("enableDistanceMeasurer", DistanceMeasurerOptions.defaultEnabled);
			distanceMeasurerMode = builder
				.comment("Sets the Distance Measurer mode.")
				.defineEnum(
					"distanceMeasurerMode",
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
			enableDurabilityInfo = builder
				.comment("Enable/Disable showing all durability info")
				.define("enableDurabilityInfo", DurabilityInfoOptions.defaultEnabled);
			autoUnequipArmor = builder
				.comment("Enable/Disable automatically unequipping armor before it breaks")
				.define("autoUneqipArmor", DurabilityInfoOptions.defaultAutoUnequipArmor);
			autoUnequipTools = builder
				.comment("Enable/Disable automatically unequipping tools before they breaks")
				.define("autoUnequipTools", DurabilityInfoOptions.defaultAutoUnequipTools);
			armorDurabilityDisplayThreshold = builder
				.comment("Display when armor has less than this fraction of its durability")
				.defineInRange(
					"armorDurabilityDisplayThreshold",
					DurabilityInfoOptions.defaultDurabilityDisplayThreshold,
					DurabilityInfoOptions.minDurabilityDisplayThreshold,
					DurabilityInfoOptions.maxDurabilityDisplayThreshold
				);
			hideDurabilityInfoInChat = builder
				.comment("Hide/Show durability info while chat is open.")
				.define("hideDurabilityInfoInChat", DurabilityInfoOptions.defaultHideDurabilityInfoInChat);
			itemDurabilityDisplayThreshold = builder
				.comment("Display when an item has less than this fraction of its durability")
				.defineInRange(
					"itemDurabilityDisplayThreshold",
					DurabilityInfoOptions.defaultDurabilityDisplayThreshold,
					DurabilityInfoOptions.minDurabilityDisplayThreshold,
					DurabilityInfoOptions.maxDurabilityDisplayThreshold
				);
			durabilityInfoTextMode = builder
				.comment("Sets Durability Info's number display mode.")
				.defineEnum(
					"durabilityInfoTextMode", DurabilityInfoOptions.DurabilityInfoTextModes.NONE,
					EnumGetMethod.NAME_IGNORECASE
				);
			durabilityHorizontalPos = builder
				.comment(
					"The horizontal position of the durability icons. The far left is at " +
					DurabilityInfoOptions.minArmorPosXY + ", and " +
					DurabilityInfoOptions.maxArmorIconPosX + " is the far right."
				)
				.defineInRange(
					"durabilityHorizontalPos", DurabilityInfoOptions.defaultArmorIconPosX,
					DurabilityInfoOptions.minArmorPosXY, DurabilityInfoOptions.maxArmorIconPosX
				);
			durabilityVerticalPos = builder
				.comment(
					"The vertical position of the durability icons. The top is at " +
					DurabilityInfoOptions.minArmorPosXY + ", and " +
					DurabilityInfoOptions.maxArmorIconPosY + " is the very bottom."
				)
				.defineInRange(
					"durabilityVerticalPos", DurabilityInfoOptions.defaultArmorIconPosY,
					DurabilityInfoOptions.minArmorPosXY, DurabilityInfoOptions.maxArmorIconPosY
				);
			durabilityScale = builder
				.comment("How large the durability icons are rendered, 1.0 being the normal size")
				.defineInRange(
					"durabilityScale", DurabilityInfoOptions.defaultDurabilityIconScale,
					DurabilityInfoOptions.minIconScale, DurabilityInfoOptions.maxIconScale
				);
			showArmorDurability = builder
				.comment("Enable/Disable showing breaking armor")
				.define("showArmorDurability", DurabilityInfoOptions.defaultShowArmorDurability);
			showIndividualArmorIcons = builder
				.comment("Enable/Disable showing armor peices instead of the big broken armor icon")
				.define("showIndividualArmorIcons", DurabilityInfoOptions.defaultShowIndividualArmorIcons);
			showItemDurability = builder
				.comment("Enable/Disable showing breaking items")
				.define("showItemDurability", DurabilityInfoOptions.defaultShowItemDurability);
			useColoredNumbers = builder
				.comment("Toggle using colored numbering")
				.define("useColoredNumbers", DurabilityInfoOptions.defaultUseColoredNumbers);
		}
		builder.pop();

		// ######################################################################
		// Eating Aid Module
		// ######################################################################
//		builder.comment(
//			"Eating Aid makes eating food quick and easy."
//		).push(CATEGORY_EATINGAID);
//		{
//			enableEatingAid = builder
//				.comment(
//					"Enables pressing the " + CATEGORY_EATINGAID + " keybind (default: 'G')" +
//					" to eat food even if it is in your main inventory and not your hotbar"
//				)
//				.define("enableEatingAid", EatingAidOptions.defaultEnabled);
//			eatGoldenFood = builder
//				.comment("Enable/Disable using golden apples and golden carrots as food")
//				.define("eatGoldenFood", EatingAidOptions.defaultEatGoldenFood);
//			eatRawFood = builder
//				.comment("Enable/Disable eating raw chicken, beef, and porkchops")
//				.define("eatRawFood", EatingAidOptions.defaultEatRawFood);
//			eatingAidMode = builder
//				.comment("Sets the Eating Aid mode.")
//				.defineEnum("eatingAidMode", EatingAidOptions.EatingAidModes.INTELLIGENT, EnumGetMethod.NAME_IGNORECASE);
//			prioritizeFoodInHotbar = builder
//				.comment("Use food that is in your hotbar before looking for food in your main inventory")
//				.define("prioritizeFoodInHotbar", EatingAidOptions.defaultPrioritizeFoodInHotbar);
//			usePvPSoup = builder
//				.comment(
//					"If you are connected to a Bukkit server that uses PvP Soup or Fast Soup (mushroom stew) with this enabled, " +
//					"Eating Aid will use it instead of other foods"
//				)
//				.define("usePvPSoup", EatingAidOptions.defaultUsePvPSoup);
//		}
//		builder.pop();

		// ######################################################################
		// Ender Pearl Aid Module
		// ######################################################################
		builder.comment(
			"Ender Pearl Aid makes it easier to quickly throw ender pearls."
		).push(CATEGORY_ENDERPEARLAID);
		{
			enableEnderPearlAid = builder
				.comment(
					"Enables pressing the " + CATEGORY_ENDERPEARLAID + " keybind (default: 'C')" +
					" to use an enderpearl even if it is in your main inventory and not your hotbar"
				)
				.define("enableEnderPearlAid", EnderPearlAidOptions.defaultEnabled);
		}
		builder.pop();

		// ######################################################################
		// FPS Module
		// ######################################################################
		builder.comment(
			"FPS shows your frames per second without having to go into the F3 menu."
		).push(CATEGORY_FPS);
		{
			enableFPS = builder
				.comment("Enable/Disable showing your FPS at the end of the Info Line")
				.define("enableFPS", FpsOptions.defaultEnabled);
		}
		builder.pop();

		// ######################################################################
		// Health Monitor Module
		// ######################################################################
		builder.comment(
			"Plays warning beeps when you are low on health."
		).push(CATEGORY_HEALTHMONITOR);
		{
			enableHealthMonitor = builder
				.comment("Enable/Disable using the Health Monitor")
				.define("enableHealthMonitor", HealthMonitorOptions.defaultEnabled);
			healthMonitorMode = builder
				.comment("Sets the Health Monitor sound.")
				.defineEnum(
					"healthMonitorMode", HealthMonitorOptions.HealthMonitorModes.OOT,
					EnumGetMethod.NAME_IGNORECASE
				);
			healthMonitorVolume = builder
				.comment("Set the volume of the beeps")
				.defineInRange(
					"healthMonitorVolume", (float) HealthMonitorOptions.defaultSoundVolume,
					HealthMonitorOptions.minSoundVolume, HealthMonitorOptions.maxSoundVolume
				);
			lowHealthSoundThreshold = builder
				.comment("A sound will start playing when you have less than this much health left")
				// Apparently the max number of regular (non-absorption) hearts is 1024. Don't know why you'd ever want to
				// be warned when your health is 1 less than max, but I'll allow it
				// 4 health (2 hearts) is when the health bar starts to shake
				.defineInRange(
					"lowHealthSoundThreshold", HealthMonitorOptions.defaultLowHealthSoundThreshold,
					HealthMonitorOptions.minLowHealthSoundThreshold, HealthMonitorOptions.maxLowHealthSoundThreshold
				);
			playFasterNearDeath = builder
				.comment("Play the warning sounds quicker the closer you get to dieing")
				.define("playFasterNearDeath", HealthMonitorOptions.defaultPlayFasterNearDeath);
		}
		builder.pop();

		// ######################################################################
		// Info Line Module
		// ######################################################################
		builder.comment(
			"Info Line displays the status of other features in the top left corner of the screen."
		).push(CATEGORY_INFOLINE);
		{
			enableInfoLine = builder
				.comment(
					"Enable/Disable the entire info line in the top left part of the screen. " +
					"This includes the clock, coordinates, compass, module status, etc"
				)
				.define("enableInfoLine", InfoLineOptions.defaultEnabled);
			infoLineHorizontalPos = builder
				.comment(
					"The horizontal position of the info line. The far left is at " +
					InfoLineOptions.minLeftOffset + ", and " + InfoLineOptions.maxLeftOffset + " is the far right"
				)
				.defineInRange(
					"infoLineHorizontalPos", InfoLineOptions.defaultLeftOffset,
					InfoLineOptions.minLeftOffset, InfoLineOptions.maxLeftOffset
				);
			infoLineVerticalPos = builder
				.comment(
					"The vertical position of the info line. The top is at " +
					InfoLineOptions.minTopOffset + ", and the bottom is at " + InfoLineOptions.maxTopOffset
				)
				.defineInRange(
					"infoLineVerticalPos", InfoLineOptions.defaultTopOffset,
					InfoLineOptions.minTopOffset, InfoLineOptions.maxTopOffset
				);
			showBiome = builder
				.comment("Enable/Disable showing what biome you are in on the Info Line")
				.define("showBiome", InfoLineOptions.defaultShowBiome);
			showCanSnow = builder
				.comment("Enable/Disable showing if it can snow at the player's feet on the Info Line")
				.define("showCanSnow", InfoLineOptions.defaultShowCanSnow);
			showPing = builder
				.comment("Enable/Disable showing the player's ping while on a multiplayer server")
				.define("showPing", InfoLineOptions.defaultShowPing);
		}
		builder.pop();

		// ######################################################################
		// Item Selector Module
		// ######################################################################
		builder.comment(
			"Item Selector allows you to conveniently swap your currently selected hotbar item with something in your inventory."
		).push(CATEGORY_ITEMSELECTOR);
		{
			enableItemSelector = builder
				.comment(
					"Enables/Disable using mouse wheel scrolling whilst holding the " + CATEGORY_ITEMSELECTOR +
					" keybind (default: Left Alt) to swap the selected item with an inventory item"
				)
				.define("enableItemSelector", ItemSelectorOptions.defaultEnabled);
			itemSelectorMode = builder
				.comment("Sets the Item Selector mode.")
				.defineEnum(
					"itemSelectorMode", ItemSelectorOptions.ItemSelectorModes.ALL, EnumGetMethod.NAME_IGNORECASE
				);
			itemSelectorSideButtons = builder
				.comment("Enable/disable use of side buttons for item selection")
				.define("itemSelectorSideButtons", ItemSelectorOptions.defaultUseMouseSideButtons);
			itemSelectorTimeout = builder
				.comment(
					"Specifies how many ticks until the item selector confirms your choice and performs the item swap"
				)
				.defineInRange(
					"itemSelectorTimeout", ItemSelectorOptions.defaultTimeout,
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
			showAnvilRepairs = builder
				.comment("Enable/Disable showing the repair count on items while using the anvil")
				.define("showAnvilRepairs", MiscOptions.showAnvilRepairs);
			useQuickPlaceSign = builder
				.comment("Enable/Disable being able to place a sign with no text by sneaking while placing a sign")
				.define("useQuickPlaceSign", MiscOptions.useQuickPlaceSign);
			useUnlimitedSprintingSP = builder
				.comment("Enable/Disable overriding the default sprint behavior and run forever. SINGLEPLAYER ONLY.")
				.define("useUnlimitedSprintingSP", MiscOptions.useUnlimitedSprintingSP);
		}
		builder.pop();

		// ######################################################################
		// Player Locator Module
		// ######################################################################
		builder.comment(
			"Player Locator gives you a radar-like ability to easily see where other people are."
		).push(CATEGORY_PLAYERLOCATOR);
		{
			enablePlayerLocator = builder
				.comment("Enable/Disable the Player Locator")
				.define("enablePlayerLocator", LocatorOptions.defaultEnabled);
			playerLocatorMinViewDistance = builder
				.comment("Stop showing player names when they are this close (distance measured in blocks)")
				.defineInRange(
					"playerLocatorMinViewDistance", LocatorOptions.defaultViewDistanceCutoff,
					LocatorOptions.minViewDistanceCutoff, LocatorOptions.maxViewDistanceCutoff
				);
			playerLocatorMode = builder
				.comment("Sets the Player Locator mode.")
				.defineEnum(
					"playerLocatorMode", LocatorOptions.LocatorModes.OFF, EnumGetMethod.NAME_IGNORECASE
				);
			showDistanceToPlayers = builder
				.comment("Show how far away you are from the other players next to their name")
				.define("showDistanceToPlayers", LocatorOptions.defaultShowDistanceToPlayers);
			showPlayerHealth = builder
				.comment("Show how much health players have by their name")
				.define("showPlayerHealth", LocatorOptions.defaultShowPlayerHealth);
			showWitherSkeletons = builder
				.comment("Show your tamed wolves in addition to other players")
				.define("showWitherSkeletons", LocatorOptions.defaultShowWitherSkeletons);
			showWolves = builder
				.comment("Show your tamed wolves in addition to other players")
				.define("showWolves", LocatorOptions.defaultShowWolves);
			useWolfColors = builder
				.comment("Use the color of your wolf's collar to colorize their name")
				.define("useWolfColors", LocatorOptions.defaultUseWolfColors);
		}
		builder.pop();

		// ######################################################################
		// Potion Aid Module
		// ######################################################################
		builder.comment(
			"Potion Aid helps you quickly drink potions based on your circumstance."
		).push(CATEGORY_POTIONAID);
		{
			enablePotionAid = builder
				.comment(
					"Enables pressing " + CATEGORY_POTIONAID + " keybind (default: 'V')" +
					" to drink a potion even if it is in your main inventory and not your hotbar"
				)
				.define("enablePotionAid", PotionAidOptions.defaultEnabled);
		}
		builder.pop();

		// ######################################################################
		// Potion Timers Module
		// ######################################################################
//		builder.comment(
//			"Potion Timers shows the duration remaining on potions that you drink."
//		).push(CATEGORY_POTIONTIMERS);
//		{
//			enablePotionTimers = builder
//				.comment("Enable/Disable showing the time remaining on potions")
//				.define("enablePotionTimers", PotionTimerOptions.defaultEnabled);
//			potionTimerMode = builder
//				.comment("Sets Potion Timer's text display mode.")
//				.defineEnum("potionTimerMode", PotionTimers.PotionTimerModes.COLORED, EnumGetMethod.NAME_IGNORECASE);
//			hidePotionEffectsInInventory = builder
//				.comment("Enable/Disable hiding the default potion effects when you open your inventory")
//				.define("hidePotionEffectsInInventory", PotionTimerOptions.defaultHidePotionEffectsInInventory);
//			potionScale = builder
//				.comment("How large the potion timers are rendered, 1.0 being the normal size")
//				.defineInRange(
//					"potionScale", PotionTimerOptions.defaultPotionScale,
//					PotionTimerOptions.minPotionScale, PotionTimerOptions.maxPotionScale
//				);
//			potionTimersHorizontalPos = builder
//				.comment("The horizontal position of the potion timers. 0 is left, 400 is far right")
//				.defineInRange(
//					"potionTimersHorizontalPos", PotionTimerOptions.defaultPotionTimersHorizontalPos,
//					PotionTimerOptions.minPotionTimersHorizontalPos, PotionTimerOptions.maxPotionTimersHorizontalPos
//				);
//			potionTimersVerticalPos = builder
//				.comment("The vertical position of the potion timers. 0 is top, 200 is very bottom")
//				.defineInRange(
//					"potionTimersVerticalPos", PotionTimerOptions.defaultPotionTimersVerticalPos,
//					PotionTimerOptions.minPotionTimersVerticalPos, PotionTimerOptions.maxPotionTimersVerticalPos
//				);
//			showPotionIcons = builder
//				.comment("Enable/Disable showing the status effect of potions next to the timers")
//				.define("showPotionIcons", PotionTimerOptions.defaultShowPotionIcons);
//		}
//		builder.pop();

		// ######################################################################
		// Quick Deposit Module
		// ######################################################################
		builder.comment(
			"Quick Stack allows you to intelligently deposit every item in your inventory quickly into a chest."
		).push(CATEGORY_QUICKDEPOSIT);
		{
			enableQuickDeposit = builder
				.comment("Enables Quick Deposit.")
				.define("enableQuickDeposit", QuickDepositOptions.defaultEnabled);
			blacklistArrow = builder
				.comment("Stop Quick Deposit from putting arrows in chests?")
				.define("blacklistArrows", QuickDepositOptions.blacklistArrow);
			blacklistClockCompass = builder
				.comment("Stop Quick Deposit from putting clocks and compasses in chests?")
				.define("blacklistClockCompass", QuickDepositOptions.blacklistClockCompass);
			blacklistEnderPearl = builder
				.comment("Stop Quick Deposit from putting ender pearls in chests?")
				.define("blacklistEnderPearl", QuickDepositOptions.blacklistEnderPearl);
			blacklistFood = builder
				.comment("Stop Quick Deposit from putting food in chests?")
				.define("blacklistFood", QuickDepositOptions.blacklistFood);
			blacklistTools = builder
				.comment("Stop Quick Deposit from putting tools (picks, axes, shovels, shears) in chests?")
				.define("blacklistTools", QuickDepositOptions.blacklistTools);
			blacklistTorch = builder
				.comment("Stop Quick Deposit from putting torches in chests?")
				.define("blacklistTorches", QuickDepositOptions.blacklistTorch);
			blacklistWaterBucket = builder
				.comment("Stop Quick Deposit from putting water buckets in chests?")
				.define("blacklistWaterBucket", QuickDepositOptions.blacklistWaterBucket);
			blacklistWeapons = builder
				.comment("Stop Quick Deposit from putting swords and bows in chests?")
				.define("blacklistWeapons", QuickDepositOptions.blacklistWeapons);
			closeChestAfterDepositing = builder
				.comment(
					"Closes the chest GUI after you deposit your items in it. " +
					"Allows quick and easy depositing of all your items into multiple chests"
				)
				.define("closeChestAfterDepositing", QuickDepositOptions.closeChestAfterDepositing);
			ignoreItemsInHotbar = builder
				.comment(
					"Determines if items in your hotbar will be deposited into chests when the " +
					CATEGORY_QUICKDEPOSIT + " keybind (default: 'X') is pressed"
				)
				.define("ignoreItemsInHotbar", QuickDepositOptions.ignoreItemsInHotbar);
		}
		builder.pop();

		// ######################################################################
		// Safe Overlay Module
		// ######################################################################
		builder.comment(
			"Safe Overlay shows you which blocks are dark enough to spawn mobs."
		).push(CATEGORY_SAFEOVERLAY);
		{
			enableSafeOverlay = builder
				.comment("Enable/Disable the Safe Overlay")
				.define("enableSafeOverlay", SafeOverlayOptions.defaultEnabled);
			safeOverlayDisplayInNether = builder
				.comment("Enable/Disable showing unsafe areas in the Nether")
				.define("safeOverlayDisplayInNether", SafeOverlayOptions.defaultDisplayInNether);
			safeOverlayDrawDistance = builder
				.comment(
					"How far away unsafe spots should be rendered around the player measured in blocks. " +
					"This can be changed in game by pressing the " + CATEGORY_SAFEOVERLAY + " keybind (default: 'L') " +
					"in combination with the '-' and '+' keys"
				)
				.defineInRange(
					"safeOverlayDrawDistance", SafeOverlayOptions.defaultDrawDistance,
					SafeOverlayOptions.minDrawDistance, SafeOverlayOptions.maxDrawDistance
				);
			safeOverlayMode = builder
				.comment("Sets the Safe Overlay mode.")
				.defineEnum(
					"safeOverlayMode", SafeOverlayOptions.safeOverlayModes.OFF,
					EnumGetMethod.NAME_IGNORECASE
				);
			safeOverlaySeeThroughWalls = builder
				.comment(
					"Enable/Disable showing unsafe areas through walls. Toggle in game with 'Ctrl' + the " +
					CATEGORY_SAFEOVERLAY + " keybind (default: 'L')"
				)
				.define("safeOverlaySeeThroughWalls", SafeOverlayOptions.defaultRenderThroughWalls);
			safeOverlayTransparency = builder
				.comment("The transparency of the unsafe marks")
				.defineInRange(
					"safeOverlayTransparency", SafeOverlayOptions.defaultUnsafeOverlayTransparency,
					SafeOverlayOptions.minUnsafeOverlayTransparency,
					SafeOverlayOptions.maxUnsafeOverlayTransparency
				);
		}
		builder.pop();

		// ######################################################################
		// Torch Aid Module
		// ######################################################################
		builder.comment(
			"Torch Aid lets you sneak + right click while holding an axe, pickaxe, shovel, or when you have nothing in your hand to place a torch."
		).push(CATEGORY_TORCHAID);
		{
			enableTorchAid = builder
				.comment("Enable/Disable using Torch Aid to help you place torches more easily")
				.define("enableTorchAid", TorchAidOptions.defaultEnabled);
		}
		builder.pop();

		// ######################################################################
		// Weapon Swap Module
		// ######################################################################
		builder.comment(
			"Weapon Swap allows you to quickly select your sword and bow."
		).push(CATEGORY_WEAPONSWAP);
		{
			enableWeaponSwap = builder
				.comment(
					"Enables pressing the " + CATEGORY_WEAPONSWAP + " keybind (default: 'F') to swap between your sword and bow"
				)
				.define("enableWeaponSwap", WeaponSwapOptions.defaultEnabled);
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
//		ModConfig config = configEvent.getConfig();
//		if (config.getModId().equals(MODID)){
//			do stuff??
//		}
	}

	public static ForgeConfigSpec getConfigSpec() {
		return SPEC;
	}

	public static Type getConfigType() {
		return Type.CLIENT;
	}
}
