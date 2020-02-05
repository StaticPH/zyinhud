package com.zyin.zyinhud.helper;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.zyin.zyinhud.ZyinHUDConfig.EnableLoggingAllEntitiesFound;

public class EntityTrackerHelper {
	@Nonnull
	public static Collection<Entity> findEntities(ClientWorld clientWorld) {
		return findEntities(clientWorld, (entity -> true), null);
	}

	@Nonnull
	public static Collection<Entity> findEntities(ClientWorld clientWorld, Predicate<Entity> matching) {
		return findEntities(clientWorld, matching, null);
	}

	@Nonnull
	public static Collection<Entity> findEntities(ClientWorld clientWorld, Logger logger) {
		return findEntities(clientWorld, (entity -> true), logger);
	}

	public static Collection<Entity> findEntities(ClientWorld clientWorld, Predicate<Entity> matching, Logger logger) {
		// Best guess at a way to iterate through all loaded entities
		// if unmapped name doesnt work, try entitiesById
		//_CHECK: ClientWorld or ServerWorld?
		Int2ObjectMap<Entity> entitiesById =
			ObfuscationReflectionHelper.getPrivateValue(ClientWorld.class, clientWorld, "field_217429_b");
		if (entitiesById == null) { return new ArrayList<Entity>(); }
		else {
			return entitiesById.values().parallelStream()
			                   .filter(matching)
			                   .peek(entity -> {
				                   if (EnableLoggingAllEntitiesFound.get() && logger != null) {
					                   logger.info(
						                   "Found entity UUID:{}  other:{}",
						                   entity.getCachedUniqueIdString(),
						                   entity.toString()
					                   );
				                   }
			                   })
			                   .collect(Collectors.toList());
		}
	}
}
