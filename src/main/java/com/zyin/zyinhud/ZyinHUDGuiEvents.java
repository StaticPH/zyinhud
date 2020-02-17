//package com.zyin.zyinhud;
//
//import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
//import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//
//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;
//
///**
// * Used to capture GUI Events for vanilla screens in order to add custom functionality to them.
// */
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
//public class ZyinHUDGuiEvents {
//	public static final ZyinHUDGuiEvents instance = new ZyinHUDGuiEvents();
//
//	/**
//	 * Init gui event post.
//	 * Used to inject new buttons into vanilla GUIs
//	 *
//	 * @param event the event
//	 */
//	@SubscribeEvent
//	public void initGuiEventPost(InitGuiEvent.Post event) {
//		GuiZyinHUDOptions.initGuiEventPost(event);
//	}
//
//	/**
//	 * Action performed event post.
//	 * Used to capture when a custom button is clicked in a vanilla GUI
//	 *
//	 * @param event the event
//	 */
//	@SubscribeEvent
//	public void actionPerformedEventPost(ActionPerformedEvent.Post event) {
//		GuiZyinHUDOptions.actionPerformedEventPost(event);
//	}
//}
