package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
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
import net.minecraft.tags.ItemTags;

/**
 * Quick Deposit allows you to inteligently deposit every item in your inventory quickly into a chest.
 */
public class QuickDeposit extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	//TODO: Add blacklist option for additional items
	public static boolean Enabled = ZyinHUDConfig.EnableQuickDeposit.get();
	public static boolean BlacklistClockCompass = ZyinHUDConfig.BlacklistClockCompass.get();
	public static boolean BlacklistWeapons = ZyinHUDConfig.BlacklistWeapons.get();
	public static boolean BlacklistArrow = ZyinHUDConfig.BlacklistArrow.get();
	public static boolean BlacklistEnderPearl = ZyinHUDConfig.BlacklistEnderPearl.get();
	public static boolean BlacklistFood = ZyinHUDConfig.BlacklistFood.get();
	public static boolean BlacklistTools = ZyinHUDConfig.BlacklistTools.get();
	public static boolean BlacklistTorch = ZyinHUDConfig.BlacklistTorch.get();
	public static boolean BlacklistWaterBucket = ZyinHUDConfig.BlacklistWaterBucket.get();
	public static boolean CloseChestAfterDepositing = ZyinHUDConfig.CloseChestAfterDepositing.get();
	public static boolean IgnoreItemsInHotbar = ZyinHUDConfig.IgnoreItemsInHotbar.get();


	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		ZyinHUDConfig.EnableQuickDeposit.set(!Enabled);
		ZyinHUDConfig.EnableQuickDeposit.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return Enabled = !Enabled;
	}

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
	@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "deprecation"})
	public static boolean IsAllowedToBeDepositedInContainer(ItemStack itemStack) {
		//TODO: figure out what the heck Forge intends me to use instead of Item.getItemFromBlock
		Item item = itemStack.getItem(); // no need to call this for every comparison; just once will do
		return !itemStack.isEmpty() &&
		       !(BlacklistTorch &&
		         (item == Item.getItemFromBlock(Blocks.TORCH) ||
		         tag_TORCH.contains(item) || tag_TORCH_PLACER.contains(item))
		       ) &&
		       (
			       !(BlacklistTools && item instanceof ToolItem) &&
			       !(item instanceof HoeItem) &&
			       !(item instanceof ShearsItem) &&
			       !ModCompatibility.TConstruct.IsTConstructHarvestTool(item)
		       ) &&
		       (
			       !(BlacklistWeapons && item instanceof SwordItem) &&
			       !(item instanceof BowItem)
		       ) &&
		       !(BlacklistArrow &&
		         (item == Items.ARROW || ItemTags.ARROWS.contains(item))
		       ) &&
		       !(BlacklistEnderPearl && item == Items.ENDER_PEARL) &&
		       !(BlacklistWaterBucket && item == Items.WATER_BUCKET) &&
		       !(BlacklistFood && item.isFood()) && // item == Items.CAKE)) ||
		       !(BlacklistClockCompass && (item == Items.COMPASS || item == Items.CLOCK));
		//TODO: replace/supplement the item checks with tag checks wherever possible. like with torches
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
