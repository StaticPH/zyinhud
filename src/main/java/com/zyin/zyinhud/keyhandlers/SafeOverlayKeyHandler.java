package com.zyin.zyinhud.keyhandlers;

import com.zyin.zyinhud.modules.ZyinHUDModuleModes.SafeOverlayOptions.SafeOverlayModes;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

import org.lwjgl.glfw.GLFW;

import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.ZyinHUDSound;
import com.zyin.zyinhud.modules.SafeOverlay;
import com.zyin.zyinhud.util.Localization;

public class SafeOverlayKeyHandler implements ZyinHUDKeyHandlerBase {
	public static final String HotkeyDescription = "key.zyinhud.safeoverlay";

	public static void Pressed(KeyInputEvent event) {
		long handle = mc.mainWindow.getHandle();

		//don't activate if the user is looking at a GUI
		if (mc.currentScreen != null || !SafeOverlay.Enabled) { return; }

		//if "+" is pressed, increase the draw distance
		if ((GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_EQUAL) == GLFW.GLFW_PRESS) ||    //keyboard "+" ("=")
		    (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_ADD) == GLFW.GLFW_PRESS)    //numpad "+"
		) {
			int drawDistance = SafeOverlay.instance.IncreaseDrawDistance();

			if (drawDistance == SafeOverlay.maxDrawDistance) {
				ZyinHUDRenderer.DisplayNotification(
					Localization.get("safeoverlay.distance") + ' ' + drawDistance +
					" (" + Localization.get("safeoverlay.distance.max") + ')'
				);
			}
			else {
				ZyinHUDRenderer.DisplayNotification(Localization.get("safeoverlay.distance") + ' ' + drawDistance);
			}

			return;
		}

		//if "-" is pressed, decrease the draw distance
		if ((GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_MINUS) == GLFW.GLFW_PRESS)) {
			int drawDistance = SafeOverlay.instance.DecreaseDrawDistance();
			ZyinHUDRenderer.DisplayNotification(Localization.get("safeoverlay.distance") + ' ' + drawDistance);

			return;
		}

		//if "0" is pressed, set to the default draw distance
		if ((GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_0) == GLFW.GLFW_PRESS) ||
		    (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_0) == GLFW.GLFW_PRESS)
		) {
			int drawDistance = SafeOverlay.instance.SetDrawDistance(SafeOverlay.defaultDrawDistance);
			SafeOverlay.instance.SetSeeUnsafePositionsThroughWalls(false);
			ZyinHUDRenderer.DisplayNotification(
				Localization.get("safeoverlay.distance") + ' ' +
				Localization.get("safeoverlay.distance.default") +
				" (" + drawDistance + ')'
			);

			ZyinHUDSound.PlayButtonPress();
			return;
		}

		//if Control is pressed, enable see through mode
		if ((GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) ||
		    (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS)
		) {
			boolean seeThroughWalls = SafeOverlay.instance.ToggleSeeUnsafePositionsThroughWalls();

			if (seeThroughWalls) {
				ZyinHUDRenderer.DisplayNotification(Localization.get("safeoverlay.seethroughwallsenabled"));
			}
			else {
				ZyinHUDRenderer.DisplayNotification(Localization.get("safeoverlay.seethroughwallsdisabled"));
			}

			ZyinHUDSound.PlayButtonPress();
			return;
		}

		//if nothing is pressed, do the default behavior

		SafeOverlay.Mode = (SafeOverlayModes) SafeOverlay.Mode.next();
		ZyinHUDSound.PlayButtonPress();
	}

}