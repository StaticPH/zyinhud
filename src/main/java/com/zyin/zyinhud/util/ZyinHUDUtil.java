package com.zyin.zyinhud.util;

import java.lang.reflect.Field;

import net.minecraft.block.*;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.IIcon;
import net.minecraft.util.math.RayTraceResult;

/**
 * General utility class for ZyinHUD.
 */
public class ZyinHUDUtil
{
	/**
	 * The constant mc.
	 */
	protected static Minecraft mc = Minecraft.getInstance();
	/**
	 * The constant itemRenderer.
	 */
	protected static final ItemRenderer itemRenderer = mc.getItemRenderer();
	/**
	 * The constant textureManager.
	 */
	protected static final TextureManager textureManager = mc.getTextureManager();

	/***
	 * Determines if something will happen if you right click on the block the
	 * player is currently looking at
	 *
	 * @return boolean
	 */
	public static boolean IsMouseoveredBlockRightClickable() {
		if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
			Block block = GetMouseOveredBlock();

			return ZyinHUDUtil.IsBlockRightClickable(block);
        }
        return false;
	}

	/**
	 * Determines if something will happen if you right click a block
	 *
	 * @param block the block
	 * @return boolean
	 */
	public static boolean IsBlockRightClickable(Block block)
	{
        //couldn't find a way to see if a block is 'right click-able' without running the onBlockActivated() method
        //for that block, which we don't want to do
        return block instanceof ContainerBlock    //BlockContainer = beacons, brewing stand, chest, command block, daylight detector, dispenser, enchantment table, ender chest, end portal, flower pot, furnace, hopper, jukebox, mob spawner, note block, piston moving, sign, skull
               || block instanceof AbstractButtonBlock
               || block instanceof LeverBlock
               || block instanceof RedstoneDiodeBlock    //BlockRedstoneDiode = repeaters + comparators
               || block instanceof DoorBlock
               || block instanceof AnvilBlock
               || block instanceof BedBlock
               || block instanceof CakeBlock
               || block instanceof FenceGateBlock
               || block instanceof TrapDoorBlock
               || block instanceof CraftingTableBlock;
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
			try
			{
				field = classToAccess.getDeclaredField(fieldName);
			}
			catch(NoSuchFieldException ignored){}
			
			if(field != null)
				break;
	    }
		
		if(field != null) {
			field.setAccessible(true);
			T fieldT = null;
			try {
				fieldT = (T) field.get(instance);
			} catch (IllegalArgumentException | IllegalAccessException ignored) {
			}

			return fieldT;
		}

		return null;
	}

	/**
	 * Get mouse overed block block.
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
	public static Block GetBlock(BlockPos pos) {
		BlockState blockState = GetBlockState(pos);
		if (blockState == null)
			return null;
		else
			return blockState.getBlock();
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
	public static BlockState GetBlockState(BlockPos pos) {
		if(mc.world != null)
			return mc.world.getBlockState(pos);
		else
    		return null;
    }


}
