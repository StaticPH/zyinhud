package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUD;
import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.ZyinHUDSound;
//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.DurabilityInfoOptions;
import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.Localization;
import com.zyin.zyinhud.util.ModCompatibility;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Durability Info checks to see if any equipment (items in the hotbar, and armor) is damaged
 * and then displays info about them onto the HUD.
 */
public class DurabilityInfo extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled = ZyinHUDConfig.EnableDurabilityInfo.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		ZyinHUDConfig.EnableDurabilityInfo.set(!Enabled);
		ZyinHUDConfig.EnableDurabilityInfo.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return Enabled = !Enabled;
	}

	/**
	 * The current mode for this module
	 */
	protected static DurabilityInfoOptions.DurabilityInfoTextModes Mode = ZyinHUDConfig.DurabilityInfoTextMode.get();

	protected static final ResourceLocation durabilityIconsResourceLocation =
		new ResourceLocation("zyinhud:textures/durability_icons.png");

	private static boolean AutoUnequipArmor = ZyinHUDConfig.AutoUnequipArmor.get();
	private static boolean AutoUnequipTools = ZyinHUDConfig.AutoUnequipTools.get();
	private static boolean ShowArmorDurability = ZyinHUDConfig.ShowArmorDurability.get();
	//	public static boolean ShowDamageAsPercentage; //_Consider: implement this?
	private static boolean ShowItemDurability = ZyinHUDConfig.ShowItemDurability.get();
	private static boolean ShowIndividualArmorIcons = ZyinHUDConfig.ShowIndividualArmorIcons.get();
	private static boolean UseColoredNumbers = ZyinHUDConfig.UseColoredNumbers.get();

	private static float DurabilityIconScale = ZyinHUDConfig.DurabilityScale.get().floatValue();
	public static boolean HideDurabilityInfoInChat; //TODO: make configurable
	public static final int durabilityUpdateFrequency = 600;

	protected static float armorDurabilityScaler = 0.2f;
	/** X coordinate of the texture inside of the image */
	protected static int armorDurabilityIconU = 0;
	/** Y coordinate of the texture inside of the image */
	protected static int armorDurabilityIconV = 0;
	/** Width of the image */
	protected static int armorDurabilityIconWidth = (int) (5 * 16 * armorDurabilityScaler);
	/** Height of the image */
	protected static int armorDurabilityIconHeight = (int) (7.5 * 16 * armorDurabilityScaler);

	//the height/width of the tools being rendered
	public static int toolIconWidth = DurabilityInfoOptions.toolIconWidth;
	public static int toolIconHeight = DurabilityInfoOptions.toolIconHeight;

	//where the armor icon is rendered
	private static int durabalityLocX = ZyinHUDConfig.DurabilityLocationHorizontal.get();
	private static int durabalityLocY = ZyinHUDConfig.DurabilityLocationVertical.get();

	//where the tool icons are rendered; SEEMS TO BE UNUTILIZED
//	protected static int equipmentLocX = 20 + armorDurabilityIconWidth;
//	protected static int equipmentLocY = 20;

	private static float durabilityDisplayThresholdForArmor = ZyinHUDConfig.DurabilityDisplayThresholdForArmor.get().floatValue();
	private static float durabilityDisplayThresholdForItem = ZyinHUDConfig.DurabilityDisplayThresholdForItem.get().floatValue();

	//used to push items into the list of broken equipment to render
	private static ArrayList<ItemStack> damagedItemsList = new ArrayList<ItemStack>(13);


	/**
	 * The last time the item cache was generated
	 */
	private static long lastGenerate;

	/**
	 * Renders the main durability icon and any damaged tools onto the screen.
	 */
	public static void RenderOntoHUD() {
		//if the player is in the world (which can be assumed if the mouse is grabbed or chat is shown)
		//and not in a menu, other than the custom Options menu or chat (when HideDurabilityInfoInChat == false)
		//and F3 not shown
		if (
			DurabilityInfo.Enabled &&
			(mc.mouseHelper.isMouseGrabbed() || ((mc.currentScreen instanceof ChatScreen && !HideDurabilityInfoInChat)/* || TabIsSelectedInOptionsGui()*/)) &&
			!mc.gameSettings.showDebugInfo
		) {
			//don't waste time recalculating things every tick
			if (System.currentTimeMillis() - lastGenerate > durabilityUpdateFrequency) {
				CalculateDurabilityIcons();
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
				int xPos = (int) Math.floor(durabalityLocX / DurabilityIconScale);
				int yPos = (int) Math.floor(durabalityLocY / DurabilityIconScale);

				//if this equipment is an armor
				if (equipment instanceof ArmorItem || equipment instanceof ElytraItem) {
					if (ShowArmorDurability) {
						GL11.glScalef(DurabilityIconScale, DurabilityIconScale, DurabilityIconScale);

						if (ShowIndividualArmorIcons) {
							RenderItemIcon(itemStack, xPos, (yPos + (numArmors * toolIconHeight)));
							numArmors++;
						}
						else {
							DrawBrokenArmorTexture(xPos, yPos);
						}

						GL11.glScalef(1f / DurabilityIconScale, 1f / DurabilityIconScale, 1f / DurabilityIconScale);
					}
				}
				else {
					//if this equipment is an equipment/equipment
					if (ShowItemDurability) {
						GL11.glScalef(DurabilityIconScale, DurabilityIconScale, DurabilityIconScale);

						//Render the item icon, pushing it to the right if armor is also being rendered
						RenderItemIcon(
							itemStack,
							(armorExists && ShowArmorDurability) ? xPos : (xPos + toolIconWidth),
							(yPos + (numTools * toolIconHeight))
						);

						GL11.glScalef(1f / DurabilityIconScale, 1f / DurabilityIconScale, 1f / DurabilityIconScale);

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
	protected static void RenderItemIcon(ItemStack itemStack, int x, int y) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);    //so the enchanted item effect is rendered properly

		//render the item with enchant effect
		itemRenderer.renderItemAndEffectIntoGUI(itemStack, x, y);

		//render the item's durability bar
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x, y, null);

		GL11.glDisable(GL11.GL_LIGHTING);    //the itemRenderer.renderItem() method enables lighting

		//render the number of durability it has left
		if (Mode != DurabilityInfoOptions.DurabilityInfoTextModes.NONE && itemStack.getDamage() != 0) {
// _CHECK: I can only assume that unicode is somehow supported by default, because I can't seem to find anything dealing with it.
//				boolean unicodeFlag = mc.fontRenderer.getUnicodeFlag();
//				mc.fontRenderer.setUnicodeFlag(true);

			String damageStringText;
			int itemDamage = itemStack.getDamage();
			int itemMaxDamage = itemStack.getMaxDamage();

			if (Mode == DurabilityInfoOptions.DurabilityInfoTextModes.PERCENTAGE) {
				damageStringText = 100 - (int) ((double) itemDamage / itemMaxDamage * 100) + "%";
			}
			else if (Mode == DurabilityInfoOptions.DurabilityInfoTextModes.TEXT) {
				if (ModCompatibility.TConstruct.IsTConstructItem(itemStack.getItem())) {
					Integer temp = ModCompatibility.TConstruct.GetDamage(itemStack);
					if (temp != null) {
						itemDamage = temp;
						itemMaxDamage = ModCompatibility.TConstruct.GetMaxDamage(itemStack);
						damageStringText = Integer.toString(itemMaxDamage - itemDamage);
					}
					else { damageStringText = ""; }
				}
				else { damageStringText = Integer.toString(itemMaxDamage - itemDamage); }

			}
			else { damageStringText = ""; }

			int damageStringX = x + toolIconWidth - mc.fontRenderer.getStringWidth(damageStringText);
			int damageStringY = y + toolIconHeight - mc.fontRenderer.FONT_HEIGHT - 2;
			int damageStringColor = 0xffffff;

			if (UseColoredNumbers) {
				damageStringColor = GetDamageColor(itemStack.getDamage(), itemStack.getMaxDamage());
			}

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
	protected static int GetDamageColor(int currentDamage, int maxDamage) {
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
	protected static void DrawBrokenArmorTexture(int x, int y) {
		GL11.glEnable(GL11.GL_BLEND);    //for a transparent texture
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glColor4f(255f, 255f, 255f, 255f);    //fixes transparency issue when a InfoLine Notification is displayed

		ZyinHUDRenderer.RenderCustomTexture(
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
	protected static void CalculateDurabilityIcons() {
		//if the player is in the world
		//and not in a menu (except for chat and the custom Options menu)
		//and not typing
		if (
			mc.mouseHelper.isMouseGrabbed() ||
			(mc.currentScreen instanceof ChatScreen /*|| mc.currentScreen instanceof GuiZyinHUDOptions && ((GuiZyinHUDOptions)mc.currentScreen).IsButtonTabSelected(Localization.get("durabilityinfo.name"))*/) &&
			!mc.gameSettings.keyBindPlayerList.isPressed()
		) {
			damagedItemsList.clear();
			UnequipDamagedArmor();
			UnequipDamagedTool();
			CalculateDurabilityIconsForTools();
			CalculateDurabilityIconsForArmor();
			lastGenerate = System.currentTimeMillis();
		}
	}

	/**
	 * Examines the players first 9 inventory slots (the players hotbar) and sees if any tools are damaged.
	 * It adds damaged tools to the static damagedItemsList list.
	 */
	private static void CalculateDurabilityIconsForTools() {
		NonNullList<ItemStack> items = mc.player.inventory.mainInventory;
		NonNullList<ItemStack> offhanditems = mc.player.inventory.offHandInventory;

		for (int i = 0; i < 10; i++) {
			ItemStack itemStack;
			if (i < 9) { itemStack = items.get(i); }
			else { itemStack = offhanditems.get(0); }//There will be only one item here

			if (!itemStack.isEmpty()) {
				Item item = itemStack.getItem();
				if (IsTool(item)) {
					int itemDamage = itemStack.getDamage();
					int maxDamage = itemStack.getMaxDamage();

					if (maxDamage != 0 && ((1 - ((double) itemDamage / maxDamage)) <= durabilityDisplayThresholdForItem)) {
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
	private static void CalculateDurabilityIconsForArmor() {
		NonNullList<ItemStack> armorStacks = mc.player.inventory.armorInventory;

		//iterate backwards over the armor the user is wearing so the helm is displayed first
		for (int i = armorStacks.size() - 1; i >= 0; i--) {
			ItemStack armorStack = armorStacks.get(i);
			if (!armorStack.isEmpty()) {
				int itemDamage = armorStack.getDamage();
				int maxDamage = armorStack.getMaxDamage();

				if (maxDamage != 0 && (1 - (double) itemDamage / maxDamage) <= durabilityDisplayThresholdForArmor) {
					damagedItemsList.add(armorStack);
				}
			}
		}
	}

	/**
	 * Determines if the item is a tool. Pickaxe, sword, bow, shears, etc.
	 *
	 * @param item
	 * @return
	 */
	private static boolean IsTool(Item item) {
		return item instanceof ToolItem ||
		       item instanceof SwordItem ||
		       item instanceof BowItem ||
		       item instanceof HoeItem ||
		       item instanceof ShearsItem ||
		       item instanceof FishingRodItem ||
		       item instanceof ShieldItem ||
		       ModCompatibility.TConstruct.IsTConstructHarvestTool(item) ||
		       ModCompatibility.TConstruct.IsTConstructWeapon(item) ||
		       ModCompatibility.TConstruct.IsTConstructBow(item);
	}

	/**
	 * Takes off any armor the player is wearing if it is close to being destroyed,
	 * and puts it in their inventory if the player has room in their inventory.
	 */
	private static void UnequipDamagedArmor() {
		if (AutoUnequipArmor) {
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
						InventoryUtil.MoveArmorIntoPlayerInventory(i);
						ZyinHUDSound.PlayPlopSound();
						ZyinHUDRenderer.DisplayNotification(
							Localization.get("durabilityinfo.name") +
							Localization.get("durabilityinfo.unequippeditem") +
							itemStack.getDisplayName()
						);
						ZyinHUD.ZyinLogger.info(
							"Unequipped {} because it was at low durability ({}/{})",
							itemStack.getDisplayName(), itemDamage, maxDamage
						);
					}
				}
			}
		}
	}

	/**
	 * Takes off any tools the player is using if it is close to being destroyed,
	 * and puts it in their inventory if the player has room in their inventory.
	 */
	private static void UnequipDamagedTool() {
		if (AutoUnequipTools) {
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
						InventoryUtil.MoveHeldItemIntoPlayerInventory();
						ZyinHUDSound.PlayPlopSound();
						ZyinHUDRenderer.DisplayNotification(
							Localization.get("durabilityinfo.name") +
							Localization.get("durabilityinfo.unequippeditem") +
							item.getDisplayName(itemStack).toString()
						);
						ZyinHUD.ZyinLogger.info(
							"Unequipped {} because it was at low durability ({}/{})",
							item.getDisplayName(itemStack).toString(), itemDamage, maxDamage
						);
					}
				}
			}
		}
	}


	/**
	 * Checks to see if the Durability Info tab is selected in GuiZyinHUDOptions
	 * @return
	 */
//    private static boolean TabIsSelectedInOptionsGui()
//    {
//    	return mc.currentScreen instanceof GuiZyinHUDOptions &&
//    		(((GuiZyinHUDOptions)mc.currentScreen).IsButtonTabSelected(Localization.get("durabilityinfo.name")));
//    }

	/**
	 * Get durability display threshold for armor float.
	 *
	 * @return the float
	 */
	public static float GetDurabilityDisplayThresholdForArmor() {
		return durabilityDisplayThresholdForArmor;
	}

	/**
	 * Set durability display threshold for armor.
	 *
	 * @param durabilityDisplayThreshold the durability display threshold
	 */
	public static void SetDurabilityDisplayThresholdForArmor(float durabilityDisplayThreshold) {
		durabilityDisplayThresholdForArmor = durabilityDisplayThreshold;
		CalculateDurabilityIcons();
	}

	/**
	 * Get durability display threshold for item float.
	 *
	 * @return the float
	 */
	public static float GetDurabilityDisplayThresholdForItem() {
		return durabilityDisplayThresholdForItem;
	}

	/**
	 * Set durability display threshold for item.
	 *
	 * @param durabilityDisplayThreshold the durability display threshold
	 */
	public static void SetDurabilityDisplayThresholdForItem(float durabilityDisplayThreshold) {
		durabilityDisplayThresholdForItem = durabilityDisplayThreshold;
		CalculateDurabilityIcons();
	}

	/**
	 * Gets the horizontal location where the durability icons are rendered.
	 *
	 * @return int
	 */
	public static int GetHorizontalLocation() {
		return durabalityLocX;
	}

	/**
	 * Sets the horizontal location where the durability icons are rendered.
	 *
	 * @param x the x
	 * @return the new x location
	 */
	public static int SetHorizontalLocation(int x) {
		durabalityLocX = MathHelper.clamp(x, 0, mc.mainWindow.getWidth());
//		equipmentLocX = durabalityLocX + armorDurabilityIconWidth;
		return durabalityLocX;
	}

	/**
	 * Gets the vertical location where the durability icons are rendered.
	 *
	 * @return int
	 */
	public static int GetVerticalLocation() {
		return durabalityLocY;
	}

	/**
	 * Sets the vertical location where the durability icons are rendered.
	 *
	 * @param y the y
	 * @return the new y location
	 */
	public static int SetVerticalLocation(int y) {
		durabalityLocY = MathHelper.clamp(y, 0, mc.mainWindow.getHeight());
//		equipmentLocY = durabalityLocY;
		return durabalityLocY;
	}

	/**
	 * Toggles showing durability for armor
	 *
	 * @return boolean
	 */
	public static boolean ToggleShowArmorDurability() {
		return ShowArmorDurability = !ShowArmorDurability;
	}

	/**
	 * Toggles showing durability for items
	 *
	 * @return boolean
	 */
	public static boolean ToggleShowItemDurability() {
		return ShowItemDurability = !ShowItemDurability;
	}

	/**
	 * Toggles showing icons or an image for broken armor
	 *
	 * @return boolean
	 */
	public static boolean ToggleShowIndividualArmorIcons() {
		return ShowIndividualArmorIcons = !ShowIndividualArmorIcons;
	}

	/**
	 * Toggles unequipping breaking armor
	 *
	 * @return boolean
	 */
	public static boolean ToggleAutoUnequipArmor() {
		return AutoUnequipArmor = !AutoUnequipArmor;
	}

	/**
	 * Toggles unequipping breaking tools
	 *
	 * @return boolean
	 */
	public static boolean ToggleAutoUnequipTools() {
		return AutoUnequipTools = !AutoUnequipTools;
	}

	/**
	 * Toggles using color
	 *
	 * @return boolean
	 */
	public static boolean ToggleUseColoredNumbers() {
		return UseColoredNumbers = !UseColoredNumbers;
	}

	public static boolean ToggleHideDurabilityInfoInChat() {
		return HideDurabilityInfoInChat = !HideDurabilityInfoInChat;
	}

}
