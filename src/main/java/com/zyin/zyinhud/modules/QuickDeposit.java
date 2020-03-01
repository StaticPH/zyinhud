package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.config.ZyinHUDConfig;
import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.compat.GeneralCompat.ItemLike;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Quick Deposit allows you to intelligently deposit every item in your inventory quickly into a chest.
 */
public class QuickDeposit extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(QuickDeposit.class);

	//TODO: Add blacklist option(s) for additional items
	//      consider expanding "arrows" to "ammunition" in general?
	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled;
	private static boolean blacklistClockCompass;
	private static boolean blacklistWeapons;
	private static boolean blacklistArrow;
	private static boolean blacklistEnderPearl;
	private static boolean blacklistFood;
	private static boolean blacklistTools;
	private static boolean blacklistTorch;
	private static boolean blacklistWaterBucket;
	private static boolean closeChestAfterDepositing;
	private static boolean ignoreItemsInHotbar;

	static { loadFromConfig(); }

	public static void loadFromConfig() {
		isEnabled = ZyinHUDConfig.enableQuickDeposit.get();
		blacklistClockCompass = ZyinHUDConfig.blacklistClockCompass.get();
		blacklistWeapons = ZyinHUDConfig.blacklistWeapons.get();
		blacklistArrow = ZyinHUDConfig.blacklistArrow.get();
		blacklistEnderPearl = ZyinHUDConfig.blacklistEnderPearl.get();
		blacklistFood = ZyinHUDConfig.blacklistFood.get();
		blacklistTools = ZyinHUDConfig.blacklistTools.get();
		blacklistTorch = ZyinHUDConfig.blacklistTorch.get();
		blacklistWaterBucket = ZyinHUDConfig.blacklistWaterBucket.get();
		closeChestAfterDepositing = ZyinHUDConfig.closeChestAfterDepositing.get();
		ignoreItemsInHotbar = ZyinHUDConfig.ignoreItemsInHotbar.get();
	}

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
				InventoryUtil.depositAllMatchingItemsInFurnace();
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
