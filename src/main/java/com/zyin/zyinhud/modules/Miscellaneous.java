package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.config.ZyinHUDConfig;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.CheckForNull;
import java.lang.reflect.Field;

import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.findField;

/**
 * The Miscellaneous module has other functionality not relating to anything specific.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Miscellaneous extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(Miscellaneous.class);
//	public static final Miscellaneous instance = new Miscellaneous();

	private static boolean useQuickPlaceSign;
	private static boolean useUnlimitedSprintingSP;
	private static boolean showAnvilRepairs;

	private static final int maxRepairTimes = 6;

	/**
	 * The private RepairContainer field "inputSlots"
	 */
	private static final Field anvilInputSlots = findField(RepairContainer.class, "field_82853_g");

	static { loadFromConfig(); }

	public static void loadFromConfig() {
		useQuickPlaceSign = ZyinHUDConfig.useQuickPlaceSign.get();
		useUnlimitedSprintingSP = ZyinHUDConfig.useUnlimitedSprintingSP.get();
		showAnvilRepairs = ZyinHUDConfig.showAnvilRepairs.get();
	}

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

	@CheckForNull
	private static IInventory getAnvilInputSlots(AnvilScreen gui) {
		RepairContainer anvil = gui.getContainer();
		try { return (IInventory) anvilInputSlots.get(anvil); }
		catch (IllegalAccessException e) { e.printStackTrace(); }
		return null;
	}

	/**
	 * Draws text above the anvil's repair slots showing how many more times it can be repaired
	 *
	 * @param guiRepair the gui repair
	 */
	public static void drawGuiRepairCounts(AnvilScreen guiRepair) {
		IInventory inputSlots = getAnvilInputSlots(guiRepair);
		if (inputSlots == null) { return; }

		int xSize = guiRepair.getXSize();
		int ySize = guiRepair.getYSize();

		int guiRepairXOrigin = (guiRepair.width - xSize) / 2;
		int guiRepairYOrigin = (guiRepair.height - ySize) / 2;

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
	 * Returns how many times an item has been used with an anvil
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
