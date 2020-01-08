package com.zyin.zyinhud.mods;

import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.util.Localization;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.screen.ChatScreen;
//import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

//TODO: Tamed cats, maybe birds and other creatures?

/**
 * The Player Locator checks for nearby players and displays their name on screen wherever they are.
 */
public class PlayerLocator extends ZyinHUDModBase {
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
        OFF("playerlocator.mode.off"),
        /**
         * On modes.
         */
        ON("playerlocator.mode.on");

        private String unfriendlyName;

        private Modes(String unfriendlyName) {
            this.unfriendlyName = unfriendlyName;
        }

        /**
         * Sets the next availble mode for this mod
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
     * Shows how far you are from other players next to their name
     */
    public static boolean ShowDistanceToPlayers;
    /**
     * The constant ShowPlayerHealth.
     */
    public static boolean ShowPlayerHealth;
    /**
     * The constant ShowWitherSkeletons.
     */
    public static boolean ShowWitherSkeletons;
    /**
     * The constant ShowWolves.
     */
    public static boolean ShowWolves;
    /**
     * The constant UseWolfColors.
     */
    public static boolean UseWolfColors;

    private static final ResourceLocation iconsResourceLocation = new ResourceLocation("textures/gui/icons.png");

    private static final double pi = Math.PI;
    
    private static final String sprintingMessagePrefix = "";
    private static final String sneakingMessagePrefix = TextFormatting.ITALIC.toString();
    private static final String ridingMessagePrefix = "    ";    //space for the saddle/minecart/boat/horse armor icon

    /**
     * Don't render players that are closer than this
     */
    public static int viewDistanceCutoff = 0;
    /**
     * The constant minViewDistanceCutoff.
     */
    public static final int minViewDistanceCutoff = 0;
    /**
     * The constant maxViewDistanceCutoff.
     */
    public static final int maxViewDistanceCutoff = 130;    //realistic max distance the game will render entities: up to ~115 blocks away

    /**
     * The constant numOverlaysRendered.
     */
    public static int numOverlaysRendered;
    /**
     * The constant maxNumberOfOverlays.
     */
    public static final int maxNumberOfOverlays = 50;    //render only the first nearest 50 players

    /**
     * Get the type of the horse armor in int type
     *
     * @param armor_list List of the armors that player's horse currently have
     * @return Horse Armor Type
     */
    public static int getHorseArmorType(Iterable<ItemStack> armor_list) {
        if (armor_list == null) {
            return 0;
        } else {
            if (armor_list.iterator().hasNext()) {
                Item armor_single_item;
                try {
                    armor_single_item = armor_list.iterator().next().getItem();
                } catch (NullPointerException e) {
                    return 0;
                }
                if (armor_single_item != null) {
                    return armor_single_item == Items.IRON_HORSE_ARMOR ? 1 : (armor_single_item == Items.GOLDEN_HORSE_ARMOR ? 2 : (armor_single_item == Items.DIAMOND_HORSE_ARMOR ? 3 : 0));
                }
                return 0;
            }

        }
        return 0;
    }

    /**
     * Renders nearby players's names on the screen.
     *
     * @param entity the entity
     * @param x      location on the HUD
     * @param y      location on the HUD
     */
    public static void RenderEntityInfoOnHUD(Entity entity, int x, int y) {
        if (numOverlaysRendered > maxNumberOfOverlays)
            return;

        if (!(entity instanceof RemoteClientPlayerEntity ||
              entity instanceof WolfEntity ||
              entity instanceof WitherSkeletonEntity)) {
            return;    //we only care about other players and wolves
        }

        //if the player is in the world
        //and not looking at a menu
        //and F3 not pressed
        if (PlayerLocator.Enabled && Mode == Modes.ON &&
                (mc.mouseHelper.isMouseGrabbed() || mc.currentScreen == null || mc.currentScreen instanceof ChatScreen)
                && !mc.gameSettings.showDebugInfo) {

            //only show entities that are close by
            float distanceFromMe = mc.player.getDistance(entity);

            if (distanceFromMe > maxViewDistanceCutoff
                    || distanceFromMe < viewDistanceCutoff
                    || distanceFromMe == 0) //don't render ourselves!
            {
                return;
            }

            String overlayMessage = "";
            int rgb = 0xFFFFFF;
            //calculate the color of the overlayMessage based on the distance from me
            int alpha = (int) (0x55 + 0xAA * ((maxViewDistanceCutoff - distanceFromMe) / maxViewDistanceCutoff));

            if (entity instanceof RemoteClientPlayerEntity) {
                overlayMessage = GetOverlayMessageForOtherPlayer((RemoteClientPlayerEntity) entity, distanceFromMe);

                //format the string to be the same color as that persons team color
                ScorePlayerTeam team = (ScorePlayerTeam) ((RemoteClientPlayerEntity) entity).getTeam();
                if (team != null)
                    overlayMessage = team.getName();// .formatString(overlayMessage);
            } else if (entity instanceof WolfEntity) {
                if (!ShowWolves || !PlayerIsWolfsOwner((WolfEntity) entity))
                    return;

                overlayMessage = GetOverlayMessageForWolf((WolfEntity) entity, distanceFromMe);

                if (UseWolfColors) {
                    DyeColor collarColor = ((WolfEntity) entity).getCollarColor();
                    float[] dyeRGBColors = SheepEntity.getDyeRgb(collarColor);    //func_175513_a() friendly name is probably "getHexColorsFromDye"

                    int r = (int) (dyeRGBColors[0] * 255);
                    int g = (int) (dyeRGBColors[1] * 255);
                    int b = (int) (dyeRGBColors[2] * 255);
                    rgb = (r << 4 * 4) + (g << 4 * 2) + b;    //actual collar color

                    r = (0xFF - r) / 2;
                    g = (0xFF - g) / 2;
                    b = (0xFF - b) / 2;
                    rgb = rgb + ((r << 4 * 4) + (g << 4 * 2) + b);    //a more white version of the collar color
                }
            } else if (entity instanceof WitherSkeletonEntity) {
                if (!ShowWitherSkeletons)
                    return;

                overlayMessage = GetOverlayMessageForWitherSkeleton((WitherSkeletonEntity) entity, distanceFromMe);

                rgb = 0x555555;
                alpha = alpha / 6;
            }

            if (entity.getRidingEntity() != null)
                overlayMessage = "    " + overlayMessage;    //make room for any icons we render

            int overlayMessageWidth = mc.fontRenderer.getStringWidth(overlayMessage);    //the width in pixels of the message
            int width = mc.mainWindow.getScaledWidth();        //~427
            int height = mc.mainWindow.getScaledHeight();        //~240

            //center the text horizontally over the entity
            x -= overlayMessageWidth / 2;

            //check if the text is attempting to render outside of the screen, and if so, fix it to snap to the edge of the screen.
            x = Math.min(x, width - overlayMessageWidth);
            x = Math.max(x, 0);
            y = (y > height - 10 && !ShowPlayerHealth) ? height - 10 : y;
            y = (y > height - 20 && ShowPlayerHealth) ? height - 20 : y;
            if (y < 10 && InfoLine.infoLineLocY <= 1 &&
                    (x > InfoLine.infoLineLocX + mc.fontRenderer.getStringWidth(InfoLine.infoLineMessage) || x < InfoLine.infoLineLocX - overlayMessageWidth))
                y = Math.max(y, 0);    //if the text is to the right or left of the info line then allow it to render in that open space
            else
                y = Math.max(y, 10);    //use 10 instead of 0 so that we don't write text onto the top left InfoLine message area


            //render the overlay message
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            int color = (alpha << 24) + rgb;    //alpha:r:g:b, (alpha << 24) turns it into the format: 0x##000000
            mc.fontRenderer.drawStringWithShadow(overlayMessage, x, y, color);

            //also render whatever the player is currently riding on
            if (entity.getRidingEntity() instanceof HorseEntity) {
                //armor is 0 when no horse armor is equipped
                Iterable<ItemStack> armor_list = entity.getRidingEntity().getArmorInventoryList();
                int armor = getHorseArmorType(armor_list);

                if (armor == 1)
                    RenderHorseArmorIronIcon(x, y);
                else if (armor == 2)
                    RenderHorseArmorGoldIcon(x, y);
                else if (armor == 3)
                    RenderHorseArmorDiamondIcon(x, y);
                else if (((HorseEntity) entity.getRidingEntity()).isHorseSaddled())
                    RenderSaddleIcon(x, y);
            }
            if (entity.getRidingEntity() instanceof PigEntity) {
                RenderSaddleIcon(x, y);
            } else if (entity.getRidingEntity() instanceof AbstractMinecartEntity) {
                RenderMinecartIcon(x, y);
            } else if (entity.getRidingEntity() instanceof BoatEntity) {
                RenderBoatIcon(x, y, ((BoatEntity) entity.getRidingEntity()).getItemBoat());
            } else if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isElytraFlying()) {
                RenderElytraIcon(x, y);
            }

            //if showing player health is turned on, render the hp and a heart icon under their name
            if (ShowPlayerHealth && !(entity instanceof MonsterEntity))    //but don't show health for mobs, such as Wither Skeletons
            {
                int numHearts = (int) ((((LivingEntity) entity).getHealth() + 1) / 2);
                String hpOverlayMessage = numHearts + "";

                int hpOverlayMessageWidth = mc.fontRenderer.getStringWidth(hpOverlayMessage);
                int offsetX = (overlayMessageWidth - hpOverlayMessageWidth - 9) / 2;

                mc.fontRenderer.drawStringWithShadow(hpOverlayMessage, x + offsetX, y + 10, (alpha << 24) + 0xFFFFFF);

                GL11.glColor4f(1f, 1f, 1f, ((float) alpha) / 0xFF);
                ZyinHUDRenderer.RenderCustomTexture(x + offsetX + hpOverlayMessageWidth + 1, y + 9, 16, 0, 9, 9, iconsResourceLocation, 1f);    //black outline of the heart icon
                ZyinHUDRenderer.RenderCustomTexture(x + offsetX + hpOverlayMessageWidth + 1, y + 9, 52, 0, 9, 9, iconsResourceLocation, 1f);    //red interior of the heart icon
                GL11.glColor4f(1f, 1f, 1f, 1f);
            }

            numOverlaysRendered++;
        }
    }


    private static boolean PlayerIsWolfsOwner(WolfEntity wolf) {
        return wolf.isOnSameTeam(mc.player);
    }


    private static String GetOverlayMessageForWitherSkeleton(WitherSkeletonEntity witherSkeleton, float distanceFromMe) {
        String overlayMessage;
        if (witherSkeleton.hasCustomName()) {
            overlayMessage = witherSkeleton.getName().getString();
            overlayMessage += "(";
            overlayMessage += I18n.format("entity.WitherSkeleton.name");
            overlayMessage += ")";
        } else {
            overlayMessage = I18n.format("entity.WitherSkeleton.name");
        }

        //add distance to this wither skeleton into the message
        if (ShowDistanceToPlayers) {
            overlayMessage = TextFormatting.GRAY + "[" + (int) distanceFromMe + "] " + TextFormatting.RESET + overlayMessage;
        }

        return overlayMessage;
    }

    private static String GetOverlayMessageForWolf(WolfEntity wolf, float distanceFromMe) {
        String overlayMessage;

        if (wolf.hasCustomName()) {
            overlayMessage = Objects.requireNonNull(wolf.getCustomName()).getString() + "(" + Localization.get("entity.Wolf.name") + ")";
        } else {
            overlayMessage = Localization.get("entity.Wolf.name");
        }

        //add distance to this wolf into the message
        if (ShowDistanceToPlayers) {
            overlayMessage = TextFormatting.GRAY + "[" + (int) distanceFromMe + "] " + TextFormatting.RESET + overlayMessage;
        }

        return overlayMessage;
    }


    private static String GetOverlayMessageForOtherPlayer(RemoteClientPlayerEntity otherPlayer, float distanceFromMe) {
        String overlayMessage = otherPlayer.getDisplayName().getString();

        //add distance to this player into the message
        if (ShowDistanceToPlayers) {
            //overlayMessage = "[" + (int)distanceFromMe + "] " + overlayMessage;
            overlayMessage = TextFormatting.GRAY + "[" + (int) distanceFromMe + "] " + TextFormatting.RESET + overlayMessage;
        }

        //add special effects based on what the other player is doing
        if (otherPlayer.isSprinting()) {
            overlayMessage = sprintingMessagePrefix + overlayMessage;    //nothing
        }
        if (otherPlayer.isSneaking()) {
            overlayMessage = sneakingMessagePrefix + overlayMessage;    //italics
        }
        if (otherPlayer.isPassenger() || otherPlayer.isElytraFlying())    //this doesn't work on some servers
        {
            overlayMessage = ridingMessagePrefix + overlayMessage;        //space for the saddle and horse armor icons
        }

        return overlayMessage;
    }

    private static void RenderBoatIcon(int x, int y, Item boat) {
        itemRenderer.renderItemIntoGUI(new ItemStack(boat), x, y - 4);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    private static void RenderMinecartIcon(int x, int y) {
        itemRenderer.renderItemIntoGUI(new ItemStack(Items.MINECART), x, y - 4);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    private static void RenderHorseArmorDiamondIcon(int x, int y) {
        itemRenderer.renderItemIntoGUI(new ItemStack(Items.DIAMOND_HORSE_ARMOR), x, y - 4);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    private static void RenderHorseArmorGoldIcon(int x, int y) {
        itemRenderer.renderItemIntoGUI(new ItemStack(Items.GOLDEN_HORSE_ARMOR), x, y - 4);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    private static void RenderHorseArmorIronIcon(int x, int y) {
        itemRenderer.renderItemIntoGUI(new ItemStack(Items.IRON_HORSE_ARMOR), x, y - 4);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    private static void RenderSaddleIcon(int x, int y) {
        itemRenderer.renderItemIntoGUI(new ItemStack(Items.SADDLE), x, y - 4);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    private static void RenderElytraIcon(int x, int y) {
        itemRenderer.renderItemIntoGUI(new ItemStack(Items.ELYTRA), x, y - 4);
        GL11.glDisable(GL11.GL_LIGHTING);
    }
    
	
    /*
    public static double AngleBetweenTwoVectors(Vec3 a, Vec3 b)
    {
        return Math.acos(a.dotProduct(b) / (a.lengthVector() * b.lengthVector()));
    }
    public static double SignedAngleBetweenTwoVectors(Vec3 a, Vec3 b)
    {
    	// Get the angle in degrees between 0 and 180
    	double angle = AngleBetweenTwoVectors(b, a);

    	// the vector perpendicular to referenceForward (90 degrees clockwise)
    	// (used to determine if angle is positive or negative)
    	Vec3 referenceRight = (Vec3.createVectorHelper(0, 1, 0)).crossProduct(a);

    	// Determine if the degree value should be negative.  Here, a positive value
    	// from the dot product means that our vector is the right of the reference vector
    	// whereas a negative value means we're on the left.
    	double sign = (b.dotProduct(referenceRight) > 0.0) ? 1.0: -1.0;

    	return sign * angle;
    }
    */


    /**
     * Gets the status of the Player Locator
     *
     * @return the string "players" if the Player Locator is enabled, otherwise "".
     */
    public static String CalculateMessageForInfoLine() {
        if (Mode == Modes.OFF || !PlayerLocator.Enabled) {
            return "";
        } else if (Mode == Modes.ON) {
            return TextFormatting.WHITE + Localization.get("playerlocator.infoline");
        } else {
            return TextFormatting.WHITE + "???";
        }
    }

    /**
     * Toggle showing the distance to players
     *
     * @return The new Clock mode
     */
    public static boolean ToggleShowDistanceToPlayers() {
        return ShowDistanceToPlayers = !ShowDistanceToPlayers;
    }

    /**
     * Toggle showing the players health
     *
     * @return The new Clock mode
     */
    public static boolean ToggleShowPlayerHealth() {
        return ShowPlayerHealth = !ShowPlayerHealth;
    }

    /**
     * Toggle showing wolves in addition to other players
     *
     * @return The new Clock mode
     */
    public static boolean ToggleShowWolves() {
        return ShowWolves = !ShowWolves;
    }

    /**
     * Toggle using the coler of the wolf's collar to colorize the wolf's name
     *
     * @return The new Clock mode
     */
    public static boolean ToggleUseWolfColors() {
        return UseWolfColors = !UseWolfColors;
    }

    /**
     * Toggle showing wolves in addition to other players
     *
     * @return The new Clock mode
     */
    public static boolean ToggleShowWitherSkeletons() {
        return ShowWitherSkeletons = !ShowWitherSkeletons;
    }

}
