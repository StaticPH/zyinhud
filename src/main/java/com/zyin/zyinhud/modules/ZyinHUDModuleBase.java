package com.zyin.zyinhud.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.LazyLoadBase;

/**
 * The type Zyin hud module base.
 */
public abstract class ZyinHUDModuleBase {
	protected static final Minecraft mc = Minecraft.getInstance();

	/** A lazily initialized copy of Minecraft.itemRenderer.
	 Modules loading early for EventBus registration may result in mc.itemRenderer being null at the time
	 this value is set. Therefore, this field must delay acquisition of the value of mc.itemRenderer until
	 a time when it is known to be non-null, like after a world has loaded.
	*/
	static final LazyLoadBase<ItemRenderer> itemRenderer = new LazyLoadBase<>(mc::getItemRenderer);

	// We can't move the static variable Enabled to this base module because then if one module
	// sets it to false, ALL modules will be set to false
}
