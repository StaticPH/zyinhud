package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.DistanceMeasurerOptions;
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
public class DistanceMeasurer extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled = ZyinHUDConfig.EnableDistanceMeasurer.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		ZyinHUDConfig.EnableDistanceMeasurer.set(!Enabled);
		ZyinHUDConfig.EnableDistanceMeasurer.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return Enabled = !Enabled;
	}

	/**
	 * The current mode for this module
	 */
	public static DistanceMeasurerOptions.DistanceMeasurerModes Mode = ZyinHUDConfig.DistanceMeasurerMode.get();


	/**
	 * Render onto hud.
	 */
	public static void RenderOntoHUD() {
		//if the player is in the world
		//and not looking at a menu
		//and F3 not pressed
		if (DistanceMeasurer.Enabled &&
		    Mode != DistanceMeasurerOptions.DistanceMeasurerModes.OFF &&
		    !mc.gameSettings.showDebugInfo &&
		    (mc.mouseHelper.isMouseGrabbed() || ((mc.currentScreen instanceof ChatScreen)))
		) {
			String distanceString = CalculateDistanceString();

			int width = mc.mainWindow.getScaledWidth();
			int height = mc.mainWindow.getScaledHeight();
			int distanceStringWidth = mc.fontRenderer.getStringWidth(distanceString);

			mc.fontRenderer.drawStringWithShadow(
				distanceString, (width / 2.0f) - (distanceStringWidth / 2.0f), (height / 2.0f) - 10, 0xffffff
			);
		}
	}


	/**
	 * Calculates the distance of the block the player is pointing at
	 *
	 * @return the distance to a block if Distance Measurer is enabled, otherwise "".
	 */
	@SuppressWarnings("ConstantConditions")
	protected static String CalculateDistanceString() {
//        RayTraceResult objectMouseOver = mc.player.rayTrace(300.0d, 1.0f, RayTraceFluidMode.ALWAYS);
		// If the third parameter of "func_213324_a" here is true, the raytrace will use RayTraceContext.FluidMode.ANY
		// see DebugOverlayGui:rayTraceFluid
		RayTraceResult objectMouseOver = mc.player.func_213324_a(300.0d, 1.0f, true);

		if (objectMouseOver != null && objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
			if (Mode == DistanceMeasurerOptions.DistanceMeasurerModes.SIMPLE) {
				double playerX = mc.player.posX;
				double playerY = mc.player.posY + mc.player.getEyeHeight();
				double playerZ = mc.player.posZ;
				//Might be able to replace this pretty much this entire block with something
				// like mc.player.getPositionVector().distanceTo(mc.player.getLookVec());
				double blockX = objectMouseOver.getHitVec().x;  //might also work with Entity.*look* returns some Vec?
				double blockY = objectMouseOver.getHitVec().y;  //EX: mc.player.getLookVec().getY();
				double blockZ = objectMouseOver.getHitVec().z;

				double deltaX;
				double deltaY;
				double deltaZ;

				if (playerX < blockX) { deltaX = blockX - playerX; }
				else if (playerX > blockX + 0.5) { deltaX = playerX - blockX; }
				else { deltaX = playerX - blockX; }

				if (playerY < blockY) { deltaY = blockY - playerY; }
				else if (playerY > blockY) { deltaY = playerY - blockY; }
				else { deltaY = playerY - blockY; }

				if (playerZ < blockZ) { deltaZ = blockZ - playerZ; }
				else if (playerZ > blockZ) { deltaZ = playerZ - blockZ; }
				else { deltaZ = playerZ - blockZ; }

				double farthestHorizontalDistance = Math.max(Math.abs(deltaX), Math.abs(deltaZ));
				double farthestDistance = Math.max(Math.abs(deltaY), farthestHorizontalDistance);

				return TextFormatting.GOLD + "[" + String.format("%1$,.1f", farthestDistance) + ']';
			}
			else if (Mode == DistanceMeasurerOptions.DistanceMeasurerModes.COORDINATE) {
				BlockPos pos = ((BlockRayTraceResult) objectMouseOver).getPos();

				return TextFormatting.GOLD + "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ']';
			}
			else { return TextFormatting.GOLD + "[???]"; }
		}
		else { return TextFormatting.GOLD + "[" + Localization.get("distancemeasurer.far") + ']'; }
	}
}