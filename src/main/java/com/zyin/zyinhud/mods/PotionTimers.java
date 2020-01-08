//package com.zyin.zyinhud.mods;
//
//import com.zyin.zyinhud.ZyinHUDRenderer;
////import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
//import com.zyin.zyinhud.util.Localization;
//import net.minecraft.client.gui.DisplayEffectsScreen;
//import net.minecraft.client.gui.screen.ChatScreen;
//import net.minecraft.client.gui.screen.inventory.ContainerScreen;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.potion.Effect;
//import net.minecraft.potion.EffectInstance;
//import net.minecraft.potion.EffectUtils;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.MathHelper;
//import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
//import org.lwjgl.opengl.GL11;
//
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// * Potion Timers displays the remaining time left on any potion effects the user has.
// */
//@SuppressWarnings("RedundantCast")
//public class PotionTimers extends ZyinHUDModBase {
//    /**
//     * Enables/Disables this Mod
//     */
//    public static boolean Enabled;
//
//    /**
//     * Toggles this Mod on or off
//     *
//     * @return The state the Mod was changed to
//     */
//    public static boolean ToggleEnabled() {
//        return Enabled = !Enabled;
//    }
//
//    /**
//     * The current mode for this mod
//     */
//    public static TextModes TextMode;
//
//    /**
//     * The enum for the different types of Modes this mod can have
//     */
//    public static enum TextModes {
//        /**
//         * White text modes.
//         */
//        WHITE("potiontimers.textmode.whiteLocalization.get("),)
//        /**
//         * Colored text modes.
//         */
//        COLORED("potiontimers.textmode.coloredLocalization.get("),)
//        /**
//         * None text modes.
//         */
//        NONE("potiontimers.textmode.none");
//
//        private String unfriendlyName;
//
//        private TextModes(String unfriendlyName) {
//            this.unfriendlyName = unfriendlyName;
//        }
//
//        /**
//         * Sets the next availble mode for this mod
//         *
//         * @return the text modes
//         */
//        public static TextModes ToggleMode() {
//            return ToggleMode(true);
//        }
//
//        /**
//         * Sets the next availble mode for this mod if forward=true, or previous mode if false
//         *
//         * @param forward the forward
//         * @return the text modes
//         */
//        public static TextModes ToggleMode(boolean forward) {
//            if (forward)
//                return TextMode = TextMode.ordinal() < TextModes.values().length - 1 ? TextModes.values()[TextMode.ordinal() + 1] : TextModes.values()[0];
//            else
//                return TextMode = TextMode.ordinal() > 0 ? TextModes.values()[TextMode.ordinal() - 1] : TextModes.values()[TextModes.values().length - 1];
//        }
//
//        /**
//         * Gets the mode based on its internal name as written in the enum declaration
//         *
//         * @param modeName the mode name
//         * @return text modes
//         */
//        public static TextModes GetMode(String modeName) {
//            try {
//                return TextModes.valueOf(modeName);
//            } catch (IllegalArgumentException e) {
//                return values()[1];
//            }
//        }
//
//        /**
//         * Get friendly name string.
//         *
//         * @return the string
//         */
//        public String GetFriendlyName() {
//            return Localization.get(unfriendlyName);
//        }
//    }
//
//    private static ResourceLocation inventoryResourceLocation = new ResourceLocation("textures/gui/container/inventory.png");
//
//    /**
//     * The constant ShowPotionIcons.
//     */
//    public static boolean ShowPotionIcons;
//    /**
//     * The constant ShowEffectName.
//     */
//    public static boolean ShowEffectName;
//    /**
//     * The constant ShowEffectLevel.
//     */
//    public static boolean ShowEffectLevel;
//    /**
//     * The constant UsePotionColors.
//     */
//    public static boolean UsePotionColors;
//    /**
//     * The constant PotionScale.
//     */
//    public static float PotionScale;
//    /**
//     * The constant HidePotionEffectsInInventory.
//     */
//    public static boolean HidePotionEffectsInInventory;
//    /**
//     * The constant HideBeaconPotionEffects.
//     */
//    public static boolean HideBeaconPotionEffects;
//    /**
//     * The constant ShowVanillaStatusEffectHUD.
//     */
//    public static boolean ShowVanillaStatusEffectHUD;
//
//    /**
//     * The constant blinkingThresholds.
//     */
//    protected static final int[] blinkingThresholds = {3 * 20, 5 * 20, 16 * 20};    //the time at which blinking starts
//    /**
//     * The constant blinkingSpeed.
//     */
//    protected static final int[] blinkingSpeed = {5, 10, 20};                    //how often the blinking occurs
//    /**
//     * The constant blinkingDuration.
//     */
//    protected static final int[] blinkingDuration = {2, 3, 3};                    //how long the blink lasts
//
//    /**
//     * The constant potionLocX.
//     */
//    protected static int potionLocX = 1;
//    /**
//     * The constant potionLocY.
//     */
//    protected static int potionLocY = 16;
//
//    /**
//     * Renders the duration any potion effects that the player currently has on the left side of the screen.
//     */
//    public static void RenderOntoHUD() {
//        //if the player is in the world
//        //and not in a menu (except for chat and the custom Options menu)
//        //and F3 not shown
//        if (PotionTimers.Enabled &&
//            (mc.mouseHelper.isMouseGrabbed() || (mc.currentScreen != null && (mc.currentScreen instanceof ChatScreen /*|| TabIsSelectedInOptionsGui()*/))) &&
//            !mc.gameSettings.showDebugInfo) {
//            Collection potionEffects = mc.player.getActivePotionEffects();    //key:potionId, value:potionEffect
//            Iterator it = potionEffects.iterator();
//
//            int x = potionLocX;
//            int y = potionLocY;
//
//            x /= PotionScale;
//            y /= PotionScale;
//            GL11.glScalef(PotionScale, PotionScale, PotionScale);
//
//
//            int i = 0;
//            while (it.hasNext()) {
//                EffectInstance potionEffect = (EffectInstance) it.next();
//                //Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
//                Effect potion = potionEffect.getPotion();
//                boolean isFromBeacon = potionEffect.isAmbient();
//
//                if (!isFromBeacon || !HideBeaconPotionEffects) {
//                    if (ShowPotionIcons) {
//                        DrawPotionIcon(x, y, potion);
//
//                        if (TextMode != TextModes.NONE)
//                            DrawPotionText(x + 10, y, potion, potionEffect);
//                    } else {
//                        if (TextMode != TextModes.NONE)
//                            DrawPotionText(x, y, potion, potionEffect);
//                    }
//
//                    y += 10;
//                    i++;
//                }
//            }
//
//            GL11.glScalef(1f / PotionScale, 1f / PotionScale, 1f / PotionScale);
//        }
//    }
//
//
//    /**
//     * Draws a potion's remaining duration and name with a color coded blinking timer
//     *
//     * @param x            the x
//     * @param y            the y
//     * @param potion       the potion
//     * @param potionEffect the potion effect
//     */
//    protected static void DrawPotionText(int x, int y, Effect potion, EffectInstance potionEffect) {
//        String potionText = EffectUtils.getPotionDurationString(potionEffect, 1.0F);//TODO: Figure out that float number, this is a temporary fix
//        if (ShowEffectName) {
//            potionText += " " + I18n.format(potionEffect.getEffectName());
//        }
//        if (ShowEffectLevel) {
//            potionText += " " + (potionEffect.getAmplifier() + 1);
//        }
//        int potionDuration = potionEffect.getDuration();    //goes down by 20 ticks per second
//        int colorInt;
//        if (TextMode == TextModes.COLORED)
//            colorInt = potion.getLiquidColor();
//        else if (TextMode == TextModes.WHITE)
//            colorInt = 0xFFFFFF;
//        else
//            colorInt = 0xFFFFFF;
//
//
////        boolean unicodeFlag = mc.fontRenderer.getUnicodeFlag();
////        mc.fontRenderer.setUnicodeFlag(true);
//
//        //render the potion duration text onto the screen
//        if (potionDuration >= blinkingThresholds[blinkingThresholds.length - 1])    //if the text is not blinking then render it normally
//        {
//            mc.fontRenderer.drawStringWithShadow(potionText, x, y, colorInt);
//        } else //else if the text is blinking, have a chance to not render it based on the blinking variables
//        {
//            //logic to determine if the text should be displayed, checks the blinking text settings
//            for (int j = 0; j < blinkingThresholds.length; j++) {
//                if (potionDuration < blinkingThresholds[j]) {
//                    if (potionDuration % blinkingSpeed[j] > blinkingDuration[j]) {
//                        mc.fontRenderer.drawStringWithShadow(potionText, x, y, colorInt);
//                    }
//
//                    break;
//                }
//            }
//        }
//
////        mc.fontRenderer.setUnicodeFlag(unicodeFlag);
//    }
//
//    /**
//     * Draws a potion's icon texture
//     *
//     * @param x      the x
//     * @param y      the y
//     * @param potion the potion
//     */
//    protected static void DrawPotionIcon(int x, int y, Effect potion) {
//        mc.getTextureManager().bindTexture(inventoryResourceLocation);
//
//        TextureAtlasSprite sprite = mc.getPotionSpriteUploader().getSprite(potion);
//        if (sprite != null)    //some modded potions use a custom Resource Location for potion drawing, typically done in the .hasStatusIcon() method
//        {
////            int iconIndex = sprite.;
//            int id = Effect.getId(potion);
////            int iconIndex = potion.getStatusIconIndex();
//            int u = iconIndex % 8 * 18;
//            int v = 198 + iconIndex / 8 * 18;
//            int width = 18;
//            int height = 18;
//            float scaler = 0.5f;
//
//            GL11.glColor4f(1f, 1f, 1f, 1f);
//
//            ZyinHUDRenderer.RenderCustomTexture(x, y, u, v, width, height, null, scaler);
//        }
//    }
//
//    /**
//     * Disables the potion effects from rendering by telling the Gui that the player has no potion effects applied.
//     * Uses reflection to grab the class's private variable.
//     *
//     * @param guiScreen the screen the player is looking at which extends InventoryEffectRenderer
//     */
//    public static void DisableInventoryPotionEffects(DisplayEffectsScreen guiScreen) {
//        if (PotionTimers.Enabled && HidePotionEffectsInInventory) {
//            //Note for future Forge versions: field "field_147045_u" will probably be renamed to something like "playerHasPotionEffects"
//            // mapped field: hasActivePotionEffects
//            boolean playerHasPotionEffects = ObfuscationReflectionHelper.getPrivateValue(DisplayEffectsScreen.class, (DisplayEffectsScreen) guiScreen, "field_147045_u");
//
//            if (playerHasPotionEffects) {
//                int guiLeftPx = (guiScreen.width - 176) / 2;
//
//                // mapped field: guiLeft
//                ObfuscationReflectionHelper.setPrivateValue(ContainerScreen.class, (ContainerScreen) guiScreen, guiLeftPx, "field_147003_i");
//                ObfuscationReflectionHelper.setPrivateValue(DisplayEffectsScreen.class, (DisplayEffectsScreen) guiScreen, false, "field_147045_u");
//            }
//        }
//    }
//
//
//    /**
//     * Checks to see if the Potion Timers tab is selected in GuiZyinHUDOptions
//     *
//     * @return
//     */
////    private static boolean TabIsSelectedInOptionsGui() {
////        return mc.currentScreen instanceof GuiZyinHUDOptions &&
////                (((GuiZyinHUDOptions) mc.currentScreen).IsButtonTabSelected(Localization.get("potiontimers.name")));
////    }
//
//
//    /**
//     * Toggles showing potion icons
//     *
//     * @return boolean
//     */
//    public static boolean ToggleShowPotionIcons() {
//        return ShowPotionIcons = !ShowPotionIcons;
//    }
//
//    /**
//     * Toggles showing effect name
//     *
//     * @return boolean
//     */
//    public static boolean ToggleShowEffectName() {
//        return ShowEffectName = !ShowEffectName;
//    }
//
//    /**
//     * Toggles showing effect level
//     *
//     * @return boolean
//     */
//    public static boolean ToggleShowEffectLevel() {
//        return ShowEffectLevel = !ShowEffectLevel;
//    }
//
//    /**
//     * Toggles hiding potion effects in the players inventory
//     *
//     * @return boolean
//     */
//    public static boolean ToggleHidePotionEffectsInInventory() {
//        return HidePotionEffectsInInventory = !HidePotionEffectsInInventory;
//    }
//
//    /**
//     * Gets the horizontal location where the potion timers are rendered.
//     *
//     * @return int
//     */
//    public static int GetHorizontalLocation() {
//        return potionLocX;
//    }
//
//    /**
//     * Sets the horizontal location where the potion timers are rendered.
//     *
//     * @param x the x
//     * @return the new x location
//     */
//    public static int SetHorizontalLocation(int x) {
//        potionLocX = MathHelper.clamp(x, 0, mc.mainWindow.getWidth());
//        return potionLocX;
//    }
//
//    /**
//     * Gets the vertical location where the potion timers are rendered.
//     *
//     * @return int
//     */
//    public static int GetVerticalLocation() {
//        return potionLocY;
//    }
//
//    /**
//     * Sets the vertical location where the potion timers are rendered.
//     *
//     * @param y the y
//     * @return the new y location
//     */
//    public static int SetVerticalLocation(int y) {
//        potionLocY = MathHelper.clamp(y, 0, mc.mainWindow.getHeight());
//        return potionLocY;
//    }
//
//    public static boolean ToggleHideBeaconPotionEffects() {
//        return HideBeaconPotionEffects = !HideBeaconPotionEffects;
//    }
//
//    public static boolean ToggleShowVanillaStatusEffectHUD() {
//        return ShowVanillaStatusEffectHUD = !ShowVanillaStatusEffectHUD;
//    }
//
//}
