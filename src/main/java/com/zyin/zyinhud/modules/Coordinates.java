package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.CoordinateOptions;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.text.TextFormatting;

/**
 * The Coordinates calculates the player's position.
 */
public class Coordinates extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled = ZyinHUDConfig.EnableCoordinates.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		ZyinHUDConfig.EnableCoordinates.set(!Enabled);
		ZyinHUDConfig.EnableCoordinates.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return Enabled = !Enabled;
	}

	/**
	 * The current mode for this module
	 */
	public static CoordinateOptions.CoordinateModes Mode = ZyinHUDConfig.CoordinatesMode.get();

	/**
	 * A String which replaces "{x}", "{y}", and "{z}" with coordinates
	 */
	public static String ChatStringFormat = ZyinHUDConfig.CoordinatesChatStringFormat.get();

	private static boolean ShowChunkCoordinates = ZyinHUDConfig.ShowChunkCoordinates.get();
	/**
	 * Use colors to show what ores spawn at the elevation level
	 */
	private static boolean UseYCoordinateColors = ZyinHUDConfig.UseYCoordinateColors.get();


	private static final int[] oreBoundaries = {
		5,    //nothing below 5
		12,    //diamonds stop
		23,    //lapis lazuli stops
		29    //gold stops
		//128	//coal stops
	};
	private static final String[] oreBoundaryColors = {
		TextFormatting.WHITE.toString(),    //nothing below 5
		TextFormatting.AQUA.toString(),        //diamonds stop
		TextFormatting.BLUE.toString(),        //lapis lazuli stops
		TextFormatting.YELLOW.toString()    //gold stops
		//TextFormatting.GRAY		//coal stops
	};

	/**
	 * Calculates the player's coordinates
	 *
	 * @return coordinates string if the Coordinates are enabled, otherwise "".
	 */
	public static String CalculateMessageForInfoLine() {
		if (Coordinates.Enabled) {
			int coordX = GetXCoordinate();
			int coordY = GetYCoordinate();
			int coordZ = GetZCoordinate();
			String yColor = "";

			if (UseYCoordinateColors) {
				for (int y = 0; y < oreBoundaries.length; y++) {
					if (coordY < oreBoundaries[y]) {
						yColor = oreBoundaryColors[y];
						break;
					}
				}
			}

			String coordinatesString = "";
			if (Mode == CoordinateOptions.CoordinateModes.XZY) {
				coordinatesString += TextFormatting.WHITE + "[" + coordX + ", " + coordZ +
				                     ", " + yColor + coordY + TextFormatting.WHITE + ']';

				if (ShowChunkCoordinates) {
					coordinatesString += TextFormatting.ITALIC + " [" + TextFormatting.WHITE +
					                     (GetXCoordinate() & 15) + ", " + (GetZCoordinate() & 15) +
					                     ", " + (GetYCoordinate() & 15) + TextFormatting.ITALIC + ']';
				}
			}
			else if (Mode == CoordinateOptions.CoordinateModes.XYZ) {
				coordinatesString += TextFormatting.WHITE + "[" + coordX + ", " + yColor +
				                     coordY + TextFormatting.WHITE + ", " + coordZ + ']';

				if (ShowChunkCoordinates) {
					coordinatesString += TextFormatting.ITALIC + " [" + TextFormatting.WHITE +
					                     (GetXCoordinate() & 15) + ", " + (GetYCoordinate() & 15) +
					                     ", " + (GetZCoordinate() & 15) + TextFormatting.ITALIC + ']';
				}
			}
			else {
				coordinatesString += "[??, ??, ??]";

				if (ShowChunkCoordinates) { coordinatesString += " [?, ?, ?]"; }
			}

			return coordinatesString;
		}

		return "";
	}

	/**
	 * Paste coordinates into chat.
	 */
	public static void PasteCoordinatesIntoChat() {
		if (mc.currentScreen instanceof ChatScreen) {
			String coordinateString = Coordinates.ChatStringFormat;
			coordinateString = coordinateString.replace("{x}", Integer.toString(Coordinates.GetXCoordinate()));
			coordinateString = coordinateString.replace("{y}", Integer.toString(Coordinates.GetYCoordinate()));
			coordinateString = coordinateString.replace("{z}", Integer.toString(Coordinates.GetZCoordinate()));

			mc.player.sendChatMessage(coordinateString);
		}
	}

	public static int GetXCoordinate() {
		return (int) Math.floor(mc.getRenderViewEntity().posX);
	}

	public static int GetYCoordinate() {
		return (int) Math.floor(mc.getRenderViewEntity().posY);
	}

	public static int GetZCoordinate() {
		return (int) Math.floor(mc.getRenderViewEntity().posZ);
	}

	/**
	 * Toggles using color coded y coordinates
	 *
	 * @return The state it was changed to
	 */
	public static boolean ToggleUseYCoordinateColors() {
		return UseYCoordinateColors = !UseYCoordinateColors;
	}

	/**
	 * Toggles showing chunk coordinates
	 *
	 * @return The state it was changed to
	 */
	public static boolean ToggleShowChunkCoordinates() {
		return ShowChunkCoordinates = !ShowChunkCoordinates;
	}
}
