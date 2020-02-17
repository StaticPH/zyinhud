package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.modules.Coordinates;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

import static com.zyin.zyinhud.ZyinHUDKeyHandlers.coordinateKey;

public class CoordinatesKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String hotkeyDescription = "key.zyinhud.coordinates";
	private static boolean keyDown = false;

	public static void onPressed(KeyInputEvent event) {
		Coordinates.shareCoordinatesInChat();
	}

	//FIXME:Unused?
	public static void onClientTickEvent(ClientTickEvent event) {
		long handle = mc.mainWindow.getHandle();
		int keyState = GLFW.glfwGetKey(handle, coordinateKey.getKey().getKeyCode());

		if (mc.currentScreen instanceof ChatScreen) {
			if (keyState == GLFW.GLFW_PRESS || keyState == GLFW.GLFW_RELEASE) {
				if (keyState == GLFW.GLFW_PRESS) {
					if (!keyDown) { onKeyDown(); }
					keyDown = true;
				}
				else {
					//if(keyDown == true)
					//OnKeyUp();
					keyDown = false;
				}
			}

		}
	}

	private static void onKeyDown() {
		onPressed(null);
	}
}