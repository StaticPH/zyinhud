package com.zyin.zyinhud.util;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.renderer.ItemRenderer;
//import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.stream.Stream;

/**
 * General utility class for ZyinHUD.
 */
public class ZyinHUDUtil {
//	private static final ItemRenderer itemRenderer = mc.getItemRenderer();
//	private static final TextureManager textureManager = mc.getTextureManager();
	private static final Method itemUseMethod =     // the private method: rightClickMouse()
		ObfuscationReflectionHelper.findMethod(Minecraft.class, "func_147121_ag");
	private static Minecraft mc = Minecraft.getInstance();

	public static boolean doesScreenShowHUD(Screen screen) {
		return (screen == null || screen instanceof ChatScreen || screen instanceof DeathScreen);
	}

	/***
	 * Determines if something will happen if you right click on the block the
	 * player is currently looking at
	 *
	 * @return boolean
	 */
	public static boolean isMouseoveredBlockRightClickable() {
		if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
			Block block = getMousedOverBlock();

			return ZyinHUDUtil.isBlockRightClickable(block.getClass());
		}
		return false;
	}

	/**
	 * Determines if something will happen if you right click a block, without holding any particular item
	 *
	 * @param block the block
	 * @return boolean
	 */
	public static boolean isBlockRightClickable(Class<? extends Block> block) {
		//couldn't find a way to see if a block is 'right click-able' without running the onBlockActivated() method
		//for that block, which we don't want to do
		/*Consider adding:
			ScaffoldingBlock
			LoomBlock
			GrindstoneBlock
			FenceGateBlock
			DragonEggBlock
			ComposterBlock
			CauldronBlock
			BushBlock
			CartographyTableBlock
		 */
		/*
        (we dont actually care about end portal, flower pot, piston moving, skull, maybe sign, and probably mob spawner,
        but this way is more convenient, and reduces the number of required imports)
		ContainerBlock = beacons, brewing stand, chest, command block, daylight detector, dispenser, enchantment table,
		                 ender chest, end portal, flower pot, furnace, hopper, jukebox, mob spawner, note block,
		                 piston moving, sign, skull
		RedstoneDiodeBlock = repeaters + comparators
		 */
		return Stream.of(
			ContainerBlock.class, AbstractButtonBlock.class, LeverBlock.class, RedstoneDiodeBlock.class,
			DoorBlock.class, AnvilBlock.class, BedBlock.class, CakeBlock.class, FenceGateBlock.class,
			TrapDoorBlock.class, CraftingTableBlock.class
		).anyMatch((blockType) -> blockType.isAssignableFrom(block));
	}

	/**
	 * Gets a protected/private field from a class using reflection.
	 *
	 * @param <T>           The return type of the field you are getting
	 * @param <E>           The class the field is in
	 * @param classToAccess The ".class" of the class the field is in
	 * @param instance      The instance of the class
	 * @param fieldNames    comma separated names the field may have (i.e. obfuscated, non obfuscated). Obfustated field names can be found in %USERPROFILE%\.gradle\caches\minecraft\de\oceanlabs\mcp\...\fields.csv
	 * @return t
	 */
	@SuppressWarnings("unchecked")
	public static <T, E> T getFieldByReflection(Class<? super E> classToAccess, E instance, String... fieldNames) {
		Field field = null;
		for (String fieldName : fieldNames) {
			try { field = classToAccess.getDeclaredField(fieldName); }
			catch (NoSuchFieldException ignored) { /* Do nothing */ }

			if (field != null) { break; }
		}

		if (field != null) {
			field.setAccessible(true);
			T fieldT = null;
			try { fieldT = (T) field.get(instance); }
			catch (IllegalArgumentException | IllegalAccessException ignored) { /* Do nothing */ }

			return fieldT;
		}

		return null;
	}

	/**
	 * Get mouse overed block.
	 *
	 * @return the block
	 */
	public static Block getMousedOverBlock() {
		int x = (int) mc.objectMouseOver.getHitVec().getX();
		int y = (int) mc.objectMouseOver.getHitVec().getY();
		int z = (int) mc.objectMouseOver.getHitVec().getZ();
		return getBlock(x, y, z);
	}

	/**
	 * Get mouse overed block pos block pos.
	 *
	 * @return the block pos
	 */
	@Nonnull
	public static BlockPos getMousedOverBlockPos() {
		int x = (int) mc.objectMouseOver.getHitVec().getX();
		int y = (int) mc.objectMouseOver.getHitVec().getY();
		int z = (int) mc.objectMouseOver.getHitVec().getZ();
		return new BlockPos(x, y, z);
	}

	/**
	 * Get block block.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the block
	 */
	public static Block getBlock(int x, int y, int z) {
		return getBlock(new BlockPos(x, y, z));
	}

	/**
	 * Get block block.
	 *
	 * @param pos the pos
	 * @return the block
	 */
	@CheckForNull
	public static Block getBlock(BlockPos pos) {
		BlockState blockState = getBlockState(pos);
		if (blockState == null) { return null; }
		else { return blockState.getBlock(); }
	}

	/**
	 * Get block state block state.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the block state
	 */
	public static BlockState getBlockState(int x, int y, int z) {
		return getBlockState(new BlockPos(x, y, z));
	}

	/**
	 * Get block state block state.
	 *
	 * @param pos the pos
	 * @return the block state
	 */
	@CheckForNull
	public static BlockState getBlockState(BlockPos pos) {
		if (mc.world != null) { return mc.world.getBlockState(pos); }
		else { return null; }
	}

	@Nonnull
	public static String bindingToKeyName(@Nonnull KeyBinding key) {
		String s = key.getTranslationKey();
		return s.substring(1 + s.lastIndexOf('.')).toUpperCase();
	}

	public static void useItem() {
		try { itemUseMethod.invoke(mc); }
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static <T> boolean objectEqualsAnyOf(Object object, T[] possibilities) {
		for (T thing : possibilities) {
			if (object.equals(thing)) { return true; }
		}
		return false;
	}

	/**
	 * Extremely minimal Object for holding a <tt>double[]</tt> that must always contain exactly 3 values
	 * The only available methods are the static <tt>create</tt> method, whose parameters cannot be null,
	 * and the <tt>get</tt> method, which returns the internal double[].
	 */
	@ParametersAreNonnullByDefault
	public static final class Array3d {
		@Nonnull
		private final double[] array;

		private Array3d(double a, double b, double c) {
			this.array = new double[]{a, b, c};
		}

		/**
		 * Creates and returns a new Array3d instance
		 *
		 * @return the new Array3d instance
		 */
		@Nonnull
		public static Array3d create(double a, double b, double c) {
			return new Array3d(a, b, c);
		}

		/**
		 * @return the internal double[]
		 */
		public double[] get() {return this.array;}
	}

	// Because lwjgl3 doesnt provide glu anymore, we're just going to have to provide the functions we needed ourselves,
	// along with some wrapper calls for convenience
	public static class ProjectionHelper {
		private static final float[] in = new float[4];
		private static final float[] out = new float[4];

		/**
		 * This method is functionally identical to <tt>org.lwjgl.util.glu.GLU.__gluMultMatrixVecf</tt> from <tt>lwjgl2</tt>
		 *
		 * @param matrix
		 * @param in
		 * @param out
		 */
		@SuppressWarnings("PointlessArithmeticExpression")
		public static void multMatrixVecf(FloatBuffer matrix, float[] in, float[] out) {
			for (int i = 0; i < 4; i++) {
				out[i] = in[0] * matrix.get(matrix.position() + i + 0) +
				         in[1] * matrix.get(matrix.position() + i + 4) +
				         in[2] * matrix.get(matrix.position() + i + 8) +
				         in[3] * matrix.get(matrix.position() + i + 12);
			}
		}

		/**
		 * This method is functionally identical to <tt>org.lwjgl.util.glu.GLU.gluProject</tt> from <tt>lwjgl2</tt>
		 *
		 * @param objx
		 * @param objy
		 * @param objz
		 * @param modelMatrix
		 * @param projMatrix
		 * @param viewport
		 * @param win_pos
		 * @return
		 */
		@SuppressWarnings("PointlessArithmeticExpression")
		public static boolean mapTargetCoordsToWindowCoords(
			float objx, float objy, float objz,
			FloatBuffer modelMatrix, FloatBuffer projMatrix,
			IntBuffer viewport, FloatBuffer win_pos
		) {
			float[] in = ProjectionHelper.in;
			float[] out = ProjectionHelper.out;

			in[0] = objx;
			in[1] = objy;
			in[2] = objz;
			in[3] = 1.0f;

			multMatrixVecf(modelMatrix, in, out);
			multMatrixVecf(projMatrix, out, in);

			if (in[3] == 0.0) { return false; }

			in[3] = 0.5f / in[3]; //(1.0f / in[3]) * 0.5f;
			// Map x, y and z to range 0-1
			in[0] = (in[0] * in[3]) + 0.5f;
			in[1] = (in[1] * in[3]) + 0.5f;
			in[2] = (in[2] * in[3]) + 0.5f;

			// Map x,y to viewport
			win_pos.put(0, (in[0] * viewport.get(viewport.position() + 2)) + viewport.get(viewport.position() + 0))
			       .put(1, (in[1] * viewport.get(viewport.position() + 3)) + viewport.get(viewport.position() + 1))
			       .put(2, in[2]);

			return true;
		}

		public static boolean mapTargetCoordsToWindowCoords(
			@Nonnull Array3d coords,
			FloatBuffer modelMatrix, FloatBuffer projMatrix,
			IntBuffer viewport, FloatBuffer win_pos
		) {
			return mapTargetCoordsToWindowCoords(
				(float) coords.array[0], (float) coords.array[1], (float) coords.array[2],
				modelMatrix, projMatrix, viewport, win_pos
			);
		}

		public static boolean mapTargetVecToWindowCoords(
			@Nonnull Vec3d targetVec,
			FloatBuffer modelMatrix, FloatBuffer projMatrix,
			IntBuffer viewport, FloatBuffer win_pos
		) {
			return mapTargetCoordsToWindowCoords(
				(float) targetVec.x, (float) targetVec.y, (float) targetVec.z,
				modelMatrix, projMatrix, viewport, win_pos
			);
		}
	}
}
