package com.zyin.zyinhud.helper;

import com.zyin.zyinhud.modules.PlayerLocator;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.LocatorOptions;
import com.zyin.zyinhud.util.ZyinHUDUtil;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static com.zyin.zyinhud.helper.EntityTrackerHelper.findEntities;
import static com.zyin.zyinhud.helper.EntityTrackerHelper.playerLocatorMaybeTrack;
import static com.zyin.zyinhud.util.ZyinHUDUtil.doesScreenShowHUD;

/**
 * The EntityTrackerHUDHelper calculates the (x,y) position on the HUD for
 * entities in the game world.
 */
public class HUDEntityTrackerHelper {
	private static final Minecraft mc = Minecraft.getInstance();
	private static final Logger logger = LogManager.getLogger(HUDEntityTrackerHelper.class);
	private static FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
	private static FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);

	/**
	 * Stores world render transform matrices for later use when rendering HUD.
	 */
	public static void storeMatrices() {
		modelMatrix.rewind();
		GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		projMatrix.rewind();
		GL11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projMatrix);
	}

	/**
	 * Send information about the positions of entities to modules that need this
	 * information.
	 * <p>
	 * Place new rendering methods for modules in this function.
	 *
	 * @param entity which entity the information is about
	 * @param x      location on the HUD
	 * @param y      location on the HUD
	 */
	private static void renderEntityInfoOnHUD(Entity entity, int x, int y) {
		PlayerLocator.renderEntityInfoOnHUD(entity, x, y);
	}

	/* Alternative to Entity.getLook() and/or Entity.getLookVec()
	 The resulting vector components are typically the same for at least the first 3 digits after the decimal.
	 */
	@Nonnull
	static Vec3d customPlayerFacing(PlayerEntity player) {
		double pitch = ((player.rotationPitch + 90) * Math.PI) / 180;
		double yaw = ((player.rotationYaw + 90) * Math.PI) / 180;
		return new Vec3d(
			Math.sin(pitch) * Math.cos(yaw), Math.cos(pitch), Math.sin(pitch) * Math.sin(yaw)
		);
	}

	/**
	 * Calculates the on-screen (x,y) positions of entities and renders various
	 * overlays over them.
	 *
	 * @param partialTickTime the partial tick time
	 */
	public static void renderEntityInfo(float partialTickTime) {
		PlayerLocator.resetNumOverlaysRendered();

		if (
			PlayerLocator.isEnabled && PlayerLocator.mode == LocatorOptions.LocatorModes.ON &&
			mc.isGameFocused() && doesScreenShowHUD(mc.currentScreen)
		) {
			PlayerEntity player = mc.player;

			// direction the player is facing
			Vec3d lookDir = player.getLook(partialTickTime);

//			if (mc.gameRenderer.getActiveRenderInfo().isThirdPerson()) {        TODO: is this better??
			// When in the reversed 3rd-person view, flip the look direction
			if (mc.gameSettings.thirdPersonView == 2) {
				lookDir = lookDir.inverse();
			}

			IntBuffer viewport = BufferUtils.createIntBuffer(16);
			GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);

			//iterate over all the loaded Entity objects and find just the entities we are tracking (i.e. other players/wolves/witherskeletons)
			for (Entity entity : findEntities(mc.world, playerLocatorMaybeTrack, logger)) {
				// This shouldn't be necessary, given that the predicate will always fails for null values...
				// But the inspector is being obnoxious, and this shuts it up
				if (entity == null) {continue;}

				// direction to target entity
				Vec3d toEntity = entity.getPositionVec().subtract(player.getPositionVec());

				double dist = Math.sqrt(toEntity.lengthSquared());
				Vec3d toEntityNormal = toEntity.normalize();
				Vec3d targetVec = (lookDir.dotProduct(toEntityNormal) <= 0.02) ?
				                  createDummyTargetLocation(lookDir, toEntityNormal, dist) :
				                  toEntity;

				FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);

				modelMatrix.rewind();
				projMatrix.rewind();

				// map target's entity coordinates into window coordinates
				// using world render transform matrices stored by StoreMatrices()
				ZyinHUDUtil.ProjectionHelper.mapTargetVecToWindowCoords(
					targetVec, modelMatrix, projMatrix, viewport, screenCoords
				);

				renderHudAtScaledCoordinates(entity, screenCoords);
			}
		}
	}

	@Nonnull
	private static Vec3d createDummyTargetLocation(Vec3d lookDir, Vec3d toEntity, double dist) {
		// angle between vectors is greater than about 89 degrees, so
		// create a dummy target location that is 89 degrees away from look direction
		// along the arc between look direction and direction to target entity

		final double angle = 89.0 * Math.PI / 180;
		final double sin = Math.sin(angle);
		final double cos = Math.cos(angle);

		// vector orthogonal to look direction and direction to target entity
		Vec3d ortho = lookDir.crossProduct(toEntity);
		double ox = ortho.x;
		double oy = ortho.y;
		double oz = ortho.z;

		// build a rotation matrix to rotate around a vector (ortho) by an angle (89 degrees)
		// from http://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle
		double m00 = cos + ox * ox * (1 - cos);
		double m01 = ox * oy * (1 - cos) - oz * sin;
		double m02 = ox * oz * (1 - cos) + oy * sin;
		double m10 = oy * ox * (1 - cos) + oz * sin;
		double m11 = cos + oy * oy * (1 - cos);
		double m12 = oy * oz * (1 - cos) - ox * sin;
		double m20 = oz * ox * (1 - cos) - oy * sin;
		double m21 = oz * oy * (1 - cos) + ox * sin;
		double m22 = cos + oz * oz * (1 - cos);

		// transform (multiply) look direction vector with rotation matrix and scale by distance to target entity;
		// this produces the coordinates for the dummy target
		return new Vec3d(
			(float) (dist * (m00 * lookDir.x + m01 * lookDir.y + m02 * lookDir.z)), //x
			(float) (dist * (m10 * lookDir.x + m11 * lookDir.y + m12 * lookDir.z)), //y
			(float) (dist * (m20 * lookDir.x + m21 * lookDir.y + m22 * lookDir.z))  //z
		);
	}

	private static void renderHudAtScaledCoordinates(Entity entity, FloatBuffer screenCoords) {
		MainWindow res = Minecraft.getInstance().mainWindow;
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();

		int hudX = Math.round(screenCoords.get(0)) / (int) res.getGuiScaleFactor();
		int hudY = height - Math.round(screenCoords.get(1)) / (int) res.getGuiScaleFactor();

		// if <hudX, hudY> is outside the screen, scale the coordinates so they're
		// at the edge of the screen (to preserve angle)

		//use X overshoot to scale Y
		int newHudY = calcScaledHudPos(height, width, hudY, hudX);

		//use Y overshoot to scale X
		int newHudX = calcScaledHudPos(width, height, hudX, hudY);

		renderEntityInfoOnHUD(entity, newHudX, newHudY);
	}

	private static int calcScaledHudPos(int scaleDimension, int overshotDimension, int posToScale, int overshotPos) {
		//applied -ax + b == b - ax
		if (overshotPos < 0) {
			return (scaleDimension / 2) + (int) ((posToScale - scaleDimension / 2) / (-(2f * overshotPos / overshotDimension) + 1));
		}
		else if (overshotPos > overshotDimension) {
			return (scaleDimension / 2) + (int) ((posToScale - scaleDimension / 2) / ((2f * overshotPos / overshotDimension) - 1));
		}
		else { return posToScale; }
	}

}
