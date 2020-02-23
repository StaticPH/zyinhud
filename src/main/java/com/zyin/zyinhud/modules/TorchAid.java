package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.helper.TagHelper.ItemLike;
import com.zyin.zyinhud.util.InventoryUtil;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.zyin.zyinhud.helper.TagHelper.ItemLike.canItemPlaceTorches;
import static com.zyin.zyinhud.util.ZyinHUDUtil.useItem;

/**
 * TorchAid Aid allows the player to easily use a torch without having it selected. It does this by
 * selecting a torch before the Use Block key is pressed, then unselecting the torch after the Use Block
 * key is released.
 */
public class TorchAid extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(TorchAid.class);

	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled = ZyinHUDConfig.enableTorchAid.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableTorchAid.set(!isEnabled);
		ZyinHUDConfig.enableTorchAid.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * Use this instance for all instance method calls.
	 */
	public static TorchAid instance = new TorchAid();

	private TorchAid() {}

	/**
	 * After the <code>EquipTorchIfToolIsEquipped()</code> function fires, this is set to the index of where the torch was in the inventory,
	 * or the index of the hotbar slot that was selected. The <code>UnequipTorch()</code> function uses this value to determine
	 * what to do next. -1 means there are no torches in inventory.
	 */
	private static int previousTorchIndex = -1;

	public static void onPressed() {
		// FIXME: resorting to requiring sneak to use this feature was a cop-out,
		//  because I was having a hard time using events to make this behave more intelligently
		if (TorchAid.isEnabled && mc.player.isSneaking()) { equipTorchIfToolIsEquipped(); }
	}

	public static void onReleased() {
		if (TorchAid.isEnabled) { unequipTorch(); }
	}

	/**
	 * Makes the player place a Torch if they are currently using a tool.
	 */
	public static void equipTorchIfToolIsEquipped() {
		if (mc.currentScreen == null && mc.mouseHelper.isMouseGrabbed()) {
			ItemStack currentItemStack = mc.player.getHeldItemMainhand();
			if (!currentItemStack.isEmpty() && canItemPlaceTorches(currentItemStack.getItem())) {
				useTorch();
			}
		}
	}

	/**
	 * Makes the player place a Torch if they have one by selecting a Torch in their inventory then right clicking.
	 */
	public static void useTorch() {
//        if (EatingAid.instance.isEating())
//        {
//            EatingAid.instance.stopEating();    //it's not good if we have a torch selected and hold right click down...
//        }

		if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
			int torchHotbarIndex = InventoryUtil.getItemIndexFromHotbar(Blocks.TORCH, ItemLike::isTorchLike);

			if (torchHotbarIndex < 0) {
				int torchInventoryIndex = InventoryUtil.getItemIndexFromInventory(Blocks.TORCH, ItemLike::isTorchLike);

				if (torchInventoryIndex >= 0) {
					previousTorchIndex = torchInventoryIndex;
					equipItemFromInventory(torchInventoryIndex);
				}
				//player has no torches
				//don't display a notification because the player may be trying to interact with a usable block

			}
			else {
				previousTorchIndex =
					InventoryUtil.translateHotbarIndexToInventoryIndex(mc.player.inventory.currentItem);
				equipItemFromHotbar(torchHotbarIndex);
			}
			useItem();
			//TODO: Find a way to swap back if the player opens a gui/screen
		}
	}

	/**
	 * Selects the item at the specified inventory index by swapping it with the currently held item.
	 *
	 * @param inventoryIndex 9-35
	 */
	private static void equipItemFromInventory(int inventoryIndex) {
		if (inventoryIndex < 9 || inventoryIndex > 35) { return; }

		int currentItemInventoryIndex = InventoryUtil.getCurrentlySelectedItemInventoryIndex();

		InventoryUtil.swap(currentItemInventoryIndex, inventoryIndex);
	}

	/**
	 * Selects the item at specified hotbar index.
	 *
	 * @param hotbarIndex 36-44
	 */
	private static void equipItemFromHotbar(int hotbarIndex) {
		if (hotbarIndex < 36 || hotbarIndex > 44) { return; }

		hotbarIndex = InventoryUtil.translateInventoryIndexToHotbarIndex(hotbarIndex);

		mc.player.inventory.currentItem = hotbarIndex;
	}

	/**
	 * Uses the <code>previousTorchIndex</code> variable to determine how to unequip the currently held torch.
	 * after placing one.
	 */
	private static void unequipTorch() {
		if (previousTorchIndex < 0) { return; }
		else {
			if (previousTorchIndex >= 36 && previousTorchIndex <= 44) {    //on the hotbar
				mc.player.inventory.currentItem = InventoryUtil.translateInventoryIndexToHotbarIndex(
					previousTorchIndex);
			}
			else {
				InventoryUtil.swap(
					InventoryUtil.translateHotbarIndexToInventoryIndex(mc.player.inventory.currentItem),
					previousTorchIndex
				);
			}
		}
		previousTorchIndex = -1;
	}
}