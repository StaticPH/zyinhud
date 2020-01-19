//package com.zyin.zyinhud.gui.buttons;
//
//import com.zyin.zyinhud.util.Localization;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.widget.button.Button;
//import net.minecraft.client.settings.KeyBinding;
//import net.minecraft.client.util.InputMappings;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraftforge.client.settings.KeyModifier;
//
///**
// * A button used to change Minecraft's key bindings
// */
//public class GuiHotkeyButton extends Button
//{
//	/**
//	 * The constant mc.
//	 */
//	protected static Minecraft mc = Minecraft.getInstance();
//
//	/**
//	 * The Waiting for hotkey input.
//	 */
//	protected boolean waitingForHotkeyInput = false;
//	/**
//	 * The Hotkey.
//	 */
//	protected String hotkey;    //E.x.: "P"
//	/**
//	 * The Hotkey description.
//	 */
//	protected String hotkeyDescription;    //E.x.: "key.zyinhud.somemod"
//
//	/**
//	 * Instantiates a new Gui hotkey button.
//	 *
//	 * @param id                the id
//	 * @param x                 the x
//	 * @param y                 the y
//	 * @param width             the width
//	 * @param height            the height
//	 * @param hotkeyDescription This should be the same string used in the localization file, E.x.: "key.zyinhud.somemod"
//	 */
//	public GuiHotkeyButton(int id, int x, int y, int width, int height, String hotkeyDescription)
//	{
//		super(id, x, y, width, height, "");
//		this.hotkeyDescription = hotkeyDescription;
//		this.hotkey = GetHotkey();
//		UpdateDisplayString();
//	}
//
//	/**
//	 * This method should be called whenever this button is clicked.
//	 */
//	public void Clicked()
//	{
//		waitingForHotkeyInput = !waitingForHotkeyInput;
//		UpdateDisplayString();
//	}
//
//	/**
//	 * Make this button stop accepting hotkey input.
//	 */
//	public void Cancel()
//	{
//		waitingForHotkeyInput = false;
//		UpdateDisplayString();
//	}
//
//	/**
//	 * Update display string.
//	 */
//	protected void UpdateDisplayString() {
//		if (waitingForHotkeyInput)
//			displayString = Localization.get("gui.options.hotkey") + TextFormatting.WHITE + "> " + TextFormatting.YELLOW + GetHotkey() + TextFormatting.WHITE + " <";
//		else
//			displayString = Localization.get("gui.options.hotkey") + GetHotkey();
//
//	}
//
//	/**
//	 * Is waiting for hotkey input boolean.
//	 *
//	 * @return the boolean
//	 */
//	public boolean IsWaitingForHotkeyInput()
//	{
//		return waitingForHotkeyInput;
//	}
//
//	/**
//	 * Finds the KeyBinding object that Minecraft uses based on the hotkey description (it sounds like
//	 * bad practice to use the description, but that's how Minecraft does it).
//	 * @param hotkeyDescription
//	 * @return
//	 */
//	private KeyBinding FindKeyBinding(String hotkeyDescription)
//	{
//
//        KeyBinding keyBinding = null;
//        KeyBinding[] keyBindings = mc.gameSettings.keyBindings;
//		for (KeyBinding keyBinding1 : keyBindings) {
//			if (keyBinding1.getKeyDescription().equals(hotkeyDescription)) {
//				return keyBinding1;
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * Called when a key is pressed on the GuiZyinHUDOptions screen.
//	 * Updates Minecraft's keybinding.
//	 *
//	 * @param newHotkey e.x. 37 (K), 1 (Esc), 55 (*)
//	 */
//	public void ApplyHotkey(int newHotkey)
//	{
//		waitingForHotkeyInput = false;
//		InputMappings.Input key = InputMappings.getInputByCode(newHotkey, 0);
//		hotkey = KeyModifier.getActiveModifier().getLocalizedComboName(key);
//
//		//SetHotkey(hotkey);
//		UpdateDisplayString();
//
//		//update key binding in Minecraft
//        KeyBinding keyBinding = FindKeyBinding(GetHotkeyDescription());
//        if(keyBinding != null)
//        {
//        	keyBinding.bind(key);
//        	KeyBinding.resetKeyBindingArrayAndHash();
//        }
//	}
//
//
//	/**
//	 * Searches Minecraft's key bindings to get the hotkey based on the hotkey description, then caches the result for future use.
//	 *
//	 * @return String representation of the hotkey, e.x. "K", "LMENU"
//	 */
//	public String GetHotkey()
//	{
//		if(hotkey == null)
//		{
//			//get key binding in Minecraft
//	        KeyBinding keyBinding = FindKeyBinding(GetHotkeyDescription());
//	        if(keyBinding != null)
//	        {
//	        	SetHotkey(hotkey);
//	        	return keyBinding.getLocalizedName();
//	        }
//	        else
//	        {
//	        	return "?";
//	        }
//		}
//		else
//			return hotkey;
//	}
//
//
//	/**
//	 * Sets the modules hotkey
//	 *
//	 * @param hotkey the new hotkey to use
//	 */
//	protected void SetHotkey(String hotkey)
//	{
//		this.hotkey = hotkey;
//	}
//
//	/**
//	 * Gets the description for the modules hotkey
//	 *
//	 * @return string
//	 */
//	protected String GetHotkeyDescription()
//	{
//		return hotkeyDescription;
//	}
//}
