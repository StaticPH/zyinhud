package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.zyin.zyinhud.util.ZyinHUDUtil;

/**
 * The Miscellaneous module has other functionality not relating to anything specific.
 */
public class Miscellaneous extends ZyinHUDModuleBase {
	public static final Miscellaneous instance = new Miscellaneous();

	public static boolean useQuickPlaceSign = ZyinHUDConfig.useQuickPlaceSign.get();
	public static boolean useUnlimitedSprintingSP = ZyinHUDConfig.useUnlimitedSprintingSP.get();
	public static boolean showAnvilRepairs = ZyinHUDConfig.showAnvilRepairs.get();

	private static final int maxRepairTimes = 6;


	@SubscribeEvent
	public static void onGuiOpenEvent(GuiOpenEvent event) {
		if (useQuickPlaceSign && event.getGui() instanceof EditSignScreen && mc.player.isSneaking()) {
			event.setCanceled(true);
			event.getGui().onClose();
		}
	}

	@SubscribeEvent
	public static void onDrawScreenEvent(DrawScreenEvent.Post event) {
		if (showAnvilRepairs && event.getGui() instanceof AnvilScreen) {
			drawGuiRepairCounts((AnvilScreen) event.getGui());
		}
	}

	/**
	 * Draws text above the anvil's repair slots showing how many more times it can be repaired
	 *
	 * @param guiRepair the gui repair
	 */
	public static void drawGuiRepairCounts(AnvilScreen guiRepair) {
		RepairContainer anvil = guiRepair.getContainer();
		IInventory inputSlots = ZyinHUDUtil.getFieldByReflection(
			RepairContainer.class, anvil, "inputSlots", "field_82853_g"
		); //Not sure if this should/can be saved in a static final field...

		int xSize = guiRepair.getXSize();
		int ySize = guiRepair.getYSize();

		int guiRepairXOrigin = (guiRepair.width - xSize) / 2;
		int guiRepairYOrigin = (guiRepair.height - ySize) / 2;

		assert inputSlots != null;

		ItemStack leftItemStack = inputSlots.getStackInSlot(0);
		ItemStack rightItemStack = inputSlots.getStackInSlot(1);
//		ItemStack finalItemStack = inputSlots.getStackInSlot(2);

		if (!leftItemStack.isEmpty()) {
			int timesRepaired = getTimesRepaired(leftItemStack);
			String leftItemRepairCost = timesRepaired >= maxRepairTimes ?
			                            TextFormatting.RED + "" + timesRepaired + TextFormatting.DARK_GRAY + '/' + maxRepairTimes :
			                            TextFormatting.DARK_GRAY + "" + timesRepaired + '/' + maxRepairTimes;

			mc.fontRenderer.drawString(leftItemRepairCost, guiRepairXOrigin + 26, guiRepairYOrigin + 37, 0xffffff);
		}
		if (!rightItemStack.isEmpty()) {
			int timesRepaired = getTimesRepaired(rightItemStack);
			String rightItemRepairCost = timesRepaired >= maxRepairTimes ?
			                             TextFormatting.RED + "" + timesRepaired + TextFormatting.DARK_GRAY + '/' + maxRepairTimes :
			                             TextFormatting.DARK_GRAY + "" + timesRepaired + '/' + maxRepairTimes;

			mc.fontRenderer.drawString(rightItemRepairCost, guiRepairXOrigin + 76, guiRepairYOrigin + 37, 0xffffff);
		}
		if (!leftItemStack.isEmpty() && !rightItemStack.isEmpty()) {
			int timesRepaired = getTimesRepaired(leftItemStack) + getTimesRepaired(rightItemStack) + 1;
			String finalItemRepairCost = TextFormatting.DARK_GRAY + "" + timesRepaired + '/' + maxRepairTimes;

			if (timesRepaired <= maxRepairTimes) {
				mc.fontRenderer.drawString(
					finalItemRepairCost, guiRepairXOrigin + 133, guiRepairYOrigin + 37, 0xffffff
				);
			}
		}
	}

	/**
	 * Returns how many times an item has been used with an Anvil
	 *
	 * @param itemStack the item stack
	 * @return int
	 */
	protected static int getTimesRepaired(ItemStack itemStack) {
		/*
    	times repaired: repair cost, xp
    	0: 0, 2
    	1: 1, 3
    	2: 3, 5
    	3: 7, 9
    	4: 15, 17
    	5: 31, 33
    	6: 63, 65 (too expensive)

    	equation is 2^n - 1, log2(n + 1)
    	*/
		return log(itemStack.getRepairCost() + 1, 2);
	}

	/**
	 * Takes the log with a specified base.
	 *
	 * @param x
	 * @param base
	 * @return log[base](x)
	 */
	@SuppressWarnings("SameParameterValue")
	private static int log(int x, int base) {
		return (int) (Math.log(x) / Math.log(base));
	}

	/**
	 * Client tick event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent()
	public static void onClientTickEvent(ClientTickEvent event) {
		if (mc.isSingleplayer() && useUnlimitedSprintingSP) {
			makeSprintingUnlimited();
		}
	}


	/**
	 * Lets the player sprint longer than 30 seconds at a time. Needs to be called on every game tick to be effective.
	 */
	public static void makeSprintingUnlimited() {
		if (mc.player == null) { return; }

		//sprintingTicksLeft is set to 600 when EntityPlayerSP.setSprinting() is called
		mc.player.sprintingTicksLeft = mc.player.isSprinting() ? 600 : 0;
	}


	/**
	 * Toggles quick sign placement ability
	 *
	 * @return boolean
	 */
	public static boolean toggleUseQuickPlaceSign() {
		return useQuickPlaceSign = !useQuickPlaceSign;
	}

	/**
	 * Toggles unlimited sprinting
	 *
	 * @return boolean
	 */
	public static boolean toggleUseUnlimitedSprinting() {
		return useUnlimitedSprintingSP = !useUnlimitedSprintingSP;
	}

	/**
	 * Toggles showing anvil repairs
	 *
	 * @return boolean
	 */
	public static boolean toggleShowAnvilRepairs() {
		return showAnvilRepairs = !showAnvilRepairs;
	}
}
