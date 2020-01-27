package com.zyin.zyinhud.modules;


import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.helper.TagHelper.ItemLike;
import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.ModCompatibility;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.RayTraceResult;

import static com.zyin.zyinhud.util.ZyinHUDUtil.useItem;

/**
 * TorchAid Aid allows the player to easily use a torch without having it selected. It does this by
 * selecting a torch before the Use Block key is pressed, then unselecting the torch after the Use Block
 * key is released.
 */
public class TorchAid extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled = ZyinHUDConfig.EnableTorchAid.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		ZyinHUDConfig.EnableTorchAid.set(!Enabled);
		ZyinHUDConfig.EnableTorchAid.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return Enabled = !Enabled;
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

	public void Pressed() {
		if (TorchAid.Enabled) { EquipTorchIfToolIsEquipped(); }
	}

	public void Released() {
		if (TorchAid.Enabled) { UnequipTorch(); }
	}

	/**
	 * Makes the player place a Torch if they are currently using a tool.
	 */
	public void EquipTorchIfToolIsEquipped() {
		if (mc.currentScreen == null && mc.mouseHelper.isMouseGrabbed()) {
			ItemStack currentItemStack = mc.player.getHeldItemMainhand();
			if (currentItemStack.isEmpty()) { return; }
			else if (
				currentItemStack.getItem() instanceof ToolItem ||
				ModCompatibility.TConstruct.IsTConstructToolWithoutARightClickAction(currentItemStack.getItem())
			) {
				UseTorch();
			}
		}
	}

	/**
	 * Makes the player place a Torch if they have one by selecting a Torch in their inventory then right clicking.
	 */
	public void UseTorch() {
//        if (EatingAid.instance.isEating())
//        {
//            EatingAid.instance.StopEating();    //it's not good if we have a torch selected and hold right click down...
//        }

		if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
			int torchHotbarIndex = InventoryUtil.GetItemIndexFromHotbar(Blocks.TORCH, ItemLike::isTorchLike);

			if (torchHotbarIndex < 0) {
				int torchInventoryIndex = InventoryUtil.GetItemIndexFromInventory(Blocks.TORCH, ItemLike::isTorchLike);

				if (torchInventoryIndex >= 0) {
					previousTorchIndex = torchInventoryIndex;
					EquipItemFromInventory(torchInventoryIndex);
				}
				//player has no torches
				//don't display a notification because the player may be trying to interact with a usable block

			}
			else {
				previousTorchIndex =
					InventoryUtil.TranslateHotbarIndexToInventoryIndex(mc.player.inventory.currentItem);
				EquipItemFromHotbar(torchHotbarIndex);
			}
			useItem();

		}
	}

	/**
	 * Selects the item at the specified inventory index by swapping it with the currently held item.
	 *
	 * @param inventoryIndex 9-35
	 */
	private void EquipItemFromInventory(int inventoryIndex) {
		if (inventoryIndex < 9 || inventoryIndex > 35) { return; }

		int currentItemInventoryIndex = InventoryUtil.GetCurrentlySelectedItemInventoryIndex();

		InventoryUtil.Swap(currentItemInventoryIndex, inventoryIndex);
	}

	/**
	 * Selects the item at specified hotbar index.
	 *
	 * @param hotbarIndex 36-44
	 */
	private void EquipItemFromHotbar(int hotbarIndex) {
		if (hotbarIndex < 36 || hotbarIndex > 44) { return; }

		hotbarIndex = InventoryUtil.TranslateInventoryIndexToHotbarIndex(hotbarIndex);

		mc.player.inventory.currentItem = hotbarIndex;
	}

	/**
	 * Uses the <code>previousTorchIndex</code> variable to determine how to unequip the currently held torch.
	 * after placing one.
	 */
	private void UnequipTorch() {
		if (previousTorchIndex < 0) { return; }
		else {
			if (previousTorchIndex >= 36 && previousTorchIndex <= 44) {    //on the hotbar
				mc.player.inventory.currentItem = InventoryUtil.TranslateInventoryIndexToHotbarIndex(
					previousTorchIndex);
			}
			else {
				InventoryUtil.Swap(
					InventoryUtil.TranslateHotbarIndexToInventoryIndex(mc.player.inventory.currentItem),
					previousTorchIndex
				);
			}
		}
		previousTorchIndex = -1;
	}
}
