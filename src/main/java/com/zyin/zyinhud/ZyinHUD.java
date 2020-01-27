/* ========================================================================================================
 *
 * README
 *
 * Zyin's HUD
 *
 * This code is all open source and you are free to, and encouraged to, do whatever you want with it.
 *
 * Adding your own functionality is (relatively) simple. First make a class in com.zyin.zyinhud.modules
 * which contains all of your module's logic. Then you need a way to interact with your module. You can
 * do this with a Tick Handler (already setup for you in ZyinHUDRenderer.java), a Hotkey (follow the
 * examples in ZyinHUDKeyHandlers.java), or a single-player only command (see com.zyin.zyinhud.command).
 *
 * To add configurable options to your module, you need to add a new tab to GuiZyinHUDOptions.java.
 * You do this by modifing the tabbedButtonNames and tabbedButtonIDs variables. Then add your new button
 * actions in the actionPerformed() method. To have these configurable options persist after logging out,
 * you need to follow the examples in ZyinHUDConfig.java to write your data to the config file.
 *
 * That's it! Make sure to check out the other classes as they have useful helper functions. If you don't
 * know how to do something, just look at how another mod does something similar to it.
 *
 * ========================================================================================================
 */

package com.zyin.zyinhud;

import com.zyin.zyinhud.command.CommandFps;
//import com.zyin.zyinhud.command.CommandZyinHUDOptions;
import com.zyin.zyinhud.modules.HealthMonitor;
import com.zyin.zyinhud.modules.Miscellaneous;
import com.zyin.zyinhud.util.ModCompatibility;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ZyinHUD.MODID)
public class ZyinHUD {
	/**
	 * Version number must be changed in 3 spots before releasing a build:
	 * <br><ol>
	 * <li>VERSION
	 * <li>src/main/resources/META-INF/mods.toml:"version" ???maybe not???
	 * <li>gradle.properties:mod_version
	 * </ol>
	 * If incrementing the Minecraft version, also update "curseFilenameParser" in AddVersionChecker()
	 */
	public static final String VERSION = "@VERSION@";
	public static final String MODID = "zyinhud";
	public static final String MODNAME = "Zyin's HUD";
	public static final String buildTime = "@BUILD_TIME@";
//    public static final String updateJSON = "https://raw.githubusercontent.com/cyilin/zyinhud-update/master/update.json";
//    public static final String dependencies = "required-after:forge@[14.23.1.2554,);";

	public static final Logger ZyinLogger = LogManager.getLogger(MODID);

//	@SuppressWarnings("Convert2MethodRef")
//    public static CommonProxy proxy = DistExecutor.runForDist(()->()->new ClientProxy(), () -> CommonProxy::new);

	public ZyinHUD() {
		//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(
			ExtensionPoint.DISPLAYTEST,
			() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true)
		);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ZyinHUDConfig.getConfigSpec());
		FMLJavaModLoadingContext.get().getModEventBus().register(ZyinHUDConfig.class);
//        AddVersionChecker();

//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postLoad);
	}

	private void setupCommon(final FMLCommonSetupEvent event) {

	}

	//    private void doSetupClient(final FMLClientSetupEvent event){
//        DistExecutor.callWhenOn(Dist.CLIENT,()->{
//            setupClient(event);
//            return null;
//        });
//    }Unload
	private void setupClient(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(ZyinHUDKeyHandlers.instance);
//        MinecraftForge.EVENT_BUS.register(ZyinHUDGuiEvents.instance);     temporarily disabled because gui stuff is HARD
		MinecraftForge.EVENT_BUS.register(ZyinHUDRenderer.instance);
		MinecraftForge.EVENT_BUS.register(Miscellaneous.instance);
		MinecraftForge.EVENT_BUS.register(HealthMonitor.instance);

		ModCompatibility.TConstruct.isLoaded = ModList.get().isLoaded("TConstruct");
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {

	}

	private void processIMC(final InterModProcessEvent event) {

	}

	/**
	 * Server starting.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event) {
	//FIXME: Perhaps command registration should be moved to the player login event PlayerLoggedInEvent...or is it the login event LoggedInEvent? need to find out the difference
		CommandFps.register(event.getCommandDispatcher());

//        CommandZyinHUDOptions.register(event.getCommandDispatcher());
	}

	@SubscribeEvent
	public void postLoad(FMLLoadCompleteEvent event){

	}

	/**
	 * Adds support for the Version Checker mod.
	 *
	 * @link http ://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2091981-version-checker-auto-update-mods-and-clean
	 */
//	public void AddVersionChecker() {
//		InterModComms.sendTo(ZyinHUD.MODID, "VersionChecker", "addCurseCheck", () -> {
//			CompoundNBT compound = new CompoundNBT();
//			//http://minecraft.curseforge.com/mc-mods/59953-zyins-hud
//			compound.putString("curseProjectName", "59953-zyins-hud");
//			compound.putString("curseFilenameParser", "ZyinsHUD-(1.9)-v.[].jar");
//			return compound;
//		});
//	}


}

