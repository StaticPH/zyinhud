package com.zyin.zyinhud.mods;

import net.minecraft.item.Items;

import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.Localization;

/**
 * EnderPearl Aid allows the player to easily use an ender pearl on their hotbar by calling its UseEnderPearl() method.
 */
public class EnderPearlAid extends ZyinHUDModBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled;

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		return Enabled = !Enabled;
	}

	/**
	 * Makes the player throw an ender pearl if they have one.
	 */
	public static void UseEnderPearl() {
        /*if(mc.playerController.isInCreativeMode())
        {
        	ZyinHUDRenderer.DisplayNotification(Localization.get("enderpearlaid.increative"));
        	return;
        }*/

//        if (EatingAid.instance.isEating())
//        {
//            EatingAid.instance.StopEating();    //it's not good if we have an ender pearl selected and hold right click down...
//        }

		boolean usedEnderPearlSuccessfully = InventoryUtil.UseItem(Items.ENDER_PEARL);

		if (!usedEnderPearlSuccessfully) {
			ZyinHUDRenderer.DisplayNotification(Localization.get("enderpearlaid.noenderpearls"));
		}
	}
}
