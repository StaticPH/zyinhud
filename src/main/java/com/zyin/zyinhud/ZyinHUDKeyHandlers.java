package com.zyin.zyinhud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.InputEvent.RawMouseEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.zyin.zyinhud.keyhandlers.AnimalInfoKeyHandler;
import com.zyin.zyinhud.keyhandlers.CoordinatesKeyHandler;
import com.zyin.zyinhud.keyhandlers.DistanceMeasurerKeyHandler;
//import com.zyin.zyinhud.keyhandlers.EatingAidKeyHandler;
import com.zyin.zyinhud.keyhandlers.EnderPearlAidKeyHandler;
import com.zyin.zyinhud.keyhandlers.ItemSelectorKeyHandler;
import com.zyin.zyinhud.keyhandlers.PlayerLocatorKeyHandler;
import com.zyin.zyinhud.keyhandlers.PotionAidKeyHandler;
import com.zyin.zyinhud.keyhandlers.QuickDepositKeyHandler;
import com.zyin.zyinhud.keyhandlers.SafeOverlayKeyHandler;
import com.zyin.zyinhud.keyhandlers.WeaponSwapperKeyHandler;
import com.zyin.zyinhud.keyhandlers.ZyinHUDOptionsKeyHandler;
import com.zyin.zyinhud.modules.TorchAid;


import static com.zyin.zyinhud.helper.ZHKeyBindingHelper.*;
import static com.zyin.zyinhud.ZyinHUDConfig.enableLoggingKeybindInputs;
import static com.zyin.zyinhud.util.ZyinHUDUtil.doesScreenAllowKeybinds;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ZyinHUDKeyHandlers {
	private static final Minecraft mc = Minecraft.getInstance();
	private static final MouseHelper mouseHelper = new MouseHelper(mc);
	private static boolean doLogKeybindInputs = enableLoggingKeybindInputs.get();
	private static final Logger logger = LogManager.getLogger(ZyinHUDKeyHandlers.class);

	public static KeyBinding animalInfoKey = addKeyBind(new KeyBinding(
		AnimalInfoKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_O), ZyinHUD.MODNAME
	));//[0]
	public static KeyBinding coordinateKey = addKeyBind(new KeyBinding(
		CoordinatesKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_F4), ZyinHUD.MODNAME
	));//[1]
	public static KeyBinding distanceKey = addKeyBind(new KeyBinding(
		DistanceMeasurerKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_K), ZyinHUD.MODNAME
	));//[2]
	//	public static KeyBinding eatingAidKey = addKeyBind(new KeyBinding(
//		EatingAidKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
//		mapKey(GLFW.GLFW_KEY_G), ZyinHUD.MODNAME
//	));//[3]
	public static KeyBinding enderPearlKey = addKeyBind(new KeyBinding(
		EnderPearlAidKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_C), ZyinHUD.MODNAME
	));//[4]
	public static KeyBinding itemSelectorKey = addKeyBind(new KeyBinding(
		ItemSelectorKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_LEFT_ALT), ZyinHUD.MODNAME
	));//[11]
	public static KeyBinding optionsKey = addKeyBind(new KeyBinding(
		ZyinHUDOptionsKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_Z), ZyinHUD.MODNAME
	));//[10]
	public static KeyBinding playerLocatorKey = addKeyBind(new KeyBinding(
		PlayerLocatorKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_P), ZyinHUD.MODNAME
	));//[5]
	public static KeyBinding potionAidKey = addKeyBind(new KeyBinding(
		PotionAidKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_V), ZyinHUD.MODNAME
	));//[6]
	public static KeyBinding quickDepositKey = addKeyBind(new KeyBinding(
		QuickDepositKeyHandler.hotkeyDescription, KeyConflictContext.GUI,
		mapKey(GLFW.GLFW_KEY_X), ZyinHUD.MODNAME
	));//[7]
	public static KeyBinding safeOverlayKey = addKeyBind(new KeyBinding(
		SafeOverlayKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_L), ZyinHUD.MODNAME
	));//[8]
	public static KeyBinding weaponSwapperKey = addKeyBind(new KeyBinding(
		WeaponSwapperKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
		mapKey(GLFW.GLFW_KEY_F), ZyinHUD.MODNAME
	));//[9]

	/**
	 * Instantiates a new Zyin hud key handlers.
	 */
	public ZyinHUDKeyHandlers() {}

	/**
	 * Key input event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public static void onKeyInputEvent(KeyInputEvent event) {
		if (!doesScreenAllowKeybinds(mc.currentScreen)) { return; }

		//KeyInputEvent will not fire when looking at a GuiScreen - 1.7.2
		if (doLogKeybindInputs) {
			ZHKeyBindings.getValue()
			             .filter(KeyBinding::isKeyDown)
			             .forEach(key -> logger.debug(
				             "Keybinding {} (bound to '{}') is being held", key.getLocalizedName(),
				             key.getKeyDescription()
			             ));
		}

		if (event.getAction() == GLFW.GLFW_PRESS) { onKeyPress(event); }
		else if (event.getAction() == GLFW.GLFW_RELEASE) { onKeyRelease(event); }
	}

	private static void onKeyPress(KeyInputEvent event) {
		if (animalInfoKey.isKeyDown()) { AnimalInfoKeyHandler.onPressed(event); }
		else if (coordinateKey.isKeyDown()) { CoordinatesKeyHandler.onPressed(event);} //THIS WILL NOT FIRE ON A Screen
		else if (distanceKey.isKeyDown()) { DistanceMeasurerKeyHandler.onPressed(event); }
//		else if (eatingAidKey.isKeyDown()) { EatingAidKeyHandler.onPressed(event); }
		else if (enderPearlKey.isKeyDown()) { EnderPearlAidKeyHandler.onPressed(event); }
		else if (playerLocatorKey.isKeyDown()) { PlayerLocatorKeyHandler.onPressed(event); }
		else if (potionAidKey.isKeyDown()) { PotionAidKeyHandler.onPressed(event); }
		else if (quickDepositKey.isKeyDown()) { QuickDepositKeyHandler.onPressed(event);} //THIS WILL NOT FIRE ON A Screen
		else if (safeOverlayKey.isKeyDown()) { SafeOverlayKeyHandler.onPressed(event); }
		else if (weaponSwapperKey.isKeyDown()) { WeaponSwapperKeyHandler.onPressed(event); }
		else if (optionsKey.isKeyDown()) { ZyinHUDOptionsKeyHandler.onPressed(event); }
		else if (itemSelectorKey.isKeyDown()) { ItemSelectorKeyHandler.onPressed(event); }
	}

	private static void onKeyRelease(KeyInputEvent event) {
		if (event.getKey() == itemSelectorKey.getKey().getKeyCode()) {
			ItemSelectorKeyHandler.onReleased(event);
		}
	}

	/**
	 * Mouse event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public static void onMouseEvent(InputEvent event) {
		if ((event instanceof MouseScrollEvent) && ((MouseScrollEvent) event).getScrollDelta() != 0) {
			if (itemSelectorKey.isKeyDown()) { ItemSelectorKeyHandler.onMouseWheelScroll((MouseScrollEvent) event); }
		}

		if (event instanceof MouseInputEvent) {
			//Mouse side buttons
			if (((MouseInputEvent) event).getButton() == 3 || ((MouseInputEvent) event).getButton() == 4) {
				if (mouseHelper.isLeftDown() || mouseHelper.isRightDown()) {
					ItemSelectorKeyHandler.onMouseSideButton((MouseInputEvent) event);
				}
			}
		}

		if (event instanceof RawMouseEvent) {
			if (itemSelectorKey.isKeyDown() && mc.gameSettings.keyBindAttack.isPressed()) {
				ItemSelectorKeyHandler.onAbort((RawMouseEvent) event);
			}
		}
	}

	/**
	 * Client tick event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END && (mc.currentScreen == null || mc.currentScreen.passEvents)) {
			//This tick handler is to overcome the Screen + KeyInputEvent limitation
			//for Coordinates and QuickDeposit

//FIXME: Yeah, i have no idea; now, if I could get an InputEvent here, then I might know what to do
//			In the first place, I don't think this is even necessary anymore
//		if (Keyboard.getEventKey() == coordinateKey.getKey().getKeyCode())
//	    	CoordinatesKeyHandler.onClientTickEvent(event);
//		else if(Keyboard.getEventKey() == quickDepositKey.getKey().getKeyCode())
//			QuickDepositKeyHandler.onClientTickEvent(event);

			//since this method is in the ClientTickEvent, it'll overcome the Screen limitation of not handling mouse clicks
			onFireUseBlockEvents();
		}
	}

	private static boolean useBlockButtonPreviouslyDown = false;

	private static void onFireUseBlockEvents() {//TODO: Migrate either to a dedicated TorchAid event helper, or to TorchAid itself
		//For now, we're just going to assume that nobody uses anything other than the default left and right click bindings
		// for attacking and using items
		// that way, I can just use reflection to get Minecraft.rightClickMouse(), and not bother with other scenarios.
		boolean useBlockButtonDown = mc.gameSettings.keyBindUseItem.isKeyDown();
		if (useBlockButtonDown & !useBlockButtonPreviouslyDown) { TorchAid.onPressed(); }
		else if (!useBlockButtonDown & useBlockButtonPreviouslyDown) { TorchAid.onReleased(); }

		useBlockButtonPreviouslyDown = useBlockButtonDown;
	}
}