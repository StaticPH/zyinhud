package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.ZyinHUDKeyHandlers;
import com.zyin.zyinhud.mods.Coordinates;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

/**
 * The type Coordinates key handler.
 */
public class CoordinatesKeyHandler implements ZyinHUDKeyHandlerBase {
    /**
     * The constant HotkeyDescription.
     */
    public static final String HotkeyDescription = "key.zyinhud.coordinates";

    /**
     * Pressed.
     *
     * @param event the event
     */
    public static void Pressed(KeyInputEvent event) {
        Coordinates.PasteCoordinatesIntoChat();
    }


    private static boolean keyDown = false;

    /**
     * Client tick event.
     *
     * @param event the event
     */
    public static void ClientTickEvent(ClientTickEvent event) {
        long handle = mc.mainWindow.getHandle();
        int keyState = GLFW.glfwGetKey(handle, ZyinHUDKeyHandlers.KEY_BINDINGS[1].getKey().getKeyCode());

        if (mc.currentScreen instanceof ChatScreen) {
            if (keyState == GLFW.GLFW_PRESS || keyState == GLFW.GLFW_RELEASE) {
                if (keyState == GLFW.GLFW_PRESS) {
                    if (!keyDown)
                        OnKeyDown();
                    keyDown = true;
                } else {
                    //if(keyDown == true)
                    //OnKeyUp();
                    keyDown = false;
                }
            }

        }
    }

    private static void OnKeyDown() {
        Pressed(null);
    }
}