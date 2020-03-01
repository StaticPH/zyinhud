//package com.zyin.zyinhud.modules;
//
//import com.zyin.zyinhud.config.ZyinHUDConfig;
//import com.zyin.zyinhud.ZyinHUDRenderer;
////import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
//import com.zyin.zyinhud.modules.ZyinHUDModuleModes.PotionTimerOptions;
//import net.minecraft.client.gui.DisplayEffectsScreen;
//import net.minecraft.client.gui.screen.inventory.ContainerScreen;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.potion.Effect;
//import net.minecraft.potion.EffectInstance;
//import net.minecraft.potion.EffectUtils;
//import net.minecraft.util.math.MathHelper;
//import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
//import org.lwjgl.opengl.GL11;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//import static com.zyin.zyinhud.util.ZyinHUDUtil.doesScreenShowHUD;
//import static net.minecraft.client.gui.screen.inventory.ContainerScreen.INVENTORY_BACKGROUND;
//
///**
// * Potion Timers displays the remaining time left on any potion effects the user has.
// */
//@SuppressWarnings("RedundantCast")
//public class PotionTimers extends ZyinHUDModuleBase {
//	/**
//	 * Enables/Disables this module
//	 */
//	public static boolean isEnabled = ZyinHUDConfig.enablePotionTimers.get();
//
//	/**
//	 * Toggles this module on or off
//	 *
//	 * @return The state the module was changed to
//	 */
//	public static boolean toggleEnabled() {
//		ZyinHUDConfig.enablePotionTimers.set(!isEnabled);
//		ZyinHUDConfig.enablePotionTimers.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
//		return isEnabled = !isEnabled;
//	}
//
//	/**
//	 * The current mode for this module
//	 */
//	public static PotionTimerOptions.PotionTimerModes mode = ZyinHUDConfig.potionTimerMode.get();
//
//	static boolean showPotionIcons = ZyinHUDConfig.showPotionIcons.get();
////	private static boolean showEffectName;
////	private static boolean showEffectLevel;
////	private static boolean UsePotionColors;     UNUSED
//	static float potionScale = ZyinHUDConfig.potionScale.get().floatValue();
//	static boolean hidePotionEffectsInInventory = ZyinHUDConfig.hidePotionEffectsInInventory.get();
////	private static boolean hideBeaconPotionEffects;
////	private static boolean showVanillaStatusEffectHUD;
//
//	protected static final int[] blinkingThresholds = {3 * 20, 5 * 20, 16 * 20};    //the time at which blinking starts
//	protected static final int[] blinkingSpeed = {5, 10, 20};                    //how often the blinking occurs
//	protected static final int[] blinkingDuration = {2, 3, 3};                    //how long the blink lasts
//
//	protected static int potionLocX = PotionTimerOptions.defaultPotionTimersHorizontalPos;
//	protected static int potionLocY = PotionTimerOptions.defaultPotionTimersVerticalPos;
//
//	/**
//	 * Renders the duration any potion effects that the player currently has on the left side of the screen.
//	 */
//	public static void renderOntoHUD() {
//		//if the player is in the world
//		//and not in a menu (except for chat and the custom Options menu)
//		//and F3 not shown
//		if (PotionTimers.isEnabled &&
//		    (mc.mouseHelper.isMouseGrabbed() || (doesScreenShowHUD(mc.currentScreen) /*|| tabIsSelectedInOptionsGui()*/)) &&
//		    !mc.gameSettings.showDebugInfo
//		) {
//			Collection potionEffects = mc.player.getActivePotionEffects();    //key:potionId, value:potionEffect
//			Iterator it = potionEffects.iterator();
//
//			int x = potionLocX;
//			int y = potionLocY;
//
//			x /= potionScale;
//			y /= potionScale;
//			GL11.glScalef(potionScale, potionScale, potionScale);
//
//
//			int i = 0;
//			while (it.hasNext()) {
//				EffectInstance potionEffect = (EffectInstance) it.next();
//				//Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
//				Effect potion = potionEffect.getPotion();
//				boolean isFromBeacon = potionEffect.isAmbient();
//
//				if (!isFromBeacon) {// || !hideBeaconPotionEffects) {
//					if (showPotionIcons) {
//						drawPotionIcon(x, y, potion);
//
//						if (mode != PotionTimerOptions.PotionTimerModes.NONE) {
//							drawPotionText(x + 10, y, potion, potionEffect);
//						}
//					}
//					else if (mode != PotionTimerOptions.PotionTimerModes.NONE) {
//						drawPotionText(x, y, potion, potionEffect);
//					}
//
//					y += 10;
//					i++;
//				}
//			}
//
//			GL11.glScalef(1f / potionScale, 1f / potionScale, 1f / potionScale);
//		}
//	}
//
//
//	/**
//	 * Draws a potion's remaining duration and name with a color coded blinking timer
//	 *
//	 * @param x            the x
//	 * @param y            the y
//	 * @param potion       the potion
//	 * @param potionEffect the potion effect
//	 */
//	protected static void drawPotionText(int x, int y, Effect potion, EffectInstance potionEffect) {
//		//TODO: Figure out that float number, this is a temporary fix
//		String potionText = EffectUtils.getPotionDurationString(potionEffect, 1.0F);
//
////		UNUSED
////		if (showEffectName) {
////			potionText += " " + I18n.format(potionEffect.getEffectName());
////		}
////		if (showEffectLevel) {
////			potionText += " " + (potionEffect.getAmplifier() + 1);
////		}
//
//		int potionDuration = potionEffect.getDuration();    //goes down by 20 ticks per second
//		int colorInt;
//		if (mode == PotionTimerOptions.PotionTimerModes.COLORED) {
//			colorInt = potion.getLiquidColor();
//		}
//		else if (mode == PotionTimerOptions.PotionTimerModes.WHITE) { colorInt = 0xFFFFFF; }
//		else { colorInt = 0xFFFFFF; }
//
//
////        boolean unicodeFlag = mc.fontRenderer.getUnicodeFlag();
////        mc.fontRenderer.setUnicodeFlag(true);
//
//		//render the potion duration text onto the screen
//		if (potionDuration >= blinkingThresholds[blinkingThresholds.length - 1])    //if the text is not blinking then render it normally
//		{
//			mc.fontRenderer.drawStringWithShadow(potionText, x, y, colorInt);
//		}
//		else //else if the text is blinking, have a chance to not render it based on the blinking variables
//		{
//			//logic to determine if the text should be displayed, checks the blinking text settings
//			for (int j = 0; j < blinkingThresholds.length; j++) {
//				if (potionDuration < blinkingThresholds[j]) {
//					if (potionDuration % blinkingSpeed[j] > blinkingDuration[j]) {
//						mc.fontRenderer.drawStringWithShadow(potionText, x, y, colorInt);
//					}
//
//					break;
//				}
//			}
//		}
//
////        mc.fontRenderer.setUnicodeFlag(unicodeFlag);
//	}
//
//	/**
//	 * Draws a potion's icon texture
//	 *
//	 * @param x      the x
//	 * @param y      the y
//	 * @param potion the potion
//	 */
//	protected static void drawPotionIcon(int x, int y, Effect potion) {
//		mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
//		// see net/minecraft/client/gui/DisplayEffectsScreen.java
//		//      func_214079_a       renders the box around the potion effect displayed in the player inventory
//		//      func_214077_b       renders the icon??
//		//      func_214078_c       render the text ???and the icon in the inventory???
//		//  in 1.13, DisplayEffectsScreen was more-or-less covered by net/minecraft/client/renderer/InventoryEffectRenderer
//		// Honestly, it might be easier to just hijack IngameGui:renderPotionEffects
//		//  potion icon textures found in assets/textures/mob_effects/      based on constructor in PotionSpriteUploader
//		TextureAtlasSprite sprite = mc.getPotionSpriteUploader().getSprite(potion);
//		//some modded potions use a custom Resource Location for potion drawing, typically done in the .hasStatusIcon() method
//		if (sprite != null) {
////            int iconIndex = sprite.;
//			int id = Effect.getId(potion);
////            int iconIndex = potion.getStatusIconIndex();
//			int u = iconIndex % 8 * 18;
//			int v = 198 + iconIndex / 8 * 18;
//			int width = 18;
//			int height = 18;
//			float scaler = 0.5f;
//
//			GL11.glColor4f(1f, 1f, 1f, 1f);
//
//			ZyinHUDRenderer.renderCustomTexture(x, y, u, v, width, height, null, scaler);
//		}
//	}
//
//	/**
//	 * Disables the potion effects from rendering by telling the Gui that the player has no potion effects applied.
//	 * Uses reflection to grab the class's private variable.
//	 *
//	 * @param guiScreen the screen the player is looking at which extends InventoryEffectRenderer
//	 */
//	public static void disableInventoryPotionEffects(DisplayEffectsScreen guiScreen) {
//		if (PotionTimers.isEnabled && hidePotionEffectsInInventory) {
//			if (!mc.player.getActivePotionEffects().isEmpty()) {
//				int guiLeftPx = (guiScreen.width - 176) / 2;
//
//				// mapped field: guiLeft
//				ObfuscationReflectionHelper.setPrivateValue(
//					ContainerScreen.class, (ContainerScreen) guiScreen, guiLeftPx, "field_147003_i"
//				);
//				ObfuscationReflectionHelper.setPrivateValue(
//					DisplayEffectsScreen.class, (DisplayEffectsScreen) guiScreen, false, "field_147045_u"
//				);
//			}
//		}
//	}
//	/**
//	 * Checks to see if the Potion Timers tab is selected in GuiZyinHUDOptions
//	 *
//	 * @return
//	 */
////    private static boolean tabIsSelectedInOptionsGui() {
////        return mc.currentScreen instanceof GuiZyinHUDOptions &&
////                (((GuiZyinHUDOptions) mc.currentScreen).isButtonTabSelected(Localization.get("potiontimers.name")));
////    }
//
//
//	/**
//	 * Toggles showing potion icons
//	 *
//	 * @return boolean
//	 */
//	public static boolean toggleShowPotionIcons() {
//		return showPotionIcons = !showPotionIcons;
//	}
//
////	UNUSED
////	/**
////	 * Toggles showing effect name
////	 *
////	 * @return boolean
////	 */
////	public static boolean toggleShowEffectName() {
////		return showEffectName = !showEffectName;
////	}
////
////	/**
////	 * Toggles showing effect level
////	 *
////	 * @return boolean
////	 */
////	public static boolean toggleShowEffectLevel() {
////		return showEffectLevel = !showEffectLevel;
////	}
//
//	/**
//	 * Toggles hiding potion effects in the players inventory
//	 *
//	 * @return boolean
//	 */
//	public static boolean toggleHidePotionEffectsInInventory() {
//		return hidePotionEffectsInInventory = !hidePotionEffectsInInventory;
//	}
//
//	/**
//	 * Gets the horizontal location where the potion timers are rendered.
//	 *
//	 * @return int
//	 */
//	public static int getHorizontalLocation() {
//		return potionLocX;
//	}
//
//	/**
//	 * Sets the horizontal location where the potion timers are rendered.
//	 *
//	 * @param x the x
//	 * @return the new x location
//	 */
//	public static int setHorizontalLocation(int x) {
//		potionLocX = MathHelper.clamp(x, 0, mc.mainWindow.getWidth());
//		return potionLocX;
//	}
//
//	/**
//	 * Gets the vertical location where the potion timers are rendered.
//	 *
//	 * @return int
//	 */
//	public static int getVerticalLocation() {
//		return potionLocY;
//	}
//
//	/**
//	 * Sets the vertical location where the potion timers are rendered.
//	 *
//	 * @param y the y
//	 * @return the new y location
//	 */
//	public static int setVerticalLocation(int y) {
//		potionLocY = MathHelper.clamp(y, 0, mc.mainWindow.getHeight());
//		return potionLocY;
//	}
//
//// UNUSED
////	public static boolean toggleHideBeaconPotionEffects() {
////		return hideBeaconPotionEffects = !hideBeaconPotionEffects;
////	}
////
////	public static boolean toggleShowVanillaStatusEffectHUD() {
////		return showVanillaStatusEffectHUD = !showVanillaStatusEffectHUD;
////	}
//
//}
