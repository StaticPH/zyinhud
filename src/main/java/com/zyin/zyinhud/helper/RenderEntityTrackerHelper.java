package com.zyin.zyinhud.helper;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;

import com.zyin.zyinhud.modules.AnimalInfo;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static com.zyin.zyinhud.modules.ZyinHUDModuleModes.AnimalInfoOptions.*;

/**
 * The RenderEntityTrackerHelper finds entities in the game world.
 */
public class RenderEntityTrackerHelper {
	private static Minecraft mc = Minecraft.getInstance();

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
		if ((AnimalInfo.Mode == AnimalInfoModes.ON) && mc.isGameFocused()) {
			// Best guess at a way to iterate through all loaded entities
			// if mapped name doesnt work, try field_217429_b
			//_CHECK: ClientWorld or ServerWorld?
			Int2ObjectMap<Entity> entitiesById =
				ObfuscationReflectionHelper.getPrivateValue(ClientWorld.class, mc.world, "entitiesById");

			if (entitiesById == null) { return;}

			//iterate over all the loaded Entity objects and find just the players
			entitiesById.values().stream()
			            .filter(entity-> !(entity instanceof AnimalEntity || entity instanceof VillagerEntity))
			            .forEach(entity -> RenderEntityInfoInWorld(entity, partialTickTime));
		}
	}
}
