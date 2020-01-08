/* ========================================================================================================
 *
 * README
 *
 * Zyin's HUD
 *
 * This code is all open source and you are free to, and encouraged to, do whatever you want with it.
 *
 * Adding your own functionality is (relatively) simple. First make a class in com.zyin.zyinhud.mods
 * which contains all of your mods logic. Then you need a way to interact with your mod. You can
 * do this with a Tick Handler (already setup for you in ZyinHUDRenderer.java), a Hotkey (follow the
 * examples in ZyinHUDKeyHandlers.java), or a single-player only command (see com.zyin.zyinhud.command).
 *
 * To add configurable options to your mod, you need to add a new tab to GuiZyinHUDOptions.java.
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
import com.zyin.zyinhud.mods.HealthMonitor;
import com.zyin.zyinhud.mods.Miscellaneous;
import com.zyin.zyinhud.util.ModCompatibility;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import java.io.File;

/**
 * The type Zyin hud.
 */
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
    /**
     * The constant MODID.
     */
    public static final String MODID = "zyinhud";
    /**
     * The constant MODNAME.
     */
    public static final String MODNAME = "Zyin's HUD";

//    public static final String updateJSON = "https://raw.githubusercontent.com/cyilin/zyinhud-update/master/update.json";

//    public static final String dependencies = "required-after:forge@[14.23.1.2554,);";

    public static final String buildTime = "@BUILD_TIME@";



    public static final Logger ZyinLogger =  LogManager.getLogger(MODID);

    /**
     * The constant proxy.
     */
    @SuppressWarnings("Convert2MethodRef")
//    public static CommonProxy proxy = DistExecutor.runForDist(()->()->new ClientProxy(), () -> CommonProxy::new);
    // ???: Or do i want to do it like this?
//    DistExecutor.callWhenOn(Dist.CLIENT, ()->()->new CommonProxy());

    /**
     * The constant mc.
     */
    protected static final Minecraft mc = Minecraft.getInstance();

//    private File configFile;
    /**
     * Instantiates a new Zyin hud.
     */
    public ZyinHUD() {
        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(
            ExtensionPoint.DISPLAYTEST,
            () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true)
        );

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ZyinHUDConfig.getConfigSpec());
        FMLJavaModLoadingContext.get().getModEventBus().register(ZyinHUDConfig.class);

//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
    }

    private void setupCommon(final FMLCommonSetupEvent event){

    }

//    private void doSetupClient(final FMLClientSetupEvent event){
//        DistExecutor.callWhenOn(Dist.CLIENT,()->{
//            setupClient(event);
//            return null;
//        });
//    }
    private void setupClient(final FMLClientSetupEvent event){
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ZyinHUDKeyHandlers.instance);
//        MinecraftForge.EVENT_BUS.register(ZyinHUDGuiEvents.instance);     temporarily disabled because gui stuff is HARD
        MinecraftForge.EVENT_BUS.register(ZyinHUDRenderer.instance);
        MinecraftForge.EVENT_BUS.register(Miscellaneous.instance);
        MinecraftForge.EVENT_BUS.register(HealthMonitor.instance);

        ModCompatibility.TConstruct.isLoaded = ModList.get().isLoaded("TConstruct");
    }

    private void enqueueIMC(final InterModEnqueueEvent event){

    }

    private void processIMC(final InterModProcessEvent event){

    }

    /**
     * Pre init.
     *
     * @param event the event
     */
//    @EventHandler
//    public void preInit(FMLPreInitializationEvent event) {
//        configFile = event.getSuggestedConfigurationFile();
//
//        //AddVersionChecker();
//    }

//    /**
//     * Init.
//     *
//     * @param event the event
//     */
//    @EventHandler
//    public void init(FMLInitializationEvent event) {
//        ZyinLogger.info(String.format("version: %s (%s)", VERSION, buildTime));
//        //load all our Key Handlers
//        //FMLCommonHandler.instance().bus().register(ZyinHUDKeyHandlers.instance);
//        MinecraftForge.EVENT_BUS.register(ZyinHUDKeyHandlers.instance);
//
//        //load configuration settings from the ZyinHUD.cfg file
//        ZyinHUDConfig.LoadConfigSettings(configFile);
//
//        //needed for @SubscribeEvent method subscriptions:
//        //   MinecraftForge.EVENT_BUS.register()          --> is used for net.minecraftforge events
//        //   FMLCommonHandler.instance().bus().register() --> is used for cpw.mods.fml events
//        MinecraftForge.EVENT_BUS.register(this);
////        MinecraftForge.EVENT_BUS.register(ZyinHUDGuiEvents.instance);     temporarily disabled because gui stuff is HARD
//        MinecraftForge.EVENT_BUS.register(ZyinHUDRenderer.instance);
//        MinecraftForge.EVENT_BUS.register(Miscellaneous.instance);
//        MinecraftForge.EVENT_BUS.register(HealthMonitor.instance);
//    }

//    /**
//     * Post init.
//     *
//     * @param event the event
//     */
//    @EventHandler
//    public void postInit(FMLPostInitializationEvent event) {
//        ModCompatibility.TConstruct.isLoaded = ModList.get().isLoaded("TConstruct");
//    }

    /**
     * Server starting.
     *
     * @param event the event
     */
    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event) {
        //THIS EVENT IS NOT FIRED ON SMP SERVERS
        CommandFps.register(event.getCommandDispatcher());

//        CommandZyinHUDOptions.register(event.getCommandDispatcher());
    }

    /**
     * Adds support for the Version Checker mod.
     *
     * @link http ://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2091981-version-checker-auto-update-mods-and-clean
     */
    public void AddVersionChecker() {
        InterModComms.sendTo(ZyinHUD.MODID, "VersionChecker", "addCurseCheck", ()-> {
            CompoundNBT compound = new CompoundNBT();
            compound.putString("curseProjectName", "59953-zyins-hud");    //http://minecraft.curseforge.com/mc-mods/59953-zyins-hud
            compound.putString("curseFilenameParser", "ZyinsHUD-(1.9)-v.[].jar");
            return compound;
        });
    }


}

