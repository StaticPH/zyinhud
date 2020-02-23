package com.zyin.zyinhud.modules;

import com.google.common.collect.Multimap;
import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.util.InventoryUtil;
import com.zyin.zyinhud.util.Localization;
import com.zyin.zyinhud.util.ModCompatibility;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Weapon Swap allows the player to quickly equip their sword and bow.
 */
@SuppressWarnings("ConstantConditions")
public class WeaponSwapper extends ZyinHUDModuleBase {
	private static final Logger logger = LogManager.getLogger(WeaponSwapper.class);

	/**
	 * Enables/Disables this module
	 */
	public static boolean isEnabled = ZyinHUDConfig.enableWeaponSwap.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean toggleEnabled() {
		ZyinHUDConfig.enableWeaponSwap.set(!isEnabled);
		ZyinHUDConfig.enableWeaponSwap.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return isEnabled = !isEnabled;
	}

	//private static List<Class> meleeWeaponClasses = null;
	private static List<Class> rangedWeaponClasses = null;

	/**
	 * Makes the player select their sword. If a sword is already selected, it selects the bow instead.
	 */
	public static void swapWeapons() {
		ItemStack currentItemStack = mc.player.getHeldItemMainhand(); //back when there was no off-hand, this used getHeldItem();

		initializeListOfWeaponClasses();

		int meleeWeaponSlot = getMostDamagingWeaponSlotFromHotbar();
		int rangedWeaponSlot = getBowSlotFromHotbar(rangedWeaponClasses);

		if (meleeWeaponSlot < 0 && rangedWeaponSlot < 0) {
			//we dont have a sword or a bow on the hotbar, so check our inventory

			meleeWeaponSlot = getMostDamagingWeaponSlotFromInventory();
			if (meleeWeaponSlot < 0) {
				rangedWeaponSlot = getItemSlotFromInventory(rangedWeaponClasses);
				if (rangedWeaponSlot < 0) {
					ZyinHUDRenderer.displayNotification(Localization.get("weaponswapper.noweapons"));
				}
				else {
					InventoryUtil.swap(InventoryUtil.getCurrentlySelectedItemInventoryIndex(), rangedWeaponSlot);
				}
			}
			else {
				InventoryUtil.swap(InventoryUtil.getCurrentlySelectedItemInventoryIndex(), meleeWeaponSlot);
			}
		}
		else if (meleeWeaponSlot >= 0 && rangedWeaponSlot < 0) {
			//we have a sword, but no bow
			selectHotbarSlot(meleeWeaponSlot);
		}
		else if (meleeWeaponSlot < 0 && rangedWeaponSlot >= 0) {
			//we have a bow, but no sword
			selectHotbarSlot(rangedWeaponSlot);
		}
		else {
			//we have both a bow and a sword
			if (mc.player.inventory.currentItem == meleeWeaponSlot) {
				//we are selected on the best melee weapon, so select the ranged weapon
				selectHotbarSlot(rangedWeaponSlot);
			}
			else {
				//we are not selecting the best melee weapon, so select the melee weapon
				selectHotbarSlot(meleeWeaponSlot);
			}
		}
	}

	/**
	 * Gets the inventory index of the most damaging melee weapon on the hotbar.
	 *
	 * @param minInventoryIndex the min inventory index
	 * @param maxInventoryIndex the max inventory index
	 * @return 0 -9
	 */
	protected static int getMostDamagingWeaponSlot(int minInventoryIndex, int maxInventoryIndex) {
		NonNullList<ItemStack> items = mc.player.inventory.mainInventory;
		double highestWeaponDamage = -1;
		double highestAttackSpeed = -1;
		int highestWeaponDamageSlot = -1;
		double highestSwordDamage = -1;
		double highestSwordAttackSpeed = -1;
		int highestSwordDamageSlot = -1;

		for (int i = minInventoryIndex; i <= maxInventoryIndex; i++) {
			ItemStack itemStack = items.get(i);

			if (!itemStack.isEmpty()) {
				double weaponDamage = getItemWeaponDamage(itemStack);
				double weaponAttackSpeed = getAttackSpeed(itemStack);
				if (itemStack.getItem() instanceof SwordItem) {
					if (
						(weaponDamage > highestSwordDamage && weaponAttackSpeed >= highestSwordAttackSpeed) ||
						(weaponDamage >= highestSwordDamage && weaponAttackSpeed > highestSwordAttackSpeed)
					) {
						highestSwordDamage = weaponDamage;
						highestSwordAttackSpeed = weaponAttackSpeed;
						highestSwordDamageSlot = i;
					}
				}
				else {
					if (
						(weaponDamage > highestWeaponDamage && weaponAttackSpeed >= highestAttackSpeed) ||
						(weaponDamage >= highestWeaponDamage && weaponAttackSpeed > highestAttackSpeed)
					) {
						highestWeaponDamage = weaponDamage;
						highestAttackSpeed = weaponAttackSpeed;
						highestWeaponDamageSlot = i;
					}
				}
			}
		}
		if (highestSwordDamageSlot == -1) {
			return highestWeaponDamageSlot;
		}
		else if (//FIXME? Something seems very screwy about these conditionals...
			(highestAttackSpeed > highestSwordDamage && highestAttackSpeed >= highestSwordAttackSpeed) ||
			(highestWeaponDamage >= highestSwordDamage && highestAttackSpeed > highestSwordAttackSpeed)
		) {
			return highestWeaponDamageSlot;
		}
		else {
			return highestSwordDamageSlot;
		}
	}

	/**
	 * Gets the hotbar index of the most damaging melee weapon on the hotbar.
	 *
	 * @return 0 -8, -1 if none found
	 */
	public static int getMostDamagingWeaponSlotFromHotbar() {
		return getMostDamagingWeaponSlot(0, 8);
	}

	/**
	 * Gets the inventory index of the most damaging melee weapon in the inventory.
	 *
	 * @return 9 -35, -1 if none found
	 */
	public static int getMostDamagingWeaponSlotFromInventory() {
		return getMostDamagingWeaponSlot(9, 35);
	}

	/**
	 * Gets the amount of melee damage delt by the specified item
	 *
	 * @param itemStack the item stack
	 * @return -1 if it doesn't have a damage modifier
	 */
	public static double getItemWeaponDamage(ItemStack itemStack) {
		EquipmentSlotType EquipmentSlot = EquipmentSlotType.MAINHAND;
		Multimap<String, AttributeModifier> multimap =
			itemStack.getItem().getAttributeModifiers(EquipmentSlot, itemStack);
		double enchantDamage = getEnchantDamage(itemStack);

		if (multimap.containsKey(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
			Collection<AttributeModifier> attributes = multimap.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
			if (!attributes.isEmpty()) {
				AttributeModifier attribute = attributes.iterator().next();
				if (attribute != null) {
					return attribute.getAmount() + enchantDamage;
				}
			}
		}
		else if (enchantDamage > 0.0D) {
			return enchantDamage;
		}
		return -1;
	}

	private static void initializeListOfWeaponClasses() {
		if (rangedWeaponClasses == null) {
			rangedWeaponClasses = new ArrayList<Class>();
			rangedWeaponClasses.add(BowItem.class);

			if (ModCompatibility.TConstruct.isLoaded) {
				try {
					rangedWeaponClasses.add(Class.forName(ModCompatibility.TConstruct.tConstructBowClass));
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Determines if an item is a melee weapon.
	 *
	 * @param item
	 * @return
	 */
	private static boolean isRangedWeapon(Item item) {
		if (rangedWeaponClasses == null) { return false; }

		for (Class rangedWeaponClass : rangedWeaponClasses) {
			if (rangedWeaponClass.isInstance(item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Makes the player select a slot on their hotbar
	 *
	 * @param slot 0 through 8
	 */
	protected static void selectHotbarSlot(int slot) {
		if (slot < 0 || slot > 8) { return; }

		mc.player.inventory.currentItem = slot;
	}


	/**
	 * Gets the index of an item that exists in the player's hotbar.
	 *
	 * @param itemClasses       the type of item to find (i.e. ItemSword.class, ItemBow.class)
	 * @param minInventoryIndex the min inventory index
	 * @param maxInventoryIndex the max inventory index
	 * @return 0 through 8, inclusive. -1 if not found.
	 */
	protected static int getItemSlot(List<Class> itemClasses, int minInventoryIndex, int maxInventoryIndex) {
		NonNullList<ItemStack> items = mc.player.inventory.mainInventory;

		for (int i = minInventoryIndex; i <= maxInventoryIndex; i++) {
			ItemStack itemStack = items.get(i);

			if (!itemStack.isEmpty()) {
				Item item = itemStack.getItem();

				for (Class itemClass : itemClasses) {
					if (itemClass.isInstance(item)) {
						return i;
					}
				}
			}
		}

		return -1;
	}

	/**
	 * Gets the index of an item that exists in the player's hotbar.
	 *
	 * @param itemClasses the type of item to find (i.e. ItemSword.class, ItemBow.class)
	 * @return 0 through 8, inclusive. -1 if not found.
	 */
	public static int getItemSlotFromHotbar(List<Class> itemClasses) {
		return getItemSlot(itemClasses, 0, 8);
	}

	/**
	 * Gets the index of an item that exists in the player's hotbar.
	 *
	 * @param itemClasses the type of item to find (i.e. ItemSword.class, ItemBow.class)
	 * @return 9 through 35, inclusive. -1 if not found.
	 */
	public static int getItemSlotFromInventory(List<Class> itemClasses) {
		return getItemSlot(itemClasses, 9, 35);
	}

	public static double getEnchantDamage(ItemStack item) {
		double damage = 0.0D;
		if (item.isEnchanted()) {
			damage = EnchantmentHelper.getModifierForCreature(item, CreatureAttribute.UNDEFINED);
		}
		return damage;
	}

	public static double getAttackSpeed(ItemStack item) {
		EquipmentSlotType EquipmentSlot = EquipmentSlotType.MAINHAND;
		Multimap<String, AttributeModifier> multimap = item.getItem().getAttributeModifiers(EquipmentSlot, item);

		if (multimap.containsKey(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
			Collection<AttributeModifier> attributes = multimap.get(SharedMonsterAttributes.ATTACK_SPEED.getName());
			if (!attributes.isEmpty()) {
				AttributeModifier attribute = attributes.iterator().next();
				if (attribute != null) {
					return (4.0D) + (attribute.getAmount());
				}
			}
		}
		return -1;
	}

	public static double getBowDamage(ItemStack item) {
		double damage = 0.0D;
		if (item.isEnchanted()) {
			int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, item);
			if (power > 0) {
				damage = 2.0D + (double) power * 0.5D + 0.5D;
			}
		}
		return damage;
	}

	public static int getBowSlotFromHotbar(List<Class> itemClasses) {
		NonNullList<ItemStack> items = mc.player.inventory.mainInventory;
		double highestDamage = -1;
		int slot = -1;
		for (int i = 0; i <= 8; i++) {
			ItemStack itemStack = items.get(i);

			if (!itemStack.isEmpty()) {
				Item item = itemStack.getItem();

				for (Class itemClass : itemClasses) {
					if (itemClass.isInstance(item)) {
						if (getBowDamage(itemStack) > highestDamage) {
							highestDamage = getBowDamage(itemStack);
							slot = i;
						}
					}
				}
			}
		}
		return slot;
	}
}
