package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.ZyinHUDKeyHandlers;
import com.zyin.zyinhud.modules.QuickDeposit;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

public class QuickDepositKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.quickdeposit";
	private static long handle = mc.mainWindow.getHandle();
	private static boolean keyDown = false;

	//_CHECK: having set the conflict context in ZyinHUDKeyHandlers may make the ContainerScreen check unnecessary
	public static void Pressed(KeyInputEvent event) {
		//don't activate if the user isn't looking at a container gui
		if (!(mc.currentScreen instanceof ContainerScreen)) { return; }

		if (QuickDeposit.Enabled) {
			if ((GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS)) {
				QuickDeposit.QuickDepositItemsInChest(false);
			}
			else { QuickDeposit.QuickDepositItemsInChest(true); }
		}
	}

	public static void ClientTickEvent(ClientTickEvent event) {
		long handle = mc.mainWindow.getHandle();
		int keyState = GLFW.glfwGetKey(handle, ZyinHUDKeyHandlers.KEY_BINDINGS[7].getKey().getKeyCode());

		if (mc.currentScreen instanceof ContainerScreen) {
			if (keyState == GLFW.GLFW_PRESS){
				if (!keyDown) { OnKeyDown(); }
				keyDown = true;
			}
			else {keyDown = false; }
		}
	}

	private static void OnKeyDown() {
		Pressed(null);
	}
}