package com.zyin.zyinhud.compat;

public class CompatDefaults {
	// Oh if only the server didn't just ignore non-matching client-side tags >.<
	public static String defaultTorchLike = String.join(
		", ",
		// Torches
		"bonetorch:bonetorch",
		"burningtorch:burningtorch",
		"druidcraft:fiery_torch",
		"modernity:anthracite_torch",
		"modernity:lightrock_torch",
		"silentgear:stone_torch",
		"tconstruct:stone_torch",
		"upgrade_aquatic:jelly_torch_blue",
		"upgrade_aquatic:jelly_torch_green",
		"upgrade_aquatic:jelly_torch_orange",
		"upgrade_aquatic:jelly_torch_pink",
		"upgrade_aquatic:jelly_torch_purple",
		"upgrade_aquatic:jelly_torch_red",
		"upgrade_aquatic:jelly_torch_white",
		"upgrade_aquatic:jelly_torch_yellow",
		// Torch placers
		"torchbandolier:torch_bandolier",
		"xreliquary:sojourner_staff"
	);

	public static String defaultArrowLike = String.join(
		", ",
		// Arrows
		"archers_paradox:diamond_arrow",
		"archers_paradox:prismarine_arrow",
		"archers_paradox:quartz_arrow",
		"roots:living_arrow",
		// Arrow quivers/holders
		"roots:living_quiver",
		"roots:wildwood_quiver"
	);

	public static String defaultCrossbowAmmoLike = String.join(
		", ",
		defaultArrowLike,
		"minecraft:firework_rocket"
	);
	public static String defaultEnderPearlLike = "";
//	public static String defaultToolLike = "";
	public static String defaultTorchPlacingTools = "";
}
/*
	TODO for torches:
		equivalent of including "#ilikewood:torches" in a tag

	TODO for arrows:
		support for the remainder of (the special arrows)
		https://github.com/KingLemming/1.14/blob/master/ArchersParadox/src/main/java/cofh/archersparadox/init/ModReferences.java
		"flamingarrows:flaming_arrow"     <-- "special" arrow


*/