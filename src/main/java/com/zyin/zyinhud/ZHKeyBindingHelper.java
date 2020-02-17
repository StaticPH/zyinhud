package com.zyin.zyinhud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ZHKeyBindingHelper {
	private static final Logger logger = LogManager.getLogger(ZHKeyBindingHelper.class);
	private static final Minecraft mc = Minecraft.getInstance();
	private static List<KeyBinding> keyBindings = new ArrayList<KeyBinding>();
	public static Supplier<Stream<KeyBinding>> ZHKeyBindings = () -> keyBindings.stream();

	@Nonnull
	public static KeyBinding addKeyBind(@Nonnull KeyBinding kb) {
//		logger.info("Adding new KeyBinding: {}", kb.getKeyDescription());
		keyBindings.add(kb);
		ClientRegistry.registerKeyBinding(kb);
//		logger.info("Successfully bound {} to key '{}'", kb.getKeyDescription(), kb.getTranslationKey());
		return kb;
	}

	public static KeyBinding addKeyBind(String description, int keyCode, String category) {
		return addKeyBind(new KeyBinding(description, InputMappings.Type.KEYSYM, keyCode, category));
	}

	public static KeyBinding addKeyBind(String description, InputMappings.Type type, int keyCode, String category) {
		return addKeyBind(new KeyBinding(description, type, keyCode, category));
	}

	public static KeyBinding addKeyBind(
		String description, IKeyConflictContext keyConflictContext,
		InputMappings.Type inputType, int keyCode, String category
	) {
		return addKeyBind(new KeyBinding(description, keyConflictContext, inputType, keyCode, category));
	}

	public static KeyBinding addKeyBind(
		String description, IKeyConflictContext keyConflictContext, InputMappings.Input keyCode, String category
	) {
		return addKeyBind(new KeyBinding(description, keyConflictContext, keyCode, category));
	}

	public static KeyBinding addKeyBind(
		String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier,
		InputMappings.Type inputType, int keyCode, String category
	) {
		return addKeyBind(new KeyBinding(description, keyConflictContext, keyModifier, inputType, keyCode, category));
	}

	public static KeyBinding addKeyBind(
		String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier,
		int keyCode, String category
	) {
		return addKeyBind(
			new KeyBinding(description, keyConflictContext, keyModifier, InputMappings.Type.KEYSYM, keyCode, category)
		);
	}

	public static KeyBinding addKeyBind(
		String description, IKeyConflictContext keyConflictContext, int keyCode, String category
	) {
		return addKeyBind(
			new KeyBinding(description, keyConflictContext, InputMappings.Type.KEYSYM, keyCode, category)
		);
	}

	public static KeyBinding addKeyBind(
		String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier,
		InputMappings.Input keyCode, String category
	) {
		return addKeyBind(new KeyBinding(description, keyConflictContext, keyModifier, keyCode, category));
	}

	@Nonnull
	public static InputMappings.Input mapKey(int key) {
		return InputMappings.Type.KEYSYM.getOrMakeInput(key);
	}

	// Alternative to these isXKeyDown methods, use Screen.hasXDown methods
	public static boolean isCtrlKeyDown() {
		long handle = mc.mainWindow.getHandle();
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
		long handle = mc.mainWindow.getHandle();
		return InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) ||
		       InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);
	}

	public static boolean isAltKeyDown() {
		long handle = mc.mainWindow.getHandle();
		return InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_ALT) ||
		       InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_ALT);
	}


}
