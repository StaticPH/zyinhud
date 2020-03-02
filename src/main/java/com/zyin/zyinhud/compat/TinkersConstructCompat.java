package com.zyin.zyinhud.compat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

//TODO: It is very likely that most, if not all of this no longer works as once intended
//      And that's putting aside the lack of Tinkers Construct for 1.14 at this time
public class TinkersConstructCompat {     //Tinker's Construct integration
	/**
	 * The constant isLoaded.
	 */
	public static boolean isLoaded;

	/**
	 * The constant tConstructWeaponClass.
	 */
	public static final String tConstructWeaponClass = "tconstruct.library.tools.Weapon";
	/**
	 * The constant tConstructBowClass.
	 */
	public static final String tConstructBowClass = "tconstruct.items.tools.BowBase";
	/**
	 * The constant tConstructHarvestToolClass.
	 */
	public static final String tConstructHarvestToolClass = "tconstruct.library.tools.HarvestTool";
	/**
	 * The constant tConstructDualHarvestToolClass.
	 */
	public static final String tConstructDualHarvestToolClass = "tconstruct.library.tools.DualHarvestTool";

	/**
	 * Is Tinker's Construct harvest tool boolean.
	 *
	 * @param item the item
	 * @return the boolean
	 */
	public static boolean isTConstructHarvestTool(Item item) {
		if (isLoaded) {
			String className = item.getClass().getSuperclass().getName();
			return className.equals(tConstructHarvestToolClass) || className.equals(tConstructDualHarvestToolClass);
		}

		return false;
	}

	/**
	 * Is Tinker's Construct weapon boolean.
	 *
	 * @param item the item
	 * @return the boolean
	 */
	public static boolean isTConstructWeapon(Item item) {
		if (isLoaded) {
			String className = item.getClass().getSuperclass().getName();
			return className.equals(tConstructWeaponClass);
		}

		return false;
	}

	/**
	 * Is Tinker's Construct bow boolean.
	 *
	 * @param item the item
	 * @return the boolean
	 */
	public static boolean isTConstructBow(Item item) {
		if (isLoaded) {
			String className = item.getClass().getSuperclass().getName();
			return className.equals(tConstructBowClass);
		}

		return false;
	}

	/**
	 * Is Tinker's Construct item boolean.
	 *
	 * @param item the item
	 * @return the boolean
	 */
	public static boolean isTConstructItem(Item item) {
		return isTConstructHarvestTool(item) ||
		       isTConstructWeapon(item) ||
		       isTConstructBow(item);
	}

	/**
	 * Is Tinker's Construct tool without a right click action boolean.
	 *
	 * @param item the item
	 * @return the boolean
	 */
	public static boolean isTConstructToolWithoutARightClickAction(Item item) {
		if (isLoaded) {
			String className = item.getClass().getSuperclass().getName();
			return className.equals(tConstructHarvestToolClass);
			//|| className.equals(tConstructDualHarvestToolClass))	//the only DualHarvestTool is the Mattock which also tills dirt on right click
		}

		return false;
	}

	/**
	 * Get damage integer.
	 *
	 * @param itemStack the item stack
	 * @return returns the damage value of the tool, 			returns the energy if it has any, 			or returns -1 if the tool is broken.
	 */
	public static Integer getDamage(ItemStack itemStack) {
		CompoundNBT tags = itemStack.getTag();
		if (tags == null) {
			return null;
		}
//			else if (tags.contains("Energy")) {
//				return tags.getInt("Energy");
//			}
		else {
			if (tags.getCompound("InfiTool").getBoolean("Broken")) { return -1; }
			else { return tags.getCompound("InfiTool").getInt("Damage"); }
		}
	}

	/**
	 * Get max damage int.
	 *
	 * @param itemStack the item stack
	 * @return returns the max durability of the tool. 			returns 400000 if it has energy.
	 */
	public static int getMaxDamage(ItemStack itemStack) {
		CompoundNBT tags = itemStack.getTag();
		if (tags == null) {
			return -1;
		}
//			else if (tags.contains("Energy")) {
//				return 400000;    //is this right??
//			}
		else {
			return tags.getCompound("InfiTool").getInt("TotalDurability");
		}
	}
}