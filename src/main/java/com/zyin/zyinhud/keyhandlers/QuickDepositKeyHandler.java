package com.zyin.zyinhud.keyhandlers;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;

import org.lwjgl.glfw.GLFW;

import com.zyin.zyinhud.ZyinHUDKeyHandlers;
import com.zyin.zyinhud.mods.QuickDeposit;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

/**
 * The type Quick deposit key handler.
 */
public class QuickDepositKeyHandler implements ZyinHUDKeyHandlerBase
{
	/**
	 * The constant HotkeyDescription.
	 */
	public static final String HotkeyDescription = "key.zyinhud.quickdeposit";
	private static long handle = mc.mainWindow.getHandle();

	/**
	 * Pressed.
	 *
	 * @param event the event
	 */
	public static void Pressed(KeyInputEvent event)
	{
		if (!(mc.currentScreen instanceof GuiContainer))
        {
            return;    //don't activate if the user isn't looking at a container gui
        }
        
        if (QuickDeposit.Enabled)
        {
            if ((GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS))
            	QuickDeposit.QuickDepositItemsInChest(false);
            else
            	QuickDeposit.QuickDepositItemsInChest(true);
        }
	}
    
    
    
    private static boolean keyDown = false;

	/**
	 * Client tick event.
	 *
	 * @param event the event
	 */
	public static void ClientTickEvent(ClientTickEvent event)
    {
		long handle = mc.mainWindow.getHandle();
		int keyState = GLFW.glfwGetKey(handle, ZyinHUDKeyHandlers.KEY_BINDINGS[7].getKey().getKeyCode());

    	if(mc.currentScreen instanceof GuiContainer)
    	{
    		if(keyState == GLFW.GLFW_PRESS || keyState == GLFW.GLFW_RELEASE)
    		{
    			if(keyState == GLFW.GLFW_PRESS)
    			{
    				if(keyDown == false)
    					OnKeyDown();
    	            keyDown = true;
    	        }
    	        else
    	        {
    				//if(keyDown == true)
    					//OnKeyUp();
    	            keyDown = false;
    	        }
    		}
    		
    	}
    }

	private static void OnKeyDown()
	{
        Pressed(null);
	}
}