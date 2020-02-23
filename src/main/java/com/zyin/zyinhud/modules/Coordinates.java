package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.CoordinateOptions;
import com.zyin.zyinhud.util.Localization;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * The Coordinates calculates the player's position.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Coordinates extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(Coordinates.class);
	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled = ZyinHUDConfig.enableCoordinates.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableCoordinates.set(!isEnabled);
		ZyinHUDConfig.enableCoordinates.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * The current mode for this module
	 */
	public static CoordinateOptions.CoordinateModes mode = ZyinHUDConfig.coordinatesMode.get();

	/**
	 * A String which replaces "{x}", "{y}", and "{z}" with coordinates
	 */
	public static String chatStringFormat = ZyinHUDConfig.coordinatesChatStringFormat.get();

	static boolean showChunkCoordinates = ZyinHUDConfig.showChunkCoordinates.get();
	/**
	 * Use colors to show what ores spawn at the elevation level
	 */
	static boolean useYCoordinateColors = ZyinHUDConfig.useYCoordinateColors.get();
	static boolean showDeathLocation = ZyinHUDConfig.showDeathLocation.get();


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
	public static String calculateMessageForInfoLine() {
		if (Coordinates.isEnabled) {
			int coordX = getXCoordinate();
			int coordY = getYCoordinate();
			int coordZ = getZCoordinate();
			String yColor = "";

			if (useYCoordinateColors) {
				for (int y = 0; y < oreBoundaries.length; y++) {
					if (coordY < oreBoundaries[y]) {
						yColor = oreBoundaryColors[y];
						break;
					}
				}
			}

			String coordinatesString = "";
			if (mode == CoordinateOptions.CoordinateModes.XZY) {
				coordinatesString += TextFormatting.WHITE + "[" + coordX + ", " + coordZ +
				                     ", " + yColor + coordY + TextFormatting.WHITE + ']';

				if (showChunkCoordinates) {
					coordinatesString += TextFormatting.ITALIC + " [" + TextFormatting.WHITE +
					                     (getXCoordinate() & 15) + ", " + (getZCoordinate() & 15) +
					                     ", " + (getYCoordinate() & 15) + TextFormatting.ITALIC + ']';
				}
			}
			else if (mode == CoordinateOptions.CoordinateModes.XYZ) {
				coordinatesString += TextFormatting.WHITE + "[" + coordX + ", " + yColor +
				                     coordY + TextFormatting.WHITE + ", " + coordZ + ']';

				if (showChunkCoordinates) {
					coordinatesString += TextFormatting.ITALIC + " [" + TextFormatting.WHITE +
					                     (getXCoordinate() & 15) + ", " + (getYCoordinate() & 15) +
					                     ", " + (getZCoordinate() & 15) + TextFormatting.ITALIC + ']';
				}
			}
			else {
				coordinatesString += "[??, ??, ??]";

				if (showChunkCoordinates) { coordinatesString += " [?, ?, ?]"; }
			}

			return coordinatesString;
		}

		return "";
	}

	/**
	 * Share the user's coordinates in chat.
	 */
	public static void shareCoordinatesInChat() {
		if (mc.currentScreen == null) {
			mc.player.sendChatMessage(fmtCoordinateString());
		}
	}

	//TODO: I'd love to be able to have this occur AFTER the normal death message is shown...
	@SubscribeEvent
	public static void showDeathLocation(LivingDeathEvent event) {
		if (showDeathLocation) {
			Entity entity = event.getEntity();
			if (entity instanceof PlayerEntity && !entity.world.isRemote) {
				//TODO: Decide how to handle localization for this message
				mc.ingameGUI.getChatGUI().printChatMessage(new StringTextComponent(
					entity.getName().getString() + " died in " +
					getDimensionStr(entity.dimension) + " at " + fmtCoordinateString()
				));
			}
		}
	}

	@Nonnull
	public static String fmtCoordinateString() {
		return chatStringFormat.replace("{x}", Integer.toString(Coordinates.getXCoordinate()))
		                       .replace("{y}", Integer.toString(Coordinates.getYCoordinate()))
		                       .replace("{z}", Integer.toString(Coordinates.getZCoordinate()));
	}

	@SuppressWarnings("ConstantConditions")
	public static String getDimensionStr(DimensionType dimType) {
		ResourceLocation r = dimType.getRegistryName();
		String name = Localization.toTitleCase(
			Localization.get(r.getPath()).trim().replace('_', ' ').toLowerCase()
		);
		return name.startsWith("The") ? name : "The " + name;
	}

	@SuppressWarnings("ConstantConditions")
	public static int getXCoordinate() {
		return (int) Math.floor(mc.getRenderViewEntity().posX);
	}

	@SuppressWarnings("ConstantConditions")
	public static int getYCoordinate() {
		return (int) Math.floor(mc.getRenderViewEntity().posY);
	}

	@SuppressWarnings("ConstantConditions")
	public static int getZCoordinate() {
		return (int) Math.floor(mc.getRenderViewEntity().posZ);
	}

	/**
	 * Toggles using color coded y coordinates
	 *
	 * @return The state it was changed to
	 */
	public static boolean toggleUseYCoordinateColors() {
		return useYCoordinateColors = !useYCoordinateColors;
	}

	/**
	 * Toggles showing chunk coordinates
	 *
	 * @return The state it was changed to
	 */
	public static boolean toggleShowChunkCoordinates() {
		return showChunkCoordinates = !showChunkCoordinates;
	}
}
