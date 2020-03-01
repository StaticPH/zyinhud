//package com.zyin.zyinhud.gui;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.widget.button.Button;
//import net.minecraft.client.gui.screen.OptionsScreen;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.lwjgl.glfw.GLFW;
//import org.lwjgl.opengl.GL11;
//
//import com.zyin.zyinhud.ZyinHUD;
//import com.zyin.zyinhud.config.ZyinHUDConfig;
//import com.zyin.zyinhud.ZyinHUDKeyHandlers;
//import com.zyin.zyinhud.gui.buttons.GuiHotkeyButton;
//import com.zyin.zyinhud.gui.buttons.GuiLabeledButton;
//import com.zyin.zyinhud.gui.buttons.GuiNumberSlider;
//import com.zyin.zyinhud.gui.buttons.GuiNumberSliderWithUndo;
//import com.zyin.zyinhud.keyhandlers.AnimalInfoKeyHandler;
//import com.zyin.zyinhud.keyhandlers.CoordinatesKeyHandler;
//import com.zyin.zyinhud.keyhandlers.DistanceMeasurerKeyHandler;
////import com.zyin.zyinhud.keyhandlers.EatingAidKeyHandler;
//import com.zyin.zyinhud.keyhandlers.EnderPearlAidKeyHandler;
//import com.zyin.zyinhud.keyhandlers.ItemSelectorKeyHandler;
//import com.zyin.zyinhud.keyhandlers.PlayerLocatorKeyHandler;
//import com.zyin.zyinhud.keyhandlers.PotionAidKeyHandler;
//import com.zyin.zyinhud.keyhandlers.QuickDepositKeyHandler;
//import com.zyin.zyinhud.keyhandlers.SafeOverlayKeyHandler;
//import com.zyin.zyinhud.keyhandlers.WeaponSwapperKeyHandler;
//import com.zyin.zyinhud.modules.AnimalInfo;
//import com.zyin.zyinhud.modules.Clock;
//import com.zyin.zyinhud.modules.Compass;
//import com.zyin.zyinhud.modules.Coordinates;
//import com.zyin.zyinhud.modules.DistanceMeasurer;
//import com.zyin.zyinhud.modules.DurabilityInfo;
////import com.zyin.zyinhud.modules.EatingAid;
//import com.zyin.zyinhud.modules.EnderPearlAid;
//import com.zyin.zyinhud.modules.Fps;
//import com.zyin.zyinhud.modules.HealthMonitor;
//import com.zyin.zyinhud.modules.InfoLine;
//import com.zyin.zyinhud.modules.ItemSelector;
//import com.zyin.zyinhud.modules.Miscellaneous;
//import com.zyin.zyinhud.modules.PlayerLocator;
//import com.zyin.zyinhud.modules.PotionAid;
//import com.zyin.zyinhud.modules.PotionTimers;
//import com.zyin.zyinhud.modules.QuickDeposit;
//import com.zyin.zyinhud.modules.SafeOverlay;
//import com.zyin.zyinhud.modules.TorchAid;
//import com.zyin.zyinhud.modules.WeaponSwapper;
//import com.zyin.zyinhud.util.Localization;
//
//import static com.zyin.zyinhud.keyhandlers.ZyinHUDKeyHandlerBase.mc;
//
///**
// * This is the options GUI which is used to change any configurable setting while in game.
// * <p>
// * The tabs on the left side represent the various modules and more can be added using the
// * tabbedButtonNames and tabbedButtonIDs variables, then adding functionality to the actionPerformed()
// * method to draw additional buttons specific to the mod.
// * <p>
// * There are 5 types of buttons we use in this GUI:<br>
// * <ol>
// * <li>"Enabled" button (GuiButton)
// * <li>"Mode" button (GuiButton)
// * <li>"Boolean" button (GuiButton)
// * <li>"Slider" button (GuiNumberSlider) *custom*
// * <li>"Hotkey" button (GuiHotkeyButton) *custom*
// * </ol>
// * See existing examples on how to use these.
// * <p>
// * We are able to access this screen by using a hotkey (Ctrl + Alt + Z), or navigating through the
// * default options window. We put an additional button into the Options window by replacing the normal
// * GuiOptions class with our custom OverrideGuiOptions class.
// * <p>
// * In order to get the GuiNumberSlider to work when we click and drag it, we override and modify 3 methods:
// * mouseClicked(), mouseMovedOrUp(), and actionPerformed_MouseUp().
// * <p>
// * GuiHotkeyButton is a class used to assign hotkeys. It relies on GuiZyinHUDOptions to function properly.
// */
//@SuppressWarnings("RedundantCast")
//public class GuiZyinHUDOptions extends GuiTooltipScreen {
//	public static final String hotkeyDescription = "key.zyinhud.zyinhudoptions";
//
//	protected Screen parentGuiScreen;
//	protected static Button zyinHudOptionsButton;
//
//	/**
//	 * The title string that is displayed in the top-center of the screen.
//	 */
//	protected String screenTitle;
//
//	/**
//	 * The button that was just pressed.
//	 */
//	protected Button selectedButton;
//
//
//	protected static final int[] rightClickableButtonsIDs = new int[]{
//		202,    //Clock mode
//		304,    //Coordinate mode
//		1005,    //Potion Timers text mode
//		1110,    //Durability info text mode
//		1303,    //Eating Aid mode
//		1704,    //Item Selector mode
//		1802    //Health Monitor mode
//	};
//
//	protected Object[][] tabbedButtons = {
//		{2000, Localization.get("miscellaneous.name"), null},
//		{100, Localization.get("infoline.name"), null},
//		{200, Localization.get("clock.name"), null},
//		{300, Localization.get("coordinates.name"), getKeyBindingAsString(1)},
//		{400, Localization.get("compass.name"), null},
//		{500, Localization.get("fps.name"), null},
//		{600, Localization.get("distancemeasurer.name"), getKeyBindingAsString(2)},
//		{700, Localization.get("safeoverlay.name"), getKeyBindingAsString(8)},
//		{800, Localization.get("playerlocator.name"), getKeyBindingAsString(5)},
//		{900, Localization.get("animalinfo.name"), getKeyBindingAsString(0)},
//		{1100, Localization.get("durabilityinfo.name"), null},
//		{1000, Localization.get("potiontimers.name"), null},
//		{1200, Localization.get("enderpearlaid.name"), getKeyBindingAsString(4)},
////            {1300, Localization.get("eatingaid.name"), getKeyBindingAsString(3)},
//		{1400, Localization.get("potionaid.name"), getKeyBindingAsString(6)},
//		{1900, Localization.get("torchaid.name"), null},
//		{1500, Localization.get("weaponswapper.name"), getKeyBindingAsString(9)},
//		{1600, Localization.get("quickdeposit.name"), getKeyBindingAsString(7)},
//		{1700, Localization.get("itemselector.name"), getKeyBindingAsString(11)},
//		{1800, Localization.get("healthmonitor.name"), null}
//	};
//
//	private GuiHotkeyButton currentlySelectedHotkeyButton;
//	private static Button currentlySelectedTabButton = null;
//	private final String currentlySelectedTabButtonColor = TextFormatting.YELLOW.toString();
//
//
//	//variables influencing the placement/sizing of the tab buttons on the left
//	protected int tabbedButtonX;
//	protected int tabbedButtonY;
//	protected int tabbedButtonWidth;
//	protected int tabbedButtonHeight;
//	protected int tabbedButtonSpacing;
//
//	protected int pagingButtonWidth;
//	protected int pagingButtonHeight;
//
//	/**
//	 * The current tab page. It is 0 indexed.
//	 */
//	protected static int tabbedPage = 0;
//
//	/**
//	 * The amount of tabs shown on each page.
//	 */
//	protected static int tabbedPageSize = 12;
//	protected static int tabbedMaxPages;
//
//
////variables influencing the placement/sizing of the buttons inside each tab
//	/**
//	 * The Button y.
//	 */
//	protected int buttonY;
//	/**
//	 * The Button width.
//	 */
//	protected int buttonWidth;
//	/**
//	 * The Button width double.
//	 */
//	protected int buttonWidth_double;
//	/**
//	 * The Button height.
//	 */
//	protected int buttonHeight;
//	/**
//	 * The Button spacing.
//	 */
//	protected int buttonSpacing;
//
//
//	/**
//	 * Instantiates a new Gui zyin hud options.
//	 *
//	 * @param parentGuiScreen the parent gui screen
//	 * @param titleIn         the screen title
//	 */
//	public GuiZyinHUDOptions(Screen parentGuiScreen, ITextComponent titleIn) {
//		super(titleIn);
//		this.parentGuiScreen = parentGuiScreen;
//		tabbedMaxPages = (int) Math.ceil((double) (tabbedButtons.length) / tabbedPageSize);
//	}
//
//	public GuiZyinHUDOptions(Screen parentGuiScreen) {
//		this(parentGuiScreen, null);
//	}
//
//	/**
//	 * Used to insert buttons into the vanilla GuiOptions screen
//	 *
//	 * @param event the event
//	 */
//	public static void initGuiEventPost(InitGuiEvent.Post event) {
//		int width = event.getGui().width;
//		int height = event.getGui().height;
//
//		if (event.getGui() instanceof OptionsScreen && Minecraft.getInstance().world != null) {
//			zyinHudOptionsButton = new Button(
//				width / 2 + 5, height / 6 + 24 - 6, 150, 20,
//				Localization.get("gui.override.options.buttons.options"),
//				(button) -> Minecraft.getInstance().displayGuiScreen(new GuiZyinHUDOptions(event.getGui()))
//			);
//			event.addWidget(zyinHudOptionsButton);  //???: no clue if this is what i needed to do
//		}
//	}
//
//
//	/**
//	 * @param keyBindingIndex the index in <code>ZyinHUDKeyHandlers.KEY_BINDINGS[]</code>
//	 * @return hotkey as a string
//	 */
//	private String getKeyBindingAsString(int keyBindingIndex) {
//		try {
//			return ZyinHUDKeyHandlers.KEY_BINDINGS[keyBindingIndex].getLocalizedName(); //was getDisplayName
//		}
//		catch (ArrayIndexOutOfBoundsException e) {
//			return "[?]";    //A user reported having getKeyCode() returning -89 and causing this exception
//		}
//	}
//
//	/**
//	 * Used to capture when the "Zyin HUD..." button is clicked in the vanilla GuiOptions screen
//	 *
//	 * @param event the event
//	 */
////    public static void actionPerformedEventPost(ActionPerformedEvent.Post event) {
////        if (event.getGui() instanceof OptionsScreen) {
////            if (zyinHudOptionsButton != null && event.getButton().id == zyinHudOptionsButton.id) {
////                Minecraft.getInstance().displayGuiScreen(new GuiZyinHUDOptions(event.getGui()));
////            }
////        }
////    }
//
//	/**
//	 * Adds the buttons (and other controls) to the screen in question.
//	 */
//	public void initGui() {
//		//button variables
//		buttonSpacing = 2;
//		buttonWidth = 130;
//		buttonWidth_double = buttonWidth * 2 + buttonSpacing * 2;
//
//		//tabbed button variables
//		tabbedButtonSpacing = 0;
//		tabbedButtonWidth = 130;
//		tabbedButtonHeight = 14;
//		tabbedButtonX = width / 2 - (tabbedButtonWidth + buttonWidth_double) / 2;
//		tabbedButtonY = (int) (height * 0.13);    //0.16
//
//		//button variables
//		buttonHeight = 20;
//		buttonY = (int) (height * 0.13);    //0.17
//
//		//paging buttons
//		pagingButtonWidth = 15;
//		pagingButtonHeight = 14;
//
//		screenTitle = Localization.get("gui.options.title");
//
//		drawAllButtons();
//
//		//simulate a click on the last tabbed button that was clicked to re-open it
//		actionPerformed(currentlySelectedTabButton, 0);
//	}
//
//	/**
//	 * Draw all buttons.
//	 */
//	protected void drawAllButtons() {
//		this.zLevel = 0f;
//
//		this.buttons.clear();
//		//currentlySelectedTabButton = null;
//		drawOtherButtons();
//		drawTabbedButtons();
//	}
//
//	/**
//	 * Other misc text that is rendered on various screens
//	 */
//	private void drawMiscText() {
//		if (currentlySelectedTabButton == null) {
//			int x = (int) (width - width * 0.05);
//			int y = (int) (height / 6 + 158);
//			int lineHeight = 10;
//
//			String[] text = {
//				ZyinHUD.MODNAME,
//				"v." + ZyinHUD.VERSION,
//				"",
//				"To reset values to their default",
//				"setting, delete it in the configuration",
//				"file at /.minecraft/config/ZyinHUD.cfg",
//				"",
//				"Found a bug? Want an enhancement? Submit",
//				"it to my GitHub at github.com/Zyin055/zyinhud"
//			};
//
//			for (int i = 0; i < text.length; i++) {
//				int strWidth = mc.fontRenderer.getStringWidth(text[i]);
//				int xOffset = -strWidth;
//				int yOffset = -(lineHeight * (text.length - i));
//
//				GL11.glEnable(GL11.GL_BLEND);    //for transparent text
//				mc.fontRenderer.drawString(text[i], x + xOffset, y + yOffset, 0x22ffffff);
//				GL11.glDisable(GL11.GL_BLEND);
//			}
//		}
//		else if (currentlySelectedTabButton.id == 1600)    //Quick Deposit
//		{
//			String text = TextFormatting.UNDERLINE + Localization.get("quickdeposit.options.blacklist");
//
//			int x = tabbedButtonWidth + tabbedButtonX + buttonSpacing * 2 + buttonWidth + buttonSpacing * 2 + buttonWidth / 2 - mc.fontRenderer.getStringWidth(
//				text) / 2;
//			int y = buttonY - buttonHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 3;
//
//			mc.fontRenderer.drawString(text, x, y, 0xffffff);
//		}
//	}
//
//	private void drawOtherButtons() {
//		//Save button
//		buttons.add(new Button(1, width / 2 - 100, height / 6 + 168, Localization.get("gui.options.saveandexit")));
//
//	}
//
//	private void drawTabbedButtons() {
//		//make the paging controls
//		Button prevPageButton = new Button(
//			10, tabbedButtonX, tabbedButtonY - pagingButtonHeight, pagingButtonWidth, pagingButtonHeight, "<");
//		Button nextPageButton = new Button(
//			11, tabbedButtonX + tabbedButtonWidth - pagingButtonWidth + 1, tabbedButtonY - pagingButtonHeight,
//			pagingButtonWidth, pagingButtonHeight, ">"
//		);
//
//		if (tabbedPage == 0) { prevPageButton.active = false; }
//		else if (tabbedPage == tabbedMaxPages - 1) { nextPageButton.active = false; }
//
//		//add the paging controls
//		buttons.add(prevPageButton);
//		buttons.add(nextPageButton);
//
//		int Y = tabbedButtonY;
//
//		//make the tabbed buttons
//		for (int i = 0; i < tabbedPageSize; i++) {
//			int index = (tabbedPage * tabbedPageSize + i);
//			if (index >= tabbedButtons.length) { break; }
//			int id = (Integer) tabbedButtons[index][0];
//			String buttonName = (String) tabbedButtons[index][1];
//			String buttonLabel = (String) tabbedButtons[index][2];
//
//			buttons.add(new GuiLabeledButton(id, tabbedButtonX, Y, tabbedButtonWidth, tabbedButtonHeight, buttonName,
//			                                 buttonLabel
//			));
//
//			Y += tabbedButtonHeight;
//		}
//	}
//
//	/**
//	 * Helper method for adding buttons at specific positions when a tab is clicked. This will correctly set
//	 * the button's xPosition and yPosition based on the specified row and column arguments.
//	 * There are 2 columns and 8 rows visible on screen.
//	 *
//	 * @param column values: [0, 1]
//	 * @param row    values: [0, 1, 2, 3, 4, 5, 6, 7]
//	 * @param button
//	 */
//	private void addButtonAt(int column, int row, Button button) {
//		button.x = tabbedButtonWidth + tabbedButtonX + buttonSpacing * 2 + (buttonWidth + buttonSpacing * 2) * column;
//		button.y = buttonY + (buttonHeight + buttonSpacing) * row;
//
//		buttons.add(button);
//	}
//
//	private void drawMiscellaneousButtons() {
//		addButtonAt(
//			0, 1, new Button(2002, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("miscellaneous.options.usequickplacesign",
//			                                        Miscellaneous.useQuickPlaceSign
//			                 )
//			));
//		addButtonAt(
//			0, 2, new Button(2003, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("miscellaneous.options.useunlimitedsprinting",
//			                                        Miscellaneous.UseUnlimitedSprinting
//			                 )
//			));
//		addButtonAt(
//			0, 3, new Button(2004, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("miscellaneous.options.showanvilrepairs",
//			                                        Miscellaneous.showAnvilRepairs
//			                 )
//			));
//	}
//
//	private void drawInfoLineButtons() {
//		addButtonAt(0, 0, new Button(101, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(InfoLine.Enabled)));
//		addButtonAt(
//			0, 1, new Button(102, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("infoline.options.showbiome", InfoLine.showBiome)
//			));
//		addButtonAt(
//			0, 2, new Button(105, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("infoline.options.showcansnow", InfoLine.showCanSnow)
//			));
//		addButtonAt(
//			0, 3, new Button(106, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("infoline.options.showping", InfoLine.showPing)
//			));
//		addButtonAt(
//			0, 6, new GuiNumberSliderWithUndo(103, 0, 0, buttonWidth_double, buttonHeight,
//			                                  Localization.get("infoline.options.offsetx"), 1, width - 25,
//			                                  InfoLine.getHorizontalLocation(), 1f, GuiNumberSlider.Modes.INTEGER
//			));
//		addButtonAt(
//			0, 7, new GuiNumberSliderWithUndo(104, 0, 0, buttonWidth_double, buttonHeight,
//			                                  Localization.get("infoline.options.offsety"), 1, height - 8,
//			                                  InfoLine.getVerticalLocation(), 1f, GuiNumberSlider.Modes.INTEGER
//			));
//	}
//
//	private void drawClockButtons() {
//		addButtonAt(0, 0, new Button(201, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(Clock.Enabled)));
//		addButtonAt(
//			0, 1, new Button(202, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Mode(Clock.Mode.getFriendlyName())));
//	}
//
//	private void drawCoordinatesButtons() {
//		addButtonAt(
//			0, 0, new Button(301, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(Coordinates.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(303, 0, 0, buttonWidth, buttonHeight, CoordinatesKeyHandler.hotkeyDescription));
//		addButtonAt(
//			0, 2, new Button(304, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Mode(Coordinates.Mode.getFriendlyName())
//			));
//		addButtonAt(
//			0, 3, new Button(302, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("coordinates.options.useycoordinatecolors",
//			                                        Coordinates.useYCoordinateColors
//			                 )
//			));
//		addButtonAt(
//			0, 4, new Button(305, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("coordinates.options.showchunkcoordinates",
//			                                        Coordinates.showChunkCoordinates
//			                 )
//			));
//
//	}
//
//	private void drawCompassButtons() {
//		addButtonAt(0, 0, new Button(401, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(Compass.Enabled)));
//	}
//
//	private void drawFPSButtons() {
//		addButtonAt(0, 0, new Button(501, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(Fps.Enabled)));
//	}
//
//	private void drawDistanceMeasurerButtons() {
//		addButtonAt(
//			0, 0, new Button(601, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(DistanceMeasurer.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(602, 0, 0, buttonWidth, buttonHeight,
//			                          DistanceMeasurerKeyHandler.hotkeyDescription
//			));
//	}
//
//	private void drawSafeOverlayButtons() {
//		addButtonAt(
//			0, 0, new Button(701, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(SafeOverlay.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(702, 0, 0, buttonWidth, buttonHeight, SafeOverlayKeyHandler.hotkeyDescription));
//		addButtonAt(
//			0, 2, new GuiNumberSliderWithUndo(703, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("safeoverlay.options.drawdistance"),
//			                                  SafeOverlay.minDrawDistance, SafeOverlay.maxDrawDistance,
//			                                  SafeOverlay.instance.GetDrawDistance(), SafeOverlay.defaultDrawDistance,
//			                                  GuiNumberSlider.Modes.INTEGER
//			));
//		addButtonAt(
//			0, 3, new GuiNumberSliderWithUndo(704, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("safeoverlay.options.transparency"),
//			                                  SafeOverlay.instance.GetUnsafeOverlayMinTransparency(),
//			                                  SafeOverlay.instance.GetUnsafeOverlayMaxTransparency(),
//			                                  SafeOverlay.instance.getUnsafeOverlayTransparency(), 0.3f,
//			                                  GuiNumberSlider.Modes.PERCENT
//			));
//		addButtonAt(
//			0, 4, new Button(705, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("safeoverlay.options.displayinnether",
//			                                        SafeOverlay.instance.getDisplayInNether()
//			                 )
//			));
//		addButtonAt(
//			0, 5, new Button(706, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("safeoverlay.options.seethroughwalls",
//			                                        SafeOverlay.instance.GetSeeUnsafePositionsThroughWalls()
//			                 )
//			));
//	}
//
//	private void drawPlayerLocatorButtons() {
//		addButtonAt(
//			0, 0, new Button(801, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(PlayerLocator.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(802, 0, 0, buttonWidth, buttonHeight, PlayerLocatorKeyHandler.hotkeyDescription));
//		addButtonAt(
//			0, 2, new GuiNumberSliderWithUndo(803, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("playerlocator.options.minviewdistance"),
//			                                  PlayerLocator.minViewDistanceCutoff, PlayerLocator.maxViewDistanceCutoff,
//			                                  PlayerLocator.viewDistanceCutoff, 0f, GuiNumberSlider.Modes.INTEGER
//			));
//		addButtonAt(
//			0, 3, new Button(804, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("playerlocator.options.showdistancetoplayers",
//			                                        PlayerLocator.showDistanceToPlayers
//			                 )
//			));
//		addButtonAt(
//			0, 4, new Button(805, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("playerlocator.options.showplayerhealth",
//			                                        PlayerLocator.showPlayerHealth
//			                 )
//			));
//
//		addButtonAt(
//			1, 0, new Button(808, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("playerlocator.options.showwitherskeletons",
//			                                        PlayerLocator.showWitherSkeletons
//			                 )
//			));
//		addButtonAt(
//			1, 1, new Button(806, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("playerlocator.options.showwolves", PlayerLocator.showWolves)
//			));
//		addButtonAt(
//			1, 2, new Button(807, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("playerlocator.options.usewolfcolors", PlayerLocator.useWolfColors)
//			));
//	}
//
//	private void drawAnimalInfoButtons() {
//
//		addButtonAt(0, 0, new Button(901, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(AnimalInfo.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(902, 0, 0, buttonWidth, buttonHeight, AnimalInfoKeyHandler.hotkeyDescription));
//		addButtonAt(
//			0, 2, new GuiNumberSliderWithUndo(903, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("animalinfo.options.maxviewdistance"),
//			                                  AnimalInfo.minViewDistanceCutoff, AnimalInfo.maxViewDistanceCutoff,
//			                                  AnimalInfo.viewDistanceCutoff, 8f, GuiNumberSlider.Modes.INTEGER
//			));
//		addButtonAt(
//			0, 3, new Button(907, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("animalinfo.options.showtextbackground",
//			                                        AnimalInfo.showTextBackgrounds
//			                 )
//			));
//		addButtonAt(
//			0, 4, new GuiNumberSliderWithUndo(904, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("animalinfo.options.numdecimalsdisplayed"),
//			                                  AnimalInfo.minNumberOfDecimalsDisplayed,
//			                                  AnimalInfo.maxNumberOfDecimalsDisplayed,
//			                                  AnimalInfo.getNumberOfDecimalsDisplayed(), 1f,
//			                                  GuiNumberSlider.Modes.INTEGER
//			));
//		addButtonAt(
//			0, 5, new Button(905, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("animalinfo.options.showhorsestatsonf3menu",
//			                                        AnimalInfo.showHorseStatsOnF3Menu
//			                 )
//			));
//		addButtonAt(
//			0, 6, new Button(906, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("animalinfo.options.showhorsestatsoverlay",
//			                                        AnimalInfo.showHorseStatsOverlay
//			                 )
//			));
//
//		addButtonAt(
//			1, 0, new Button(916, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("animalinfo.options.showbreedingicons",
//			                                        AnimalInfo.showBreedingIcons
//			                 )
//			));
//		//addButtonAt(1, 1, new GuiButton(917, 0, 0, buttonWidth, buttonHeight, GetButtonLabel_Boolean("animalinfo.options.showbreedingtimers", AnimalInfo.showBreedingTimers)));
//	}
//
//	private void drawPotionTimerButtons() {
//		addButtonAt(
//			0, 0, new Button(1001, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(PotionTimers.Enabled)));
//		addButtonAt(
//			0, 1, new Button(1005, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_String("potiontimers.options.textmode",
//			                                       PotionTimers.TextMode.getFriendlyName()
//			                 )
//			));
//		addButtonAt(
//			0, 2, new Button(1002, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("potiontimers.options.showpotionicons",
//			                                        PotionTimers.showPotionIcons
//			                 )
//			));
//		addButtonAt(
//			0, 3, new Button(1007, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("potiontimers.options.hidepotioneffectsininventory",
//			                                        PotionTimers.hidePotionEffectsInInventory
//			                 )
//			));
//		addButtonAt(
//			0, 4, new Button(1008, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("potiontimers.options.hidebeaconpotioneffects",
//			                                        PotionTimers.hideBeaconPotionEffects
//			                 )
//			));
//		addButtonAt(
//			0, 5, new GuiNumberSliderWithUndo(1006, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("potiontimers.options.potionscale"), 0.5f, 4.0f,
//			                                  PotionTimers.potionScale, 1f, GuiNumberSlider.Modes.PERCENT
//			));
//		addButtonAt(
//			0, 6, new GuiNumberSliderWithUndo(1003, 0, 0, buttonWidth_double, buttonHeight,
//			                                  Localization.get("potiontimers.options.offsetx"), 1, width - 25,
//			                                  PotionTimers.getHorizontalLocation(), 1f, GuiNumberSlider.Modes.INTEGER
//			));
//		addButtonAt(
//			0, 7, new GuiNumberSliderWithUndo(1004, 0, 0, buttonWidth_double, buttonHeight,
//			                                  Localization.get("potiontimers.options.offsety"), 0, height - 10,
//			                                  PotionTimers.getVerticalLocation(), 16f, GuiNumberSlider.Modes.INTEGER
//			));
//		addButtonAt(
//			1, 0, new Button(1009, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("potiontimers.options.showvanillastatuseffecthud",
//			                                        PotionTimers.showVanillaStatusEffectHUD
//			                 )
//			));
//		addButtonAt(
//			1, 1, new Button(1010, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("potiontimers.options.showeffectname", PotionTimers.showEffectName)
//			));
//		addButtonAt(
//			1, 2, new Button(1011, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("potiontimers.options.showeffectlevel",
//			                                        PotionTimers.showEffectLevel
//			                 )
//			));
//	}
//
//	private void drawDurabilityInfoButtons() {
//		addButtonAt(
//			0, 0, new Button(1101, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(DurabilityInfo.Enabled)));
//		addButtonAt(
//			0, 1, new Button(1102, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("durabilityinfo.options.showarmordurability",
//			                                        DurabilityInfo.showArmorDurability
//			                 )
//			));
//		addButtonAt(
//			0, 2, new GuiNumberSliderWithUndo(1103, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("durabilityinfo.options.armordurabilitythreshold"), 0f,
//			                                  1f, DurabilityInfo.getArmorDurabilityDisplayThreshold(), 0.1f,
//			                                  GuiNumberSlider.Modes.PERCENT
//			));
//		addButtonAt(
//			0, 3, new Button(1111, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("durabilityinfo.options.autounequiparmor",
//			                                        DurabilityInfo.autoUnequipArmor
//			                 )
//			));
//		addButtonAt(
//			0, 4, new Button(1104, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("durabilityinfo.options.showindividualarmoricons",
//			                                        DurabilityInfo.showIndividualArmorIcons
//			                 )
//			));
//		addButtonAt(
//			0, 5, new GuiNumberSliderWithUndo(1114, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("durabilityinfo.options.durabilityscale"), 0.5f, 4.0f,
//			                                  DurabilityInfo.durabilityScale, 1.0f, GuiNumberSlider.Modes.PERCENT
//			));
//		addButtonAt(
//			0, 6, new GuiNumberSliderWithUndo(1108, 0, 0, buttonWidth_double, buttonHeight,
//			                                  Localization.get("durabilityinfo.options.offsetx"), 0,
//			                                  width - DurabilityInfo.toolX, DurabilityInfo.durabilityLocX, 30f,
//			                                  GuiNumberSlider.Modes.INTEGER
//			));
//		addButtonAt(
//			0, 7, new GuiNumberSliderWithUndo(1109, 0, 0, buttonWidth_double, buttonHeight,
//			                                  Localization.get("durabilityinfo.options.offsety"), 0,
//			                                  height - DurabilityInfo.toolY, DurabilityInfo.durabilityLocY, 20f,
//			                                  GuiNumberSlider.Modes.INTEGER
//			));
//
//		addButtonAt(
//			1, 0, new Button(1113, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("durabilityinfo.options.usecolorednumbers",
//			                                        DurabilityInfo.useColoredNumbers
//			                 )
//			));
//		addButtonAt(
//			1, 1, new Button(1105, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("durabilityinfo.options.showitemdurability",
//			                                        DurabilityInfo.showItemDurability
//			                 )
//			));
//		addButtonAt(
//			1, 2, new GuiNumberSliderWithUndo(1106, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("durabilityinfo.options.itemdurabilitythreshold"), 0f,
//			                                  1f, DurabilityInfo.getItemDurabilityDisplayThreshold(), 0.1f,
//			                                  GuiNumberSlider.Modes.PERCENT
//			));
//		addButtonAt(
//			1, 3, new Button(1112, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("durabilityinfo.options.autounequiptools",
//			                                        DurabilityInfo.autoUnequipTools
//			                 )
//			));
//		addButtonAt(
//			1, 4, new Button(1110, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_String("durabilityinfo.options.textmode",
//			                                       DurabilityInfo.TextMode.getFriendlyName()
//			                 )
//			));
//		addButtonAt(
//			1, 5, new Button(1118, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("durabilityinfo.options.hidedurabilityinfoinchat",
//			                                        DurabilityInfo.hideDurabilityInfoInChat
//			                 )
//			));
//	}
//
//	private void drawEnderPearlAidButtons() {
//		addButtonAt(
//			0, 0, new Button(1201, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(EnderPearlAid.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(1202, 0, 0, buttonWidth, buttonHeight,
//			                          EnderPearlAidKeyHandler.hotkeyDescription
//			));
//	}
//
////    private void drawEatingAidButtons() {
////        addButtonAt(0, 0, new Button(1301, 0, 0, buttonWidth, buttonHeight, GetButtonLabel_Enabled(EatingAid.Enabled)));
////        addButtonAt(0, 1, new GuiHotkeyButton(1302, 0, 0, buttonWidth, buttonHeight, EatingAidKeyHandler.hotkeyDescription));
////        addButtonAt(0, 2, new Button(1303, 0, 0, buttonWidth, buttonHeight, GetButtonLabel_Mode(EatingAid.Mode.getFriendlyName())));
////        addButtonAt(0, 3, new Button(1304, 0, 0, buttonWidth, buttonHeight, GetButtonLabel_Boolean("eatingaid.options.eatgoldenfood", EatingAid.eatGoldenFood)));
////        addButtonAt(0, 4, new Button(1306, 0, 0, buttonWidth, buttonHeight, GetButtonLabel_Boolean("eatingaid.options.eatrawfood", EatingAid.eatRawFood)));
////        addButtonAt(0, 5, new Button(1305, 0, 0, buttonWidth, buttonHeight, GetButtonLabel_Boolean("eatingaid.options.prioritizefoodinhotbar", EatingAid.prioritizeFoodInHotbar)));
////
////        addButtonAt(1, 0, new Button(1307, 0, 0, buttonWidth, buttonHeight, GetButtonLabel_Boolean("eatingaid.options.usepvpsoup", EatingAid.usePvPSoup)));
////    }
//
//	private void drawPotionAidButtons() {
//		addButtonAt(0, 0, new Button(1401, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(PotionAid.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(1402, 0, 0, buttonWidth, buttonHeight, PotionAidKeyHandler.hotkeyDescription));
//	}
//
//	private void drawWeaponSwapperButtons() {
//		addButtonAt(
//			0, 0, new Button(1501, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(WeaponSwapper.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(1502, 0, 0, buttonWidth, buttonHeight,
//			                          WeaponSwapperKeyHandler.hotkeyDescription
//			));
//	}
//
//	private void drawQuickDepositButtons() {
//		addButtonAt(
//			0, 0, new Button(1601, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(QuickDeposit.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(1602, 0, 0, buttonWidth, buttonHeight, QuickDepositKeyHandler.hotkeyDescription));
//		addButtonAt(
//			0, 2, new Button(1603, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.ignoreitemsinhotbar",
//			                                        QuickDeposit.IgnoreItemsInHotbar
//			                 )
//			));
//		addButtonAt(
//			0, 3, new Button(1604, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.closechestafterdepositing",
//			                                        QuickDeposit.CloseChestAfterDepositing
//			                 )
//			));
//
//		addButtonAt(
//			1, 0, new Button(1605, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.blacklisttorch", QuickDeposit.blacklistTorch)
//			));
//		addButtonAt(
//			1, 1, new Button(1612, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.blacklisttools", QuickDeposit.blacklistTools)
//			));
//		addButtonAt(
//			1, 2, new Button(1611, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.blacklistweapons",
//			                                        QuickDeposit.blacklistWeapons
//			                 )
//			));
//		addButtonAt(
//			1, 3, new Button(1606, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.blacklistarrow", QuickDeposit.blacklistArrow)
//			));
//		addButtonAt(
//			1, 4, new Button(1607, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.blacklistfood", QuickDeposit.blacklistFood)
//			));
//		addButtonAt(
//			1, 5, new Button(1608, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.blacklistenderpearl",
//			                                        QuickDeposit.blacklistEnderPearl
//			                 )
//			));
//		addButtonAt(
//			1, 6, new Button(1609, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.blacklistwaterbucket",
//			                                        QuickDeposit.blacklistWaterBucket
//			                 )
//			));
//		addButtonAt(
//			1, 7, new Button(1610, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("quickdeposit.options.blacklistclockcompass",
//			                                        QuickDeposit.blacklistClockCompass
//			                 )
//			));
//	}
//
//	private void drawItemSelectorButtons() {
//		addButtonAt(
//			0, 0, new Button(1701, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(ItemSelector.Enabled)));
//		addButtonAt(
//			0, 1, new GuiHotkeyButton(1702, 0, 0, buttonWidth, buttonHeight, ItemSelectorKeyHandler.hotkeyDescription));
//		addButtonAt(
//			0, 2, new Button(1704, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Mode(ItemSelector.Mode.getFriendlyName())
//			));
//		addButtonAt(
//			0, 3, new GuiNumberSliderWithUndo(1703, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("itemselector.options.ticks"), ItemSelector.minTimeout,
//			                                  ItemSelector.maxTimeout, ItemSelector.getTimeout(), 200f,
//			                                  GuiNumberSlider.Modes.INTEGER
//			));
//		addButtonAt(
//			0, 4, new Button(1705, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("itemselector.options.sideButtons",
//			                                        ItemSelector.useMouseSideButtons
//			                 )
//			));
//	}
//
//	private void drawHealthMonitorButtons() {
//		addButtonAt(
//			0, 0, new Button(1801, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(HealthMonitor.Enabled)));
//		addButtonAt(
//			0, 1, new Button(1802, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Mode(HealthMonitor.Mode.getFriendlyName())
//			));
//		addButtonAt(
//			0, 2, new Button(1804, 0, 0, buttonWidth, buttonHeight,
//			                 getButtonLabel_Boolean("healthmonitor.options.playfasterneardeath",
//			                                        HealthMonitor.playFasterNearDeath
//			                 )
//			));
//		addButtonAt(
//			0, 3, new GuiNumberSliderWithUndo(1805, 0, 0, buttonWidth_double, buttonHeight,
//			                                  Localization.get("healthmonitor.options.lowhealthsoundthreshold"), 1, 20,
//			                                  HealthMonitor.getLowHealthSoundThreshold(), 6f,
//			                                  GuiNumberSlider.Modes.INTEGER
//			));
//
//		addButtonAt(
//			1, 1, new Button(1803, 0, 0, buttonWidth / 2, buttonHeight,
//			                 Localization.get("healthmonitor.options.mode.play")
//			));
//		addButtonAt(
//			1, 2, new GuiNumberSliderWithUndo(1806, 0, 0, buttonWidth, buttonHeight,
//			                                  Localization.get("healthmonitor.options.volume"), 0, 1,
//			                                  HealthMonitor.getVolume(), 1f, GuiNumberSlider.Modes.PERCENT
//			));
//	}
//
//	private void drawTorchAidButtons() {
//		addButtonAt(0, 0, new Button(1901, 0, 0, buttonWidth, buttonHeight, getButtonLabel_Enabled(TorchAid.Enabled)));
//	}
//
//	/**
//	 * Helper method to get the text for a button that toggles between modes for a module.
//	 *
//	 * @param modeName The friendly name of the mode (use Mode.toString())
//	 * @return a String to be used as the button label
//	 */
//	private static String getButtonLabel_Mode(String modeName) {
//		return Localization.get("gui.options.mode") + modeName;
//	}
//
//	/**
//	 * Helper method to get the text for a button that toggles the module on and off.
//	 *
//	 * @param enabled the current enabled/disabled boolean status of the module
//	 * @return a color coded String to be used as the button label
//	 */
//	private static String getButtonLabel_Enabled(boolean enabled) {
//		if (enabled) {
//			return Localization.get("gui.options.enabled") + TextFormatting.GREEN + Localization.get(
//				"options.on") + TextFormatting.WHITE;
//		}
//		else {
//			return Localization.get("gui.options.enabled") + TextFormatting.RED + Localization.get(
//				"options.off") + TextFormatting.WHITE;
//		}
//	}
//
//	/**
//	 * Helper method to get the text for a button that displays text as a value.
//	 *
//	 * @param localizationString the text from the localization file to be used as the label for the button
//	 * @param text               string value to display
//	 * @return a String to be used as the button label
//	 */
//	private static String getButtonLabel_String(String localizationString, String text) {
//		return Localization.get(localizationString) + text;
//	}
//
//	/**
//	 * Helper method to get the text for a button that toggles between true and false.
//	 *
//	 * @param localizationString the text from the localization file to be used as the label for the button
//	 * @param bool               boolean value to display
//	 * @return a String to be used as the button label
//	 */
//	private static String getButtonLabel_Boolean(String localizationString, boolean bool) {
//		if (bool) { return Localization.get(localizationString) + Localization.get("options.on"); }
//		else { return Localization.get(localizationString) + Localization.get("options.off"); }
//	}
//
//
//	/**
//	 * Called when the mouse is clicked.
//	 */
//	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
//		//this part is mostly copy/pasted from GuiScreen.mouseClicked() because we need it to call our own actionPerformed() method, not the one in GuiScreen
//		//play a sound and fire the actionPerformed() method when a button is left clicked
//		//if (mouseButton == 0)	//left click
//		//{
//		for (int l = 0; l < this.buttons.size(); ++l) {
//			Button guibutton = (Button) this.buttons.get(l);
//			if ((mouseButton == 1 && (ArrayUtils.contains(rightClickableButtonsIDs, guibutton.id))) //right click
//			    || mouseButton == 0) //left click
//			{
////                if (guibutton.mousePressed(mc, mouseX, mouseY)) {
//				if (guibutton.mouseClicked(mouseX, mouseY, 0)) {
//					selectedButton = guibutton;
//					guibutton.playDownSound(mc.getSoundHandler());
//					actionPerformed(guibutton, mouseButton);
//				}
//			}
//		}
//		//}
//	}
//
////    protected void mouseReleased(int mouseX, int mouseY, int state) {
////        if (this.selectedButton != null && state == 0)    //released the mouse click
////        {
////            this.selectedButton.mouseReleased(mouseX, mouseY);
////            this.selectedButton = null;
////        }
////    }
//
//	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
//		if (clickedMouseButton == 0) {    //left click
//			if (selectedButton != null && selectedButton instanceof GuiNumberSlider) {
//				//continuously apply updates for any GuiNumberSlider buttons as they are being dragged
//				actionPerformed(selectedButton, clickedMouseButton);
//			}
//		}
//	}
//
//	/**
//	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
//	 * In this method we handle every buttons action.
//	 *
//	 * @param button      the button
//	 * @param mouseButton 0 = left click, 1 = right click
//	 */
//	protected void actionPerformed(Button button, int mouseButton) {
//		if (button != null && button.active) {
//			/////////////////////////////////////////////////////////////////////////
//			// Tab buttons
//			/////////////////////////////////////////////////////////////////////////
//
//			if (button.id % 100 == 0)    //clicked one of the tabs
//			{
//				drawAllButtons();
//
//				Button clickedButton = getButtonById(button.id);
//				if (clickedButton != null) {
//					currentlySelectedTabButton = clickedButton;
//					currentlySelectedHotkeyButton = null;
//
//					//show this button as selected by changing it's color
//					clickedButton.setMessage(currentlySelectedTabButtonColor + clickedButton.getMessage());
//				}
//			}
//
//			switch (button.id) {
//				/////////////////////////////////////////////////////////////////////////
//				// Misc
//				/////////////////////////////////////////////////////////////////////////
//
//				case 1:    //Save
//					//the actual saving is done in onGuiClosed()
//					mc.displayGuiScreen(parentGuiScreen);
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Paging
//				/////////////////////////////////////////////////////////////////////////
//
//				case 10:    //Previous Page
//					decrementTabbedPage();
//					break;
//				case 11:    //Next Page
//					incrementTabbedPage();
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Info Line
//				/////////////////////////////////////////////////////////////////////////
//
//				case 100:
//					screenTitle = Localization.get("infoline.name");
//					drawInfoLineButtons();
//					break;
//				case 101:    //Enable/Disable
//					InfoLine.toggleEnabled();
//					button.setMessage(getButtonLabel_Enabled(InfoLine.Enabled));
//					break;
//				case 102:    //Show Biome
//					InfoLine.toggleShowBiome();
//					button.displayString = getButtonLabel_Boolean("infoline.options.showbiome", InfoLine.showBiome);
//					break;
//				case 105:    //Show if it can snow
//					InfoLine.toggleShowCanSnow();
//					button.displayString = getButtonLabel_Boolean("infoline.options.showcansnow", InfoLine.showCanSnow);
//					break;
//				case 106:    //Show Ping
//					InfoLine.toggleShowPing();
//					button.displayString = getButtonLabel_Boolean("infoline.options.showping", InfoLine.showPing);
//					break;
//				case 103:    //Horizontal location
//					InfoLine.setHorizontalLocation(((GuiNumberSlider) button).getValueAsInteger());
//					break;
//				case 104:    //Vertical location
//					InfoLine.setVerticalLocation(((GuiNumberSlider) button).getValueAsInteger());
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Clock
//				/////////////////////////////////////////////////////////////////////////
//
//				case 200:
//					screenTitle = Localization.get("clock.name");
//					drawClockButtons();
//					break;
//				case 201:    //Enable/Disable
//					Clock.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(Clock.Enabled);
//					break;
//				case 202:    //Mode
//					Clock.Mode.ToggleMode(mouseButton == 0);
//					button.displayString = getButtonLabel_Mode(Clock.Mode.getFriendlyName());
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Coordinates
//				/////////////////////////////////////////////////////////////////////////
//
//				case 300:
//					screenTitle = Localization.get("coordinates.name");
//					drawCoordinatesButtons();
//					break;
//				case 301:    //Enable/Disable
//					Coordinates.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(Coordinates.Enabled);
//					break;
//				case 302:    //Y Colors
//					Coordinates.toggleUseYCoordinateColors();
//					button.displayString = getButtonLabel_Boolean(
//						"coordinates.options.useycoordinatecolors", Coordinates.useYCoordinateColors);
//					break;
//				case 303:    //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//				case 304:    //Mode
//					Coordinates.Modes.ToggleMode(mouseButton == 0);
//					button.displayString = getButtonLabel_Mode(Coordinates.Mode.getFriendlyName());
//					break;
//				case 305:    //Chunk coords
//					Coordinates.toggleShowChunkCoordinates();
//					button.displayString = getButtonLabel_Boolean(
//						"coordinates.options.showchunkcoordinates", Coordinates.showChunkCoordinates);
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Compass
//				/////////////////////////////////////////////////////////////////////////
//
//				case 400:
//					screenTitle = Localization.get("compass.name");
//					drawCompassButtons();
//					break;
//				case 401:    //Enable/Disable
//					Compass.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(Compass.Enabled);
//
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// FPS
//				/////////////////////////////////////////////////////////////////////////
//
//				case 500:
//					screenTitle = Localization.get("fps.name");
//					drawFPSButtons();
//					break;
//				case 501:    //Enable/Disable
//					Fps.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(Fps.Enabled);
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Distance Measurer
//				/////////////////////////////////////////////////////////////////////////
//
//				case 600:
//					screenTitle = Localization.get("distancemeasurer.name");
//					drawDistanceMeasurerButtons();
//					break;
//				case 601:    //Enable/Disable
//					DistanceMeasurer.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(DistanceMeasurer.Enabled);
//					break;
//				case 602:    //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Safe Overlay
//				/////////////////////////////////////////////////////////////////////////
//
//				case 700:
//					screenTitle = Localization.get("safeoverlay.name");
//					drawSafeOverlayButtons();
//					break;
//				case 701:    //Enable/Disable
//					SafeOverlay.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(SafeOverlay.Enabled);
//					break;
//				case 702:    //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//				case 703:    //Draw distance slider
//					int value = ((GuiNumberSlider) button).getValueAsInteger();
//					SafeOverlay.instance.setDrawDistance(value);
//					break;
//				case 704:    //Draw distance slider
//					SafeOverlay.instance.setUnsafeOverlayTransparency(((GuiNumberSlider) button).getValueAsFloat());
//					break;
//				case 705:    //Show in Nether
//					SafeOverlay.instance.toggleDisplayInNether();
//					button.displayString = getButtonLabel_Boolean(
//						"safeoverlay.options.displayinnether", SafeOverlay.instance.getDisplayInNether());
//					break;
//				case 706:    //X-ray
//					SafeOverlay.instance.toggleSeeUnsafePositionsThroughWalls();
//					button.displayString = getButtonLabel_Boolean(
//						"safeoverlay.options.seethroughwalls",
//						SafeOverlay.instance.GetSeeUnsafePositionsThroughWalls()
//					);
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Player Locator
//				/////////////////////////////////////////////////////////////////////////
//
//				case 800:
//					screenTitle = Localization.get("playerlocator.name");
//					drawPlayerLocatorButtons();
//					break;
//				case 801:    //Enable/Disable
//					PlayerLocator.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(PlayerLocator.Enabled);
//					break;
//				case 802:    //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//				case 803:    //Min view distance slider
//					PlayerLocator.viewDistanceCutoff = ((GuiNumberSlider) button).getValueAsInteger();
//					break;
//				case 804:    //Show distance to players
//					PlayerLocator.toggleShowDistanceToPlayers();
//					button.displayString = getButtonLabel_Boolean(
//						"playerlocator.options.showdistancetoplayers", PlayerLocator.showDistanceToPlayers);
//					break;
//				case 805:    //Show players health
//					PlayerLocator.toggleShowPlayerHealth();
//					button.displayString = getButtonLabel_Boolean(
//						"playerlocator.options.showplayerhealth", PlayerLocator.showPlayerHealth);
//					break;
//				case 806:    //Show tamed wolves
//					PlayerLocator.toggleShowWolves();
//					button.displayString = getButtonLabel_Boolean(
//						"playerlocator.options.showwolves", PlayerLocator.showWolves);
//					break;
//				case 807:    //Use wolf colors
//					PlayerLocator.toggleUseWolfColors();
//					button.displayString = getButtonLabel_Boolean(
//						"playerlocator.options.usewolfcolors", PlayerLocator.useWolfColors);
//					break;
//				case 808:    //Show wither skeletons
//					PlayerLocator.toggleShowWitherSkeletons();
//					button.displayString = getButtonLabel_Boolean(
//						"playerlocator.options.showwitherskeletons", PlayerLocator.showWitherSkeletons);
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Animal(Mainly Horse) Info
//				/////////////////////////////////////////////////////////////////////////
//
//				case 900:
//					screenTitle = Localization.get("animalinfo.name");
//					drawAnimalInfoButtons();
//					break;
//				case 901:    //Enable/Disable
//					AnimalInfo.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(AnimalInfo.Enabled);
//					break;
//				case 902:    //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//				case 903:    //Min view distance slider
//					AnimalInfo.viewDistanceCutoff = ((GuiNumberSlider) button).getValueAsInteger();
//					break;
//				case 904:    //Decimal slider
//					AnimalInfo.setNumberOfDecimalsDisplayed(((GuiNumberSlider) button).getValueAsInteger());
//					break;
//				case 905:    //Show on F3 menu
//					AnimalInfo.toggleShowHorseStatsOnF3Menu();
//					button.displayString = getButtonLabel_Boolean(
//						"animalinfo.options.showhorsestatsonf3menu", AnimalInfo.showHorseStatsOnF3Menu);
//					break;
//				case 906:    //Show on F3 menu
//					AnimalInfo.toggleShowHorseStatsOverlay();
//					button.displayString = getButtonLabel_Boolean(
//						"animalinfo.options.showhorsestatsoverlay", AnimalInfo.showHorseStatsOverlay);
//					break;
//				case 907:    //Show text backgrounds
//					AnimalInfo.toggleShowTextBackgrounds();
//					button.displayString = getButtonLabel_Boolean(
//						"animalinfo.options.showtextbackground", AnimalInfo.showTextBackgrounds);
//					break;
//				case 916:    //Toggle showing breeding icons
//					AnimalInfo.toggleShowBreedingIcons();
//					button.displayString = getButtonLabel_Boolean(
//						"animalinfo.options.showbreedingicons", AnimalInfo.showBreedingIcons);
//					break;
//                /*
//	            case 917:	//Toggle showing breeding timers
//	            	AnimalInfo.toggleShowBreedingTimers();
//	            	button.displayString = GetButtonLabel_Boolean("animalinfo.options.showbreedingtimers", AnimalInfo.showBreedingTimers);
//	            	break;
//	            */
//
//				/////////////////////////////////////////////////////////////////////////
//				// Potion Timers
//				/////////////////////////////////////////////////////////////////////////
//
//				case 1000:
//					screenTitle = Localization.get("potiontimers.name");
//					drawPotionTimerButtons();
//					break;
//				case 1001:    //Enable/Disable
//					PotionTimers.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(PotionTimers.Enabled);
//					break;
//				case 1002:    //Show potion icons
//					PotionTimers.toggleShowPotionIcons();
//					button.displayString = getButtonLabel_Boolean(
//						"potiontimers.options.showpotionicons", PotionTimers.showPotionIcons);
//					break;
//				case 1005:    //Toggle text mode
//					ZyinHUD.log("MODE:" + PotionTimers.TextMode);
//					PotionTimers.TextMode.ToggleMode(mouseButton == 0);
//					ZyinHUD.log("MODE:" + PotionTimers.TextMode);
//					button.displayString = getButtonLabel_String(
//						"potiontimers.options.textmode", PotionTimers.TextMode.getFriendlyName());
//					break;
//				case 1007:    //Hide default potion effects in inventory
//					PotionTimers.toggleHidePotionEffectsInInventory();
//					button.displayString = getButtonLabel_Boolean(
//						"potiontimers.options.hidepotioneffectsininventory", PotionTimers.hidePotionEffectsInInventory);
//					break;
//				case 1006:    //Potion scale slider
//					PotionTimers.potionScale = ((GuiNumberSlider) button).getValueAsFloat();
//					break;
//				case 1003:    //Horizontal location
//					PotionTimers.setHorizontalLocation(((GuiNumberSlider) button).getValueAsInteger());
//					break;
//				case 1004:    //Vertical location
//					PotionTimers.setVerticalLocation(((GuiNumberSlider) button).getValueAsInteger());
//					break;
//				case 1008:    //Beacon
//					PotionTimers.toggleHideBeaconPotionEffects();
//					button.displayString = getButtonLabel_Boolean(
//						"potiontimers.options.hidebeaconpotioneffects", PotionTimers.hideBeaconPotionEffects);
//					break;
//				case 1009:    //Vanilla Status effect HUD
//					PotionTimers.toggleShowVanillaStatusEffectHUD();
//					button.displayString = getButtonLabel_Boolean(
//						"potiontimers.options.showvanillastatuseffecthud", PotionTimers.showVanillaStatusEffectHUD);
//					break;
//				case 1010:    //Show effect name
//					PotionTimers.toggleShowEffectName();
//					button.displayString = getButtonLabel_Boolean(
//						"potiontimers.options.showeffectname", PotionTimers.showEffectName);
//					break;
//				case 1011:    //Show effect level
//					PotionTimers.toggleShowEffectLevel();
//					button.displayString = getButtonLabel_Boolean(
//						"potiontimers.options.showeffectlevel", PotionTimers.showEffectLevel);
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Durability Info
//				/////////////////////////////////////////////////////////////////////////
//
//				case 1100:
//					screenTitle = Localization.get("durabilityinfo.name");
//					drawDurabilityInfoButtons();
//					break;
//				case 1101:    //Enable/Disable
//					DurabilityInfo.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(DurabilityInfo.Enabled);
//					break;
//				case 1102:    //Enable Armor
//					DurabilityInfo.toggleShowArmorDurability();
//					button.displayString = getButtonLabel_Boolean(
//						"durabilityinfo.options.showarmordurability", DurabilityInfo.showArmorDurability);
//					break;
//				case 1103:    //Armor durability threshold slider
//					DurabilityInfo.setArmorDurabilityDisplayThreshold(((GuiNumberSlider) button).getValueAsFloat());
//					break;
//				case 1104:    //Show armor icons
//					DurabilityInfo.toggleShowIndividualArmorIcons();
//					button.displayString = getButtonLabel_Boolean(
//						"durabilityinfo.options.showindividualarmoricons", DurabilityInfo.showIndividualArmorIcons);
//					break;
//				case 1105:    //Enable Items
//					DurabilityInfo.toggleShowItemDurability();
//					button.displayString = getButtonLabel_Boolean(
//						"durabilityinfo.options.showitemdurability", DurabilityInfo.showItemDurability);
//					break;
//				case 1106:    //Item  durability threshold slider
//					DurabilityInfo.setItemDurabilityDisplayThreshold(((GuiNumberSlider) button).getValueAsFloat());
//					break;
//				case 1108:    //Horizontal location
//					DurabilityInfo.setHorizontalLocation(((GuiNumberSlider) button).getValueAsInteger());
//					break;
//				case 1109:    //Vertical location
//					DurabilityInfo.setVerticalLocation(((GuiNumberSlider) button).getValueAsInteger());
//					break;
//				case 1110:    //Toggle Text Mode
//					DurabilityInfo.TextMode.ToggleMode(mouseButton == 0);
//					button.displayString = getButtonLabel_String(
//						"durabilityinfo.options.textmode", DurabilityInfo.TextMode.getFriendlyName());
//					break;
//				case 1111:    //Auto unequip Armor
//					DurabilityInfo.toggleAutoUnequipArmor();
//					button.displayString = getButtonLabel_Boolean(
//						"durabilityinfo.options.autounequiparmor", DurabilityInfo.autoUnequipArmor);
//					break;
//				case 1112:    //Auto unequip Tools
//					DurabilityInfo.toggleAutoUnequipTools();
//					button.displayString = getButtonLabel_Boolean(
//						"durabilityinfo.options.autounequiptools", DurabilityInfo.autoUnequipTools);
//					break;
//				case 1113:    //Use colored numbers
//					DurabilityInfo.toggleUseColoredNumbers();
//					button.displayString = getButtonLabel_Boolean(
//						"durabilityinfo.options.usecolorednumbers", DurabilityInfo.useColoredNumbers);
//					break;
//				case 1114:    //Durability scale slider
//					DurabilityInfo.durabilityScale = ((GuiNumberSlider) button).getValueAsFloat();
//					break;
//				case 1118:
//					DurabilityInfo.toggleHideDurabilityInfoInChat();
//					button.displayString = getButtonLabel_Boolean(
//						"durabilityinfo.options.hidedurabilityinfoinchat", DurabilityInfo.hideDurabilityInfoInChat);
//					break;
//
//
//				/////////////////////////////////////////////////////////////////////////
//				// Ender Pearl Aid
//				/////////////////////////////////////////////////////////////////////////
//
//				case 1200:
//					screenTitle = Localization.get("enderpearlaid.name");
//					drawEnderPearlAidButtons();
//					break;
//				case 1201:    //Enabled/Disabled
//					EnderPearlAid.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(EnderPearlAid.Enabled);
//					break;
//				case 1202:    //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Eating Aid
//				/////////////////////////////////////////////////////////////////////////
//
////                case 1300:
////                    screenTitle = Localization.get("eatingaid.name");
////                    drawEatingAidButtons();
////                    break;
////                case 1301:    //Enabled/Disabled
////                    EatingAid.toggleEnabled();
////                    button.displayString = GetButtonLabel_Enabled(EatingAid.Enabled);
////                    break;
////                case 1302:    //Hotkey
////                    HotkeyButtonClicked((GuiHotkeyButton) button);
////                    break;
////                case 1303:    //Eating Mode
////                    EatingAid.Modes.ToggleMode(mouseButton == 0);
////                    button.displayString = GetButtonLabel_Mode(EatingAid.Mode.getFriendlyName());
////                    break;
////                case 1304:    //Eat golden food
////                    EatingAid.toggleEatingGoldenFood();
////                    button.displayString = GetButtonLabel_Boolean("eatingaid.options.eatgoldenfood", EatingAid.eatGoldenFood);
////                    break;
////                case 1306:    //Eat raw food
////                    EatingAid.toggleEatingRawFood();
////                    button.displayString = GetButtonLabel_Boolean("eatingaid.options.eatrawfood", EatingAid.eatRawFood);
////                    break;
////                case 1305:    //Prioritize food in hotbar
////                    EatingAid.togglePrioritizeFoodInHotbar();
////                    button.displayString = GetButtonLabel_Boolean("eatingaid.options.prioritizefoodinhotbar", EatingAid.prioritizeFoodInHotbar);
////                    break;
////                case 1307:    //Use PvP Soup
////                    EatingAid.toggleUsePvPSoup();
////                    button.displayString = GetButtonLabel_Boolean("eatingaid.options.usepvpsoup", EatingAid.usePvPSoup);
////                    break;
//
//				/////////////////////////////////////////////////////////////////////////
//				// Potion Aid
//				/////////////////////////////////////////////////////////////////////////
//
//				case 1400:
//					screenTitle = Localization.get("potionaid.name");
//					drawPotionAidButtons();
//					break;
//				case 1401:    //Enabled/Disabled
//					PotionAid.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(PotionAid.Enabled);
//					break;
//				case 1402:    //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//
//
//				/////////////////////////////////////////////////////////////////////////
//				// Weapon Swapper
//				/////////////////////////////////////////////////////////////////////////
//
//				case 1500:
//					screenTitle = Localization.get("weaponswapper.name");
//					drawWeaponSwapperButtons();
//					break;
//				case 1501:    //Enabled/Disabled
//					WeaponSwapper.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(WeaponSwapper.Enabled);
//					break;
//				case 1502:    //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//
//
//				/////////////////////////////////////////////////////////////////////////
//				// Quick Deposit
//				/////////////////////////////////////////////////////////////////////////
//
//				case 1600:
//					screenTitle = Localization.get("quickdeposit.name");
//					drawQuickDepositButtons();
//					break;
//				case 1601:    //Enabled/Disabled
//					QuickDeposit.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(QuickDeposit.Enabled);
//					break;
//				case 1602:    //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//				case 1603:    //Ignore hotbar
//					QuickDeposit.toggleIgnoreItemsInHotbar();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.ignoreitemsinhotbar", QuickDeposit.IgnoreItemsInHotbar);
//					break;
//				case 1604:    //Closes chest
//					QuickDeposit.toggleCloseChestAfterDepositing();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.closechestafterdepositing", QuickDeposit.CloseChestAfterDepositing);
//					break;
//				case 1605:    //Blacklist torches
//					QuickDeposit.toggleBlacklistTorch();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.blacklisttorch", QuickDeposit.blacklistTorch);
//					break;
//				case 1606:    //Blacklist arrows
//					QuickDeposit.toggleBlacklistArrow();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.blacklistarrow", QuickDeposit.blacklistArrow);
//					break;
//				case 1607:    //Blacklist food
//					QuickDeposit.toggleBlacklistFood();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.blacklistfood", QuickDeposit.blacklistFood);
//					break;
//				case 1608:    //Blacklist ender pearls
//					QuickDeposit.toggleBlacklistEnderPearl();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.blacklistenderpearl", QuickDeposit.blacklistEnderPearl);
//					break;
//				case 1609:    //Blacklist water buckets
//					QuickDeposit.toggleBlacklistWaterBucket();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.blacklistwaterbucket", QuickDeposit.blacklistWaterBucket);
//					break;
//				case 1610:    //Blacklist clock/compass
//					QuickDeposit.toggleBlacklistClockCompass();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.blacklistclockcompass", QuickDeposit.blacklistClockCompass);
//					break;
//				case 1611:    //Blacklist weapons
//					QuickDeposit.toggleBlacklistWeapons();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.blacklistweapons", QuickDeposit.blacklistWeapons);
//					break;
//				case 1612:    //Blacklist tools
//					QuickDeposit.toggleBlacklistTools();
//					button.displayString = getButtonLabel_Boolean(
//						"quickdeposit.options.blacklisttools", QuickDeposit.blacklistTools);
//					break;
//
//
//				/////////////////////////////////////////////////////////////////////////
//				// Item Selector
//				/////////////////////////////////////////////////////////////////////////
//				case 1700:
//					screenTitle = Localization.get("itemselector.name");
//					drawItemSelectorButtons();
//					break;
//				case 1701:  //Enabled/Disabled
//					ItemSelector.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(ItemSelector.Enabled);
//					break;
//				case 1702:  //Hotkey
//					hotkeyButtonClicked((GuiHotkeyButton) button);
//					break;
//				case 1703:  //Ticks slider
//					int itemSelectorTicks = ((GuiNumberSlider) button).getValueAsInteger();
//					ItemSelector.setTimeout(itemSelectorTicks);
//					break;
//				case 1704:  //Mode
//					ItemSelector.Modes.ToggleMode(mouseButton == 0);
//					button.displayString = getButtonLabel_Mode(ItemSelector.Mode.getFriendlyName());
//					break;
//				case 1705:  //Side buttons
//					ItemSelector.toggleUseMouseSideButtons();
//					button.displayString = getButtonLabel_Boolean(
//						"itemselector.options.sideButtons", ItemSelector.useMouseSideButtons);
//					break;
//
//
//				/////////////////////////////////////////////////////////////////////////
//				// Health Monitor
//				/////////////////////////////////////////////////////////////////////////
//
//				case 1800:
//					screenTitle = Localization.get("healthmonitor.name");
//					drawHealthMonitorButtons();
//					break;
//				case 1801:    //Enable/Disable
//					HealthMonitor.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(HealthMonitor.Enabled);
//					break;
//				case 1802:    //Mode
//					HealthMonitor.Modes.ToggleMode(mouseButton == 0);
//					HealthMonitor.playLowHealthSound();
//					button.displayString = getButtonLabel_Mode(HealthMonitor.Mode.getFriendlyName());
//					break;
//				case 1803:    //Play sound
//					HealthMonitor.playLowHealthSound();
//					break;
//				case 1804:    //Play faster near death
//					HealthMonitor.togglePlayFasterNearDeath();
//					button.displayString = getButtonLabel_Boolean(
//						"healthmonitor.options.playfasterneardeath", HealthMonitor.playFasterNearDeath);
//					break;
//				case 1805:    //Low Health Sound Threshold
//					HealthMonitor.setLowHealthSoundThreshold(((GuiNumberSlider) button).getValueAsInteger());
//					break;
//				case 1806:    //Volume
//					HealthMonitor.setVolume(((GuiNumberSlider) button).getValueAsFloat());
//					break;
//
//
//				/////////////////////////////////////////////////////////////////////////
//				// Torch Aid
//				/////////////////////////////////////////////////////////////////////////
//
//				case 1900:
//					screenTitle = Localization.get("torchaid.name");
//					drawTorchAidButtons();
//					break;
//				case 1901:    //Enable/Disable
//					TorchAid.toggleEnabled();
//					button.displayString = getButtonLabel_Enabled(TorchAid.Enabled);
//					break;
//
//
//				/////////////////////////////////////////////////////////////////////////
//				// Miscellaneous
//				/////////////////////////////////////////////////////////////////////////
//
//				case 2000:
//					screenTitle = Localization.get("miscellaneous.name");
//					drawMiscellaneousButtons();
//					break;
//				case 2001:    //do nothing; enhanced middle click is now part of vanilla
//					break;
//				case 2002:    //Use quick place sign
//					Miscellaneous.toggleUseQuickPlaceSign();
//					button.displayString = getButtonLabel_Boolean(
//						"miscellaneous.options.usequickplacesign", Miscellaneous.useQuickPlaceSign);
//					break;
//				case 2003:    //Use unlimited sprinting
//					Miscellaneous.toggleUseUnlimitedSprinting();
//					button.displayString = getButtonLabel_Boolean(
//						"miscellaneous.options.useunlimitedsprinting", Miscellaneous.UseUnlimitedSprinting);
//					break;
//				case 2004:    //Show anvil repairs
//					Miscellaneous.toggleShowAnvilRepairs();
//					button.displayString = getButtonLabel_Boolean(
//						"miscellaneous.options.showanvilrepairs", Miscellaneous.showAnvilRepairs);
//					break;
//
//
//			}
//		}
//	}
//
//
//	protected String getButtonTooltip(int buttonId) {
//		//this is where we set all of our button tooltips
//		switch (buttonId) {
//			case 100:
//				return Localization.get("infoline.options.tooltip");
//			case 105:
//				return Localization.get("infoline.options.showcansnow.tooltip");
//			case 202:
//				return Localization.get("clock.options.mode.tooltip");
//			case 300:
//				return Localization.get("coordinates.options.tooltip");
//			case 302:
//				return Localization.get("coordinates.options.useycoordinatecolors.tooltip");
//			case 303:
//				return Localization.get("coordinates.options.hotkey.tooltip");
//			case 305:
//				return Localization.get("coordinates.options.showchunkcoordinates.tooltip");
//			case 700:
//				return Localization.get("safeoverlay.options.tooltip");
//			case 702:
//				return Localization.get("safeoverlay.options.hotkey.tooltip");
//			case 703:
//				return Localization.get("safeoverlay.options.drawdistance.tooltip");
//			case 705:
//				return Localization.get("safeoverlay.options.displayinnether.tooltip");
//			case 600:
//				return Localization.get("distancemeasurer.options.tooltip");
//			case 800:
//				return Localization.get("playerlocator.options.tooltip");
//			case 803:
//				return Localization.get("playerlocator.options.minviewdistance.tooltip");
//			case 806:
//				return Localization.get("playerlocator.options.showwolves.tooltip");
//			case 807:
//				return Localization.get("playerlocator.options.usewolfcolors.tooltip");
//			case 808:
//				return Localization.get("playerlocator.options.showwitherskeletons.tooltip");
//			case 900:
//				return Localization.get("animalinfo.options.tooltip");
//			case 907:
//				return Localization.get("animalinfo.options.showtextbackground.tooltip");
//			case 905:
//				return Localization.get("animalinfo.options.showhorsestatsonf3menu.tooltip");
//			case 906:
//				return Localization.get("animalinfo.options.showhorsestatsoverlay.tooltip");
//			case 916:
//				return Localization.get("animalinfo.options.showbreedingicons.tooltip");
//			//case 917: return Localization.get("animalinfo.options.showbreedingtimers.tooltip");
//			case 1000:
//				return Localization.get("potiontimers.options.tooltip");
//			case 1005:
//				return Localization.get("potiontimers.options.textmode.tooltip");
//			case 1007:
//				return Localization.get("potiontimers.options.hidepotioneffectsininventory.tooltip");
//			case 1009:
//				return Localization.get("potiontimers.options.showvanillastatuseffecthud.tooltip");
//			case 1100:
//				return Localization.get("durabilityinfo.options.tooltip");
//			case 1103:
//				return Localization.get("durabilityinfo.options.armordurabilitythreshold.tooltip");
//			case 1104:
//				return Localization.get("durabilityinfo.options.showindividualarmoricons.tooltip");
//			case 1106:
//				return Localization.get("durabilityinfo.options.itemdurabilitythreshold.tooltip");
//			case 1110:
//				return Localization.get("durabilityinfo.options.textmode.tooltip");
//			case 1111:
//				return Localization.get("durabilityinfo.options.autounequiparmor.tooltip");
//			case 1112:
//				return Localization.get("durabilityinfo.options.autounequiptools.tooltip");
//			case 1200:
//				return Localization.get("enderpearlaid.options.tooltip");
////            case 1300:
////                return Localization.get("eatingaid.options.tooltip");
////            case 1303:
////                return Localization.get("eatingaid.options.mode.tooltip");
////            case 1307:
////                return Localization.get("eatingaid.options.usepvpsoup.tooltip");
//			case 1400:
//				return Localization.get("potionaid.options.tooltip");
//			case 1500:
//				return Localization.get("weaponswapper.options.tooltip");
//			case 1503:
//				return Localization.get("weaponswapper.options.scanhotbarforweaponsfromlefttoright.tooltip");
//			case 1600:
//				return Localization.get("quickdeposit.options.tooltip");
//			case 1602:
//				return Localization.get("quickdeposit.options.hotkey.tooltip");
//			case 1603:
//				return Localization.get("quickdeposit.options.ignoreitemsinhotbar.tooltip");
//			case 1604:
//				return Localization.get("quickdeposit.options.closechestafterdepositing.tooltip");
//			case 1700:
//				return Localization.get("itemselector.options.tooltip");
//			case 1702:
//				return Localization.get("itemselector.options.hotkey.tooltip");
//			case 1703:
//				return Localization.get("itemselector.options.ticks.tooltip");
//			case 1704:
//				return Localization.get("itemselector.options.mode.tooltip");
//			case 1705:
//				return Localization.get("itemselector.options.sideButtons.tooltip");
//			case 1800:
//				return Localization.get("healthmonitor.options.tooltip");
//			case 1802:
//				return Localization.get("healthmonitor.options.mode.tooltip");
//			case 1803:
//				return Localization.get("healthmonitor.options.mode.play.tooltip");
//			case 1804:
//				return Localization.get("healthmonitor.options.playfasterneardeath.tooltip");
//			case 1900:
//				return Localization.get("torchaid.options.tooltip");
//			case 2002:
//				return Localization.get("miscellaneous.options.usequickplacesign.tooltip");
//			case 2003:
//				return Localization.get("miscellaneous.options.useunlimitedsprinting.tooltip");
//			case 2004:
//				return Localization.get("miscellaneous.options.showanvilrepairs.tooltip");
//			default:
//				return null;
//		}
//	}
//
//	/**
//	 * Helper method to keep track of any GuiHotkeyButtons we've clicked.
//	 *
//	 * @param hotkeyButton
//	 */
//	private void hotkeyButtonClicked(GuiHotkeyButton hotkeyButton) {
//		hotkeyButton.Clicked();
//		if (hotkeyButton.IsWaitingForHotkeyInput()) { currentlySelectedHotkeyButton = hotkeyButton; }
//		else { currentlySelectedHotkeyButton = null; }
//	}
//
//	/**
//	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
//	 */
//	protected void keyTyped(char key, int keycode) {
//		//if a hotkey button is waiting for input, use this key pressed and assign it to the hotkey
//		if (currentlySelectedHotkeyButton != null) {
//			if (keycode == GLFW.GLFW_KEY_ESCAPE) {
//				currentlySelectedHotkeyButton.Cancel();
//				currentlySelectedHotkeyButton = null;
//				return;
//			}
//			else { currentlySelectedHotkeyButton.ApplyHotkey(keycode); }
//		}
//
//		//if escape is pressed, then close the screen
//		if (keycode == GLFW.GLFW_KEY_ESCAPE) {
//			mc.displayGuiScreen((Screen) null);
//			mc.setGameFocused(true);
//		}
//	}
//
//	/**
//	 * Gets a reference to a GuiButton being rendered based on its ID.
//	 *
//	 * @param id the id
//	 * @return gui button
//	 */
//	public Button getButtonById(int id) {
//		for (Button aButtonList : buttonList) {
//			Button button = (Button) aButtonList;
//			if (button.id == id) { return button; }
//		}
//		return null;
//	}
//
//	/**
//	 * Determines if a button tab (buttons on the left part of the screen) is selected.
//	 *
//	 * @param buttonTabLabel Localized name of this button tab as displayed on the button itself
//	 * @return boolean
//	 */
//	public boolean isButtonTabSelected(String buttonTabLabel) {
//		return currentlySelectedTabButton != null && currentlySelectedTabButton.displayString.replace(
//			currentlySelectedTabButtonColor, "").equals(buttonTabLabel);
//	}
//
//	/**
//	 * Goes to the next page of tabbed buttons.
//	 */
//	public void incrementTabbedPage() {
//		tabbedPage++;
//		if (tabbedPage >= tabbedMaxPages) { tabbedPage = tabbedMaxPages; }
//		else {
//			screenTitle = Localization.get("gui.options.title");
//			currentlySelectedTabButton = null;
//			drawAllButtons();
//		}
//	}
//
//	/**
//	 * Goes to the previous page of tabbed buttons.
//	 */
//	public void decrementTabbedPage() {
//		tabbedPage--;
//		if (tabbedPage < 0) { tabbedPage = 0; }
//		else {
//			screenTitle = Localization.get("gui.options.title");
//			currentlySelectedTabButton = null;
//			drawAllButtons();
//		}
//	}
//
//
//	/**
//	 * Draws the screen and all the components in it.
//	 */
//	public void drawScreen(int mouseX, int mouseY, float par3) {
//		drawDefaultBackground();
//		drawCenteredString(mc.fontRenderer, screenTitle, width / 2, 15, 0xFFFFFF);
//
//		drawMiscText();
//
//		super.drawScreen(mouseX, mouseY, par3);
//	}
//
//	/**
//	 * Called when the screen is unloaded.
//	 */
//	public void onGuiClosed() {
//		ZyinHUDConfig.SaveConfigSettings();
//
//		super.onClose();
//	}
//}
