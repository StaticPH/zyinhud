package com.zyin.zyinhud.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.stream.Stream;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * General utility class for ZyinHUD.
 */
public class ZyinHUDUtil {
	protected static Minecraft mc = Minecraft.getInstance();
	protected static final ItemRenderer itemRenderer = mc.getItemRenderer();
	protected static final TextureManager textureManager = mc.getTextureManager();
	private static final Method itemUseMethod =
		ObfuscationReflectionHelper.findMethod(Minecraft.class, "func_147121_ag"); // the private method: rightClickMouse()

	/***
	 * Determines if something will happen if you right click on the block the
	 * player is currently looking at
	 *
	 * @return boolean
	 */
	public static boolean IsMouseoveredBlockRightClickable() {
		if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
			Block block = GetMouseOveredBlock();

			return ZyinHUDUtil.IsBlockRightClickable(block.getClass());
		}
		return false;
	}

	/**
	 * Determines if something will happen if you right click a block, without holding any particular item
	 *
	 * @param block the block
	 * @return boolean
	 */
	public static boolean IsBlockRightClickable(Class<? extends Block> block) {
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
	public static <T, E> T GetFieldByReflection(Class<? super E> classToAccess, E instance, String... fieldNames) {
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
	public static Block GetMouseOveredBlock() {
		int x = (int) mc.objectMouseOver.getHitVec().getX();
		int y = (int) mc.objectMouseOver.getHitVec().getY();
		int z = (int) mc.objectMouseOver.getHitVec().getZ();
		return GetBlock(x, y, z);
	}

	/**
	 * Get mouse overed block pos block pos.
	 *
	 * @return the block pos
	 */
	@Nonnull
	public static BlockPos GetMouseOveredBlockPos() {
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
	public static Block GetBlock(int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		return GetBlock(pos);
	}

	/**
	 * Get block block.
	 *
	 * @param pos the pos
	 * @return the block
	 */
	@Nullable
	public static Block GetBlock(BlockPos pos) {
		BlockState blockState = GetBlockState(pos);
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
	public static BlockState GetBlockState(int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		return GetBlockState(pos);
	}

	/**
	 * Get block state block state.
	 *
	 * @param pos the pos
	 * @return the block state
	 */
	@Nullable
	public static BlockState GetBlockState(BlockPos pos) {
		if (mc.world != null) { return mc.world.getBlockState(pos); }
		else { return null; }
	}

	public static String bindingToKeyName(KeyBinding key) {
		String s = key.getTranslationKey();
		return s.substring(1 + s.lastIndexOf('.')).toUpperCase();
	}

	public static void useItem(){
		try { itemUseMethod.invoke(mc); }
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	// Because lwjgl3 doesnt provide glu anymore, we're just going to have to provide the function we needed ourselves
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
	}
}
