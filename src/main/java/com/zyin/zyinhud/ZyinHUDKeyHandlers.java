package com.zyin.zyinhud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

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

import javax.annotation.Nonnull;

import static java.util.Arrays.stream;
import static com.zyin.zyinhud.ZyinHUDConfig.enableLoggingKeybindInputs;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ZyinHUDKeyHandlers {
	private static final Minecraft mc = Minecraft.getInstance();
	private static final MouseHelper mouseHelper = new MouseHelper(mc);
	private static boolean doLogKeybindInputs = enableLoggingKeybindInputs.get();
	public static final Logger logger = LogManager.getLogger(ZyinHUDKeyHandlers.class);

	/**
	 * An array of all of Zyin's HUD custom key bindings. Don't reorder them since they are referenced by their position in the array.<br><ul>
	 * <li>[0] Animal Info
	 * <li>[1] Coordinates
	 * <li>[2] Distance Measurer
	 * <li>[3] Eating Aid
	 * <li>[4] Ender Pearl Aid
	 * <li>[5] Player Locator
	 * <li>[6] Potion Aid
	 * <li>[7] Quick Deposit
	 * <li>[8] Safe Overlay
	 * <li>[9] Weapon Swapper
	 * <li>[10] Zyin's HUD Options
	 * <li>[11] Item Selector
	 */
	public static final KeyBinding[] KEY_BINDINGS = {
		//TODO: It might be beneficial to migrate this to something like a Map, removing a need for ordering
		new KeyBinding(
			AnimalInfoKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_O), ZyinHUD.MODNAME
		),    //[0]
		new KeyBinding(
			CoordinatesKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_F4), ZyinHUD.MODNAME
		),    //[1]
		new KeyBinding(
			DistanceMeasurerKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_K), ZyinHUD.MODNAME
		),    //[2]
		new KeyBinding(
//			EatingAidKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			"WIP: Eating Aid", KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_G), ZyinHUD.MODNAME
		),    //[3]PLACEHOLDER
		new KeyBinding(
			EnderPearlAidKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_C), ZyinHUD.MODNAME
		),    //[4]
		new KeyBinding(
			PlayerLocatorKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_P), ZyinHUD.MODNAME
		),    //[5]
		new KeyBinding(
			PotionAidKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_V), ZyinHUD.MODNAME
		),    //[6]
		new KeyBinding(
			QuickDepositKeyHandler.hotkeyDescription, KeyConflictContext.GUI,
			mapKey(GLFW.GLFW_KEY_X), ZyinHUD.MODNAME
		),    //[7]
		new KeyBinding(
			SafeOverlayKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_L), ZyinHUD.MODNAME
		),    //[8]
		new KeyBinding(
			WeaponSwapperKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_F), ZyinHUD.MODNAME
		),    //[9]
		new KeyBinding(
			ZyinHUDOptionsKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_Z), ZyinHUD.MODNAME
		),    //[10]
		new KeyBinding(
			ItemSelectorKeyHandler.hotkeyDescription, KeyConflictContext.IN_GAME,
			mapKey(GLFW.GLFW_KEY_LEFT_ALT), ZyinHUD.MODNAME
		),    //[11]
	};

	//	This NEEDS to be AFTER the declaration and initialization of KEY_BINDINGS @formatter:off
	public static final ZyinHUDKeyHandlers instance = new ZyinHUDKeyHandlers();
	//   @formatter:on

	/**
	 * Instantiates a new Zyin hud key handlers.
	 */
	public ZyinHUDKeyHandlers() {
		for (KeyBinding keyBinding : KEY_BINDINGS) { ClientRegistry.registerKeyBinding(keyBinding); }
	}

	/**
	 * Key input event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public static void onKeyInputEvent(KeyInputEvent event) {
		//KeyInputEvent will not fire when looking at a GuiScreen - 1.7.2
//TODO: figure out how to make these keybinds work once per press, rather than until the press ends.
//TODO??:?? Migrate these individual handlers to their associated modules, and then add a method in this class that
//		calls MinecraftForge.EVENT_BUS.register on an instance of each class, in which all event methods are to be static and annotated with @SubscribeEvent
		if (doLogKeybindInputs) {
			stream(KEY_BINDINGS)
				.filter(KeyBinding::isKeyDown)
				.forEach(key -> logger.debug(
					"Keybinding {} (bound to '{}') is being held", key.getLocalizedName(), key.getKeyDescription()
				));
		}

		if (KEY_BINDINGS[0].isKeyDown()) { AnimalInfoKeyHandler.onPressed(event); }
		else if (KEY_BINDINGS[1].isKeyDown()) { CoordinatesKeyHandler.onPressed(event); } //THIS WILL NOT FIRE ON A Screen
		else if (KEY_BINDINGS[2].isKeyDown()) { DistanceMeasurerKeyHandler.onPressed(event); }
//		else if (KEY_BINDINGS[3].isKeyDown()) { EatingAidKeyHandler.onPressed(event); }
		else if (KEY_BINDINGS[4].isKeyDown()) { EnderPearlAidKeyHandler.onPressed(event); }
		else if (KEY_BINDINGS[5].isKeyDown()) { PlayerLocatorKeyHandler.onPressed(event); }
		else if (KEY_BINDINGS[6].isKeyDown()) { PotionAidKeyHandler.onPressed(event); }
		else if (KEY_BINDINGS[7].isKeyDown()) { QuickDepositKeyHandler.onPressed(event); }//THIS WILL NOT FIRE ON A Screen
		else if (KEY_BINDINGS[8].isKeyDown()) { SafeOverlayKeyHandler.onPressed(event); }
		else if (KEY_BINDINGS[9].isKeyDown()) { WeaponSwapperKeyHandler.onPressed(event); }
		else if (KEY_BINDINGS[10].isKeyDown()) { ZyinHUDOptionsKeyHandler.onPressed(event); }
		else if (KEY_BINDINGS[11].isKeyDown()) { ItemSelectorKeyHandler.onPressed(event); }
		else if (
			(event.getKey() == KEY_BINDINGS[11].getKey().getKeyCode()) && (event.getAction() == GLFW.GLFW_RELEASE)
		) {
			//on key released
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
//		logger.debug("Mouse event triggered");
		if ((event instanceof MouseScrollEvent) && ((MouseScrollEvent) event).getScrollDelta() != 0) {
			if (KEY_BINDINGS[11].isKeyDown()) { ItemSelectorKeyHandler.onMouseWheelScroll((MouseScrollEvent) event); }
		}

		//Mouse side buttons
		if (
			event instanceof MouseInputEvent &&
			(((MouseInputEvent) event).getButton() == 3 || ((MouseInputEvent) event).getButton() == 4)
		) {
			if (mouseHelper.isLeftDown() || mouseHelper.isRightDown()) {
				ItemSelectorKeyHandler.onMouseSideButton((MouseInputEvent) event);
			}
		}
	}
	// Alternative to these isXKeyDown methods, use Screen.hasXDown methods
	public static boolean isCtrlKeyDown() {
		long handle = Minecraft.getInstance().mainWindow.getHandle();
		// prioritize CONTROL, but allow OPTION as well on Mac (note: GuiScreen's isCtrlKeyDown only checks for the OPTION key on Mac)
		boolean isCtrlKeyDown = InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_CONTROL) ||
		                        InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_CONTROL);
		if (!isCtrlKeyDown && Minecraft.IS_RUNNING_ON_MAC) {
			return InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SUPER) ||
			       InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SUPER);
		}
		return isCtrlKeyDown;
	}

	public static boolean isShiftKeyDown() {
		long handle = Minecraft.getInstance().mainWindow.getHandle();
		return InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) ||
		       InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);
	}

	public static boolean isAltKeyDown() {
		long handle = Minecraft.getInstance().mainWindow.getHandle();
		return InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_ALT) ||
		       InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_ALT);
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
//		if (Keyboard.getEventKey() == KEY_BINDINGS[1].getKey().getKeyCode())
//	    	CoordinatesKeyHandler.onClientTickEvent(event);
//		else if(Keyboard.getEventKey() == KEY_BINDINGS[7].getKey().getKeyCode())
//			QuickDepositKeyHandler.onClientTickEvent(event);

			//since this method is in the ClientTickEvent, it'll overcome the Screen limitation of not handling mouse clicks
			onFireUseBlockEvents();
		}
	}


	private static boolean useBlockButtonPreviouslyDown = false;

	private static void onFireUseBlockEvents() {//TODO: Migrate either to a dedicated TorchAid event helper, or to TorchAid itself
		//.keyBindUseItem		isButtonDown()

		boolean useBlockButtonDown;

		//For now, we're just going to assume that nobody uses anything other than the default left and right click bindings
		// for attacking and using items
		// that way, I can just use reflection to get Minecraft.rightClickMouse(), and not bother with other scenarios.
//    	if(mc.gameSettings.keyBindUseItem.getKey().getKeyCode() < 0)	//the Use Block hotkey is bound to the mouse
//    	{
//            useBlockButtonDown = Mouse.isButtonDown(100 + mc.gameSettings.keyBindUseItem.getKey().getKeyCode());
//		useBlockButtonDown = mouseHelper.isRightDown();
//    	}
//    	else	//the Use Block hotkey is bound to the keyboard
//    	{
//            useBlockButtonDown = InputMappings.isKeyDown(mc.gameSettings.keyBindUseItem.getKey().getKeyCode());
		useBlockButtonDown = mc.gameSettings.keyBindUseItem.isKeyDown();
//    	}
		if (useBlockButtonDown & !useBlockButtonPreviouslyDown) { TorchAid.onPressed(); }
		else if (!useBlockButtonDown & useBlockButtonPreviouslyDown) { TorchAid.onReleased(); }

		useBlockButtonPreviouslyDown = useBlockButtonDown;
	}

	@Nonnull
	public static InputMappings.Input mapKey(int key) {
		return InputMappings.Type.KEYSYM.getOrMakeInput(key);
	}
}