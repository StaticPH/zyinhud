package com.zyin.zyinhud.modules;

import com.mojang.blaze3d.platform.GlStateManager;
import com.zyin.zyinhud.config.ZyinHUDConfig;
//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.PotionTimerOptions.PotionTimerModes;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
//import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

import static com.zyin.zyinhud.modules.ZyinHUDModuleModes.PotionTimerOptions.maxPotionTimersHorizontalPos;
import static com.zyin.zyinhud.modules.ZyinHUDModuleModes.PotionTimerOptions.maxPotionTimersVerticalPos;
import static com.zyin.zyinhud.util.ZyinHUDUtil.*;
import static net.minecraft.client.gui.AbstractGui.blit;
//import static net.minecraft.client.gui.screen.inventory.ContainerScreen.INVENTORY_BACKGROUND;
import static net.minecraft.client.renderer.texture.AtlasTexture.LOCATION_EFFECTS_TEXTURE;
import static net.minecraft.potion.Effect.getId;
import static net.minecraft.util.math.MathHelper.ceil;
import static net.minecraft.util.math.MathHelper.clamp;
import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.findField;


/**
 * Potion Timers displays the remaining time left on any potion effects the user has.
 */
@SuppressWarnings({"RedundantCast", "RedundantSuppression"})
public class PotionTimers extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(DurabilityInfo.class);
	/** mapped field: ContainerScreen.guiLeft */
	private static Field guiLeft = findField(ContainerScreen.class, "field_147003_i");
	/** mapped field: DisplayEffectsScreen.hasActivePotionEffects */
	private static Field hasActivePotionEffects = findField(DisplayEffectsScreen.class, "field_147045_u");

	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled;

	/**
	 * The current mode for this module
	 */
	public static PotionTimerModes mode;

	private static boolean hideBeaconPotionEffects;
	private static boolean hidePotionEffectsInInventory;
	private static float potionScale;
	private static int potionLocX;
	private static int potionLocY;
	private static boolean showPotionIcons;
	//	private static boolean showEffectLevel;
//	private static boolean showEffectName;
	private static boolean showVanillaStatusEffectHUD;
//	private static boolean UsePotionColors;     UNUSED

	/**
	 * How far to offset the effect duration text when the effect icon is also shown.
	 * Positive values offset to the right. Negative values offset to the left.
	 */
	protected static int textOffsetX = 10;
	/** The time at which blinking starts */
	protected static final int[] blinkingThresholds = {3 * 20, 5 * 20, 16 * 20};
	/** How often the blinking occurs */
	protected static final int[] blinkingSpeed = {5, 10, 20};
	/** How long the blink lasts */
	protected static final int[] blinkingDuration = {2, 3, 3};


	static { loadFromConfig(); }

	public static void loadFromConfig() {
		isEnabled = ZyinHUDConfig.enablePotionTimers.get();
		hideBeaconPotionEffects = ZyinHUDConfig.hideBeaconPotionEffects.get();
		hidePotionEffectsInInventory = ZyinHUDConfig.hidePotionEffectsInInventory.get();
		mode = ZyinHUDConfig.potionTimerMode.get();
		potionScale = ZyinHUDConfig.potionScale.get().floatValue();
		potionLocX = ZyinHUDConfig.potionTimersHorizontalPos.get();
		potionLocY = ZyinHUDConfig.potionTimersVerticalPos.get();
		showPotionIcons = ZyinHUDConfig.showPotionIcons.get();
		showVanillaStatusEffectHUD = ZyinHUDConfig.showVanillaStatusEffectHUD.get();
	}

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enablePotionTimers.set(!isEnabled);
		ZyinHUDConfig.enablePotionTimers.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * Renders the duration any potion effects that the player currently has on the left side of the screen.
	 */
	public static void renderOntoHUD() {
		//if the player is in the world
		//and not in a menu (except for chat and the custom Options menu)
		//and F3 not shown
		if (
			isEnabled && !mc.gameSettings.showDebugInfo &&
			(mc.mouseHelper.isMouseGrabbed() || doesScreenShowHUD(mc.currentScreen) /*|| tabIsSelectedInOptionsGui()*/)
		) {
			//FIXME: There's something incredibly wonky happening with the x values...
			int x = (int) getClampedX();
			int y = (int) clamp(scaleWithWindowSize(
				(potionLocY * potionScale), maxPotionTimersVerticalPos, false), 0f, 18 * potionScale
			);
			int potionTextOffsetPos = offsetPotionTextX(x, mc.mainWindow.getGuiScaleFactor());
			int gap = ceil(18 * potionScale);
//			GlStateManager.pushMatrix();
			GlStateManager.scalef(potionScale, potionScale, potionScale);
//			GlStateManager.translated(0.0D, 0.0D, -90.0D);
//			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

			for (EffectInstance effect : mc.player.getActivePotionEffects()) {
				if (!effect.isAmbient() || !hideBeaconPotionEffects) {
					//FIXME: really shouldnt be able to have showPotionIcons false without showEffectName being true
					if (showPotionIcons) {
						drawPotionIcon(x, y, effect.getPotion());
					}

					if (mode != PotionTimerModes.NONE) {
//						drawPotionText(potionTextOffsetPos, y+7, effect);
						drawPotionText(potionTextOffsetPos, y + (gap / 2), effect);
					}

					y += gap;
				}
			}

			GlStateManager.scalef(1f / potionScale, 1f / potionScale, 1f / potionScale);
//			GlStateManager.popMatrix();
		}
	}

	//remove potionScale as a factor in scaling X itself?
	private static float getScaledX(float newMax) {
		return scaleValue((potionLocX * potionScale), maxPotionTimersHorizontalPos, newMax);
	}

	private static float getClampedX() {
		float scaleMax = mc.mainWindow.getScaledWidth();
		float scaled = getScaledX(scaleMax);
		float max;// = scaled + ((18 * potionScale) <= (scaleMax - 1) ? (scaleMax - 1) : (scaleMax - (18 * potionScale)));
//		if (scaled > scaleMax - (18 * potionScale) ){
//			max = scaleMax;
//		}
//		else{
//
//		}
		return clamp(scaled, 0, (scaleMax - (18 * potionScale)));
	}

	/**
	 * If <tt>showPotionIcons == true</tt>, offset the horizontal positioning of the effect duration text
	 * according to which side of the window the potion icons are being displayed. <br>
	 * The text is offset to the left when icons are on the right side of the screen,
	 * and to the right when icons are on the left side of the screen.
	 *
	 * @param currentXPos The horizontal position at which the potion effect is shown.
	 * @return If icons are not shown, <tt>currentXPos</tt>,
	 * otherwise the offset x position for displaying effect duration
	 */
	protected static int offsetPotionTextX(int currentXPos, double guiScaleFactor) {
		if (!showPotionIcons) { return currentXPos; }
		else if (currentXPos >= mc.mainWindow.getScaledWidth() / 2) {
			// displaying on right side of the screen, so offset to left
			return currentXPos - (int) (guiScaleFactor * textOffsetX);
		}
		else {
			// displaying on the left side of the screen, so offset to the right
			return currentXPos + (int) (guiScaleFactor * textOffsetX);
		}
	}

	/**
	 * Draws a potion's remaining duration and name with a color coded blinking timer
	 *
	 * @param x      the x
	 * @param y      the y
	 * @param effect the potion effect
	 */
	protected static void drawPotionText(int x, int y, EffectInstance effect) {
		int potionDuration = effect.getDuration();
		int colorInt = getTextColor(effect.getPotion());

		String potionText = getEffectDurationStr(potionDuration);

//		UNUSED
//		if (showEffectName) {
//			potionText += " " + I18n.format(effect.getEffectName());
//		}
//		if (showEffectLevel) { //TODO: implement effect level display as a subscript?
//			potionText += " " + (effect.getAmplifier() + 1);
//		}

//        boolean unicodeFlag = mc.fontRenderer.getUnicodeFlag();
//        mc.fontRenderer.setUnicodeFlag(true);

		//render the potion duration text onto the screen
		if (potionDuration >= blinkingThresholds[blinkingThresholds.length - 1]) {
			//if the text is not blinking then render it normally
			mc.fontRenderer.drawStringWithShadow(
				potionText, x - mc.fontRenderer.getStringWidth(potionText), y, colorInt
			);
		}
		else {
			//else if the text is blinking, have a chance to not render it based on the blinking variables
			//logic to determine if the text should be displayed, checks the blinking text settings
			for (int j = 0; j < blinkingThresholds.length; j++) {
				if (potionDuration < blinkingThresholds[j]) {
					if (potionDuration % blinkingSpeed[j] > blinkingDuration[j]) {
						String s = TextFormatting.BOLD + potionText;
						mc.fontRenderer.drawStringWithShadow(s, x - mc.fontRenderer.getStringWidth(s), y, colorInt);
					}

					break;
				}
			}
		}

//        mc.fontRenderer.setUnicodeFlag(unicodeFlag);
	}

	/**
	 * Draws a potion's icon texture
	 *
	 * @param x      the x
	 * @param y      the y
	 * @param potion the potion
	 */
	protected static void drawPotionIcon(int x, int y, @Nonnull Effect potion) {
		// see net/minecraft/client/gui/DisplayEffectsScreen.java
		//      func_214079_a       renders the box around the potion effect displayed in the player inventory
		//      func_214077_b       renders the icon??
		//      func_214078_c       render the text ???and the icon in the inventory???
		//  in 1.13, DisplayEffectsScreen was more-or-less covered by net/minecraft/client/renderer/InventoryEffectRenderer
		// Honestly, it might be easier to just hijack IngameGui:renderPotionEffects
		//  potion icon textures found in assets/textures/mob_effects/      based on constructor in PotionSpriteUploader
//		mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
		mc.getTextureManager().bindTexture(LOCATION_EFFECTS_TEXTURE);
		TextureAtlasSprite sprite = mc.getPotionSpriteUploader().getSprite(potion);
		//some modded potions use a custom Resource Location for potion drawing, typically done in the .hasStatusIcon() method
		//noinspection ConstantConditions
		if (sprite != null) {
			//zLevel 300, because why not?
			// textureHeight and textureWidth both 256, for some reason
			int iconIndex = getEffectID(potion);
			int u = (iconIndex % 8) * 18;
			int v = 198 + ((iconIndex / 8) * 18);
			int width = 18;
			int height = 18;
//			float scaler = 1.0f;
//
//			GlStateManager.color4f(1f, 1f, 1f, 1f);

			//noinspection ConstantConditions
//			mc.getTextureManager().bindTexture(
//				new ResourceLocation("textures/mob_effect/" + potion.getRegistryName().getPath() + ".png")
//			);
			// i suspect this will cause issues when using texture packs with larger effect sprites
			blit(x, y, 18, width, height, sprite);
//			blit(x, y, 300, sprite.getMinU(), sprite.getMinV(), width, height, 256, 256);
//			ZyinHUDRenderer.renderCustomTexture(x, y, u, v, width, height, sprite.getName(), 1.0f);
		}
	}

	/**
	 * Disables the potion effects from rendering by telling the Gui that the player has no potion effects applied.
	 * Uses reflection to grab the class's private variable.
	 *
	 * @param guiScreen the <tt>Screen</tt> the player is looking at which extends <tt>DisplayEffectsScreen</tt>
	 */
	public static <T extends DisplayEffectsScreen> void disableInventoryPotionEffects(T guiScreen) {
		if (isEnabled && hidePotionEffectsInInventory) {
			if (!mc.player.getActivePotionEffects().isEmpty()) {
				int guiLeftPx = (guiScreen.width - 176) / 2;

				try { guiLeft.set((ContainerScreen) guiScreen, guiLeftPx); }
				catch (IllegalAccessException e) {
					logger.error("Unable to set any field \"guiLeft\" on type ContainerScreen", e);
//					return;
				}

				try { hasActivePotionEffects.set((DisplayEffectsScreen) guiScreen, false); }
				catch (IllegalAccessException e) {
					logger.error("Unable to set any field \"hasActivePotionEffects\" on type DisplayEffectsScreen", e);
//					return;
				}
			}
		}
	}

	/**
	 * Get the color used when displaying the text for a specific potion effect.<br>
	 * If <tt>mode</tt> is <tt>COLORED</tt>, the color is set from the color associated with the potion effect itself.
	 *
	 * @param potion The specific potion effect for which to get the text display color
	 * @return The text display color as an int.<br>
	 * Note that this returns 0xFFFFFF(White) if <tt>mode</tt> is either <tt>WHITE</tt> or <tt>NONE</tt>.
	 */
	private static int getTextColor(Effect potion) {
		return mode == PotionTimerModes.COLORED ? potion.getLiquidColor() : 0xFFFFFF;
	}

	@Nonnull
	private static String getEffectDurationStr(int effectDuration) {
		int dur = effectDuration / 20; // Duration decreases at a rate of 20 per second

		if (dur > 1600) {
			return " **:**";
		}
		else {
			String secs = ((dur % 60) < 10) ? ("0" + (dur % 60)) : ("" + (dur % 60));
			String mins = ((dur / 60) < 10) ? ("0" + (dur / 60)) : ("" + (dur / 60));

			// make the text bold when duration is low enough to start blinking
//			return (dur < blinkingThresholds[blinkingThresholds.length - 1]) ?
//			       (mins + ':' + secs) :
//			       (TextFormatting.BOLD + mins +  ':' + secs);
			return mins + ':' + secs;
		}
	}

	private static int getEffectID(Effect effect) {
		return getId(effect);
	}

	public static boolean canShowVanillaStatusEffectHUD() {
		return showVanillaStatusEffectHUD;
	}

	/**
	 * Checks to see if the Potion Timers tab is selected in GuiZyinHUDOptions
	 *
	 * @return
	 */
//    private static boolean tabIsSelectedInOptionsGui() {
//        return mc.currentScreen instanceof GuiZyinHUDOptions &&
//                (((GuiZyinHUDOptions) mc.currentScreen).isButtonTabSelected(Localization.get("potiontimers.name")));
//    }

	/**
	 * Toggles showing potion icons.<br>
	 * Inverts the value of <tt>showPotionIcons</tt>
	 *
	 * @return the updated value of <tt>showPotionIcons</tt>
	 */
/*
	public static boolean toggleShowPotionIcons() {
		return showPotionIcons = !showPotionIcons;
	}
*/

	/**
	 * Toggles hiding potion effects in the players inventory.<br>
	 * Inverts the value of <tt>hidePotionEffectsInInventory</tt>
	 *
	 * @return the updated value of <tt>hidePotionEffectsInInventory</tt>
	 */
/*
	public static boolean toggleHidePotionEffectsInInventory() {
		return hidePotionEffectsInInventory = !hidePotionEffectsInInventory;
	}
*/

	/**
	 * Gets the horizontal location where the potion timers are rendered.
	 *
	 * @return <tt>potionLocX</tt>
	 */
/*
	public static int getHorizontalLocation() {
		return potionLocX;
	}
*/

	/**
	 * Sets the horizontal location where the potion timers are rendered.
	 *
	 * @param x the x
	 * @return the new x location
	 */
/*
	public static int setHorizontalLocation(int x) {
		potionLocX = MathHelper.clamp(x, 0, mc.mainWindow.getWidth());
		return potionLocX;
	}
*/

	/**
	 * Gets the vertical location where the potion timers are rendered.
	 *
	 * @return <tt>positionLocY</tt>
	 */
/*
	public static int getVerticalLocation() {
		return potionLocY;
	}
*/

	/**
	 * Sets the vertical location where the potion timers are rendered.
	 *
	 * @param y the y
	 * @return the new y location
	 */
/*
	public static int setVerticalLocation(int y) {
		potionLocY = MathHelper.clamp(y, 0, mc.mainWindow.getHeight());
		return potionLocY;
	}
*/

// UNUSED
	/**
	 * Toggles hiding potion effects granted by nearby beacons.<br>
	 * Inverts the value of <tt>hideBeaconPotionEffects</tt>
	 *
	 * @return the updated value of <tt>hideBeaconPotionEffects</tt>
	 */
//	public static boolean toggleHideBeaconPotionEffects() {
//		return hideBeaconPotionEffects = !hideBeaconPotionEffects;
//	}

	/**
	 * Toggles showing the vanilla status effects HUD.<br>
	 * Inverts the value of <tt>showVanillaStatusEffectHUD</tt>
	 *
	 * @return the updated value of <tt>showVanillaStatusEffectHUD</tt>
	 */
//	public static boolean toggleShowVanillaStatusEffectHUD() {
//		return showVanillaStatusEffectHUD = !showVanillaStatusEffectHUD;
//	}

	/**
	 * Toggles showing effect name.<br>
	 * Inverts the value of <tt>showEffectName</tt>
	 *
	 * @return the updated value of <tt>showEffectName</tt>
	 */
//	public static boolean toggleShowEffectName() {
//		return showEffectName = !showEffectName;
//	}

	/**
	 * Toggles showing effect level.<br>
	 * Inverts the value of <tt>showEffectLevel</tt>
	 *
	 * @return the updated value of <tt>showEffectLevel</tt>
	 */
//	public static boolean toggleShowEffectLevel() {
//		return showEffectLevel = !showEffectLevel;
//	}
}
