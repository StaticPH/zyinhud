//package com.zyin.zyinhud.gui.buttons;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.widget.button.Button;
//
///**
// * A normal GuiButton but with label text to the left of the usual button text.
// */
//public class GuiLabeledButton extends Button {
//	/**
//	 * The Button label.
//	 */
//	public String buttonLabel = null;
//
//	/**
//	 * Instantiates a new Gui labeled button.
//	 *
//	 * @param x          the x
//	 * @param y          the y
//	 * @param widthIn    the width in
//	 * @param heightIn   the height in
//	 * @param buttonText the button text
//	 * @param onPress    What to do when the button is pressed
//	 */
//	public GuiLabeledButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String buttonLabel)
////	public GuiLabeledButton(int x, int y, int widthIn, int heightIn, String buttonText, IPressable onPress) {
//		super(buttonId, x, y, widthIn, heightIn, buttonText);
////		super(x, y, widthIn, heightIn, buttonText, onPress);
//	}
//
//	@Override
//	public void render(int mouseX, int mouseY, float partialTicks) {
//		super.render(mouseX, mouseY, partialTicks);
//
//		Minecraft mc = Minecraft.getInstance();
//		if (buttonLabel != null) {
//			mc.fontRenderer.drawStringWithShadow(
//				buttonLabel, this.x + 3,
//				this.y + (height - mc.fontRenderer.FONT_HEIGHT) / 2 + 1, 0x55ffffff
//			);    //func_175063_a() is drawStringWithShadow()
//		}
//	}
//}
