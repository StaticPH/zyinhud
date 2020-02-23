package com.zyin.zyinhud.modules;


import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.ItemSelectorOptions;
import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.Localization;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

/**
 * Item Selector allows the player to conveniently swap their currently selected
 * hotbar item with something in their inventory.
 */
public class ItemSelector extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(ItemSelector.class);
	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled = ZyinHUDConfig.enableItemSelector.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableItemSelector.set(!isEnabled);
		ZyinHUDConfig.enableItemSelector.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * The current mode for this module
	 */
	public static ItemSelectorOptions.ItemSelectorModes mode = ZyinHUDConfig.itemSelectorMode.get();

	/**
	 * Determines if the side buttons of supported mice can be used for item selection
	 */
	static boolean useMouseSideButtons = ZyinHUDConfig.itemSelectorSideButtons.get();

	protected static final ResourceLocation widgetTexture = new ResourceLocation("textures/gui/widgets.png");

	public static final int WHEEL_UP = -1;
	public static final int WHEEL_DOWN = 1;

	static int timeout = ZyinHUDConfig.itemSelectorTimeout.get();

	private static int[] slotMemory = new int[PlayerInventory.getHotbarSize()];

	private static boolean isCurrentlySelecting = false;
	private static boolean isCurrentlyRendering = false;
	private static int ticksToShow = 0;
	private static int scrollAmount = 0;
	private static int previousDir = 0;
	private static int targetInvSlot = -1;
	private static int currentHotbarSlot = 0;
	private static NonNullList<ItemStack> currentInventory = null;

	/**
	 * Scrolls the selector towards the specified direction. This will cause the item selector overlay to show.
	 *
	 * @param direction Direction player is scrolling toward
	 */
	public static void scroll(int direction) {
		// Bind to current player state
		currentHotbarSlot = mc.player.inventory.currentItem;
		currentInventory = mc.player.inventory.mainInventory;
		if (!adjustSlot(direction)) {
			cleanupWhenDone();
			return;
		}

		slotMemory[currentHotbarSlot] = targetInvSlot;

		scrollAmount++;
		ticksToShow = timeout;
		isCurrentlySelecting = true;
	}

	/**
	 * Swaps the currently selected item by one toward the given direction
	 *
	 * @param direction Direction player is scrolling toward
	 */
	public static void sideButton(int direction) {
		currentHotbarSlot = mc.player.inventory.currentItem;
		currentInventory = mc.player.inventory.mainInventory;

		if (adjustSlot(direction)) {
			slotMemory[currentHotbarSlot] = targetInvSlot;
			selectItem();
		}
		else { cleanupWhenDone(); }
	}

	/**
	 * Calculates the adjustment of the currently selected hotbar slot by the given direction
	 *
	 * @param direction Direction to adjust towards
	 * @return True if successful, false if attempting to switch enchanted item
	 * or no target is available
	 */
	private static boolean adjustSlot(int direction) {
		if (!mc.isSingleplayer()) {
			if (
				!currentInventory.get(currentHotbarSlot).isEmpty() &&
				currentInventory.get(currentHotbarSlot).isEnchanted()
			) {
				ZyinHUDRenderer.displayNotification(Localization.get("itemselector.error.enchant"));
				return false;
			}
		}

		int memory = slotMemory[currentHotbarSlot];    //'memory' is where the cursor was last located for this particular hotbar slot

		for (int i = 0; i < 36; i++) {
			// This complicated bit of logic allows for side button mechanism to
			// go back and forth without skipping slots
			if (scrollAmount != 0 || previousDir == direction) { memory += direction; }

			if (memory < 9 || memory >= 36) { memory = direction == WHEEL_DOWN ? 9 : 35; }

			previousDir = direction;

			if ((mode == ItemSelectorOptions.ItemSelectorModes.SAME_COLUMN && memory % 9 != currentHotbarSlot) ||
			    (currentInventory.get(memory).isEmpty()) ||
			    (!mc.isSingleplayer() && currentInventory.get(memory).isEnchanted())) {
				continue;
			}

			targetInvSlot = memory;
			break;
		}

		if (targetInvSlot == -1) {
			ZyinHUDRenderer.displayNotification(Localization.get("itemselector.error.empty"));
			return false;
		}
		else { return true; }
	}

	public static void onHotkeyPressed() {
		if (!ItemSelector.isEnabled) { return; }

		currentHotbarSlot = mc.player.inventory.currentItem;
		currentInventory = mc.player.inventory.mainInventory;
		isCurrentlyRendering = true;
	}

	public static void onHotkeyAbort() {
		if (ItemSelector.isEnabled) { cleanupWhenDone(); }
	}

	public static void onHotkeyReleased() {
		if (!ItemSelector.isEnabled) { return; }

		if (isCurrentlySelecting) { selectItem(); }
		else { cleanupWhenDone(); }
	}

	/**
	 * If selecting an item, this draws the player's inventory on-screen with the current selection.
	 *
	 * @param partialTicks the partial ticks
	 */
	public static void renderOntoHUD(float partialTicks) {
		if (!ItemSelector.isEnabled || !isCurrentlyRendering) { return; }

		//stop the item selecting if another modifier key is pressed so we don't get stuck in the selecting state
		//TODO: This may need some modifications
		if (
			GLFW.glfwGetKey(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS ||
			GLFW.glfwGetKey(mc.mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
		) {
			cleanupWhenDone();
			return;
		}

		int screenWidth = mc.mainWindow.getScaledWidth();
		int screenHeight = mc.mainWindow.getScaledHeight();
		int invWidth = 182;
		int invHeight = 22 * 3;
		int originX = (screenWidth / 2) - (invWidth / 2);
		int originZ = screenHeight - invHeight - 48;

		if (targetInvSlot > -1) {
			String labelText = currentInventory.get(targetInvSlot).getDisplayName().getString();
			//String labelText = currentInventory[targetInvSlot].getChatComponent().getFormattedText();
			int labelWidth = mc.fontRenderer.getStringWidth(labelText);
			mc.fontRenderer.drawStringWithShadow(
				labelText, (float) ((screenWidth / 2) - (labelWidth / 2)),
				originZ - mc.fontRenderer.FONT_HEIGHT - 2, 0xFFFFFFFF
			);
		}

		GL11.glEnable(GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST); // so the enchanted item effect is rendered properly

		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableLighting();    //prevents the first block in inventory from having no shadows

		int idx = 0;
		for (int z = 0; z < 3; z++) { // 3 rows of the inventory
			for (int x = 0; x < 9; x++) { // 9 cols of the inventory
				if (mode == ItemSelectorOptions.ItemSelectorModes.SAME_COLUMN && x != currentHotbarSlot) {
					// don't draw items that we will never be able to select if Same Column mode is active
					idx++;
					continue;
				}

				// Draws the selection
				if (idx + 9 == targetInvSlot) {
					GLX.glBlendFuncSeparate(770, 771, 1, 0); // so the selection graphic renders properly
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
					ZyinHUDRenderer.renderCustomTexture(
						originX + (x * 20) - 1, originZ + (z * 22) - 1, 0, 22, 24, 24, widgetTexture, 1f
					);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					//GL11.glDisable(GL11.GL_BLEND);	//causes enchanted items to render incorrectly
				}

				ItemStack itemStack = currentInventory.get(idx + 9);

				if (!itemStack.isEmpty()) {
					float anim = itemStack.getAnimationsToGo() - partialTicks;
					int dimX = originX + (x * 20) + 3;
					int dimZ = originZ + (z * 22) + 3;

					if (anim > 0.0F) {
						GL11.glPushMatrix();
						float f2 = 1.0F + anim / 5.0F;
						GL11.glTranslatef(dimX + 8, dimZ + 12, 0.0F);
						GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
						GL11.glTranslatef(-(dimX + 8), -(dimZ + 12), 0.0F);
					}

					itemRenderer.getValue().renderItemAndEffectIntoGUI(itemStack, dimX, dimZ);

					if (anim > 0.0F) { GL11.glPopMatrix(); }

					itemRenderer.getValue().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, dimX, dimZ, null);
				}

				idx++;
			}
		}

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL_RESCALE_NORMAL);
		GlStateManager.disableLighting();    // the itemRenderer.renderItem method enables lighting

		if (isCurrentlySelecting) {
			ticksToShow--;
			if (ticksToShow <= 0) { cleanupWhenDone(); }
		}
	}

	/**
	 * Moves the selected item onto the hotbar.
	 */
	private static void selectItem() {
		ItemStack currentStack = mc.player.inventory.mainInventory.get(currentHotbarSlot);
		ItemStack targetStack = mc.player.inventory.mainInventory.get(targetInvSlot);

		// Check if what was actually selected still exists in player's inventory
		if (!targetStack.isEmpty()) {
			if (!mc.isSingleplayer() && ((!currentStack.isEmpty() && currentStack.isEnchanted()) || targetStack.isEnchanted())) {
				ZyinHUDRenderer.displayNotification(Localization.get("itemselector.error.enchant"));
				cleanupWhenDone();
				return;
			}

			int currentInvSlot = InventoryUtil.translateHotbarIndexToInventoryIndex(currentHotbarSlot);

			//this can happen if the player is using a mod to increase the size of their hotbar
			if (currentInvSlot < 0) {
				ZyinHUDRenderer.displayNotification(Localization.get("itemselector.error.unsupportedhotbar"));
				cleanupWhenDone();
				return;
			}
			InventoryUtil.swap(currentInvSlot, targetInvSlot);
		}
		else { ZyinHUDRenderer.displayNotification(Localization.get("itemselector.error.emptyslot")); }

		cleanupWhenDone();
	}

	/**
	 * Cleans up after we're done rendering or selecting an item
	 */
	private static void cleanupWhenDone() {
		targetInvSlot = -1;
		scrollAmount = 0;
		currentHotbarSlot = 0;
		currentInventory = null;

		ticksToShow = 0;
		isCurrentlyRendering = false;
		isCurrentlySelecting = false;
	}


	public static boolean getIsCurrentlyRendering() {
		return isCurrentlyRendering;
	}

	/**
	 * Get timeout int.
	 *
	 * @return the int
	 */
	public static int getTimeout() {
		return timeout;
	}

	/**
	 * Set timeout.
	 *
	 * @param value the value
	 */
	public static void setTimeout(int value) {//TODO: this sort of thing would need to pass the update to the config :/
		timeout = MathHelper.clamp(value, ItemSelectorOptions.minTimeout, ItemSelectorOptions.maxTimeout);
	}

	public static boolean shouldUseMouseSideButtons() {
		return useMouseSideButtons;
	}

	/**
	 * Toggles using the mouse forward and back buttons
	 *
	 * @return boolean
	 */
	public static boolean toggleUseMouseSideButtons() {
		return useMouseSideButtons = !useMouseSideButtons;
	}
}
