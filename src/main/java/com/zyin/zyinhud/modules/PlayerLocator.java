package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.config.ZyinHUDConfig;
import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.LocatorOptions;
import com.zyin.zyinhud.util.Localization;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
//import net.minecraft.client.gui.ScaledResolution;
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
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;

import static com.zyin.zyinhud.helper.EntityTrackerHelper.getLocalizedEntityType;
import static com.zyin.zyinhud.helper.EntityTrackerHelper.playerLocatorMaybeTrack;
import static com.zyin.zyinhud.util.ZyinHUDUtil.doesScreenShowHUD;

//TODO: Tamed cats, maybe birds and other creatures?

/**
 * The Player Locator checks for nearby players and displays their name on screen wherever they are.
 */
public class PlayerLocator extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(PlayerLocator.class);

	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled;

	/**
	 * The current mode for this module
	 */
	public static LocatorOptions.LocatorModes mode;

	/**
	 * Shows how far you are from other players next to their name
	 */
	private static boolean showDistanceToPlayers;
	private static boolean showPlayerHealth;
	private static boolean showWitherSkeletons;
	private static boolean showWolves;
	private static boolean useWolfColors;

	private static final ResourceLocation iconsResourceLocation = new ResourceLocation("textures/gui/icons.png");

	private static final String sprintingMessagePrefix = "";
	private static final String sneakingMessagePrefix = TextFormatting.ITALIC.toString();
	private static final String ridingMessagePrefix = "    ";    //space for the saddle/minecart/boat/horse armor icon

	/**
	 * Don't render players that are closer than this
	 */
	private static int viewDistanceCutoff;
	private static final int minViewDistanceCutoff = LocatorOptions.minViewDistanceCutoff;
	private static final int maxViewDistanceCutoff = LocatorOptions.maxViewDistanceCutoff;    //realistic max distance the game will render entities: up to ~115 blocks away

	private static int numOverlaysRendered;
	private static final int maxNumberOfOverlays = 50;    //render only the first nearest 50 players

	static { loadFromConfig(); }

	public static void loadFromConfig() {
		isEnabled = ZyinHUDConfig.enablePlayerLocator.get();
		mode = ZyinHUDConfig.playerLocatorMode.get();
		showDistanceToPlayers = ZyinHUDConfig.showDistanceToPlayers.get();
		showPlayerHealth = ZyinHUDConfig.showPlayerHealth.get();
		showWitherSkeletons = ZyinHUDConfig.showWitherSkeletons.get();
		showWolves = ZyinHUDConfig.showWolves.get();
		useWolfColors = ZyinHUDConfig.useWolfColors.get();
		viewDistanceCutoff = ZyinHUDConfig.playerLocatorMinViewDistance.get();
	}

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enablePlayerLocator.set(!isEnabled);
		ZyinHUDConfig.enablePlayerLocator.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	public static void resetNumOverlaysRendered() {
		numOverlaysRendered = 0;
	}

	/**
	 * Get the type of the horse armor in int type
	 *
	 * @param armor_list List of the armors that player's horse currently have
	 * @return Horse Armor Type
	 */
	public static int getHorseArmorType(Iterable<ItemStack> armor_list) {
		if (armor_list != null && armor_list.iterator().hasNext()) {
			Item armor_single_item = armor_list.iterator().next().getItem();
			if (armor_single_item == Items.LEATHER_HORSE_ARMOR) { return 1; }
			else if (armor_single_item == Items.IRON_HORSE_ARMOR) { return 2; }
			else if (armor_single_item == Items.GOLDEN_HORSE_ARMOR) { return 3; }
			else { return armor_single_item == Items.DIAMOND_HORSE_ARMOR ? 4 : 0; }
		}
		else { return 0; }
	}

	/**
	 * Renders nearby players's names on the screen.
	 *
	 * @param entity the entity
	 * @param x      location on the HUD
	 * @param y      location on the HUD
	 */
	public static void renderEntityInfoOnHUD(Entity entity, int x, int y) {
		if (numOverlaysRendered > maxNumberOfOverlays) { return; }

		//we only care about other players and wolves( and wither skeletons)
		if (!(playerLocatorMaybeTrack.test(entity))) { return; }

		//if the player is in the world
		//and not looking at a menu
		//and F3 not pressed
		if (
			PlayerLocator.isEnabled && mode == LocatorOptions.LocatorModes.ON &&
			(mc.mouseHelper.isMouseGrabbed() || doesScreenShowHUD(mc.currentScreen)) &&
			!mc.gameSettings.showDebugInfo
		) {

			//only show entities that are close by
			float distanceFromMe = mc.player.getDistance(entity);

			//don't render ourselves!
			if (distanceFromMe > maxViewDistanceCutoff || distanceFromMe < viewDistanceCutoff || distanceFromMe == 0) {
				return;
			}
			//make room for any icons we will need space to render later
			String overlayMessage = entity.getRidingEntity() != null ? "    " : "";

			int rgb = 0xFFFFFF;
			//calculate the color of the overlayMessage based on the distance from me
			int alpha = (int) (0x55 + 0xAA * ((maxViewDistanceCutoff - distanceFromMe) / maxViewDistanceCutoff));

			if (entity instanceof RemoteClientPlayerEntity) {
				overlayMessage = getOverlayMessageForEntity(entity, distanceFromMe);

				//format the string to be the same color as that persons team color
				ScorePlayerTeam team = (ScorePlayerTeam) entity.getTeam();
				if (team != null) { overlayMessage = team.getName(); }
			}
			else if (entity instanceof WolfEntity) {
				if (!showWolves || !playerIsWolfsOwner((WolfEntity) entity)) { return; }

				overlayMessage = getOverlayMessageForEntity(entity, distanceFromMe);

				if (useWolfColors) {
					DyeColor collarColor = ((WolfEntity) entity).getCollarColor();
					float[] dyeRGBColors = SheepEntity.getDyeRgb(collarColor);

					int r = (int) (dyeRGBColors[0] * 255);
					int g = (int) (dyeRGBColors[1] * 255);
					int b = (int) (dyeRGBColors[2] * 255);
					rgb = (r << 4 * 4) + (g << 4 * 2) + b;    //actual collar color

					r = (0xFF - r) / 2;
					g = (0xFF - g) / 2;
					b = (0xFF - b) / 2;
					rgb = rgb + ((r << 4 * 4) + (g << 4 * 2) + b);    //a more white version of the collar color
				}
			}
			else if (entity instanceof WitherSkeletonEntity) {
				if (!showWitherSkeletons) { return; }

				overlayMessage = getOverlayMessageForEntity(entity, distanceFromMe);
//				y*=entity.getHeight();      FIXME: what must I do to get this text to render somewhere reasonable >.<
				rgb = 0x555555;
				alpha = alpha / 6;
			}
			//Else some kind of hacky nonsense is taking place

			//the width in pixels of the message
			int overlayMessageWidth = mc.fontRenderer.getStringWidth(overlayMessage);
			int width = mc.mainWindow.getScaledWidth();        //~427
			int height = mc.mainWindow.getScaledHeight();        //~240

			//center the text horizontally over the entity
			x -= overlayMessageWidth / 2;

			//check if the text is attempting to render outside of the screen, and if so, fix it to snap to the edge of the screen.
			x = Math.max(Math.min(x, width - overlayMessageWidth), 0);
			y = (y > height - 10 && !showPlayerHealth) ? height - 10 : y;
			y = (y > height - 20 && showPlayerHealth) ? height - 20 : y;
			if (
				y < 10 && InfoLine.getVerticalLocation() <= 1 &&
				(x > InfoLine.getHorizontalLocation() + mc.fontRenderer.getStringWidth(InfoLine.getInfoLineMessage()) ||
				 x < InfoLine.getHorizontalLocation() - overlayMessageWidth)
			) {
				//if the text is to the right or left of the info line then allow it to render in that open space
				y = Math.max(y, 0);
			}
			else {
				//use 10 instead of 0 so that we don't write text onto the top left InfoLine message area
				y = Math.max(y, 10);
			}


			//render the overlay message
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			int color = (alpha << 24) + rgb;    //alpha:r:g:b, (alpha << 24) turns it into the format: 0x##000000
			mc.fontRenderer.drawStringWithShadow(overlayMessage, x, y, color);

			//also render whatever the player is currently riding on
			if (entity.isPassenger()) {
				renderRidingIcon(entity, x, y);
			}
			else if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isElytraFlying()) {
				renderElytraIcon(x, y);
			}

			//if showing player health is turned on, render the hp and a heart icon under their name
			//but don't show health for mobs, such as Wither Skeletons
			if (showPlayerHealth && !(entity instanceof MonsterEntity)) {
				showOtherPlayerHealth((LivingEntity) entity, x, y, alpha, overlayMessageWidth);
			}

			numOverlaysRendered++;
		}
	}

	private static void showOtherPlayerHealth(LivingEntity entity, int x, int y, int alpha, int overlayMessageWidth) {
		int numHearts = (int) ((entity.getHealth() + 1) / 2);
		String hpOverlayMessage = numHearts + "";

		int hpOverlayMessageWidth = mc.fontRenderer.getStringWidth(hpOverlayMessage);
		int offsetX = (overlayMessageWidth - hpOverlayMessageWidth - 9) / 2;

		mc.fontRenderer.drawStringWithShadow(hpOverlayMessage, x + offsetX, y + 10, (alpha << 24) + 0xFFFFFF);

		GL11.glColor4f(1f, 1f, 1f, ((float) alpha) / 0xFF);
		//black outline of the heart icon
		ZyinHUDRenderer.renderCustomTexture(
			x + offsetX + hpOverlayMessageWidth + 1, y + 9, 16, 0, 9, 9, iconsResourceLocation, 1f
		);
		//red interior of the heart icon
		ZyinHUDRenderer.renderCustomTexture(
			x + offsetX + hpOverlayMessageWidth + 1, y + 9, 52, 0, 9, 9, iconsResourceLocation, 1f
		);
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}

	private static void renderRidingIcon(Entity entity, int x, int y) {
		if (entity.getRidingEntity() instanceof HorseEntity) {
			renderHorseArmorOrSaddleIcon((HorseEntity) (entity.getRidingEntity()), x, y);
		}
		if (entity.getRidingEntity() instanceof PigEntity) {
			renderSaddleIcon(x, y);
		}
		else if (entity.getRidingEntity() instanceof AbstractMinecartEntity) {
			renderItemIconIntoGUI(
				((AbstractMinecartEntity) entity.getRidingEntity()).getCartItem(), x, y
			);
		}
		else if (entity.getRidingEntity() instanceof BoatEntity) {
			renderItemIconIntoGUI(((BoatEntity) entity.getRidingEntity()).getItemBoat(), x, y);
		}
	}

	/**
	 * If the horse is wearing anything at all in its armor slot, render that Item's icon.
	 * If the horse is saddled, but the armor slot is empty, render the icon for a saddle.
	 * If both the horse's saddle slot and armor slot are empty, do nothing.
	 *
	 * @param horse the HorseEntity for which to render the icon
	 * @param x
	 * @param y
	 */
	@ParametersAreNonnullByDefault
	private static void renderHorseArmorOrSaddleIcon(HorseEntity horse, int x, int y) {
		Iterator<ItemStack> armorIter = horse.getArmorInventoryList().iterator();
		if (armorIter.hasNext()) {
			renderItemIconIntoGUI(armorIter.next(), x, y);
		}
		else if (horse.isHorseSaddled()) {
			renderSaddleIcon(x, y);
		}
	}

	//FIXME?: this doesnt seem correct
	private static boolean playerIsWolfsOwner(WolfEntity wolf) {
		return wolf.isOnSameTeam(mc.player);
	}

	@Nonnull
	private static String getOverlayMessageForEntity(Entity entity, float distanceFromMe) {
		String overlayMessage = "";

		//add distance to this entity into the message
		if (showDistanceToPlayers) {
			overlayMessage = TextFormatting.GRAY + "[" + (int) distanceFromMe + "] " + TextFormatting.RESET;
		}

		if (entity instanceof RemoteClientPlayerEntity) {
			return finishOverlayForOtherPlayer((RemoteClientPlayerEntity) entity, overlayMessage);
		}
		else if (entity.hasCustomName()) {
			assert entity.getCustomName() != null;
			return overlayMessage + entity.getCustomName().getString() + '(' + getLocalizedEntityType(entity) + ')';
		}
		else {
			return overlayMessage + getLocalizedEntityType(entity);
		}
	}

	private static String finishOverlayForOtherPlayer(RemoteClientPlayerEntity otherPlayer, String baseMsg) {
		String overlayMessage = baseMsg + otherPlayer.getDisplayName().getString();

		//add special effects based on what the other player is doing
		if (otherPlayer.isSprinting()) {
			overlayMessage = sprintingMessagePrefix + overlayMessage;    //nothing
		}
		if (otherPlayer.isSneaking()) {
			overlayMessage = sneakingMessagePrefix + overlayMessage;    //italics
		}
		if (otherPlayer.isPassenger() || otherPlayer.isElytraFlying()) {   //this doesn't work on some servers
			overlayMessage = ridingMessagePrefix + overlayMessage;        //space for the saddle and other icons
		}

		return overlayMessage;
	}

	private static void renderItemIconIntoGUI(ItemStack itemStack, int x, int y) {
		itemRenderer.getValue().renderItemIntoGUI(itemStack, x, y - 4);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	private static void renderItemIconIntoGUI(Item item, int x, int y) {
		renderItemIconIntoGUI(new ItemStack(item), x, y);
	}

	private static void renderSaddleIcon(int x, int y) {
		renderItemIconIntoGUI(new ItemStack(Items.SADDLE), x, y);
	}

	private static void renderElytraIcon(int x, int y) {
		renderItemIconIntoGUI(new ItemStack(Items.ELYTRA), x, y);
	}

    /*
    public static double angleBetweenTwoVectors(Vec3 a, Vec3 b)
    {
        return Math.acos(a.dotProduct(b) / (a.lengthVector() * b.lengthVector()));
    }
    public static double signedAngleBetweenTwoVectors(Vec3 a, Vec3 b)
    {
    	// Get the angle in degrees between 0 and 180
    	double angle = angleBetweenTwoVectors(b, a);

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
	public static String calculateMessageForInfoLine() {
		if (mode == LocatorOptions.LocatorModes.OFF || !PlayerLocator.isEnabled) { return ""; }
		else if (mode == LocatorOptions.LocatorModes.ON) {
			return TextFormatting.WHITE + Localization.get("playerlocator.infoline");
		}
		else { return TextFormatting.WHITE + "???"; }
	}

	/**
	 * Toggle showing the distance to players
	 *
	 * @return The new Clock mode
	 */
	public static boolean toggleShowDistanceToPlayers() {
		return showDistanceToPlayers = !showDistanceToPlayers;
	}

	/**
	 * Toggle showing the players health
	 *
	 * @return The new Clock mode
	 */
	public static boolean toggleShowPlayerHealth() {
		return showPlayerHealth = !showPlayerHealth;
	}

	/**
	 * Toggle showing wolves in addition to other players
	 *
	 * @return The new Clock mode
	 */
	public static boolean toggleShowWolves() {
		return showWolves = !showWolves;
	}

	/**
	 * Toggle using the coler of the wolf's collar to colorize the wolf's name
	 *
	 * @return The new Clock mode
	 */
	public static boolean toggleUseWolfColors() {
		return useWolfColors = !useWolfColors;
	}

	/**
	 * Toggle showing wolves in addition to other players
	 *
	 * @return The new Clock mode
	 */
	public static boolean toggleShowWitherSkeletons() {
		return showWitherSkeletons = !showWitherSkeletons;
	}
}
