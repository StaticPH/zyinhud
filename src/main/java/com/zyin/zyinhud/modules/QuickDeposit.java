package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.helper.TagHelper.ItemLike;
import com.zyin.zyinhud.util.InventoryUtil;
import net.minecraft.client.gui.screen.EnchantmentScreen;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.client.gui.screen.inventory.BeaconScreen;
import net.minecraft.client.gui.screen.inventory.BrewingStandScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.client.gui.screen.inventory.FurnaceScreen;
import net.minecraft.client.gui.screen.inventory.MerchantScreen;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

/**
 * Quick Deposit allows you to intelligently deposit every item in your inventory quickly into a chest.
 */
public class QuickDeposit extends ZyinHUDModuleBase {
	//TODO: Add blacklist option(s) for additional items
	//      consider expanding "arrows" to "ammunition" in general?
	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled = ZyinHUDConfig.enableQuickDeposit.get();
	public static boolean blacklistClockCompass = ZyinHUDConfig.blacklistClockCompass.get();
	public static boolean blacklistWeapons = ZyinHUDConfig.blacklistWeapons.get();
	public static boolean blacklistArrow = ZyinHUDConfig.blacklistArrow.get();
	public static boolean blacklistEnderPearl = ZyinHUDConfig.blacklistEnderPearl.get();
	public static boolean blacklistFood = ZyinHUDConfig.blacklistFood.get();
	public static boolean blacklistTools = ZyinHUDConfig.blacklistTools.get();
	public static boolean blacklistTorch = ZyinHUDConfig.blacklistTorch.get();
	public static boolean blacklistWaterBucket = ZyinHUDConfig.blacklistWaterBucket.get();
	public static boolean closeChestAfterDepositing = ZyinHUDConfig.closeChestAfterDepositing.get();
	public static boolean ignoreItemsInHotbar = ZyinHUDConfig.ignoreItemsInHotbar.get();


	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableQuickDeposit.set(!isEnabled);
		ZyinHUDConfig.enableQuickDeposit.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * Deposits all items in your inventory into a chest.
	 * Can optionally be limited to depositing items that are already found in the inventory.
	 *
	 * @param onlyDepositMatchingItems only deposit an item if another one exists in the chest already
	 */
	public static void quickDepositItemsInChest(boolean onlyDepositMatchingItems) {
		if (!(mc.currentScreen instanceof ContainerScreen) || !QuickDeposit.isEnabled) { return; }

		try {
			if (
				mc.currentScreen instanceof BeaconScreen ||
			    mc.currentScreen instanceof CraftingScreen ||
			    mc.currentScreen instanceof EnchantmentScreen ||
			    mc.currentScreen instanceof AnvilScreen
			) {
				//we don't support these
				return;
			}
			else if (mc.currentScreen instanceof MerchantScreen) {
				InventoryUtil.depositAllMatchingItemsInMerchant();
			}
			else if (mc.currentScreen instanceof FurnaceScreen) {
				InventoryUtil.depositAllMatchingItemsInFurance();
			}
			else if (mc.currentScreen instanceof BrewingStandScreen) {
				InventoryUtil.depositAllMatchingItemsInBrewingStand();
			}
			else {
				//single chest, double chest, donkey/mules, hopper, dropper, dispenser
				InventoryUtil.depositAllMatchingItemsInContainer(onlyDepositMatchingItems, ignoreItemsInHotbar);

				if (closeChestAfterDepositing) {
//					mc.currentScreen.onClose();
					mc.player.closeScreen();
				}
			}

			ZyinHUDSound.playButtonPress();
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
	@SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
	public static boolean isAllowedToBeDepositedInContainer(ItemStack itemStack) {
		Item item = itemStack.getItem(); // no need to call this for every comparison; just once per item will do
		return !itemStack.isEmpty() &&
		       !(blacklistTorch && ItemLike.isTorchLike(item)) &&
		       !(blacklistTools && ItemLike.isToolLike(item)) &&
		       !(blacklistWeapons && (item instanceof SwordItem || item instanceof BowItem)) &&
		       !(blacklistArrow && ItemLike.isArrowLike(item)) &&
		       !(blacklistEnderPearl && item == Items.ENDER_PEARL) &&
		       !(blacklistWaterBucket && item == Items.WATER_BUCKET) &&
		       !(blacklistFood && item.isFood()) && // item == Items.CAKE)) ||
		       !(blacklistClockCompass && (item == Items.COMPASS || item == Items.CLOCK));
		//TODO: replace/supplement the item checks with tag checks wherever possible. like with torches
	}


	/**
	 * Toggles depositing items in your hotbar
	 *
	 * @return boolean
	 */
	public static boolean toggleIgnoreItemsInHotbar() {
		return ignoreItemsInHotbar = !ignoreItemsInHotbar;
	}

	/**
	 * Toggles depositing items in your hotbar
	 *
	 * @return boolean
	 */
	public static boolean toggleCloseChestAfterDepositing() {
		return closeChestAfterDepositing = !closeChestAfterDepositing;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean toggleBlacklistTorch() {
		return blacklistTorch = !blacklistTorch;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean toggleBlacklistTools() {
		return blacklistTools = !blacklistTools;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean toggleBlacklistWeapons() {
		return blacklistWeapons = !blacklistWeapons;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean toggleBlacklistArrow() {
		return blacklistArrow = !blacklistArrow;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean toggleBlacklistEnderPearl() {
		return blacklistEnderPearl = !blacklistEnderPearl;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean toggleBlacklistFood() {
		return blacklistFood = !blacklistFood;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean toggleBlacklistWaterBucket() {
		return blacklistWaterBucket = !blacklistWaterBucket;
	}

	/**
	 * Toggles blacklisting this item
	 *
	 * @return boolean
	 */
	public static boolean toggleBlacklistClockCompass() {
		return blacklistClockCompass = !blacklistClockCompass;
	}
}
