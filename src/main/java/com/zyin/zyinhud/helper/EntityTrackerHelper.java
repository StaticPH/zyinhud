package com.zyin.zyinhud.helper;

import com.zyin.zyinhud.util.Localization;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.zyin.zyinhud.ZyinHUDConfig.EnableLoggingAllEntitiesFound;

public class EntityTrackerHelper {
	public static final Predicate<Entity> playerLocatorMaybeTrack = (entity) -> (
		entity instanceof RemoteClientPlayerEntity ||
		entity instanceof WolfEntity ||
		entity instanceof WitherSkeletonEntity
	);
	public static final Predicate<Entity> animalInfoMaybeTrack = (entity) -> (
		entity instanceof LivingEntity && !(entity instanceof PlayerEntity)
	);

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

	@ParametersAreNonnullByDefault
	public static String getUnlocalizedName(Entity entity) {
		return entity.getType().getTranslationKey();
	}

	@ParametersAreNonnullByDefault
	public static String getLocalizedEntityType(Entity entity) {
		return Localization.get(entity.getType().getTranslationKey());
	}

	@CheckForNull
	@ParametersAreNonnullByDefault
	public static String getRegistryName(Entity entity) {
		//TODO:decide between Try-catch or null check
		ResourceLocation registryName = entity.getType().getRegistryName();
		return registryName != null ? registryName.toString() : null;
	}
}
