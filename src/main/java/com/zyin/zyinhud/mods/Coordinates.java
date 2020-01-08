package com.zyin.zyinhud.mods;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.TextFormatting;

import com.zyin.zyinhud.util.Localization;
import com.zyin.zyinhud.util.ZyinHUDUtil;

/**
 * The Coordinates calculates the player's position.
 */
public class Coordinates extends ZyinHUDModBase
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
    public static Modes Mode;

    /**
     * The enum for the different types of Modes this mod can have
     */
    public static enum Modes
    {
        /**
         * Xzy modes.
         */
        XZY("coordinates.mode.xzy"),
        /**
         * Xyz modes.
         */
        XYZ("coordinates.mode.xyz");
        
        private String unfriendlyName;
        
        private Modes(String unfriendlyName)
        {
        	this.unfriendlyName = unfriendlyName;
        }

        /**
         * Sets the next availble mode for this mod
         *
         * @return the modes
         */
        public static Modes ToggleMode()
        {
        	return ToggleMode(true);
        }

        /**
         * Sets the next availble mode for this mod if forward=true, or previous mode if false
         *
         * @param forward the forward
         * @return the modes
         */
        public static Modes ToggleMode(boolean forward)
        {
        	if (forward)
        		return Mode = Mode.ordinal() < Modes.values().length - 1 ? Modes.values()[Mode.ordinal() + 1] : Modes.values()[0];
        	else
        		return Mode = Mode.ordinal() > 0 ? Modes.values()[Mode.ordinal() - 1] : Modes.values()[Modes.values().length - 1];
        }

        /**
         * Gets the mode based on its internal name as written in the enum declaration
         *
         * @param modeName the mode name
         * @return modes
         */
        public static Modes GetMode(String modeName)
        {
        	try {return Modes.valueOf(modeName);}
        	catch (IllegalArgumentException e) {return XZY;}
        }

        /**
         * Get friendly name string.
         *
         * @return the string
         */
        public String GetFriendlyName()
        {
        	return Localization.get(unfriendlyName);
        }
    }

    /**
     * The default chat format String which replaces "{x}", "{y}", and "{z}" with coordinates
     */
    public static String DefaultChatStringFormat = "[{x}, {y}, {z}]";
    /**
     * A String which replaces "{x}", "{y}", and "{z}" with coordinates
     */
    public static String ChatStringFormat;

    /**
     * Use colors to show what ores spawn at the elevation level
     */
    public static boolean UseYCoordinateColors;
    /**
     * The constant ShowChunkCoordinates.
     */
    public static boolean ShowChunkCoordinates;
    
    private static final int oreBoundaries[] =
    {
        5,	//nothing below 5
        12,	//diamonds stop
        23,	//lapis lazuli stops
        29	//gold stops
        //128	//coal stops
    };
    private static final String oreBoundaryColors[] =
    {
            TextFormatting.WHITE.toString(),    //nothing below 5
            TextFormatting.AQUA.toString(),        //diamonds stop
            TextFormatting.BLUE.toString(),        //lapis lazuli stops
            TextFormatting.YELLOW.toString()    //gold stops
            //TextFormatting.GRAY		//coal stops
    };

    /**
     * Calculates the players coordinates
     *
     * @return coordinates string if the Coordinates are enabled, otherwise "".
     */
    public static String CalculateMessageForInfoLine()
    {
        if(Coordinates.Enabled)
        {
            int coordX = GetXCoordinate();
            int coordY = GetYCoordinate();
            int coordZ = GetZCoordinate();
            String yColor = "";

            if (UseYCoordinateColors)
            {
                for (int y = 0; y < oreBoundaries.length; y++)
                {
                    if (coordY < oreBoundaries[y])
                    {
                        yColor = oreBoundaryColors[y];
                        break;
                    }
                }
            }

            String coordinatesString = "";
            if(Mode == Modes.XZY)
            {
                coordinatesString += TextFormatting.WHITE + "[" + coordX + ", " + coordZ + ", " + yColor + coordY + TextFormatting.WHITE + "]";

                if(ShowChunkCoordinates)
                    coordinatesString += TextFormatting.ITALIC + " [" + TextFormatting.WHITE + (GetXCoordinate() & 15) + ", " + (GetZCoordinate() & 15) + ", " + (GetYCoordinate() & 15) + TextFormatting.ITALIC + "]";
            }
            else if(Mode == Modes.XYZ)
            {
                coordinatesString += TextFormatting.WHITE + "[" + coordX + ", " + yColor + coordY + TextFormatting.WHITE + ", " + coordZ + "]";

	            if(ShowChunkCoordinates)
                    coordinatesString += TextFormatting.ITALIC + " [" + TextFormatting.WHITE + (GetXCoordinate() & 15) + ", " + (GetYCoordinate() & 15) + ", " + (GetZCoordinate() & 15) + TextFormatting.ITALIC + "]";
            }
            else
            {
            	coordinatesString += "[??, ??, ??]";
            	
	            if(ShowChunkCoordinates)
	            	coordinatesString += " [?, ?, ?]";
            }
            
            return coordinatesString;
        }

        return "";
    }

    /**
     * Paste coordinates into chat.
     */
    public static void PasteCoordinatesIntoChat()
    {
    	if(mc.currentScreen != null && mc.currentScreen instanceof ChatScreen)
    	{
        	String coordinateString = Coordinates.ChatStringFormat;
        	coordinateString = coordinateString.replace("{x}", Integer.toString(Coordinates.GetXCoordinate()));
        	coordinateString = coordinateString.replace("{y}", Integer.toString(Coordinates.GetYCoordinate()));
        	coordinateString = coordinateString.replace("{z}", Integer.toString(Coordinates.GetZCoordinate()));
        	
        	TextFieldWidget inputField = ZyinHUDUtil.GetFieldByReflection(ChatScreen.class, (ChatScreen)mc.currentScreen, "inputField", "field_146415_a");
        	
        	if(inputField != null)
        	{
        		inputField.writeText(coordinateString);
        	}
    	}
    }

    /**
     * Get x coordinate int.
     *
     * @return the int
     */
    public static int GetXCoordinate()
    {
    	return (int) Math.floor(mc.getRenderViewEntity().posX);
    }

    /**
     * Get y coordinate int.
     *
     * @return the int
     */
    public static int GetYCoordinate()
    {
    	return (int) Math.floor(mc.getRenderViewEntity().posY);
    }

    /**
     * Get z coordinate int.
     *
     * @return the int
     */
    public static int GetZCoordinate()
    {
    	return (int) Math.floor(mc.getRenderViewEntity().posZ);
    }

    /**
     * Toggles using color coded y coordinates
     *
     * @return The state it was changed to
     */
    public static boolean ToggleUseYCoordinateColors()
    {
    	return UseYCoordinateColors = !UseYCoordinateColors;
    }

    /**
     * Toggles showing chunk coordinates
     *
     * @return The state it was changed to
     */
    public static boolean ToggleShowChunkCoordinates()
    {
    	return ShowChunkCoordinates = !ShowChunkCoordinates;
    }
}
