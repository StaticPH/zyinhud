package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import net.minecraft.item.Items;

import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.Localization;

/**
 * EnderPearl Aid allows the player to easily use an ender pearl on their hotbar by calling its UseEnderPearl() method.
 */
public class EnderPearlAid extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled = ZyinHUDConfig.enableEnderPearlAid.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableEnderPearlAid.set(!isEnabled);
		ZyinHUDConfig.enableEnderPearlAid.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	/**
	 * Makes the player throw an ender pearl if they have one.
	 */
	public static void useEnderPearl() {
        /*if(mc.playerController.isInCreativeMode())
        {
        	ZyinHUDRenderer.displayNotification(Localization.get("enderpearlaid.increative"));
        	return;
        }*/

//        if (EatingAid.instance.isEating())
//        {
//            EatingAid.instance.stopEating();    //it's not good if we have an ender pearl selected and hold right click down...
//        }

		boolean usedEnderPearlSuccessfully = InventoryUtil.useItem(Items.ENDER_PEARL);

		if (!usedEnderPearlSuccessfully) {
			ZyinHUDRenderer.displayNotification(Localization.get("enderpearlaid.noenderpearls"));
		}
	}
}
