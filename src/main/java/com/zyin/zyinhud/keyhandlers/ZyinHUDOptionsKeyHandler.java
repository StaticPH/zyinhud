package com.zyin.zyinhud.keyhandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

//import com.zyin.zyinhud.ZyinHUDSound;
//import com.zyin.zyinhud.gui.GuiZyinHUDOptions;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

/**
 * The type Zyin hud options key handler.
 */
public class ZyinHUDOptionsKeyHandler implements ZyinHUDKeyHandlerBase {
    /**
     * The constant HotkeyDescription.
     */
    public static final String HotkeyDescription = "key.zyinhud.zyinhudoptions";

    private static Logger logger = LogManager.getLogger("OptionsKeyHandler");

    /**
     * Pressed.
     *
     * @param event the event
     */
    public static void Pressed(KeyInputEvent event) {
        if (mc.currentScreen != null) {
            return;    //don't activate if the user is looking at a GUI
        }

        long handle = mc.mainWindow.getHandle();
        //if "Ctrl" and "Alt" is pressed
        if (
//        	(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) ||
	        (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) &&
                ((GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS) ||
                 (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS))) {
            //display the GUI
//            mc.displayGuiScreen(new GuiZyinHUDOptions(null));
//            ZyinHUDSound.PlayButtonPress();
            logger.info("Gui not yet implemented. Unable to display.");
        }
    }
}