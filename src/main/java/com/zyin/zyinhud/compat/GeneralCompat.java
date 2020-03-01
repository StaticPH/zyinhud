package com.zyin.zyinhud.compat;

import com.zyin.zyinhud.config.ZyinHUDConfig;
import net.minecraft.block.Blocks;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ToolItem;

import java.util.ArrayList;

import static com.zyin.zyinhud.util.ZyinHUDUtil.csvStringConfig2ItemCollection;

public class GeneralCompat {

	//TODO: additional weapons?

	private static ArrayList<Item> getValidTorchesFromConfig() {
		return (ArrayList<Item>) csvStringConfig2ItemCollection(ZyinHUDConfig.useAsTorch);
	}

	private static ArrayList<Item> getExtraArrowsFromConfig() {
		return (ArrayList<Item>) csvStringConfig2ItemCollection(ZyinHUDConfig.treatAsArrow);
	}

	private static ArrayList<Item> getExtraCrossbowAmmoFromConfig() {
		return (ArrayList<Item>) csvStringConfig2ItemCollection(ZyinHUDConfig.treatAsCrossbowAmmo);
	}

	private static ArrayList<Item> getExtraEnderpearlsFromConfig() {
		return (ArrayList<Item>) csvStringConfig2ItemCollection(ZyinHUDConfig.treatAsEnderpearls);
	}

	private static ArrayList<Item> getTorchPlacingToolsFromConfig() {
		return (ArrayList<Item>) csvStringConfig2ItemCollection(ZyinHUDConfig.toolsForTorchAid);
	}

	public static class ItemLike {
		private static ArrayList<Item> considerAsTorch;
		private static ArrayList<Item> considerAsArrow;
		private static ArrayList<Item> considerAsCrossbowAmmo;
		private static ArrayList<Item> considerAsEnderPearl;
//		private static ArrayList<Item> considerAsTool;
		private static ArrayList<Item> torchPlacingTools;

		static{
			loadFromConfig();
		}

		public static void loadFromConfig() {
			considerAsTorch = getValidTorchesFromConfig();
			considerAsArrow = getExtraArrowsFromConfig();
			considerAsCrossbowAmmo = getExtraCrossbowAmmoFromConfig();
			considerAsEnderPearl = getExtraEnderpearlsFromConfig();
//			considerAsTool = getExtraToolsFromConfig();
			torchPlacingTools = getTorchPlacingToolsFromConfig();
		}

		//	TODO for torches:
		//		maybe even Dank Null and Dank Storage
		//		If EnderUtils ever gets updated *crosses fingers* consider the nullifier
		//		OpenBlocks /dev/null	???
		//		Can't remember if the Reliquary "Void Tear" could also be used to place items, or just store them... but maybe?
		//		Botania "Void Charm"?
		public static boolean isTorchLike(Item item) {
			if (item == null) { return false; }
			return item == Blocks.TORCH.asItem() || considerAsTorch.contains(item);
		}

		private static boolean isSpecialArrowLike(Item item) {
			return item == Items.SPECTRAL_ARROW || item == Items.TIPPED_ARROW;
		}

		public static boolean isArrowLike(Item item) {
			if (item == null) { return false; }
			return item == Items.ARROW || considerAsArrow.contains(item);
		}

		public static boolean isArrowLike(Item item, boolean includeSpecial) {
			if (item == null) { return false; }
			return isArrowLike(item) || (includeSpecial && isSpecialArrowLike(item));
		}

		public static boolean isCrossbowAmmo(Item item) {
			if (item == null) { return false; }
			return isArrowLike(item) || considerAsCrossbowAmmo.contains(item);
//			|| item == Items.FIREWORK_ROCKET
		}

		public static boolean isEnderpearlLike(Item item) {
			if (item == null) { return false; }
			return item == Items.ENDER_PEARL || considerAsEnderPearl.contains(item);
		}

		// No, YOU'RE the tool!
		public static boolean isToolLike(Item item) {
			//This one is going to just balloon if I'm not conservative here, isn't it?
			if (item == null) { return false; }
			return item instanceof ToolItem ||
			       item instanceof HoeItem ||
			       item instanceof ShearsItem ||
			       TinkersConstructCompat.isTConstructHarvestTool(item);// ||
//			       considerAsTool.contains(item);
		}

		public static boolean canItemPlaceTorches(Item item) {
			if (item == null) { return false; }
			return item instanceof ToolItem ||
			       TinkersConstructCompat.isTConstructToolWithoutARightClickAction(item) ||
			       torchPlacingTools.contains(item);
		}
	}
}
