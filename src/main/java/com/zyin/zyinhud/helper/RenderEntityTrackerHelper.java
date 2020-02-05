package com.zyin.zyinhud.helper;

import com.zyin.zyinhud.modules.AnimalInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

import static com.zyin.zyinhud.helper.EntityTrackerHelper.findEntities;
import static com.zyin.zyinhud.modules.ZyinHUDModuleModes.AnimalInfoOptions.AnimalInfoModes;
import static com.zyin.zyinhud.util.ZyinHUDUtil.doesScreenShowHUD;

/**
 * The RenderEntityTrackerHelper finds entities in the game world.
 */
public class RenderEntityTrackerHelper {
	private static Minecraft mc = Minecraft.getInstance();
	public static final Logger logger = LogManager.getLogger(RenderEntityTrackerHelper.class);
	private static final Predicate<Entity> maybeTrack = (entity) -> (
		entity instanceof LivingEntity && !(entity instanceof PlayerEntity)
	);

	/**
	 * Send information about the positions of entities to modules that need this information.
	 * <p>
	 * Place new rendering methods for modules in this function.
	 *
	 * @param entity
	 * @param partialTickTime
	 */
	private static void RenderEntityInfoInWorld(Entity entity, float partialTickTime) {
		AnimalInfo.RenderEntityInfoInWorld(entity, partialTickTime);
	}

	/**
	 * Calculates the positions of entities in the world and renders various overlays on them.
	 *
	 * @param partialTickTime the partial tick time
	 */
	public static void RenderEntityInfo(float partialTickTime) {
		if ((AnimalInfo.Mode == AnimalInfoModes.ON) && mc.isGameFocused() && doesScreenShowHUD(mc.currentScreen)) {
			//_CHECK: does everything relevant fall under AnimalEntity?
			//        if so, replace these filters with .filter(entity -> (entity instanceof AnimalEntity))
			//        And consider renaming this class to AnimalTrackerHelper,
			//        or even just merging it into AnimalInfo
			//Iterate over all the loaded Entity objects and find just the non-player creatures
			findEntities(mc.world, maybeTrack, logger).forEach(
				entity -> RenderEntityInfoInWorld(entity, partialTickTime)
			);
		}
	}
}
