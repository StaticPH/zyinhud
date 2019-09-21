package com.zyin.zyinhud.mods;

import com.zyin.zyinhud.ZyinHUD;
import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.Localization;
import com.zyin.zyinhud.util.ModCompatibility;
import net.minecraft.client.gui.GuiChat;
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
public class DurabilityInfo extends ZyinHUDModBase
{
    /**
     * Enables/Disables this Mod
     */
    public static boolean Enabled;

    /**
     * Toggles this Mod on or off
     *
     * @return The state the Mod was changed to
     */
    public static boolean ToggleEnabled()
    {
    	return Enabled = !Enabled;
    }

    /**
     * The current mode for this mod
     */
    public static TextModes TextMode;

    /**
     * The enum for the different types of Modes this mod can have
     */
    public static enum TextModes
    {
        /**
         * None text modes.
         */
        NONE(Localization.get("durabilityinfo.textmode.none")),
        /**
         * Text text modes.
         */
        TEXT(Localization.get("durabilityinfo.textmode.text")),
        /**
         * Percentage text modes.
         */
        PERCENTAGE(Localization.get("durabilityinfo.textmode.percentage"));
        
        private String friendlyName;
        
        private TextModes(String friendlyName)
        {
        	this.friendlyName = friendlyName;
        }

        /**
         * Sets the next availble mode for text display
         *
         * @return the text modes
         */
        public static TextModes ToggleMode()
        {
        	return ToggleMode(true);
        }

        /**
         * Sets the next availble mode for text display if forward=true, or previous mode if false
         *
         * @param forward the forward
         * @return the text modes
         */
        public static TextModes ToggleMode(boolean forward)
        {
        	if (forward)
        		return TextMode = TextMode.ordinal() < TextModes.values().length - 1 ? TextModes.values()[TextMode.ordinal() + 1] : TextModes.values()[0];
        	else
        		return TextMode = TextMode.ordinal() > 0 ? TextModes.values()[TextMode.ordinal() - 1] : TextModes.values()[TextModes.values().length - 1];
        }

        /**
         * Gets the mode based on its internal name as written in the enum declaration
         *
         * @param modeName the mode name
         * @return text modes
         */
        public static TextModes GetMode(String modeName)
        {
        	try {return TextModes.valueOf(modeName);}
        	catch (IllegalArgumentException e) {return values()[1];}
        }

        /**
         * Get friendly name string.
         *
         * @return the string
         */
        public String GetFriendlyName()
        {
        	return friendlyName;
        }
    }

    /**
     * The constant durabilityIconsResourceLocation.
     */
    protected static final ResourceLocation durabilityIconsResourceLocation = new ResourceLocation("zyinhud:textures/durability_icons.png");

    /**
     * The constant ShowArmorDurability.
     */
    public static boolean ShowArmorDurability;
    /**
     * The constant ShowItemDurability.
     */
    public static boolean ShowItemDurability;
    /**
     * The constant ShowIndividualArmorIcons.
     */
    public static boolean ShowIndividualArmorIcons;
    /**
     * The constant AutoUnequipArmor.
     */
//public static boolean ShowDamageAsPercentage;
    public static boolean AutoUnequipArmor;
    /**
     * The constant AutoUnequipTools.
     */
    public static boolean AutoUnequipTools;
    /**
     * The constant UseColoredNumbers.
     */
    public static boolean UseColoredNumbers;
    /**
     * The constant DurabilityScale.
     */
    public static float DurabilityScale = 1f;

    public static boolean HideDurabilityInfoInChat;

    /**
     * The constant durabilityUpdateFrequency.
     */
    public static final int durabilityUpdateFrequency = 600;

    /**
     * The constant armorDurabilityScaler.
     */
//U and V is the top left part of the image
    //X and Y is the width and height of the image
    protected static float armorDurabilityScaler = 1/5f;
    /**
     * The constant armorDurabilityIconU.
     */
    protected static int armorDurabilityIconU = 0;
    /**
     * The constant armorDurabilityIconV.
     */
    protected static int armorDurabilityIconV = 0;
    /**
     * The constant armorDurabilityIconX.
     */
    protected static int armorDurabilityIconX = (int)(5*16 * armorDurabilityScaler);
    /**
     * The constant armorDurabilityIconY.
     */
    protected static int armorDurabilityIconY = (int)(7.5*16 * armorDurabilityScaler);

    /**
     * The constant toolX.
     */
//the height/width of the tools being rendered
    public static int toolX = 1 * 16;
    /**
     * The constant toolY.
     */
    public static int toolY = 1 * 16;

    /**
     * The constant durabalityLocX.
     */
//where the armor icon is rendered (these values replaced by the config settings)
    public static int durabalityLocX = 30;
    /**
     * The constant durabalityLocY.
     */
    public static int durabalityLocY = 20;

    /**
     * The constant equipmentLocX.
     */
//where the tool icons are rendered (these values replaced by the config settings)
    protected static int equipmentLocX = 20 + armorDurabilityIconX;
    /**
     * The constant equipmentLocY.
     */
    protected static int equipmentLocY = 20;

    private static float durabilityDisplayThresholdForArmor;
    private static float durabilityDisplayThresholdForItem;

    private static ArrayList<ItemStack> damagedItemsList = new ArrayList<ItemStack>(13);	//used to push items into the list of broken equipment to render
    

    /**
     * The last time the item cache was generated
     */
    private static long lastGenerate;

    /**
     * Renders the main durability icon and any damaged tools onto the screen.
     */
    public static void RenderOntoHUD()
    {
        //if the player is in the world
        //and not in a menu (except for chat and the custom Options menu)
        //and F3 not shown
        if (DurabilityInfo.Enabled &&
                (mc.mouseHelper.isMouseGrabbed() || (mc.currentScreen != null && ((mc.currentScreen instanceof GuiChat && !HideDurabilityInfoInChat)|| TabIsSelectedInOptionsGui()))) &&
        		!mc.gameSettings.showDebugInfo)
        {
            //don't waste time recalculating things every tick
        	if(System.currentTimeMillis() - lastGenerate > durabilityUpdateFrequency)
            {
                CalculateDurabilityIcons();
            }

            boolean armorExists = false;

            for (ItemStack itemStack : damagedItemsList)
            {
                if (itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemElytra)
                    armorExists = true;
            }

            int numTools = 0;
            int numArmors = 0;

            for (ItemStack itemStack : damagedItemsList)
            {
                Item tool = itemStack.getItem();
                
                
                //if this tool is an armor
                if (tool instanceof ItemArmor || tool instanceof ItemElytra)
                {
                    if (ShowArmorDurability)
                    {
                        GL11.glScalef(DurabilityScale, DurabilityScale, DurabilityScale);
                        
                    	if(ShowIndividualArmorIcons)
                    	{
                            int x = (int) Math.floor(durabalityLocX / DurabilityScale);
                            int y = (int) Math.floor(durabalityLocY / DurabilityScale) + (numArmors * toolY);

                            
                            RenderItemIcon(itemStack, x, y);
                            
                            numArmors++;
                    	}
                    	else
                    	{
                            int x = (int) Math.floor(durabalityLocX / DurabilityScale);
                            int y = (int) Math.floor(durabalityLocY / DurabilityScale);
                            
                            DrawBrokenArmorTexture(x, y);
                    	}
                        
                        GL11.glScalef(1f/DurabilityScale, 1f/DurabilityScale, 1f/DurabilityScale);
                    }
                }
                else //if this tool is an equipment/tool
                {
                    if (ShowItemDurability)
                    {
                        int x = (int) Math.floor(durabalityLocX / DurabilityScale);
                        int y = (int) Math.floor(durabalityLocY / DurabilityScale) + (numTools * toolY);

                        if (armorExists && ShowArmorDurability)
                            //x = (int) Math.floor(equipmentLocX / DurabilityScale);    //if armor is being rendered then push this to the right
                        	x += toolX;
                        
                        //x /= DurabilityScale;
                        //y /= DurabilityScale;
                        GL11.glScalef(DurabilityScale, DurabilityScale, DurabilityScale);
                        
                        RenderItemIcon(itemStack, x, y);
                        
                        GL11.glScalef(1f/DurabilityScale, 1f/DurabilityScale, 1f/DurabilityScale);
                        
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
        GL11.glEnable(GL11.GL_DEPTH_TEST);	//so the enchanted item effect is rendered properly
		
		//render the item with enchant effect
		itemRenderer.renderItemAndEffectIntoGUI(itemStack, x, y);

		//render the item's durability bar
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x, y, null);
		
		GL11.glDisable(GL11.GL_LIGHTING);	//the itemRenderer.renderItem() method enables lighting
		
		if(TextMode == TextModes.NONE)
		{
			return;
		}
		else
		{
			//render the number of durability it has left
			if(itemStack.getDamage() != 0)
			{
//				boolean unicodeFlag = mc.fontRenderer.getUnicodeFlag();
//				mc.fontRenderer.setUnicodeFlag(true);
				
				String damageStringText;
				int itemDamage = itemStack.getDamage();
				int itemMaxDamage = itemStack.getMaxDamage();
				
				if(TextMode == TextModes.PERCENTAGE)
					damageStringText = 100 - (int)((double)itemDamage / itemMaxDamage * 100) + "%";
				else if(TextMode == TextModes.TEXT)
				{
					if(ModCompatibility.TConstruct.IsTConstructItem(itemStack.getItem()))
					{
						Integer temp = ModCompatibility.TConstruct.GetDamage(itemStack);
						if(temp != null)
						{
							itemDamage = temp;
							itemMaxDamage = ModCompatibility.TConstruct.GetMaxDamage(itemStack);
							damageStringText = Integer.toString(itemMaxDamage - itemDamage);
						}
						else
							damageStringText = "";
					}
					else
						damageStringText = Integer.toString(itemMaxDamage - itemDamage);
					
				}
				else
				{
					damageStringText = "";
				}
				
				int damageStringX = x + toolX - mc.fontRenderer.getStringWidth(damageStringText);
				int damageStringY = y + toolY - mc.fontRenderer.FONT_HEIGHT - 2;
				int damageStringColor = 0xffffff;
				
				if(UseColoredNumbers)
					damageStringColor = GetDamageColor(itemStack.getDamage(), itemStack.getMaxDamage());

				GL11.glDisable(GL11.GL_DEPTH_TEST);	//so the text renders above the item
				mc.fontRenderer.drawStringWithShadow(damageStringText, damageStringX, damageStringY, damageStringColor);
//				mc.fontRenderer.setUnicodeFlag(unicodeFlag);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
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
        float percent = 100 - (int)((double)currentDamage / maxDamage * 100);
		
		if(percent < 50)
			return (int)(0xff0000 + ((int)(0xff * percent/50) << 8));
		else
			return (int)(0x00ff00 + ((int)(0xff * (100 - (percent-50)*2)/100) << 16));
	}


    /**
     * Draws the broken durability image
     *
     * @param x the x
     * @param y the y
     */
    protected static void DrawBrokenArmorTexture(int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);	//for a transparent texture
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glColor4f(255f, 255f, 255f, 255f);	//fixes transparency issue when a InfoLine Notification is displayed
		
		ZyinHUDRenderer.RenderCustomTexture(x, y, 
				armorDurabilityIconU, armorDurabilityIconV, 
				(int)(armorDurabilityIconX/armorDurabilityScaler), (int)(armorDurabilityIconY/armorDurabilityScaler), 
				durabilityIconsResourceLocation, armorDurabilityScaler);
		
		//GL11.glDisable(GL11.GL_BLEND);	//this turned the screen dark in the options menu
	}

    /**
     * Finds items in the players hot bar and equipped armor that is damaged and adds them to the damagedItemsList list.
     */
    protected static void CalculateDurabilityIcons()
    {
        //if the player is in the world
        //and not in a menu (except for chat and the custom Options menu)
        //and not typing
        if (mc.mouseHelper.isMouseGrabbed() ||
        		(mc.currentScreen != null && (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiZyinHUDOptions && ((GuiZyinHUDOptions)mc.currentScreen).IsButtonTabSelected(Localization.get("durabilityinfo.name")))) &&
        		!mc.gameSettings.keyBindPlayerList.isPressed())
        {
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
    private static void CalculateDurabilityIconsForTools()
    {
        NonNullList<ItemStack> items = mc.player.inventory.mainInventory;
        NonNullList<ItemStack> offhanditems = mc.player.inventory.offHandInventory;

        for (int i = 0; i < 10; i++)
        {
            ItemStack itemStack;
            if (i < 9)
            {
                itemStack = items.get(i);
            }else{
                itemStack = offhanditems.get(0);//There will be only one item here
            }


            if (!itemStack.isEmpty())
            {
                Item item = itemStack.getItem();
                if (IsTool(item))
                {
                    int itemDamage = itemStack.getDamage();
                    int maxDamage = itemStack.getMaxDamage();
                    
                    if (maxDamage != 0 &&
                    		(1-(double)itemDamage / maxDamage) <= durabilityDisplayThresholdForItem)
                    {
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
    private static void CalculateDurabilityIconsForArmor()
    {
        NonNullList<ItemStack> armorStacks = mc.player.inventory.armorInventory;
        
        //iterate backwards over the armor the user is wearing so the helm is displayed first
        for(int i = armorStacks.size()-1; i >= 0 ; i--)
        {
        	ItemStack armorStack = armorStacks.get(i);
            if (!armorStack.isEmpty())
            {
                int itemDamage = armorStack.getDamage();
                int maxDamage = armorStack.getMaxDamage();

                if (maxDamage != 0 &&
                        (1-(double)itemDamage / maxDamage) <= durabilityDisplayThresholdForArmor)
                {
                    damagedItemsList.add(armorStack);
                }
            }
        }
    }
    
    /**
     * Determines if the item is a tool. Pickaxe, sword, bow, shears, etc.
     * @param item
     * @return
     */
    private static boolean IsTool(Item item)
    {
    	return item instanceof ItemTool
	    	|| item instanceof ItemSword
	    	|| item instanceof ItemBow
	    	|| item instanceof ItemHoe
	        || item instanceof ItemShears
	        || item instanceof ItemFishingRod
                || item instanceof ItemShield
	        || ModCompatibility.TConstruct.IsTConstructHarvestTool(item)
	        || ModCompatibility.TConstruct.IsTConstructWeapon(item)
	        || ModCompatibility.TConstruct.IsTConstructBow(item);
    }
    
    /**
     * Takes off any armor the player is wearing if it is close to being destroyed,
     * and puts it in their inventory if the player has room in their inventory.
     */
    private static void UnequipDamagedArmor()
    {
    	if(AutoUnequipArmor)
    	{
            NonNullList<ItemStack> itemStacks = mc.player.inventory.armorInventory;
            
            //iterate over the armor the user is wearing
            for(int i = 0; i < itemStacks.size(); i++)
            {
            	ItemStack itemStack = itemStacks.get(i);
                if (!itemStack.isEmpty() && !(itemStack.getItem() instanceof ItemElytra) &&
                        !(itemStack.isEnchanted() && EnchantmentHelper.hasBindingCurse(itemStack)))
                {
                    int itemDamage = itemStack.getDamage();
                    int maxDamage = itemStack.getMaxDamage();
                    
                    if (maxDamage != 0 &&
                    		maxDamage - itemDamage < 5)
                    {
                       InventoryUtil.MoveArmorIntoPlayerInventory(i);
	                   	ZyinHUDSound.PlayPlopSound();
	                   	ZyinHUDRenderer.DisplayNotification(Localization.get("durabilityinfo.name") + Localization.get("durabilityinfo.unequippeditem") + itemStack.getDisplayName());
	                   	ZyinHUD.log("Unequipped " + itemStack.getDisplayName() + " because it was at low durability (" + itemDamage + "/" + maxDamage + ")");
                    }
                }
            }
    	}
    }

    /**
     * Takes off any tools the player is using if it is close to being destroyed,
     * and puts it in their inventory if the player has room in their inventory.
     */
    private static void UnequipDamagedTool()
    {
    	if(AutoUnequipTools)
    	{
            ItemStack itemStack = mc.player.inventory.getCurrentItem();

            if (!itemStack.isEmpty())
            {
                Item item = itemStack.getItem();

                if (item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemBow || item instanceof ItemHoe
                        || item instanceof ItemShears || item instanceof ItemFishingRod)
                {
                    int itemDamage = itemStack.getDamage();
                    int maxDamage = itemStack.getMaxDamage();
                    int threshold = (item instanceof ItemFishingRod) ? 5 : 15;
                    
                    if (maxDamage != 0
                    	&& maxDamage - itemDamage < threshold				//less than 15 durability
                    	&& (float)itemDamage / (float)maxDamage > 0.9)		//less than 10%
                    {
                    	InventoryUtil.MoveHeldItemIntoPlayerInventory();
                    	ZyinHUDSound.PlayPlopSound();
                    	ZyinHUDRenderer.DisplayNotification(Localization.get("durabilityinfo.name") + Localization.get("durabilityinfo.unequippeditem") + item.getDisplayName(itemStack).toString());
                    	ZyinHUD.log("Unequipped " + item.getDisplayName(itemStack).toString() + " because it was at low durability (" + itemDamage + "/" + maxDamage + ")");
                    }
                }
            }
    	}
    }
    

    /**
     * Checks to see if the Durability Info tab is selected in GuiZyinHUDOptions
     * @return
     */
    private static boolean TabIsSelectedInOptionsGui()
    {
    	return mc.currentScreen instanceof GuiZyinHUDOptions &&
    		(((GuiZyinHUDOptions)mc.currentScreen).IsButtonTabSelected(Localization.get("durabilityinfo.name")));
    }

    /**
     * Get durability display threshold for armor float.
     *
     * @return the float
     */
    public static float GetDurabilityDisplayThresholdForArmor()
    {
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
    public static float GetDurabilityDisplayThresholdForItem()
    {
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
    public static int GetHorizontalLocation()
    {
    	return durabalityLocX;
    }

    /**
     * Sets the horizontal location where the durability icons are rendered.
     *
     * @param x the x
     * @return the new x location
     */
    public static int SetHorizontalLocation(int x)
    {
    	durabalityLocX = MathHelper.clamp(x, 0, mc.mainWindow.getWidth());
    	equipmentLocX = durabalityLocX + armorDurabilityIconX;
    	return durabalityLocX;
    }

    /**
     * Gets the vertical location where the durability icons are rendered.
     *
     * @return int
     */
    public static int GetVerticalLocation()
    {
    	return durabalityLocY;
    }

    /**
     * Sets the vertical location where the durability icons are rendered.
     *
     * @param y the y
     * @return the new y location
     */
    public static int SetVerticalLocation(int y)
    {
    	durabalityLocY = MathHelper.clamp(y, 0, mc.mainWindow.getHeight());
    	equipmentLocY = durabalityLocY;
    	return durabalityLocY;
    }

    /**
     * Toggles showing durability for armor
     *
     * @return boolean
     */
    public static boolean ToggleShowArmorDurability()
    {
    	return ShowArmorDurability = !ShowArmorDurability;
    }

    /**
     * Toggles showing durability for items
     *
     * @return boolean
     */
    public static boolean ToggleShowItemDurability()
    {
    	return ShowItemDurability = !ShowItemDurability;
    }

    /**
     * Toggles showing icons or an image for broken armor
     *
     * @return boolean
     */
    public static boolean ToggleShowIndividualArmorIcons()
    {
    	return ShowIndividualArmorIcons = !ShowIndividualArmorIcons;
    }

    /**
     * Toggles unequipping breaking armor
     *
     * @return boolean
     */
    public static boolean ToggleAutoUnequipArmor()
    {
    	return AutoUnequipArmor = !AutoUnequipArmor;
    }

    /**
     * Toggles unequipping breaking tools
     *
     * @return boolean
     */
    public static boolean ToggleAutoUnequipTools()
    {
    	return AutoUnequipTools = !AutoUnequipTools;
    }

    /**
     * Toggles using color
     *
     * @return boolean
     */
    public static boolean ToggleUseColoredNumbers()
    {
    	return UseColoredNumbers = !UseColoredNumbers;
    }

    public static boolean ToggleHideDurabilityInfoInChat(){
        return HideDurabilityInfoInChat = !HideDurabilityInfoInChat;
    }

}
