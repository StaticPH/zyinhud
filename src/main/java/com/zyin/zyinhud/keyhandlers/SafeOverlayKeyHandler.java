package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.modules.ZyinHUDModuleModes.SafeOverlayOptions;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

import org.lwjgl.glfw.GLFW;

import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.modules.SafeOverlay;
import com.zyin.zyinhud.util.Localization;

public class SafeOverlayKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String hotkeyDescription = "key.zyinhud.safeoverlay";

	public static void onPressed(KeyInputEvent event) {
		long handle = mc.mainWindow.getHandle();

		//don't activate if the user is looking at a GUI
		if (mc.currentScreen != null || !SafeOverlay.isEnabled) { return; }

		//if "+" is pressed, increase the draw distance
		if (
			(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_EQUAL) == GLFW.GLFW_PRESS) ||    //keyboard "+" ("=")
			(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_ADD) == GLFW.GLFW_PRESS)    //numpad "+"
		) {
			int drawDistance = SafeOverlay.increaseDrawDistance();

			if (drawDistance == SafeOverlayOptions.maxDrawDistance) {
				ZyinHUDRenderer.displayNotification(
					Localization.get("safeoverlay.distance") + ' ' + drawDistance +
					" (" + Localization.get("safeoverlay.distance.max") + ')'
				);
			}
			else {
				ZyinHUDRenderer.displayNotification(Localization.get("safeoverlay.distance") + ' ' + drawDistance);
			}

			return;
		}

		//if "-" is pressed, decrease the draw distance
		if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_MINUS) == GLFW.GLFW_PRESS) {
			int drawDistance = SafeOverlay.decreaseDrawDistance();
			ZyinHUDRenderer.displayNotification(Localization.get("safeoverlay.distance") + ' ' + drawDistance);

			return;
		}

		//if "0" is pressed, set to the default draw distance
		if (
			(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_0) == GLFW.GLFW_PRESS) ||
			(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_0) == GLFW.GLFW_PRESS)
		) {
			int drawDistance = SafeOverlay.setDrawDistance(SafeOverlayOptions.defaultDrawDistance);
			SafeOverlay.setSeeUnsafePositionsThroughWalls(false);
			ZyinHUDRenderer.displayNotification(
				Localization.get("safeoverlay.distance") + ' ' +
				Localization.get("safeoverlay.distance.default") +
				" (" + drawDistance + ')'
			);

			ZyinHUDSound.playButtonPress();
			return;
		}

		//if Control is pressed, enable see through mode
		if (
			(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) ||
			(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS)
		) {
			boolean seeThroughWalls = SafeOverlay.toggleSeeUnsafePositionsThroughWalls();

			if (seeThroughWalls) {
				ZyinHUDRenderer.displayNotification(Localization.get("safeoverlay.seethroughwallsenabled"));
			}
			else {
				ZyinHUDRenderer.displayNotification(Localization.get("safeoverlay.seethroughwallsdisabled"));
			}

			ZyinHUDSound.playButtonPress();
			return;
		}

		//if nothing is pressed, do the default behavior

		SafeOverlay.mode = (SafeOverlayOptions.safeOverlayModes) SafeOverlay.mode.next();
		ZyinHUDSound.playButtonPress();
	}

}