//package com.zyin.zyinhud.gui;
////TODO: review for buttons https://github.com/Vazkii/MCPRemapper/blob/master/1.13.1-to-1.14.3.csv#L241
//
//import java.util.ArrayList;
//
//import net.minecraft.client.gui.widget.Widget;
//import net.minecraft.client.gui.widget.button.Button;
//import net.minecraft.client.gui.screen.Screen;
//
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TextFormatting;
//
//import static com.zyin.zyinhud.keyhandlers.ZyinHUDKeyHandlerBase.mc;
//
//
///**
// * A GuiScreen replacement that supports putting tooltips onto GuiButtons.
// */
//public abstract class GuiTooltipScreen extends Screen {
//	/**
//	 * Show a white "?" in the top right part of any button with a tooltip assigned to it
//	 */
//	public static boolean showTooltipButtonEffect = true;
//
//	/**
//	 * Show an aqua "?" in the top right part of any button with a tooltip assigned to it when mouseovered
//	 */
//	public static boolean showTooltipButtonMouseoverEffect = true;
//
//	/**
//	 * Putting this string into a tooltip will cause a line break
//	 */
//	public String tooltipNewlineDelimeter = "_p"; //"�p";	//the "�" symbol doesn't seem to work
//
//	/**
//	 * The amount of time in milliseconds until a tooltip is rendered
//	 */
//	public long tooltipDelay = 900;
//
//	/**
//	 * The maximum width in pixels a tooltip can occupy before word wrapping occurs
//	 */
//	public int tooltipMaxWidth = 150;
//
//	/**
//	 * The Tooltip x offset.
//	 */
//	protected int tooltipXOffset = 0;
//	/**
//	 * The Tooltip y offset.
//	 */
//	protected int tooltipYOffset = 10;
//
//	private final static int LINE_HEIGHT = 11;
//
//	private long mouseoverTime = 0;
//	private long prevSystemTime = -1;
//
//	protected GuiTooltipScreen(ITextComponent titleIn) {
//		super(titleIn);
//	}
//
//	public void drawScreen(int mouseX, int mouseY, float f) {
//		super.render(mouseX, mouseY, f);
//
//		drawTooltipScreen(mouseX, mouseY);
//	}
//
//	/**
//	 * This method must be overriden. Gets a tooltip String for a specific button.
//	 * Recommended to use a switch/case statement for buttonId for easy implementation.
//	 *
//	 * @param buttonId The ID of the button this tooltip corresponds to
//	 * @return The tooltip string for the specified buttonId. null if no tooltip exists for this button.
//	 */
//	protected abstract String getButtonTooltip(int buttonId);
//
//	/**
//	 * Renders any special effects applied to tooltip buttons, and renders any tooltips for Buttons
//	 * that are being mouseovered.
//	 *
//	 * @param mouseX the mouse x
//	 * @param mouseY the mouse y
//	 */
//	protected void drawTooltipScreen(int mouseX, int mouseY) {
//		if (showTooltipButtonEffect) { renderTooltipButtonEffect(); }
//
//		int mousedOverButtonId = -1;
//
//		//find out which button is being mouseovered
//		for (Widget button : this.buttons) {
//			if (button.isMouseOver(mouseX, mouseY)) {   //isMouseOver vs isHovered
//				mousedOverButtonId = button.id;
//
//				if (showTooltipButtonMouseoverEffect && getButtonTooltip(mousedOverButtonId) != null) {
//					renderTooltipButtonMouseoverEffect((Button) button);
//				}
//
//				break;
//			}
//		}
//
//		//calculate how long this button has been mouseovered for
//		if (mousedOverButtonId > -1) {
//			long systemTime = System.currentTimeMillis();
//
//			if (prevSystemTime > 0) { mouseoverTime += systemTime - prevSystemTime; }
//
//			prevSystemTime = systemTime;
//		}
//		else {
//			mouseoverTime = 0;
//		}
//
//		//render the button's tooltip
//		if (mouseoverTime > tooltipDelay) {
//			String tooltip = getButtonTooltip(mousedOverButtonId);
//			if (tooltip != null) {
//				renderTooltip(mouseX, mouseY, tooltip);
//			}
//		}
//	}
//
//	/**
//	 * Determines if a GuiButton is being mouseovered.
//	 *
//	 * @param mouseX the mouse x
//	 * @param mouseY the mouse y
//	 * @param button the button
//	 * @return true if this button is mouseovered
//	 */
////	protected boolean isButtonMousedOver(int mouseX, int mouseY, Button button)
////	{
////		if(mouseX >= button.x && mouseX <= button.x + button.getWidth() && mouseY >= button.y)
////		{
////			//for some god-forsaken reason they made GuiButton.getButtonWidth() public but not height,
////			//so use reflection to grab it
////			int buttonHeight = button.getHeight();
////			if (mouseY <= button.y + buttonHeight) {
////				return true;
////			}
////		}
////		return false;
////	}
//
//	/**
//	 * Render anything special onto all buttons that have tooltips assigned to them.
//	 */
//	//_CHECK: It SEEMS like unicode handling is now internal?
//	protected void renderTooltipButtonEffect() {
//		for (Widget button : buttons) {
//			if (getButtonTooltip(button.id) != null) {
//				mc.fontRenderer.drawString("?", button.x + button.getWidth() - 5, button.y, 0x99FFFFFF);
//			}
//		}
//	}
//
//	/**
//	 * Render anything special onto buttons that have tooltips assigned to them when they are mousevered.
//	 *
//	 * @param button the button
//	 */
//	protected void renderTooltipButtonMouseoverEffect(Button button) {
//		mc.fontRenderer.drawString(TextFormatting.AQUA + "?", button.x + button.getWidth() - 5, button.y, 0xFFFFFF);
//	}
//
//	/**
//	 * Renders a tooltip at (x,y).
//	 *
//	 * @param x       the x
//	 * @param y       the y
//	 * @param tooltip the tooltip
//	 */
//	protected void renderTooltip(int x, int y, String tooltip) {
//		String[] tooltipArray = parseTooltipArrayFromString(tooltip);
//
//		int tooltipWidth = getTooltipWidth(tooltipArray);
//		int tooltipHeight = getTooltipHeight(tooltipArray);
//
//		int tooltipX = x + tooltipXOffset;
//		int tooltipY = y + tooltipYOffset;
//
//		if (tooltipX > width - tooltipWidth - 7) { tooltipX = width - tooltipWidth - 7; }
//		if (tooltipY > height - tooltipHeight - 8) { tooltipY = height - tooltipHeight - 8; }
//
//		//render the background inside box
//		int innerAlpha = -0xFEFFFF0;    //very very dark purple
//		fillGradient(tooltipX, tooltipY - 1, tooltipX + tooltipWidth + 6, tooltipY, innerAlpha, innerAlpha);
//		fillGradient(
//			tooltipX, tooltipY + tooltipHeight + 6, tooltipX + tooltipWidth + 6, tooltipY + tooltipHeight + 7,
//			innerAlpha, innerAlpha
//		);
//		fillGradient(
//			tooltipX, tooltipY, tooltipX + tooltipWidth + 6, tooltipY + tooltipHeight + 6, innerAlpha, innerAlpha);
//		fillGradient(tooltipX - 1, tooltipY, tooltipX, tooltipY + tooltipHeight + 6, innerAlpha, innerAlpha);
//		fillGradient(
//			tooltipX + tooltipWidth + 6, tooltipY, tooltipX + tooltipWidth + 7, tooltipY + tooltipHeight + 6,
//			innerAlpha, innerAlpha
//		);
//
//		//render the background outside box
//		int outerAlpha1 = 0x505000FF;
//		int outerAlpha2 = (outerAlpha1 & 0xFEFEFE) >> 1 | outerAlpha1 & -0x1000000;
//		fillGradient(tooltipX, tooltipY + 1, tooltipX + 1, tooltipY + tooltipHeight + 6 - 1, outerAlpha1, outerAlpha2);
//		fillGradient(
//			tooltipX + tooltipWidth + 5, tooltipY + 1, tooltipX + tooltipWidth + 7, tooltipY + tooltipHeight + 6 - 1,
//			outerAlpha1, outerAlpha2
//		);
//		fillGradient(tooltipX, tooltipY, tooltipX + tooltipWidth + 3, tooltipY + 1, outerAlpha1, outerAlpha1);
//		fillGradient(
//			tooltipX, tooltipY + tooltipHeight + 5, tooltipX + tooltipWidth + 7, tooltipY + tooltipHeight + 6,
//			outerAlpha2, outerAlpha2
//		);
//
//		//render the foreground text
//		int lineCount = 0;
//		for (String s : tooltipArray) {
//			mc.fontRenderer.drawString(s, tooltipX + 2, tooltipY + 2 + lineCount * LINE_HEIGHT, 0xFFFFFF);
//			lineCount++;
//		}
//		//Why not just Screen.renderTooltip though?
//	}
//
//	/**
//	 * Converts a String representation of a tooltip into a String[], and also decodes any font codes used.
//	 *
//	 * @param s Ex: "Hello,_nI am your _ltooltip_r and you love me."
//	 * @return An array of Strings such that each String width does not exceed tooltipMaxWidth
//	 */
//	protected String[] parseTooltipArrayFromString(String s) {
//		String[] tooltipSections = s.split(tooltipNewlineDelimeter);
//		ArrayList<String> tooltipArrayList = new ArrayList<String>();
//
//		for (String section : tooltipSections) {
//			String tooltip = "";
//			String[] tooltipWords = section.split(" ");
//
//			for (String tooltipWord : tooltipWords) {
//				int lineWidthWithNextWord = mc.fontRenderer.getStringWidth(tooltip + tooltipWord);
//				if (lineWidthWithNextWord > tooltipMaxWidth) {
//					tooltipArrayList.add(tooltip.trim());
//					tooltip = tooltipWord + " ";
//				}
//				else {
//					tooltip += tooltipWord + " ";
//				}
//			}
//
//			tooltipArrayList.add(tooltip.trim());
//		}
//
//		String[] tooltipArray = new String[tooltipArrayList.size()];
//		tooltipArrayList.toArray(tooltipArray);
//
//		return tooltipArray;
//	}
//
//	/***
//	 * Gets the width of the tooltip in pixels.
//	 * @param tooltipArray
//	 * @return
//	 */
//	private int getTooltipWidth(String[] tooltipArray) {
//		int longestWidth = 0;
//		for (String s : tooltipArray) {
//			int width = mc.fontRenderer.getStringWidth(s);
//			if (width > longestWidth) { longestWidth = width; }
//		}
//		return longestWidth;
//	}
//
//	/**
//	 * Gets the height of the tooltip in pixels.
//	 *
//	 * @param tooltipArray
//	 * @return
//	 */
//	private int getTooltipHeight(String[] tooltipArray) {
//		int tooltipHeight = mc.fontRenderer.FONT_HEIGHT - 2;
//		if (tooltipArray.length > 1) {
//			tooltipHeight += (tooltipArray.length - 1) * LINE_HEIGHT;
//		}
//		return tooltipHeight;
//	}
//}
