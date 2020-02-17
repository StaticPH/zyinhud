package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.modules.QuickDeposit;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

import static com.zyin.zyinhud.ZyinHUDKeyHandlers.quickDepositKey;

public class QuickDepositKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String hotkeyDescription = "key.zyinhud.quickdeposit";
	private static long handle = mc.mainWindow.getHandle();
	private static boolean isKeyDown = false;

	//_CHECK: having set the conflict context in ZyinHUDKeyHandlers may make the ContainerScreen check unnecessary
	//_CHECK: KeyInputEvent might need to be replaced by one of the events from GuiScreenEvent
	public static void onPressed(KeyInputEvent event) {
		//don't activate if the user isn't looking at a container gui
		if (!(mc.currentScreen instanceof ContainerScreen)) { return; }

		if (QuickDeposit.isEnabled) {
			if ((GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS)) {
				QuickDeposit.quickDepositItemsInChest(false);
			}
			else { QuickDeposit.quickDepositItemsInChest(true); }
		}
	}
	//FIXME:UNUSED?
	public static void onClientTickEvent(ClientTickEvent event) {
		long handle = mc.mainWindow.getHandle();
		int keyState = GLFW.glfwGetKey(handle, quickDepositKey.getKey().getKeyCode());

		if (mc.currentScreen instanceof ContainerScreen) {
			if (keyState == GLFW.GLFW_PRESS){
				if (!isKeyDown) { onKeyDown(); }
				isKeyDown = true;
			}
			else {isKeyDown = false; }
		}
	}

	private static void onKeyDown() {
		onPressed(null);
	}
}