package com.zyin.zyinhud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

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
import com.zyin.zyinhud.mods.Miscellaneous;
import com.zyin.zyinhud.mods.TorchAid;

/**
 * The type Zyin hud key handlers.
 */
public class ZyinHUDKeyHandlers
{
	private final static Minecraft mc = Minecraft.getInstance();

	private static final MouseHelper mouseHelper = new MouseHelper(mc);

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
	public static final KeyBinding[] KEY_BINDINGS =
	{
		new KeyBinding(AnimalInfoKeyHandler.HotkeyDescription,      KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_O),        ZyinHUD.MODNAME),	//[0]
	    new KeyBinding(CoordinatesKeyHandler.HotkeyDescription,     KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_F1),       ZyinHUD.MODNAME),	//[1]
	    new KeyBinding(DistanceMeasurerKeyHandler.HotkeyDescription,KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_K), 	    ZyinHUD.MODNAME),	//[2]
//	    new KeyBinding(EatingAidKeyHandler.HotkeyDescription, 		KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_G), 	    ZyinHUD.MODNAME),	//[3]
	    new KeyBinding("Not Yet Implemented: Eating Aid",           KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_G), 	    ZyinHUD.MODNAME),	//[3]PLACEHOLDER
	    new KeyBinding(EnderPearlAidKeyHandler.HotkeyDescription,   KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_C), 	    ZyinHUD.MODNAME),	//[4]
	    new KeyBinding(PlayerLocatorKeyHandler.HotkeyDescription,   KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_P), 	    ZyinHUD.MODNAME),	//[5]
	    new KeyBinding(PotionAidKeyHandler.HotkeyDescription,       KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_V), 	    ZyinHUD.MODNAME),	//[6]
	    new KeyBinding(QuickDepositKeyHandler.HotkeyDescription,    KeyConflictContext.GUI,     mapKey(GLFW.GLFW_KEY_X), 	    ZyinHUD.MODNAME),	//[7]
	    new KeyBinding(SafeOverlayKeyHandler.HotkeyDescription,     KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_L), 	    ZyinHUD.MODNAME),	//[8]
	    new KeyBinding(WeaponSwapperKeyHandler.HotkeyDescription,   KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_F), 	    ZyinHUD.MODNAME),	//[9]
	    new KeyBinding(ZyinHUDOptionsKeyHandler.HotkeyDescription,  KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_Z),        ZyinHUD.MODNAME),    //[10]
		new KeyBinding(ItemSelectorKeyHandler.HotkeyDescription,    KeyConflictContext.IN_GAME, mapKey(GLFW.GLFW_KEY_LEFT_ALT), ZyinHUD.MODNAME),    //[11]
	};

	/**
	 * The constant instance.
	 */
	public static final ZyinHUDKeyHandlers instance = new ZyinHUDKeyHandlers();

	/**
	 * Instantiates a new Zyin hud key handlers.
	 */
	public ZyinHUDKeyHandlers()
	{
		for(KeyBinding keyBinding : KEY_BINDINGS)
			ClientRegistry.registerKeyBinding(keyBinding);
	}

	/**
	 * Key input event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void KeyInputEvent(KeyInputEvent event)
	{
		//KeyInputEvent will not fire when looking at a GuiScreen - 1.7.2
		
		//if 2 KeyBindings have the same hotkey, only 1 will be flagged as "pressed" in getIsKeyPressed(),
		//which one ends up getting pressed in that scenario is undetermined
		
		if(KEY_BINDINGS[0].isKeyDown())
			AnimalInfoKeyHandler.Pressed(event);
		//else if(keyBindings[1].getIsKeyPressed())
			//CoordinatesKeyHandler.Pressed(event);		//THIS WILL NOT FIRE ON A GuiScreen
		else if(KEY_BINDINGS[2].isKeyDown())
			DistanceMeasurerKeyHandler.Pressed(event);
//		else if(KEY_BINDINGS[3].isKeyDown())
//			EatingAidKeyHandler.Pressed(event);
		else if(KEY_BINDINGS[4].isKeyDown())
			EnderPearlAidKeyHandler.Pressed(event);
		else if(KEY_BINDINGS[5].isKeyDown())
			PlayerLocatorKeyHandler.Pressed(event);
		else if(KEY_BINDINGS[6].isKeyDown())
			PotionAidKeyHandler.Pressed(event);
		//else if(keyBindings[7].getIsKeyPressed())
			//QuickDepositKeyHandler.Pressed(event);	//THIS WILL NOT FIRE ON A GuiScreen
		else if(KEY_BINDINGS[8].isKeyDown())
			SafeOverlayKeyHandler.Pressed(event);
		else if(KEY_BINDINGS[9].isKeyDown())
			WeaponSwapperKeyHandler.Pressed(event);
//		else if(KEY_BINDINGS[10].isKeyDown())
//			ZyinHUDOptionsKeyHandler.Pressed(event);
		else if(KEY_BINDINGS[11].isKeyDown())
			ItemSelectorKeyHandler.Pressed(event);
		else if((event.getKey() == KEY_BINDINGS[11].getKey().getKeyCode()) && (event.getAction() == GLFW.GLFW_RELEASE))	//on key released   _CHECK: good?
			ItemSelectorKeyHandler.Released(event);

	}

	/**
	 * Mouse event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void MouseEvent(MouseInputEvent event) {
    	//event.buttonstate = true if pressed, false if released
    	//event.button = -1 = mouse moved
    	//event.button =  0 = Left click
    	//event.button =  1 = Right click
    	//event.button =  2 = Middle click
    	//event.dwheel =    0 = mouse moved
		//event.dwheel =  120 = mouse wheel up
		//event.dwheel = -120 = mouse wheel down


//		if (event.getDx() != 0 || event.getDy() != 0)    //mouse movement event
//			return;

//	TODO://Mouse wheel scroll
//		if( (event instanceof MouseScrollEvent) && ((MouseScrollEvent)event).getScrollDelta() != 0) {
//        	if(KEY_BINDINGS[11].isKeyDown())
//				ItemSelectorKeyHandler.OnMouseWheelScroll(event);
//		}

		//Mouse side buttons
		if (event.getButton() == 3 || event.getButton() == 4) {
//			if(event.isButtonstate()) {
			if(mouseHelper.isLeftDown() || mouseHelper.isRightDown()){
				ItemSelectorKeyHandler.OnMouseSideButton(event);
			}
		}
	}


	/**
	 * Client tick event.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void ClientTickEvent(ClientTickEvent event) {
		//This tick handler is to overcome the GuiScreen + KeyInputEvent limitation
    	//for Coordinates and QuickDeposit
//FIXME: Yeah, i have no idea
//		if (Keyboard.getEventKey() == KEY_BINDINGS[1].getKey().getKeyCode())
//	    	CoordinatesKeyHandler.ClientTickEvent(event);
//		else if(Keyboard.getEventKey() == KEY_BINDINGS[7].getKey().getKeyCode())
//			QuickDepositKeyHandler.ClientTickEvent(event);
		
		//since this method is in the ClientTickEvent, it'll overcome the GuiScreen limitation of not handling mouse clicks
//		FireUseBlockEvents();
    }


    private static boolean useBlockButtonPreviouslyDown = false;
    
    private static void FireUseBlockEvents()
    {
    	//.keyBindUseItem		isButtonDown()
    	//keyboard key = postive
    	//forward click = -96	4
    	//backward click = -97	3
    	//middle click = -98	2
    	//right click = -99		1
    	//left click = -100		0
    	
    	boolean useBlockButtonDown;

	    //For now, we're just going to assume that nobody uses anything other than the default left and right click bindings
//    	if(mc.gameSettings.keyBindUseItem.getKey().getKeyCode() < 0)	//the Use Block hotkey is bound to the mouse
//    	{
//            useBlockButtonDown = Mouse.isButtonDown(100 + mc.gameSettings.keyBindUseItem.getKey().getKeyCode());
            useBlockButtonDown = mouseHelper.isRightDown();
//    	}
//    	else	//the Use Block hotkey is bound to the keyboard
//    	{
//            useBlockButtonDown = InputMappings.isKeyDown(mc.gameSettings.keyBindUseItem.getKey().getKeyCode());
//    	}
    	
    	if(useBlockButtonDown == true & useBlockButtonPreviouslyDown == false)
    		OnUseBlockPressed();
    	else if(useBlockButtonDown == false & useBlockButtonPreviouslyDown == true)
    		OnUseBlockReleased();
    	
    	useBlockButtonPreviouslyDown = useBlockButtonDown;
    }
    private static void OnUseBlockPressed()
    {
    	TorchAid.instance.Pressed();
    }
    private static void OnUseBlockReleased()
    {
    	TorchAid.instance.Released();
    }

	private static InputMappings.Input mapKey(int key) { return InputMappings.Type.KEYSYM.getOrMakeInput(key); }
}