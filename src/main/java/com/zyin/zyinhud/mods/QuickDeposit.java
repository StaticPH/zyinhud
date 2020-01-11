package com.zyin.zyinhud.mods;

import net.minecraft.client.gui.screen.EnchantmentScreen;
import net.minecraft.client.gui.screen.inventory.*;
import net.minecraft.client.gui.screen.inventory.BeaconScreen;
import net.minecraft.client.gui.screen.inventory.BrewingStandScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.client.gui.screen.inventory.FurnaceScreen;
import net.minecraft.block.Blocks;
import net.minecraft.item.BowItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;

import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.ModCompatibility;

/**
 * Quick Deposit allows you to inteligently deposit every item in your inventory quickly into a chest.
 */
public class QuickDeposit extends ZyinHUDModBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled;

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		return Enabled = !Enabled;
	}

	public static boolean IgnoreItemsInHotbar;
	public static boolean CloseChestAfterDepositing;
	public static boolean BlacklistTorch;
	public static boolean BlacklistTools;
	public static boolean BlacklistWeapons;
	public static boolean BlacklistArrow;
	public static boolean BlacklistFood;
	public static boolean BlacklistEnderPearl;
	public static boolean BlacklistWaterBucket;
	public static boolean BlacklistClockCompass;

	/**
	 * Deposits all items in your inventory into a chest, if the item exists in the chest
	 *
	 * @param onlyDepositMatchingItems only deposit an item if another one exists in the chest already
	 */
	public static void QuickDepositItemsInChest(boolean onlyDepositMatchingItems) {
		if (!(mc.currentScreen instanceof ContainerScreen) || !QuickDeposit.Enabled) { return; }

		try {
			if (mc.currentScreen instanceof BeaconScreen ||
			    mc.currentScreen instanceof CraftingScreen ||
			    mc.currentScreen instanceof EnchantmentScreen ||
			    mc.currentScreen instanceof AnvilScreen) {
				//we don't support these
				return;
			}
			else if (mc.currentScreen instanceof MerchantScreen) {
				InventoryUtil.DepositAllMatchingItemsInMerchant();
			}
			else if (mc.currentScreen instanceof FurnaceScreen) {
				InventoryUtil.DepositAllMatchingItemsInFurance();
			}
			else if (mc.currentScreen instanceof BrewingStandScreen) {
				InventoryUtil.DepositAllMatchingItemsInBrewingStand();
			}
			else {
				//single chest, double chest, donkey/mules, hopper, dropper, dispenser
				InventoryUtil.DepositAllMatchingItemsInContainer(onlyDepositMatchingItems, IgnoreItemsInHotbar);

				if (CloseChestAfterDepositing) { mc.player.closeScreen(); }
			}

			ZyinHUDSound.PlayButtonPress();
		}
		catch (Exception e) {
			//Quick Deposit has a bad history of causing unpredictable crashes, so just catch all exceptions
			e.printStackTrace();
		}//TODO: additional instanceof checks... may want to also replace some of the instanceof keywords with method calls
	}

	/**
	 * Determines if the item is allowed to be deposited in a chest based on the current list of blacklisted items
	 *
	 * @param itemStack the item stack
	 * @return true if it is allowed to be deposited
	 */
	public static boolean IsAllowedToBeDepositedInContainer(ItemStack itemStack) {
		if (itemStack.isEmpty() ||
		    (BlacklistTorch && (itemStack.getItem() == Item.getItemFromBlock(Blocks.TORCH))) ||
		    ((BlacklistTools && (itemStack.getItem() instanceof ToolItem)) ||
		     (itemStack.getItem() instanceof HoeItem) ||
		     (itemStack.getItem() instanceof ShearsItem) ||
		     ModCompatibility.TConstruct.IsTConstructHarvestTool(itemStack.getItem())) ||
		    ((BlacklistWeapons && (itemStack.getItem() instanceof SwordItem)) ||
		     (itemStack.getItem() instanceof BowItem)) ||
		    (BlacklistArrow && (itemStack.getItem() == Items.ARROW)) ||
		    (BlacklistEnderPearl && (itemStack.getItem() == Items.ENDER_PEARL)) ||
		    (BlacklistWaterBucket && (itemStack.getItem() == Items.WATER_BUCKET)) ||
		    (BlacklistFood && (itemStack.getItem().isFood())) || // itemStack.getItem() == Items.CAKE)) ||
		    (BlacklistClockCompass && (itemStack.getItem() == Items.COMPASS || itemStack.getItem() == Items.CLOCK)))
		{
			return false;
		}

		return true;
	}


	/**
	 * Toggles depositing items in your hotbar
	 *
	 * @return boolean
	 */
	public static boolean ToggleIgnoreItemsInHotbar() {
		return IgnoreItemsInHotbar = !IgnoreItemsInHotbar;
	}

	/**
	 * Toggles depositing items in your hotbar
	 *
	 * @return boolean
	 */
	public static boolean ToggleCloseChestAfterDepositing() {
		return CloseChestAfterDepositing = !CloseChestAfterDepositing;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean ToggleBlacklistTorch() {
		return BlacklistTorch = !BlacklistTorch;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean ToggleBlacklistTools() {
		return BlacklistTools = !BlacklistTools;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean ToggleBlacklistWeapons() {
		return BlacklistWeapons = !BlacklistWeapons;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean ToggleBlacklistArrow() {
		return BlacklistArrow = !BlacklistArrow;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean ToggleBlacklistEnderPearl() {
		return BlacklistEnderPearl = !BlacklistEnderPearl;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean ToggleBlacklistFood() {
		return BlacklistFood = !BlacklistFood;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean ToggleBlacklistWaterBucket() {
		return BlacklistWaterBucket = !BlacklistWaterBucket;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean ToggleBlacklistClockCompass() {
		return BlacklistClockCompass = !BlacklistClockCompass;
	}
}
