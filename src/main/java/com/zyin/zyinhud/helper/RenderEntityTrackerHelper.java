package com.zyin.zyinhud.helper;

import com.zyin.zyinhud.modules.AnimalInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.zyin.zyinhud.modules.ZyinHUDModuleModes.AnimalInfoOptions.AnimalInfoModes;
import static com.zyin.zyinhud.util.ZyinHUDUtil.doesScreenShowHUD;

/**
 * The RenderEntityTrackerHelper finds entities in the game world.
 */
public class RenderEntityTrackerHelper {
	private static Minecraft mc = Minecraft.getInstance();
	public static final Logger logger = LogManager.getLogger(RenderEntityTrackerHelper.class);

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
			// Best guess at a way to iterate through all loaded entities
			// if mapped name doesnt work, try field_217429_b
			//_CHECK: ClientWorld or ServerWorld?
			Int2ObjectMap<Entity> entitiesById =
				ObfuscationReflectionHelper.getPrivateValue(ClientWorld.class, mc.world, "entitiesById");

			if (entitiesById == null) { return;}

			//Iterate over all the loaded Entity objects and find just the non-player creatures
			entitiesById.values().stream()
//			            .filter(entity-> !(entity instanceof AnimalEntity || entity instanceof VillagerEntity))
			            //_CHECK: does everything relevant fall under AnimalEntity?
			            //        if so, replace these filters with .filter(entity -> (entity instanceof AnimalEntity))
			            //        And consider renaming this class to AnimalTrackerHelper,
			            //        or even just merging it into AnimalInfo
			            .filter(entity-> (entity instanceof LivingEntity))
			            .filter(entity-> !(entity instanceof PlayerEntity))
//			            .peek(entity-> logger.info(
//				            "Found entity UUID:{}  other:{}",
//				            entity.getCachedUniqueIdString(),
//				            entity.toString()
//			            ))
			            .forEach(entity -> RenderEntityInfoInWorld(entity, partialTickTime));
		}
	}
}
