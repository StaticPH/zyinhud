package com.zyin.zyinhud.mods;

import com.zyin.zyinhud.ZyinHUD;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.ZyinHUDUtil;

/**
 * The Miscellaneous mod has other functionality not relating to anything specific.
 */
public class Miscellaneous extends ZyinHUDModBase
{
	/**
	 * The constant instance.
	 */
	public static final Miscellaneous instance = new Miscellaneous();

	/**
	 * The constant UseQuickPlaceSign.
	 */
	public static boolean UseQuickPlaceSign;
	/**
	 * The constant UseUnlimitedSprinting.
	 */
	public static boolean UseUnlimitedSprinting;
	/**
	 * The constant ShowAnvilRepairs.
	 */
	public static boolean ShowAnvilRepairs;
	
    private static final int maxRepairTimes = 6;


	/**
	 * Gui open event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void GuiOpenEvent(GuiOpenEvent event) {
		if (UseQuickPlaceSign && event.getGui() instanceof EditSignScreen && mc.player.isSneaking()) {
			event.setCanceled(true);
		}
	}


	/**
	 * Draw screen event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void DrawScreenEvent(DrawScreenEvent.Post event) {
		if (ShowAnvilRepairs && event.getGui() instanceof AnvilScreen) {
			DrawGuiRepairCounts((AnvilScreen) event.getGui());
		}
	}

	/**
	 * Draws text above the anvil's repair slots showing how many more times it can be repaired
	 *
	 * @param guiRepair the gui repair
	 */
	public void DrawGuiRepairCounts(AnvilScreen guiRepair) {
		RepairContainer anvil = guiRepair.getContainer();
    	IInventory inputSlots = ZyinHUDUtil.GetFieldByReflection(RepairContainer.class, anvil, "inputSlots", "field_82853_g");

    	int xSize = guiRepair.getXSize();
    	int ySize = guiRepair.getYSize();

    	int guiRepairXOrigin = guiRepair.width/2 - xSize/2;
    	int guiRepairYOrigin = guiRepair.height/2 - ySize/2;
		
        ItemStack leftItemStack = inputSlots.getStackInSlot(0);
        ItemStack rightItemStack = inputSlots.getStackInSlot(1);
        ItemStack finalItemStack = inputSlots.getStackInSlot(2);
        
        if(!leftItemStack.isEmpty())
        {
        	int timesRepaired = GetTimesRepaired(leftItemStack);
        	String leftItemRepairCost;

			if (timesRepaired >= maxRepairTimes)
				leftItemRepairCost = TextFormatting.RED.toString() + timesRepaired + TextFormatting.DARK_GRAY + "/" + maxRepairTimes;
			else
				leftItemRepairCost = TextFormatting.DARK_GRAY.toString() + timesRepaired + "/" + maxRepairTimes;

			mc.fontRenderer.drawString(leftItemRepairCost, guiRepairXOrigin + 26, guiRepairYOrigin + 37, 0xffffff);
		}
        if(!rightItemStack.isEmpty())
        {
        	int timesRepaired = GetTimesRepaired(rightItemStack);
        	String rightItemRepairCost;

			if (timesRepaired >= maxRepairTimes)
				rightItemRepairCost = TextFormatting.RED.toString() + timesRepaired + TextFormatting.DARK_GRAY + "/" + maxRepairTimes;
			else
				rightItemRepairCost = TextFormatting.DARK_GRAY.toString() + timesRepaired + "/" + maxRepairTimes;

			mc.fontRenderer.drawString(rightItemRepairCost, guiRepairXOrigin + 76, guiRepairYOrigin + 37, 0xffffff);
		}
        if(!leftItemStack.isEmpty() && !rightItemStack.isEmpty())
        {
        	int timesRepaired = GetTimesRepaired(leftItemStack) + GetTimesRepaired(rightItemStack) + 1;
			String finalItemRepairCost = TextFormatting.DARK_GRAY.toString() + timesRepaired + "/" + maxRepairTimes;

			if(timesRepaired <= maxRepairTimes) {
        		mc.fontRenderer.drawString(finalItemRepairCost, guiRepairXOrigin + 133, guiRepairYOrigin + 37, 0xffffff);
			}
		}
	}

	/**
	 * Returns how many times an item has been used with an Anvil
	 *
	 * @param itemStack the item stack
	 * @return int
	 */
	protected static int GetTimesRepaired(ItemStack itemStack) {
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
     * @param x
     * @param base
     * @return log[base](x)
     */
    private static int log(int x, int base)
    {
		return (int)(Math.log(x) / Math.log(base));
    }

	/**
	 * Client tick event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void ClientTickEvent(ClientTickEvent event)
	{
		if(UseUnlimitedSprinting) {
			MakeSprintingUnlimited();
		}
	}


	/**
	 * Lets the player sprint longer than 30 seconds at a time. Needs to be called on every game tick to be effective.
	 */
	public static void MakeSprintingUnlimited() //_CHECK: something tells me this wont work anymore with a client-side only mod
	{
		if(mc.player == null)
			return;
		
		if(!mc.player.isSprinting())
			mc.player.sprintingTicksLeft = 0;
		else
			mc.player.sprintingTicksLeft = 600;	//sprintingTicksLeft is set to 600 when EntityPlayerSP.setSprinting() is called
	}



	/**
	 * Toggles quick sign placement ability
	 *
	 * @return boolean
	 */
	public static boolean ToggleUseQuickPlaceSign() {
		return UseQuickPlaceSign = !UseQuickPlaceSign;
	}

	/**
	 * Toggles unlimited sprinting
	 *
	 * @return boolean
	 */
	public static boolean ToggleUseUnlimitedSprinting() {
		return UseUnlimitedSprinting = !UseUnlimitedSprinting;
	}

	/**
	 * Toggles showing anvil repairs
	 *
	 * @return boolean
	 */
	public static boolean ToggleShowAnvilRepairs() {
		return ShowAnvilRepairs = !ShowAnvilRepairs;
    }
}
