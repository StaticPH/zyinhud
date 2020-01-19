package com.zyin.zyinhud.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;

/**
 * The type Zyin hud mod base.
 */
public abstract class ZyinHUDModuleBase {
	protected static final Minecraft mc = Minecraft.getInstance();
	static final ItemRenderer itemRenderer = mc.getItemRenderer();

	// We can't move the static variable Enabled to this base mod because then if one module
	// sets it to false, ALL modules will be set to false
}
