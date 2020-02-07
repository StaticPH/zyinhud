//package com.zyin.zyinhud.keyhandlers;
//
//import com.zyin.zyinhud.modules.EatingAid;
//
//import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
//
///**
// * The type Eating aid key handler.
// */
//public class EatingAidKeyHandler implements ZyinHUDKeyHandlerBase
//{
//    /**
//     * The constant hotkeyDescription.
//     */
//    public static final String hotkeyDescription = "key.zyinhud.eatingaid";
//
//    /**
//     * onPressed.
//     *
//     * @param event the event
//     */
//    public static void onPressed(KeyInputEvent event) {
//        if (mc.currentScreen != null)
//        {
//            return;    //don't activate if the user is looking at a GUI
//        }
//
//		if (EatingAid.isEnabled)
//            EatingAid.instance.eatFood();
//	}
//}