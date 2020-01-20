package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import net.minecraft.util.text.TextFormatting;

import com.zyin.zyinhud.util.Localization;

/**
 * The Compass determines what direction the player is facing.
 */
public class Compass extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled = ZyinHUDConfig.EnableCompass.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		ZyinHUDConfig.EnableCompass.set(!Enabled);
		ZyinHUDConfig.EnableCompass.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return Enabled = !Enabled;
	}

	public static boolean renderCompassTextInMiddle = false;

	private static String south = Localization.get("compass.south");
	private static String southwest = Localization.get("compass.southwest");
	private static String west = Localization.get("compass.west");
	private static String northwest = Localization.get("compass.northwest");
	private static String north = Localization.get("compass.north");
	private static String northeast = Localization.get("compass.northeast");
	private static String east = Localization.get("compass.east");
	private static String southeast = Localization.get("compass.southeast");

	/**
	 * Calculates the direction the player is facing
	 *
	 * @param infoLineMessageUpToThisPoint the info line message up to this point
	 * @return "[Direction]" compass formatted string if the Compass is enabled, otherwise "".
	 */
	public static String CalculateMessageForInfoLine(String infoLineMessageUpToThisPoint) {
		if (Compass.Enabled) {
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
					InfoLine.GetHorizontalLocation() + x + x_padding,
					InfoLine.GetVerticalLocation(), 0xffffff
				);

				return TextFormatting.GRAY + brackets;
			}
			else {
				return TextFormatting.GRAY + "[" + TextFormatting.RED +
				       compassDirection + TextFormatting.GRAY + ']';
			}
		}

		return "";
	}

}