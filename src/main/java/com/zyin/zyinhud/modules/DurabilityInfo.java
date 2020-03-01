package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.config.ZyinHUDConfig;
import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.ZyinHUDSound;
//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
import com.zyin.zyinhud.compat.TinkersConstructCompat;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.DurabilityInfoOptions;
import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.Localization;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Durability Info checks to see if any equipment (items in the hotbar, and armor) is damaged
 * and then displays info about them onto the HUD.
 */
public class DurabilityInfo extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(DurabilityInfo.class);
	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled;

	/**
	 * The current mode for this module
	 */
	protected static DurabilityInfoOptions.DurabilityInfoTextModes mode;

	private static final ResourceLocation durabilityIconsResourceLocation =
		new ResourceLocation("zyinhud:textures/durability_icons.png");


	/** The height of the tools being rendered */
	private static final int toolIconWidth = DurabilityInfoOptions.toolIconWidth;
	/** The width of the tools being rendered */
	private static final int toolIconHeight = DurabilityInfoOptions.toolIconHeight;
	/** The limit on how often to update the status of damaged items */
	private static final int durabilityUpdateFrequency = 600;   // In milliseconds? Should this be configurable?
	/** X coordinate of the texture inside of the image */
	protected static int armorDurabilityIconU = 0;
	/** Y coordinate of the texture inside of the image */
	protected static int armorDurabilityIconV = 0;
	private static boolean doLogUnequips;
	private static boolean autoUnequipArmor;
	private static boolean autoUnequipTools;
	private static boolean showArmorDurability;
	//	private static boolean showDamageAsPercentage; //_Consider: implement this?
	private static boolean showItemDurability;
	private static boolean showIndividualArmorIcons;
	private static boolean useColoredNumbers;
	private static boolean hideDurabilityInfoInChat;
	private static float durabilityIconScale;
	private static float armorDurabilityScaler = 0.2f;
	/** Width of the image */
	private static int armorDurabilityIconWidth = (int) (5 * 16 * armorDurabilityScaler);
	/** Height of the image */
	private static int armorDurabilityIconHeight = (int) (7.5 * 16 * armorDurabilityScaler);

	/** Where the armor icon is rendered */
	private static int durabilityLocX;
	private static int durabilityLocY;

	private static float armorDurabilityDisplayThreshold;
	private static float itemDurabilityDisplayThreshold;

	//where the tool icons are rendered; SEEMS TO BE UNUSED
//	protected static int equipmentLocX = 20 + armorDurabilityIconWidth;
//	protected static int equipmentLocY = 20;

	//used to push items into the list of broken equipment to render
	private static ArrayList<ItemStack> damagedItemsList = new ArrayList<ItemStack>(13);


	/**
	 * The last time the item cache was generated
	 */
	private static long lastGenerate;

	static { loadFromConfig(); }

	public static void loadFromConfig() {
		doLogUnequips = ZyinHUDConfig.enableLoggingUnequip.get();
		isEnabled = ZyinHUDConfig.enableDurabilityInfo.get();
		mode = ZyinHUDConfig.durabilityInfoTextMode.get();
		autoUnequipArmor = ZyinHUDConfig.autoUnequipArmor.get();
		autoUnequipTools = ZyinHUDConfig.autoUnequipTools.get();
		showArmorDurability = ZyinHUDConfig.showArmorDurability.get();
//		showDamageAsPercentage = ZyinHUDConfig.showDamageAsPercentage.get();
		showItemDurability = ZyinHUDConfig.showItemDurability.get();
		showIndividualArmorIcons = ZyinHUDConfig.showIndividualArmorIcons.get();
		useColoredNumbers = ZyinHUDConfig.useColoredNumbers.get();
		durabilityIconScale = ZyinHUDConfig.durabilityScale.get().floatValue();
		hideDurabilityInfoInChat = ZyinHUDConfig.hideDurabilityInfoInChat.get();
		durabilityLocX = ZyinHUDConfig.durabilityHorizontalPos.get();
		durabilityLocY = ZyinHUDConfig.durabilityVerticalPos.get();
		armorDurabilityDisplayThreshold = ZyinHUDConfig.armorDurabilityDisplayThreshold.get().floatValue();
		itemDurabilityDisplayThreshold = ZyinHUDConfig.itemDurabilityDisplayThreshold.get().floatValue();
	}

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableDurabilityInfo.set(!isEnabled);
		ZyinHUDConfig.enableDurabilityInfo.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * Renders the main durability icon and any damaged tools onto the screen.
	 */
	public static void renderOntoHUD() {
		//if the player is in the world (which can be assumed if the mouse is grabbed or chat is shown)
		//and not in a menu, other than the custom Options menu or chat (when hideDurabilityInfoInChat == false)
		//and F3 not shown
		if (
			DurabilityInfo.isEnabled &&
			(mc.mouseHelper.isMouseGrabbed() || ((mc.currentScreen instanceof ChatScreen && !hideDurabilityInfoInChat)/* || tabIsSelectedInOptionsGui()*/)) &&
			!mc.gameSettings.showDebugInfo
		) {
			//don't waste time recalculating things every tick
			if (System.currentTimeMillis() - lastGenerate > durabilityUpdateFrequency) {
				calculateDurabilityIcons();
			}
			//TODO: compact this?
			boolean armorExists = false;

			for (ItemStack itemStack : damagedItemsList) {
				if (itemStack.getItem() instanceof ArmorItem || itemStack.getItem() instanceof ElytraItem) {
					armorExists = true;
				}
			}

			int numTools = 0;
			int numArmors = 0;

			for (ItemStack itemStack : damagedItemsList) {
				Item equipment = itemStack.getItem();
				int xPos = (int) Math.floor(durabilityLocX / durabilityIconScale);
				int yPos = (int) Math.floor(durabilityLocY / durabilityIconScale);

				//if this equipment is an armor
				if (equipment instanceof ArmorItem || equipment instanceof ElytraItem) {
					if (showArmorDurability) {
						GL11.glScalef(durabilityIconScale, durabilityIconScale, durabilityIconScale);

						if (showIndividualArmorIcons) {
							renderItemIconWithDurability(itemStack, xPos, (yPos + (numArmors * toolIconHeight)));
							numArmors++;
						}
						else {
							drawBrokenArmorTexture(xPos, yPos);
						}

						GL11.glScalef(1f / durabilityIconScale, 1f / durabilityIconScale, 1f / durabilityIconScale);
					}
				}
				else {
					//if this equipment is an equipment/equipment
					if (showItemDurability) {
						GL11.glScalef(durabilityIconScale, durabilityIconScale, durabilityIconScale);

						//Render the item icon, pushing it to the right if armor is also being rendered
						renderItemIconWithDurability(
							itemStack,
							(armorExists && showArmorDurability) ? xPos : (xPos + toolIconWidth),
							(yPos + (numTools * toolIconHeight))
						);

						GL11.glScalef(1f / durabilityIconScale, 1f / durabilityIconScale, 1f / durabilityIconScale);

						numTools++;
					}
				}
			}
		}
	}

	/**
	 * Draws an ItemStack at the specified location on screen with its durability bar and number.
	 *
	 * @param itemStack the item stack
	 * @param x         the x
	 * @param y         the y
	 */
	protected static void renderItemIconWithDurability(ItemStack itemStack, int x, int y) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);    //so the enchanted item effect is rendered properly

		//render the item with enchant effect
		itemRenderer.getValue().renderItemAndEffectIntoGUI(itemStack, x, y);

		//render the item's durability bar
		itemRenderer.getValue().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x, y, null);

		GL11.glDisable(GL11.GL_LIGHTING);    //the itemRenderer.renderItem() method enables lighting

		//render the number of durability it has left
		if (mode != DurabilityInfoOptions.DurabilityInfoTextModes.NONE && itemStack.getDamage() != 0) {
// _CHECK: I can only assume that unicode is somehow supported by default, because I can't seem to find anything dealing with it.
//				boolean unicodeFlag = mc.fontRenderer.getUnicodeFlag();
//				mc.fontRenderer.setUnicodeFlag(true);

			String damageStringText;
			int itemDamage = itemStack.getDamage();
			int itemMaxDamage = itemStack.getMaxDamage();

			if (mode == DurabilityInfoOptions.DurabilityInfoTextModes.PERCENTAGE) {
				damageStringText = 100 - (int) ((double) itemDamage / itemMaxDamage * 100) + "%";
			}
			else if (mode == DurabilityInfoOptions.DurabilityInfoTextModes.TEXT) {
				if (TinkersConstructCompat.isTConstructItem(itemStack.getItem())) {
					Integer temp = TinkersConstructCompat.getDamage(itemStack);
					if (temp != null) {
						itemDamage = temp;
						itemMaxDamage = TinkersConstructCompat.getMaxDamage(itemStack);
						damageStringText = Integer.toString(itemMaxDamage - itemDamage);
					}
					else { damageStringText = ""; }
				}
				else { damageStringText = Integer.toString(itemMaxDamage - itemDamage); }

			}
			else { damageStringText = ""; }

			int damageStringX = x + toolIconWidth - mc.fontRenderer.getStringWidth(damageStringText);
			int damageStringY = y + toolIconHeight - mc.fontRenderer.FONT_HEIGHT - 2;
			int damageStringColor = useColoredNumbers ?
			                        getDamageColor(itemStack.getDamage(), itemStack.getMaxDamage()) :
			                        0xffffff;

			GL11.glDisable(GL11.GL_DEPTH_TEST);    //so the text renders above the item
			mc.fontRenderer.drawStringWithShadow(damageStringText, damageStringX, damageStringY, damageStringColor);
//				mc.fontRenderer.setUnicodeFlag(unicodeFlag);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
	}

	/**
	 * Returns a green/yellow/red color spectrum based on the different between currentDamage and maxDamage.
	 *
	 * @param currentDamage the current damage
	 * @param maxDamage     the max damage
	 * @return int
	 */
	protected static int getDamageColor(int currentDamage, int maxDamage) {
		float percent = 100 - (int) ((double) currentDamage / maxDamage * 100);

		if (percent < 50) { return 0xff0000 + ((int) (0xff * percent / 50) << 8); }
		else { return 0x00ff00 + ((int) (0xff * (100 - (percent - 50) * 2) / 100) << 16); }
	}


	/**
	 * Draws the broken durability image
	 *
	 * @param x the x
	 * @param y the y
	 */
	protected static void drawBrokenArmorTexture(int x, int y) {
		GL11.glEnable(GL11.GL_BLEND);    //for a transparent texture
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glColor4f(255f, 255f, 255f, 255f);    //fixes transparency issue when a InfoLine Notification is displayed

		ZyinHUDRenderer.renderCustomTexture(
			x, y,
			armorDurabilityIconU, armorDurabilityIconV,
			(int) (armorDurabilityIconWidth / armorDurabilityScaler),
			(int) (armorDurabilityIconHeight / armorDurabilityScaler),
			durabilityIconsResourceLocation, armorDurabilityScaler
		);

		//GL11.glDisable(GL11.GL_BLEND);	//this turned the screen dark in the options menu
	}

	/**
	 * Finds items in the players hot bar and equipped armor that is damaged and adds them to the damagedItemsList list.
	 */
	protected static void calculateDurabilityIcons() {
		//if the player is in the world
		//and not in a menu (except for chat and the custom Options menu)
		//and not typing
		if (
			mc.mouseHelper.isMouseGrabbed() ||
			(mc.currentScreen instanceof ChatScreen /*|| mc.currentScreen instanceof GuiZyinHUDOptions && ((GuiZyinHUDOptions)mc.currentScreen).isButtonTabSelected(Localization.get("durabilityinfo.name"))*/) &&
			!mc.gameSettings.keyBindPlayerList.isPressed()
		) {
			damagedItemsList.clear();
			unequipDamagedArmor();
			unequipDamagedTool();
			calculateDurabilityIconsForTools();
			calculateDurabilityIconsForArmor();
			lastGenerate = System.currentTimeMillis();
		}
	}

	/**
	 * Examines the players first 9 inventory slots (the players hotbar) and sees if any tools are damaged.
	 * It adds damaged tools to the static damagedItemsList list.
	 */
	private static void calculateDurabilityIconsForTools() {
		NonNullList<ItemStack> items = mc.player.inventory.mainInventory;
		NonNullList<ItemStack> offhanditems = mc.player.inventory.offHandInventory;

		for (int i = 0; i < 10; i++) {
			ItemStack itemStack;
			if (i < 9) { itemStack = items.get(i); }
			else { itemStack = offhanditems.get(0); }//There will be only one item here

			if (!itemStack.isEmpty()) {
				Item item = itemStack.getItem();
				if (isTool(item)) {
					int itemDamage = itemStack.getDamage();
					int maxDamage = itemStack.getMaxDamage();

					if (maxDamage != 0 && ((1 - ((double) itemDamage / maxDamage)) <= itemDurabilityDisplayThreshold)) {
						damagedItemsList.add(itemStack);
					}
				}
			}
		}
	}

	/**
	 * Examines the players current armor and sees if any of them are damaged.
	 * It adds damaged armors to the static damagedItemsList list.
	 */
	private static void calculateDurabilityIconsForArmor() {
		NonNullList<ItemStack> armorStacks = mc.player.inventory.armorInventory;

		//iterate backwards over the armor the user is wearing so the helm is displayed first
		for (int i = armorStacks.size() - 1; i >= 0; i--) {
			ItemStack armorStack = armorStacks.get(i);
			if (!armorStack.isEmpty()) {
				int itemDamage = armorStack.getDamage();
				int maxDamage = armorStack.getMaxDamage();

				if (maxDamage != 0 && (1 - (double) itemDamage / maxDamage) <= armorDurabilityDisplayThreshold) {
					damagedItemsList.add(armorStack);
				}
			}
		}
	}

	//FIXME: deprecate this

	/**
	 * Determines if the item is a tool. Pickaxe, sword, bow, shears, etc.
	 *
	 * @param item
	 * @return
	 */
	private static boolean isTool(Item item) {
		return item instanceof ToolItem ||
		       item instanceof SwordItem ||
		       item instanceof BowItem ||
		       item instanceof HoeItem ||
		       item instanceof ShearsItem ||
		       item instanceof FishingRodItem ||
		       item instanceof ShieldItem ||
		       TinkersConstructCompat.isTConstructHarvestTool(item) ||
		       TinkersConstructCompat.isTConstructWeapon(item) ||
		       TinkersConstructCompat.isTConstructBow(item);
	}

	/**
	 * Takes off any armor the player is wearing if it is close to being destroyed,
	 * and puts it in their inventory if the player has room in their inventory.
	 */
	private static void unequipDamagedArmor() {
		if (autoUnequipArmor) {
			NonNullList<ItemStack> itemStacks = mc.player.inventory.armorInventory;

			//iterate over the armor the user is wearing
			for (int i = 0; i < itemStacks.size(); i++) {
				ItemStack itemStack = itemStacks.get(i);
				if (
					!(itemStack.isEmpty() || itemStack.getItem() instanceof ElytraItem ||
					  (itemStack.isEnchanted() && EnchantmentHelper.hasBindingCurse(itemStack)))
				) {
					int itemDamage = itemStack.getDamage();
					int maxDamage = itemStack.getMaxDamage();

					if (maxDamage != 0 && (maxDamage - itemDamage < 5)) {
						InventoryUtil.moveArmorIntoPlayerInventory(i);
						ZyinHUDSound.playPlopSound();
						ZyinHUDRenderer.displayNotification(
							Localization.get("durabilityinfo.name") +
							Localization.get("durabilityinfo.unequippeditem") +
							itemStack.getDisplayName().getString()
						);
						if (doLogUnequips) {
							logger.info(
								"Unequipped {} because it was at low durability ({}/{})",
								itemStack.getDisplayName().getString(), itemDamage, maxDamage
							);
						}
					}
				}
			}
		}
	}

	/**
	 * Takes off any tools the player is using if it is close to being destroyed,
	 * and puts it in their inventory if the player has room in their inventory.
	 */
	private static void unequipDamagedTool() {
		if (autoUnequipTools) {
			ItemStack itemStack = mc.player.inventory.getCurrentItem();

			if (!itemStack.isEmpty()) {
				Item item = itemStack.getItem();

				if (
					item instanceof ToolItem || item instanceof SwordItem ||
					item instanceof BowItem || item instanceof HoeItem ||
					item instanceof ShearsItem || item instanceof FishingRodItem
				) {
					int itemDamage = itemStack.getDamage();
					int maxDamage = itemStack.getMaxDamage();
					int threshold = (item instanceof FishingRodItem) ? 5 : 15;

					if (
						maxDamage != 0 &&
						maxDamage - itemDamage < threshold &&                //less than 15 durability
						(float) itemDamage / (float) maxDamage > 0.9        //less than 10%
					) {
						InventoryUtil.moveHeldItemIntoPlayerInventory();
						ZyinHUDSound.playPlopSound();
						ZyinHUDRenderer.displayNotification(
							Localization.get("durabilityinfo.name") +
							Localization.get("durabilityinfo.unequippeditem") +
							item.getDisplayName(itemStack).getString()
						);
						if (doLogUnequips) {
							logger.info(
								"Unequipped {} because it was at low durability ({}/{})",
								item.getDisplayName(itemStack).getString(), itemDamage, maxDamage
							);
						}
					}
				}
			}
		}
	}


	/**
	 * Checks to see if the Durability Info tab is selected in GuiZyinHUDOptions
	 * @return
	 */
//    private static boolean tabIsSelectedInOptionsGui()
//    {
//    	return mc.currentScreen instanceof GuiZyinHUDOptions &&
//    		(((GuiZyinHUDOptions)mc.currentScreen).isButtonTabSelected(Localization.get("durabilityinfo.name")));
//    }

	/**
	 * Get durability display threshold for armor float.
	 *
	 * @return the float
	 */
	public static float getArmorDurabilityDisplayThreshold() {
		return armorDurabilityDisplayThreshold;
	}

	/**
	 * Set durability display threshold for armor.
	 *
	 * @param durabilityDisplayThreshold the durability display threshold
	 */
	public static void setArmorDurabilityDisplayThreshold(float durabilityDisplayThreshold) {
		armorDurabilityDisplayThreshold = durabilityDisplayThreshold;
		calculateDurabilityIcons();
	}

	/**
	 * Get durability display threshold for item float.
	 *
	 * @return the float
	 */
	public static float getItemDurabilityDisplayThreshold() {
		return itemDurabilityDisplayThreshold;
	}

	/**
	 * Set durability display threshold for item.
	 *
	 * @param durabilityDisplayThreshold the durability display threshold
	 */
	public static void setItemDurabilityDisplayThreshold(float durabilityDisplayThreshold) {
		itemDurabilityDisplayThreshold = durabilityDisplayThreshold;
		calculateDurabilityIcons();
	}

	/**
	 * Gets the horizontal location where the durability icons are rendered.
	 *
	 * @return int
	 */
	public static int getHorizontalLocation() {
		return durabilityLocX;
	}

	/**
	 * Sets the horizontal location where the durability icons are rendered.
	 *
	 * @param x the x
	 * @return the new x location
	 */
	public static int setHorizontalLocation(int x) {
		durabilityLocX = MathHelper.clamp(x, 0, mc.mainWindow.getWidth());
//		equipmentLocX = durabilityLocX + armorDurabilityIconWidth;
		return durabilityLocX;
	}

	/**
	 * Gets the vertical location where the durability icons are rendered.
	 *
	 * @return int
	 */
	public static int getVerticalLocation() {
		return durabilityLocY;
	}

	/**
	 * Sets the vertical location where the durability icons are rendered.
	 *
	 * @param y the y
	 * @return the new y location
	 */
	public static int setVerticalLocation(int y) {
		durabilityLocY = MathHelper.clamp(y, 0, mc.mainWindow.getHeight());
//		equipmentLocY = durabilityLocY;
		return durabilityLocY;
	}

	/**
	 * Toggles showing durability for armor
	 *
	 * @return boolean
	 */
	public static boolean toggleShowArmorDurability() {
		return showArmorDurability = !showArmorDurability;
	}

	/**
	 * Toggles showing durability for items
	 *
	 * @return boolean
	 */
	public static boolean toggleShowItemDurability() {
		return showItemDurability = !showItemDurability;
	}

	/**
	 * Toggles showing icons or an image for broken armor
	 *
	 * @return boolean
	 */
	public static boolean toggleShowIndividualArmorIcons() {
		return showIndividualArmorIcons = !showIndividualArmorIcons;
	}

	/**
	 * Toggles unequipping breaking armor
	 *
	 * @return boolean
	 */
	public static boolean toggleAutoUnequipArmor() {
		return autoUnequipArmor = !autoUnequipArmor;
	}

	/**
	 * Toggles unequipping breaking tools
	 *
	 * @return boolean
	 */
	public static boolean toggleAutoUnequipTools() {
		return autoUnequipTools = !autoUnequipTools;
	}

	/**
	 * Toggles using color
	 *
	 * @return boolean
	 */
	public static boolean toggleUseColoredNumbers() {
		return useColoredNumbers = !useColoredNumbers;
	}

	public static boolean toggleHideDurabilityInfoInChat() {
		return hideDurabilityInfoInChat = !hideDurabilityInfoInChat;
	}
}
