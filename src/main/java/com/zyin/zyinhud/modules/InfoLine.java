package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;

//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
//TODO: investigate displaying an indication that the player has not entered a bed in more than 3 days

/**
 * The Info Line consists of everything that gets displayed in the top-left portion
 * of the screen. It's job is to gather information about other classes and render
 * their message into the Info Line.
 */
public class InfoLine extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled = ZyinHUDConfig.enableInfoLine.get();
	private static long time = 0;
	private static int lastPing = 0;
	private static TextFormatting pingColor;

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableInfoLine.set(!isEnabled);
		ZyinHUDConfig.enableInfoLine.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	private static boolean showBiome = ZyinHUDConfig.showBiome.get();
	private static boolean showCanSnow = ZyinHUDConfig.showCanSnow.get();
	private static boolean showPing = ZyinHUDConfig.showPing.get();

	/**
	 * The padding string that is inserted between different elements of the Info Line
	 */
	private static final String SPACER = " ";
	private static int infoLineLocX = ZyinHUDConfig.infoLineHorizontalPos.get();
	private static int infoLineLocY = ZyinHUDConfig.infoLineVerticalPos.get();

//  UNUSED
//    /*private static final int notificationDuration = 1200;	//measured in milliseconds
//    private static long notificationTimer = 0;				//timer that goes from notificationDuration to 0
//    private static long notificationStartTime;*/
//
//	/**
//	 * The notification string currently being rendered
//	 */
//	public static String notificationMessage = "";

	/**
	 * The info line string currently being rendered
	 */
	private static String infoLineMessage;


	/**
	 * Renders the on screen message consisting of everything that gets put into the top let message area,
	 * including coordinates and the state of things that can be activated
	 */
	public static void renderOntoHUD() {
		//if the player is in the world (which can be inferred if the mouse is currently grabbed)
		//and not looking at a menu (or is looking at either chat or the custom options gui)
		//and debug info not shown
		if (InfoLine.isEnabled && !mc.gameSettings.showDebugInfo &&
		    (mc.mouseHelper.isMouseGrabbed() || (mc.currentScreen instanceof ChatScreen/* || tabIsSelectedInOptionsGui()*/))
		) {
			infoLineMessage = "";

			String clock = Clock.calculateMessageForInfoLine(infoLineMessage);
			infoLineMessage += clock.length() > 0 ? (clock + SPACER) : clock;

			String ping = calculatePingForInfoLine();
			infoLineMessage += ping.length() > 0 ? (ping + SPACER) : ping;

			String coordinates = Coordinates.calculateMessageForInfoLine();
			infoLineMessage += coordinates.length() > 0 ? (coordinates + SPACER) : coordinates;

			String compass = Compass.calculateMessageForInfoLine(infoLineMessage);
			infoLineMessage += compass.length() > 0 ? (compass + SPACER) : compass;

			String fps = Fps.calculateMessageForInfoLine();
			infoLineMessage += fps.length() > 0 ? (fps + SPACER) : fps;

			String snow = showCanSnow ? calculateCanSnowForInfoLine(infoLineMessage) : "";
			infoLineMessage += snow.length() > 0 ? (snow + SPACER) : snow;

			String biome = showBiome ? calculateBiomeForInfoLine() : "";
			infoLineMessage += biome.length() > 0 ? (biome + SPACER) : biome;

			String safe = SafeOverlay.calculateMessageForInfoLine();
			infoLineMessage += safe.length() > 0 ? (safe + SPACER) : safe;

			String players = PlayerLocator.calculateMessageForInfoLine();
			infoLineMessage += players.length() > 0 ? (players + SPACER) : players;

			String animals = AnimalInfo.calculateMessageForInfoLine();
			infoLineMessage += animals.length() > 0 ? (animals + SPACER) : animals;

			mc.fontRenderer.drawStringWithShadow(infoLineMessage, infoLineLocX, infoLineLocY, 0xffffff);
		}
	}

	/**
	 * Calculate can snow for info line string.
	 *
	 * @param infoLineMessageUpToThisPoint the info line message up to this point
	 * @return the string
	 */
	protected static String calculateCanSnowForInfoLine(String infoLineMessageUpToThisPoint) {
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
	protected static String calculateBiomeForInfoLine() {
		int xCoord = MathHelper.floor(mc.player.posX);
		int zCoord = MathHelper.floor(mc.player.posZ);

		String biomeName = mc.world.getBiome(new BlockPos(xCoord, 64, zCoord)).getDisplayName().toString();
		return TextFormatting.WHITE + biomeName;
	}

	//Stop complaining that you cant catch the NPE that is the entire reason you're in a try-catch!
	@SuppressWarnings("ConstantConditions")
	protected static String calculatePingForInfoLine() {
		if (showPing && !mc.isSingleplayer()) {
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
//    private static boolean tabIsSelectedInOptionsGui()
//    {
//    	return mc.currentScreen instanceof GuiZyinHUDOptions &&
//    		(((GuiZyinHUDOptions)mc.currentScreen).isButtonTabSelected(Localization.get("infoline.name")) ||
//			((GuiZyinHUDOptions)mc.currentScreen).isButtonTabSelected(Localization.get("clock.name")) ||
//			((GuiZyinHUDOptions)mc.currentScreen).isButtonTabSelected(Localization.get("coordinates.name")) ||
//			((GuiZyinHUDOptions)mc.currentScreen).isButtonTabSelected(Localization.get("compass.name")) ||
//			((GuiZyinHUDOptions)mc.currentScreen).isButtonTabSelected(Localization.get("fps.name")));
//    }


	/**
	 * Toggles showing the biome in the Info Line
	 *
	 * @return The state it was changed to
	 */
	public static boolean toggleShowBiome() {
		return showBiome = !showBiome;
	}

	/**
	 * Toggles showing if it is possible for snow to fall at the player's feet in the Info Line
	 *
	 * @return The state it was changed to
	 */
	public static boolean toggleShowCanSnow() {
		return showCanSnow = !showCanSnow;
	}

	public static boolean toggleShowPing() {
		return showPing = !showPing;
	}

	/**
	 * Gets the horizontal location where the potion timers are rendered.
	 *
	 * @return int
	 */
	public static int getHorizontalLocation() {
		return infoLineLocX;
	}

	/**
	 * Sets the horizontal location where the potion timers are rendered.
	 *
	 * @param x the x
	 * @return the new x location
	 */
	public static int setHorizontalLocation(int x) {
		infoLineLocX = MathHelper.clamp(x, 0, mc.mainWindow.getWidth());
		return infoLineLocX;
	}

	/**
	 * Gets the vertical location where the potion timers are rendered.
	 *
	 * @return int
	 */
	public static int getVerticalLocation() {
		return infoLineLocY;
	}

	/**
	 * Sets the vertical location where the potion timers are rendered.
	 *
	 * @param y the y
	 * @return the new y location
	 */
	public static int setVerticalLocation(int y) {
		infoLineLocY = MathHelper.clamp(y, 0, mc.mainWindow.getHeight());
		return infoLineLocY;
	}

	public static String getInfoLineMessage() {
		return infoLineMessage;
	}
}
