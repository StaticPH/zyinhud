package com.zyin.zyinhud.mods;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.RayTraceResult;

import com.zyin.zyinhud.util.Localization;

/**
 * The Distance Measurer calculates the distance from the player to whatever the player's
 * crosshairs is looking at.
 */
public class DistanceMeasurer extends ZyinHUDModBase {
    /**
     * Enables/Disables this Mod
     */
    public static boolean Enabled;

    /**
     * Toggles this Mod on or off
     *
     * @return The state the Mod was changed to
     */
    public static boolean ToggleEnabled() {
        return Enabled = !Enabled;
    }

    /**
     * The current mode for this mod
     */
    public static Modes Mode;

    /**
     * The enum for the different types of Modes this mod can have
     */
    public static enum Modes {
        /**
         * Off modes.
         */
        OFF("distancemeasurer.mode.off"),
        /**
         * Simple modes.
         */
        SIMPLE("distancemeasurer.mode.simple"),
        /**
         * Coordinate modes.
         */
        COORDINATE("distancemeasurer.mode.complex");

        private String unfriendlyName;

        private Modes(String unfriendlyName) {
            this.unfriendlyName = unfriendlyName;
        }

        /**
         * Sets the next available mode for this mod
         *
         * @return the modes
         */
        public static Modes ToggleMode() {
            return Mode = Mode.ordinal() < Modes.values().length - 1 ? Modes.values()[Mode.ordinal() + 1] : Modes.values()[0];
        }

        /**
         * Gets the mode based on its internal name as written in the enum declaration
         *
         * @param modeName the mode name
         * @return modes
         */
        public static Modes GetMode(String modeName) {
            try {
                return Modes.valueOf(modeName);
            } catch (IllegalArgumentException e) {
                return values()[0];
            }
        }

        /**
         * Get friendly name string.
         *
         * @return the string
         */
        public String GetFriendlyName() {
            return Localization.get(unfriendlyName);
        }
    }


    /**
     * Render onto hud.
     */
    public static void RenderOntoHUD() {
        //if the player is in the world
        //and not looking at a menu
        //and F3 not pressed
        if (DistanceMeasurer.Enabled && Mode != Modes.OFF &&
            (mc.mouseHelper.isMouseGrabbed() || ((mc.currentScreen instanceof ChatScreen))) &&
            !mc.gameSettings.showDebugInfo) {
            String distanceString = CalculateDistanceString();

            int width = mc.mainWindow.getScaledWidth();
            int height = mc.mainWindow.getScaledHeight();
            int distanceStringWidth = mc.fontRenderer.getStringWidth(distanceString);

            mc.fontRenderer.drawStringWithShadow(distanceString, width / 2.0f - distanceStringWidth / 2.0f, height / 2.0f - 10, 0xffffff);
        }
    }


    /**
     * Calculates the distance of the block the player is pointing at
     *
     * @return the distance to a block if Distance Measurer is enabled, otherwise "".
     */
    protected static String CalculateDistanceString() {
//        RayTraceResult objectMouseOver = mc.player.rayTrace(300.0d, 1.0f, RayTraceFluidMode.ALWAYS);
        // If the third parameter of "func_213324_a" here is true, the raytrace will use RayTraceContext.FluidMode.ANY
        RayTraceResult objectMouseOver = mc.player.func_213324_a(300.0d, 1.0f, true); // see DebugOverlayGui:rayTraceFluid

        if (objectMouseOver != null && objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
            if (Mode == Modes.SIMPLE) {
                double playerX = mc.player.posX;
                double playerY = mc.player.posY + mc.player.getEyeHeight();
                double playerZ = mc.player.posZ;

                double blockX = objectMouseOver.getHitVec().x;
                double blockY = objectMouseOver.getHitVec().y;
                double blockZ = objectMouseOver.getHitVec().z;

                double deltaX;
                double deltaY;
                double deltaZ;

                if (playerX < blockX)
                    deltaX = blockX - playerX;
                else if (playerX > blockX + 0.5)
                    deltaX = playerX - blockX;
                else
                    deltaX = playerX - blockX;

                if (playerY < blockY)
                    deltaY = blockY - playerY;
                else if (playerY > blockY)
                    deltaY = playerY - blockY;
                else
                    deltaY = playerY - blockY;

                if (playerZ < blockZ)
                    deltaZ = blockZ - playerZ;
                else if (playerZ > blockZ)
                    deltaZ = playerZ - blockZ;
                else
                    deltaZ = playerZ - blockZ;

                double farthestHorizontalDistance = Math.max(Math.abs(deltaX), Math.abs(deltaZ));
                double farthestDistance = Math.max(Math.abs(deltaY), farthestHorizontalDistance);

                return TextFormatting.GOLD + "[" + String.format("%1$,.1f", farthestDistance) + "]";
            } else if (Mode == Modes.COORDINATE) {
                BlockPos pos = ((BlockRayTraceResult)objectMouseOver).getPos();

                return TextFormatting.GOLD + "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]";
            } else {
                return TextFormatting.GOLD + "[???]";
            }
        } else {
            return TextFormatting.GOLD + "[" + Localization.get("distancemeasurer.far") + "]";
        }
    }
}
