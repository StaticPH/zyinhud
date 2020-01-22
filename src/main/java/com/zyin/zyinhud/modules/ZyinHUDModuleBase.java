package com.zyin.zyinhud.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * The type Zyin hud module base.
 */
public abstract class ZyinHUDModuleBase {
	protected static final Minecraft mc = Minecraft.getInstance();
	static final ItemRenderer itemRenderer = mc.getItemRenderer();

	// We can't move the static variable Enabled to this base module because then if one module
	// sets it to false, ALL modules will be set to false

	@Nonnull
	protected static final Tag<Item> tag_TORCH = new ItemTags.Wrapper(new ResourceLocation("forge", "torches"));
	@Nonnull
	protected static final Tag<Item> tag_TORCH_PLACER = new ItemTags.Wrapper(new ResourceLocation("forge", "torchplacers"));
}
