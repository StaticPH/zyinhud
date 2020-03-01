package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.config.ZyinHUDConfig;
import net.minecraft.util.text.TextFormatting;

import com.zyin.zyinhud.util.Localization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Compass determines what direction the player is facing.
 */
public class Compass extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(Compass.class);

	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled;

	private static boolean renderCompassTextInMiddle;

	private static String south = Localization.get("compass.south");
	private static String southwest = Localization.get("compass.southwest");
	private static String west = Localization.get("compass.west");
	private static String northwest = Localization.get("compass.northwest");
	private static String north = Localization.get("compass.north");
	private static String northeast = Localization.get("compass.northeast");
	private static String east = Localization.get("compass.east");
	private static String southeast = Localization.get("compass.southeast");

	static { loadFromConfig(); }

	public static void loadFromConfig() {
		isEnabled = ZyinHUDConfig.enableCompass.get();
		renderCompassTextInMiddle = ZyinHUDConfig.renderCompassTextInMiddle.get();
	}

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableCompass.set(!isEnabled);
		ZyinHUDConfig.enableCompass.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	//TODO: Add option to display =/- X/Z directions in addition to or instead of N/E/S/W
	//      North = -Z  South = +Z
	//      West = -X   East = +X
	//  see: Direction and Direction8 classes

	/**
	 * Calculates the direction the player is facing
	 *
	 * @param infoLineMessageUpToThisPoint the info line message up to this point
	 * @return "[Direction]" compass formatted string if the Compass is enabled, otherwise "".
	 */
	public static String calculateMessageForInfoLine(String infoLineMessageUpToThisPoint) {
		if (Compass.isEnabled) {
			String compassDirection;
			int facing;
			int yaw = (int) mc.player.rotationYaw;
			yaw += 22;    //+22 centers the compass (45degrees/2)
			yaw %= 360;

			if (yaw < 0) { yaw += 360; }

			facing = yaw / 45; //  360 degrees divided by 45 == 8 zones


			if (facing == 0) { compassDirection = south; }
			else if (facing == 1) { compassDirection = southwest; }
			else if (facing == 2) { compassDirection = west; }
			else if (facing == 3) { compassDirection = northwest; }
			else if (facing == 4) { compassDirection = north; }
			else if (facing == 5) { compassDirection = northeast; }
			else if (facing == 6) { compassDirection = east; }
			else { compassDirection = southeast; } // if(facing == 7)

			if (renderCompassTextInMiddle) {
				String brackets;
				int x;
				int x_padding;

				//the font spacing is different if we are rendering in Unicode
//            	if(mc.fontRenderer.getUnicodeFlag())
//            	{
//            		brackets = "[  ]";
//                	x = mc.fontRenderer.getStringWidth(infoLineMessageUpToThisPoint);
//                	x_padding = mc.fontRenderer.getStringWidth(brackets)/2 - 4;
//                	if(facing % 2 == 0)	//s,w,n,e
//                		x_padding += 2;
//
//            	}
//            	else
//            	{
				brackets = "[   ]";
				x = mc.fontRenderer.getStringWidth(infoLineMessageUpToThisPoint);
				x_padding = mc.fontRenderer.getStringWidth(brackets) / 2 - 6;
				if (facing % 2 == 0) { x_padding += 3; }   //s,w,n,e
//            	}

				mc.fontRenderer.drawStringWithShadow(
					TextFormatting.RED + compassDirection,
					InfoLine.getHorizontalLocation() + x + x_padding,
					InfoLine.getVerticalLocation(), 0xffffff
				);

				return TextFormatting.GRAY + brackets;
			}
			else {
				return TextFormatting.GRAY + "[" + TextFormatting.RED + compassDirection + TextFormatting.GRAY + ']';
			}
		}

		return "";
	}

}
