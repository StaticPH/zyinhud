package com.zyin.zyinhud.helper;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.zyin.zyinhud.modules.ZyinHUDModuleModes;
import com.zyin.zyinhud.util.ZyinHUDUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.zyin.zyinhud.modules.PlayerLocator;

/**
 * The EntityTrackerHUDHelper calculates the (x,y) position on the HUD for
 * entities in the game world.
 */
public class HUDEntityTrackerHelper {
	private static final Minecraft mc = Minecraft.getInstance();
	private static final double pi = Math.PI;
	private static final double twoPi = 2 * Math.PI;
	private static FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
	private static FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);

	/**
	 * Stores world render transform matrices for later use when rendering HUD.
	 */
	public static void StoreMatrices() {
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
	 * @param entity
	 * @param x      location on the HUD
	 * @param y      location on the HUD
	 */
	private static void RenderEntityInfoOnHUD(Entity entity, int x, int y) {
		PlayerLocator.RenderEntityInfoOnHUD(entity, x, y);
	}

	/**
	 * Calculates the on-screen (x,y) positions of entities and renders various
	 * overlays over them.
	 *
	 * @param partialTickTime the partial tick time
	 */
	public static void RenderEntityInfo(float partialTickTime) {
		PlayerLocator.numOverlaysRendered = 0;

		if (PlayerLocator.Enabled && PlayerLocator.Mode == ZyinHUDModuleModes.LocatorOptions.LocatorModes.ON && mc.isGameFocused()) {
			PlayerEntity me = mc.player;

			double meX = me.lastTickPosX + (me.posX - me.lastTickPosX) * partialTickTime;
			double meY = me.lastTickPosY + (me.posY - me.lastTickPosY) * partialTickTime;
			double meZ = me.lastTickPosZ + (me.posZ - me.lastTickPosZ) * partialTickTime;

			double pitch = ((me.rotationPitch + 90) * Math.PI) / 180;
			double yaw = ((me.rotationYaw + 90) * Math.PI) / 180;

			// direction the player is facing
			Vec3d lookDir = new Vec3d(
				Math.sin(pitch) * Math.cos(yaw), Math.cos(pitch), Math.sin(pitch) * Math.sin(yaw)
			);

			// When in the reversed 3rd-person view, flip the look direction
			if (mc.gameSettings.thirdPersonView == 2) {
				lookDir = new Vec3d(lookDir.x * -1, lookDir.y * -1, lookDir.z * -1);
			}

			IntBuffer viewport = BufferUtils.createIntBuffer(16);
			GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);

			// Best guess at a way to iterate through all loaded entities
			// if unmapped name doesnt work, try entitiesById
			//_CHECK: ClientWorld or ServerWorld?
			Int2ObjectMap<Entity> entitiesById =
				ObfuscationReflectionHelper.getPrivateValue(ClientWorld.class, mc.world, "field_217429_b");
			if (entitiesById == null) { return; }

			//iterate over all the loaded Entity objects and find just the entities we are tracking
			for (Entity object : entitiesById.values()) {
				//???: RESEMBLES https://github.com/Vazkii/Neat/blob/master/src/main/java/vazkii/neat/HealthBarRenderer.java#L105

//				if (object == null) { continue; }   already covered by subsequent instanceof checks

				//only track entities that we are tracking (i.e. other players/wolves/witherskeletons)
				if (!(object instanceof RemoteClientPlayerEntity ||
				      object instanceof WolfEntity ||
				      object instanceof WitherSkeletonEntity)) { continue; }

				double entityX = object.lastTickPosX + (object.posX - object.lastTickPosX) * partialTickTime;
				double entityY = object.lastTickPosY + (object.posY - object.lastTickPosY) * partialTickTime;
				double entityZ = object.lastTickPosZ + (object.posZ - object.lastTickPosZ) * partialTickTime;

				// direction to target entity
				Vec3d toEntity = new Vec3d(entityX - meX, entityY - meY, entityZ - meZ);

				float x = (float) toEntity.x;
				float y = (float) toEntity.y;
				float z = (float) toEntity.z;

				double dist = Math.sqrt(toEntity.lengthSquared());
				toEntity = toEntity.normalize();

				if (lookDir.dotProduct(toEntity) <= 0.02) {
					// angle between vectors is greater than about 89 degrees, so
					// create a dummy target location that is 89 degrees away from look direction
					// along the arc between look direction and direction to target entity

					final double angle = 89.0 * pi / 180;
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
					x = (float) (dist * (m00 * lookDir.x + m01 * lookDir.y + m02 * lookDir.z));
					y = (float) (dist * (m10 * lookDir.x + m11 * lookDir.y + m12 * lookDir.z));
					z = (float) (dist * (m20 * lookDir.x + m21 * lookDir.y + m22 * lookDir.z));
				}

				FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);

				modelMatrix.rewind();
				projMatrix.rewind();

				// map target's object coordinates into window coordinates
				// using world render transform matrices stored by StoreMatrices()
				ZyinHUDUtil.ProjectionHelper.mapTargetCoordsToWindowCoords(
					x, y, z, modelMatrix, projMatrix, viewport, screenCoords
				);

				renderHudAtScaledCoordinates(object, screenCoords);
			}
		}
	}

	private static void renderHudAtScaledCoordinates(Entity object, FloatBuffer screenCoords) {
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

		RenderEntityInfoOnHUD(object, newHudX, newHudY);
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
