package com.zyin.zyinhud.mods;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;

//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
import com.zyin.zyinhud.util.Localization;

/**
 * The Info Line consists of everything that gets displayed in the top-left portion
 * of the screen. It's job is to gather information about other classes and render
 * their message into the Info Line.
 */
public class InfoLine extends ZyinHUDModBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled;
	private static long time = 0;
	private static int lastPing = 0;
	private static TextFormatting pingColor;

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		return Enabled = !Enabled;
	}

	public static boolean ShowBiome;
	public static boolean ShowCanSnow;
	public static boolean ShowPing;

	/**
	 * The padding string that is inserted between different elements of the Info Line
	 */
	public static final String SPACER = " ";
	public static int infoLineLocX = 1;
	public static int infoLineLocY = 1;

    /*private static final int notificationDuration = 1200;	//measured in milliseconds
    private static long notificationTimer = 0;				//timer that goes from notificationDuration to 0
    private static long notificationStartTime;*/

	/**
	 * The notification string currently being rendered
	 */
	public static String notificationMessage = "";

	/**
	 * The info line string currently being rendered
	 */
	public static String infoLineMessage;


	/**
	 * Renders the on screen message consisting of everything that gets put into the top let message area,
	 * including coordinates and the state of things that can be activated
	 */
	public static void RenderOntoHUD() {
		//if the player is in the world
		//and not looking at a menu
		//and F3 not pressed
		if (InfoLine.Enabled && !mc.gameSettings.showDebugInfo &&
		    (mc.mouseHelper.isMouseGrabbed() || (mc.currentScreen != null && (mc.currentScreen instanceof ChatScreen/* || TabIsSelectedInOptionsGui()*/)))
		) {
			infoLineMessage = "";

			String clock = Clock.CalculateMessageForInfoLine(infoLineMessage);
			if (clock.length() > 0) { clock += SPACER; }
			infoLineMessage += clock;

			String ping = CalculatePingForInfoLine();
			if (ping.length() > 0) { ping += SPACER; }
			infoLineMessage += ping;

			String coordinates = Coordinates.CalculateMessageForInfoLine();
			if (coordinates.length() > 0) { coordinates += SPACER; }
			infoLineMessage = infoLineMessage + coordinates;

			String compass = Compass.CalculateMessageForInfoLine(infoLineMessage);
			if (compass.length() > 0) { compass += SPACER; }
			infoLineMessage += compass;

			String fps = Fps.CalculateMessageForInfoLine();
			if (fps.length() > 0) { fps += SPACER; }
			infoLineMessage += fps;

			String snow = ShowCanSnow ? CalculateCanSnowForInfoLine(infoLineMessage) : "";
			if (snow.length() > 0) { snow += SPACER; }
			infoLineMessage += snow;

			String biome = ShowBiome ? CalculateBiomeForInfoLine() : "";
			if (biome.length() > 0) { biome += SPACER; }
			infoLineMessage += biome;

			String safe = SafeOverlay.CalculateMessageForInfoLine();
			if (safe.length() > 0) { safe += SPACER; }
			infoLineMessage += safe;

			String players = PlayerLocator.CalculateMessageForInfoLine();
			if (players.length() > 0) { players += SPACER; }
			infoLineMessage += players;

			String animals = AnimalInfo.CalculateMessageForInfoLine();
			if (animals.length() > 0) { animals += SPACER; }
			infoLineMessage += animals;

			mc.fontRenderer.drawStringWithShadow(infoLineMessage, infoLineLocX, infoLineLocY, 0xffffff);
		}
	}

	/**
	 * Calculate can snow for info line string.
	 *
	 * @param infoLineMessageUpToThisPoint the info line message up to this point
	 * @return the string
	 */
	protected static String CalculateCanSnowForInfoLine(String infoLineMessageUpToThisPoint) {
		int xCoord = MathHelper.floor(mc.player.posX);
		int yCoord = MathHelper.floor(mc.player.posY) - 1;
		int zCoord = MathHelper.floor(mc.player.posZ);

		BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);

		boolean canSnowAtPlayersFeet = mc.world.getBiome(pos).doesSnowGenerate(mc.world, pos);

		if (canSnowAtPlayersFeet) {
			float scaler = 0.66f;
			GL11.glScalef(scaler, scaler, scaler);

			int x = (int) (mc.fontRenderer.getStringWidth(infoLineMessageUpToThisPoint) / scaler);
			int y = -1;

			itemRenderer.renderItemAndEffectIntoGUI(new ItemStack(Items.SNOWBALL), x, y);

			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glScalef(1 / scaler, 1 / scaler, 1 / scaler);

			return "  ";
		}
		return "";
	}

	/**
	 * Calculate biome for info line string.
	 *
	 * @return the string
	 */
	protected static String CalculateBiomeForInfoLine() {
		int xCoord = MathHelper.floor(mc.player.posX);
		int zCoord = MathHelper.floor(mc.player.posZ);

		String biomeName = mc.world.getBiome(new BlockPos(xCoord, 64, zCoord)).getDisplayName().toString();
		return TextFormatting.WHITE + biomeName;
	}

	protected static String CalculatePingForInfoLine() {
		if (ShowPing && !mc.isSingleplayer()) {
			if (time < System.currentTimeMillis()) {
				time = System.currentTimeMillis() + 5000;
				try {
					lastPing = mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
				}
				catch (NullPointerException e) { lastPing = -1; }

				if (lastPing < 80) { pingColor = TextFormatting.GREEN; }
				else if (lastPing < 150) { pingColor = TextFormatting.YELLOW; }
				else if (lastPing < 250) { pingColor = TextFormatting.GOLD; }
				else { pingColor = TextFormatting.RED; }
			}
			return TextFormatting.RESET + "[" + pingColor + lastPing + TextFormatting.RESET + "ms]";
		}
		return "";
	}

	/**
	 * Checks to see if the Info Line, Clock, Coordinates, Compass, or FPS tabs are selected in GuiZyinHUDOptions
	 * @return
	 */
//    private static boolean TabIsSelectedInOptionsGui()
//    {
//    	return mc.currentScreen instanceof GuiZyinHUDOptions &&
//    		(((GuiZyinHUDOptions)mc.currentScreen).IsButtonTabSelected(Localization.get("infoline.name")) ||
//			((GuiZyinHUDOptions)mc.currentScreen).IsButtonTabSelected(Localization.get("clock.name")) ||
//			((GuiZyinHUDOptions)mc.currentScreen).IsButtonTabSelected(Localization.get("coordinates.name")) ||
//			((GuiZyinHUDOptions)mc.currentScreen).IsButtonTabSelected(Localization.get("compass.name")) ||
//			((GuiZyinHUDOptions)mc.currentScreen).IsButtonTabSelected(Localization.get("fps.name")));
//    }


	/**
	 * Toggles showing the biome in the Info Line
	 *
	 * @return The state it was changed to
	 */
	public static boolean ToggleShowBiome() {
		return ShowBiome = !ShowBiome;
	}

	/**
	 * Toggles showing if it is possible for snow to fall at the player's feet in the Info Line
	 *
	 * @return The state it was changed to
	 */
	public static boolean ToggleShowCanSnow() {
		return ShowCanSnow = !ShowCanSnow;
	}

	public static boolean ToggleShowPing() {
		return ShowPing = !ShowPing;
	}

	/**
	 * Gets the horizontal location where the potion timers are rendered.
	 *
	 * @return int
	 */
	public static int GetHorizontalLocation() {
		return infoLineLocX;
	}

	/**
	 * Sets the horizontal location where the potion timers are rendered.
	 *
	 * @param x the x
	 * @return the new x location
	 */
	public static int SetHorizontalLocation(int x) {
		infoLineLocX = MathHelper.clamp(x, 0, mc.mainWindow.getWidth());
		return infoLineLocX;
	}

	/**
	 * Gets the vertical location where the potion timers are rendered.
	 *
	 * @return int
	 */
	public static int GetVerticalLocation() {
		return infoLineLocY;
	}

	/**
	 * Sets the vertical location where the potion timers are rendered.
	 *
	 * @param y the y
	 * @return the new y location
	 */
	public static int SetVerticalLocation(int y) {
		infoLineLocY = MathHelper.clamp(y, 0, mc.mainWindow.getHeight());
		return infoLineLocY;
	}
}
