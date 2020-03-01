//package com.zyin.zyinhud.modules;
//
//import com.zyin.zyinhud.config.ZyinHUDConfig;
//import com.zyin.zyinhud.ZyinHUDRenderer;
//import com.zyin.zyinhud.util.InventoryUtil;
//import com.zyin.zyinhud.util.Localization;
//import com.zyin.zyinhud.util.ZyinHUDUtil;
//import net.minecraft.inventory.container.Slot;
//import net.minecraft.item.Food;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.item.SoupItem;
//import net.minecraft.item.crafting.FurnaceRecipe;
//import net.minecraft.item.crafting.IRecipe;
//import net.minecraft.item.crafting.IRecipeType;
//import net.minecraft.potion.EffectInstance;
//
//import java.awt.*;
//import java.awt.event.InputEvent;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import static com.zyin.zyinhud.modules.ZyinHUDModuleModes.EatingAidOptions.*;
//
///**
// * Eating Helper allows the player to eat food in their inventory by calling its eatFood() method.
// */
//public class EatingAid extends ZyinHUDModuleBase {
//	/**
//	 * Enables/Disables this module
//	 */
//	public static boolean isEnabled;
//
//	/**
//	 * Toggles this module on or off
//	 *
//	 * @return The state the module was changed to
//	 */
//	public static boolean toggleEnabled() {
//		ZyinHUDConfig.enableEatingAid.set(!isEnabled);
//		ZyinHUDConfig.enableEatingAid.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
//		return isEnabled = !isEnabled;
//	}
//
//	/**
//	 * The current mode for this module
//	 */
//	public static EatingAidModes mode = ZyinHUDConfig.eatingAidMode.get();
//
//	/**
//	 * Such as golden carrots, golden apples
//	 */
//	public static boolean eatGoldenFood = ZyinHUDConfig.eatGoldenFood.get();
//	/**
//	 * Such as raw chicken/porkchop/beef
//	 */
//	public static boolean eatRawFood = ZyinHUDConfig.eatRawFood.get();
//	/**
//	 * Food found on the hotbar will be chosen over food found in the inventory
//	 */
//	public static boolean prioritizeFoodInHotbar = ZyinHUDConfig.prioritizeFoodInHotbar.get();
//	/**
//	 * Treat mushroom stew as instant-eat
//	 */
//	public static boolean usePvPSoup = ZyinHUDConfig.usePvPSoup.get();
//
//	private Timer timer = new Timer();
//	private TimerTask swapTimerTask;
//	private TimerTask eatTimerTask;
//
//	private Robot r = null;
//	private boolean isCurrentlyEating;
//	private boolean previousEatFromHotbar;
//
//	private int foodItemIndex;
//	private int currentItemInventoryIndex;
//	private int currentItemHotbarIndex;
//
//
//	/**
//	 * Use this instance for all instance method calls.
//	 */
//	public static EatingAid instance = new EatingAid();
//
//	private EatingAid() {
//		try {
//			r = new Robot();
//		}
//		catch (AWTException e) {
//			e.printStackTrace();
//		}
//
//		isCurrentlyEating = false;
//		previousEatFromHotbar = false;
//	}
//
//	/**
//	 * Makes the player eat a food item on their hotbar or in their inventory.
//	 */
//	public void eatFood() {
//		//currentItemStack.onFoodEaten(mc.theWorld, mc.thePlayer);	//INSTANT EATING (single player only)
//
//		//make sure we're not about to click on a right-clickable thing, and we're not in creative mode
//		if (ZyinHUDUtil.isMouseoveredBlockRightClickable() || mc.playerController.isInCreativeMode()) { return; }
//
//		if (isCurrentlyEating) {
//			//if we're eating and we try to eat again, then cancel whatever we're eating
//			//by releasing right click, and swapping the food back to its correct position
//			stopEating();
//			return;
//		}
//		else {
//			//we need to eat something by first finding the best food to eat, then eat it
//			if (!mc.player.getFoodStats().needFood() && !usePvPSoup) {
//				//if we're not hungry then don't do anything
//				return;
//			}
//
//			foodItemIndex = getFoodItemIndexFromInventory();
//			if (foodItemIndex < 0) {
//				ZyinHUDRenderer.displayNotification(Localization.get("eatingaid.nofood"));
//				return;
//			}
//
//			if (foodItemIndex > 35 && foodItemIndex < 45) {    //on the hotbar
//				startEatingFromHotbar(foodItemIndex);
//			}
//			else { //if(foodItemIndex  > 8 && foodItemIndex < 36)	//in the inventory
//				startEatingFromInventory(foodItemIndex);
//			}
//
//		}
//	}
//
//	/**
//	 * Changes the selected index in your hotbar to where the food is, then eats it.
//	 *
//	 * @param foodHotbarIndex 36-44
//	 */
//	private void startEatingFromHotbar(int foodHotbarIndex) {
//		if (foodHotbarIndex < 36 || foodHotbarIndex > 44) { return; }
//
////		Slot slotToUse = (Slot)mc.player.inventory.inventorySlots.get(foodHotbarIndex);
////		Food food = slotToUse.getStack().getItem().getFood();
//		Item slotToUse = mc.player.inventory.getStackInSlot(foodHotbarIndex).getItem();
//
////    	if(usePvPSoup && food.equals(Items.MUSHROOM_STEW) &&
//		if (
//			usePvPSoup && slotToUse instanceof SoupItem &&
//			(mc.player.getHealth() < 20 || mc.player.getFoodStats().needFood())
//		) {
//			int previouslySelectedHotbarSlotIndex = mc.player.inventory.currentItem;
//			mc.player.inventory.currentItem = InventoryUtil.translateInventoryIndexToHotbarIndex(foodHotbarIndex);
//
//			InventoryUtil.sendUseItem();
//
//			mc.player.inventory.currentItem = previouslySelectedHotbarSlotIndex;
//		}
//		else if (mc.player.getFoodStats().needFood()) {
//			currentItemHotbarIndex = mc.player.inventory.currentItem;
//			foodHotbarIndex = InventoryUtil.translateInventoryIndexToHotbarIndex(foodHotbarIndex);
//
//			int previouslySelectedHotbarSlotIndex = mc.player.inventory.currentItem;
//			mc.player.inventory.currentItem = foodHotbarIndex;
//
//			r.mousePress(InputEvent.BUTTON3_MASK); //perform a right click
//			isCurrentlyEating = true;
//			previousEatFromHotbar = true;
//
//			ItemStack currentItemStack = mc.player.getHeldItemMainhand();
//			Item currentFood = currentItemStack.getItem();
//
//			int eatingDurationInMilliseconds = 1000 * currentFood.getUseDuration(currentItemStack) / 20;
//
//			//after this timer runs out we'll release right click to stop eating and select the previously selected item
//			eatTimerTask = new StopEatingTimerTask(r, previouslySelectedHotbarSlotIndex);
//			timer.schedule(eatTimerTask, eatingDurationInMilliseconds + InventoryUtil.getSuggestedItemSwapDelay());
//		}
//	}
//
//	/**
//	 * Swaps an item from your inventory into your hotbar, then eats it.
//	 *
//	 * @param foodInventoryIndex 9-35
//	 */
//	private void startEatingFromInventory(int foodInventoryIndex) {
//		if (foodInventoryIndex < 9 || foodInventoryIndex > 35) { return; }
//
////		Slot slotToUse = (Slot)mc.player.container.inventorySlots.get(foodInventoryIndex);
////		Item foodItem = (slotToUse.getStack().getItem());
//		Item slotToUse = mc.player.inventory.getStackInSlot(foodInventoryIndex).getItem();
//
//		//if PvP Soup is on and we don't need eat it, then return
////        if (usePvPSoup && food.equals(Items.MUSHROOM_STEW) &&
//		if (
//			usePvPSoup && slotToUse instanceof SoupItem &&
//			mc.player.getHealth() >= 20 && !mc.player.getFoodStats().needFood()
//		) {
//			return;
//		}
//
//		currentItemInventoryIndex = InventoryUtil.getCurrentlySelectedItemInventoryIndex();
//		InventoryUtil.swap(currentItemInventoryIndex, foodInventoryIndex);
//
//		r.mousePress(InputEvent.BUTTON3_MASK); //perform a right click
//		previousEatFromHotbar = false;
//
//		ItemStack currentItemStack = mc.player.getHeldItemMainhand();
//		Item currentFood = currentItemStack.getItem();
//
//		//I think 17 is better, for 20 can only be reached by fast computers
//		int eatingDurationInMilliseconds = 1000 * currentFood.getUseDuration(currentItemStack) / 17;
//
//		//Alternatively, may be we can introduce tps dectection in the future.
////        if(usePvPSoup && food.equals(Items.MUSHROOM_STEW) &&
//		if (
//			usePvPSoup && slotToUse instanceof SoupItem &&
//			(mc.player.getHealth() < 20 || mc.player.getFoodStats().needFood())    //for PvP Soup eating
//		) {
//			isCurrentlyEating = false;
//			r.mouseRelease(InputEvent.BUTTON3_MASK); //release right click
//			eatingDurationInMilliseconds = 1;
//
//			InventoryUtil.sendUseItem();
//		}
//		else if (mc.player.getFoodStats().needFood()) {    //for normal eating
//			isCurrentlyEating = true;
//
//			//after this timer runs out we'll release right click to stop eating
//			eatTimerTask = new StopEatingTimerTask(r);
//			timer.schedule(eatTimerTask, eatingDurationInMilliseconds);
//		}
//		else {    //for if we try to eat something but aren't hungry
//			eatingDurationInMilliseconds = 1;
//			r.mouseRelease(InputEvent.BUTTON3_MASK); //release right click
//		}
//
//		swapTimerTask = InventoryUtil.instance.swapWithDelay(
//			currentItemInventoryIndex, foodInventoryIndex,
//			eatingDurationInMilliseconds + InventoryUtil.getSuggestedItemSwapDelay()
//		);
//
//	}
//
//
//	/**
//	 * Stops eating by releasing right click and moving the food back to its original position.
//	 */
//	public void stopEating() {
//		if (previousEatFromHotbar) { stopEatingFromHotbar(); }
//		else { stopEatingFromInventory(); }
//	}
//
//	private void stopEatingFromInventory() {
//		r.mouseRelease(InputEvent.BUTTON3_MASK); //release right click
//		eatTimerTask.cancel();
//		swapTimerTask.cancel();
//		InventoryUtil.swap(currentItemInventoryIndex, foodItemIndex);
//		isCurrentlyEating = false;
//	}
//
//	private void stopEatingFromHotbar() {
//		r.mouseRelease(InputEvent.BUTTON3_MASK); //release right click
//		eatTimerTask.cancel();
//		mc.player.inventory.currentItem = currentItemHotbarIndex;
//		isCurrentlyEating = false;
//	}
//
//	/**
//	 * Are we currently eating food?
//	 *
//	 * @return boolean
//	 */
//	public boolean isEating() {
//		return isCurrentlyEating;
//	}
//
//
//	/**
//	 * Get food item index from inventory int.
//	 *
//	 * @return the int
//	 */
//	public int getFoodItemIndexFromInventory() {
//		if (mode == EatingAidModes.BASIC) { return getStrongestFoodItemIndexFromInventory(); }
//		else if (mode == EatingAidModes.INTELLIGENT) { return getBestFoodItemIndexFromInventory(); }
//		else { return -2; }
//	}
//
//	/**
//	 * Finds the food with the highest saturation value and returns its index in your inventory.
//	 *
//	 * @return int
//	 */
//	public int getStrongestFoodItemIndexFromInventory() {
//		List inventorySlots = mc.player.inventory.mainInventory;
//		int bestFoodMatchIndex = -1;
//		float bestFoodMatchSaturation = 0;
//		int foodLevel = mc.player.getFoodStats().getFoodLevel();    //max 20
//
//		//iterate over the hotbar (36-44), then main inventory (9-35)
//		for (int i = inventorySlots.size() - 1; i > 8; i--) {
//			if (prioritizeFoodInHotbar && i == 35 && bestFoodMatchIndex > -1) {
//				return bestFoodMatchIndex;
//			}
//
//
//			Slot slot = (Slot) inventorySlots.get(i);
//			ItemStack itemStack = slot.getStack();
//
//			if (itemStack.isEmpty()) {
//				continue;
//			}
//
//			Item item = itemStack.getItem();
//			Food food;
//
//			if (item.isFood() && (food = item.getFood()) != null) { // second condition shouldn't cause problems, but we shall see
//				float saturationModifier = food.getSaturation();
////                EffectInstance potionId = ZyinHUDUtil.getFieldByReflection(Food.class, food, "potionId", "field_77851_ca"); //Former "aaaaa"
////                EffectInstance potionID = food.getEffects().removeIf(
////                    pair->pair.getLeft().getEffectName().equals(
////                        (name)->
////                    )
////                ); //???: how the hell am I going to do this anymore? my head hurts >.<
////                List<String> filteredEffects= Arrays.asList("effect.minecraft.saturation", "effect.minecraft.heal");
////                List<Effect> filteredEffects = Arrays.asList(
////                    Effects.SATURATION, Effects.HEALTH_BOOST, Effects.INSTANT_HEALTH, Effects.ABSORPTION,
////                    Effects.REGENERATION
////                );
////                List<EffectInstance> potionID = food.getEffects();.removeIf(
////                    pair->{
////                        Effect e = pair.getLeft().getPotion();
////                        return filteredEffects.contains(e);
////                    }
////                );
//				String potionName;
//				if (potionId == null) {
//					potionName = "";
//				}
//				else {
//					potionName = potionId.getEffectName();
//				}
//
//				if (usePvPSoup && item.equals(Items.MUSHROOM_STEW)) {
//					saturationModifier = 1000f;    //setting the saturation value very high will make it appealing to the food selection algorithm
//				}
//				else if (
//					potionName.equals("effect.minecraft.saturation") || potionName.equals("effect.minecraft.heal")
//				) {
//					// 'effect.minecraft.saturation' formerly known as 'Potion.saturation.id'
//					// 'effect.minecraft.heal' formerly known as 'Potion.heal.id'
//					// modded foods like [Botania] Mana Cookie may have these effects
//					saturationModifier = 999;    //setting the saturation value very high will make it appealing to the food selection algorithm
//				}
//				else if (item.equals(Items.GOLDEN_CARROT) || item.equals(Items.GOLDEN_APPLE)) {
//					if (!eatGoldenFood) {
//						continue;
//					}
//
//					saturationModifier = 0.0001f;    //setting the saturation value low will make it unappealing to the food selection algorithm
//				}
//                /*else if (item.equals(Items.chicken)	//raw chicken gives Potion.hunger effect
//		        		|| item.equals(Items.porkchop)
//		        		|| item.equals(Items.beef)
//		        		|| item.equals(Items.mutton)
//		        		|| item.equals(Items.rabbit)
//		            	|| item.equals(Items.fish))*/    //Items.fish refers to UNCOOKED fish: Raw Fish, Raw Salmon, Pufferfish, Clownfish. All have a Potion id of 0
//				else if (hasSmeltingRecipe(itemStack)) {
//					if (!eatRawFood) {
//						continue;
//					}
//
//					saturationModifier = 0.0003f;    //setting the saturation value low will make it unappealing to the food selection algorithm
//				}
//				else if (
//					potionName.equals("effect.minecraft.poison") ||
//					potionName.equals("effect.minecraft.hunger") ||
//					potionName.equals("effect.minecraft.confusion") ||
//					item.equals(Items.PUFFERFISH)
//				) {
//					// 'effect.minecraft.poison' formerly known as 'Potion.poison.id'
//					// 'effect.minecraft.hunger' formerly known as 'Potion.hunger.id'
//					// 'effect.minecraft.confusion' formerly known as 'Potion.confusion.id'
//					saturationModifier = 0.0002f;    //setting the saturation value low will make it unappealing to the food selection algorithm
//				}
//
//				if (saturationModifier > bestFoodMatchSaturation) {
//					bestFoodMatchIndex = i;
//					bestFoodMatchSaturation = saturationModifier;
//					continue;
//				}
//			}
//		}
//
//		return Math.max(bestFoodMatchIndex, -1);
//	}
//
//
//	/**
//	 * Determines the best food that you can eat and returns its index in your inventory.
//	 * The best food is defined by not over eating (not wasting food), but still healing the most hunger.
//	 *
//	 * @return the index in your inventory that has the best food to eat (9-34), or -1 if no food found.
//	 */
//	public int getBestFoodItemIndexFromInventory() {
//		List inventorySlots = mc.player.inventory.mainInventory;
//		int bestFoodMatchIndex = -1;
//		int bestFoodMatchOvereat = 999;
//		int bestFoodMatchHeal = -999;
//		int foodLevel = mc.player.getFoodStats().getFoodLevel();    //max 20
//
//		//iterate over the hotbar (36-44), then main inventory (9-35)
//		for (int i = inventorySlots.size() - 1; i > 8; i--) {
//			if (prioritizeFoodInHotbar && i == 35 && bestFoodMatchIndex > -1) {
//				return bestFoodMatchIndex;
//			}
//
//
//			Slot slot = (Slot) inventorySlots.get(i);
//			ItemStack itemStack = slot.getStack();
//
//			if (itemStack.isEmpty()) {
//				continue;
//			}
//
//			Item item = itemStack.getItem();
//
//			if (item.isFood()) {
//				Food food = item.getFood();
//				int foodNeeded = 20 - foodLevel;    //amount of hunger needed to be full
//				int heal = food.getHealing();    //amount of hunger restored by eating this food
//				int overeat = foodNeeded - heal;
//				overeat = (overeat > 0) ?
//				          0 :
//				          Math.abs(overeat);    //positive number, amount we would overeat by eating this food
//
//				//Integer potionId = ZyinHUDUtil.getFieldByReflection(ItemFood.class, food, "potionId", "field_77851_ca");
//				EffectInstance potionId =
//					ZyinHUDUtil.getFieldByReflection(Food.class, food, "potionId", "field_77851_ca");
//				String potionName = potionId == null ? "" : potionId.getEffectName();
//
//				if (usePvPSoup && item.equals(Items.MUSHROOM_STEW)) {
//					overeat = -1000;    //setting the overeat value very low will make it appealing to the food selection algorithm
//				}
//				//Potion.saturation.id and Potion.heal.id; modded foods like [Botania] Mana Cookie may have these effects
//				else if (
//					potionName.equals("effect.minecraft.saturation") || potionName.equals("effect.minecraft.heal")
//				) {
//					overeat = -999;    //setting the overeat value very low will make it appealing to the food selection algorithm
//				}
//				//golden food gives Potion.regeneration effect      FIXME: I dont remember the carrot doing that though?
//				else if (item.equals(Items.GOLDEN_CARROT) || item.equals(Items.GOLDEN_APPLE)) {
//					if (!eatGoldenFood) {
//						continue;
//					}
//
//					overeat = 999;    //setting the overeat value high will make it unappealing to the food selection algorithm
//				}
//                /*else if (item.equals(Items.chicken)	//raw chicken gives Potion.hunger effect
//                		|| item.equals(Items.porkchop)
//                		|| item.equals(Items.beef)
//                		|| item.equals(Items.mutton)
//                		|| item.equals(Items.rabbit)
//                    	|| item.equals(Items.fish))*/    //Items.fish refers to UNCOOKED fish: Raw Fish, Raw Salmon, Pufferfish, Clownfish. All have a Potion id of 0
//				else if (hasSmeltingRecipe(itemStack)) {
//					if (!eatRawFood) {
//						continue;
//					}
//
//					overeat = 997;    //setting the overeat value high will make it unappealing to the food selection algorithm
//				}
//				else if (
//					potionName.equals("effect.minecraft.poison") || //Potion.poison.id
//					potionName.equals("effect.minecraft.hunger") || //Potion.hunger.id
//					potionName.equals("effect.minecraft.confusion") || //Potion.confusion.id
//					item.equals(Items.PUFFERFISH)
//				) {
//					overeat = 998;    //setting the overeat value high will make it unappealing to the food selection algorithm
//				}
//
//				//this food is better if we overeat less, or the overeat is the same but it heals more hunger
//				if (
//					bestFoodMatchOvereat > overeat || ((overeat == bestFoodMatchOvereat) && (heal > bestFoodMatchHeal))
//				) {
//					bestFoodMatchIndex = i;
//					bestFoodMatchOvereat = overeat;
//					bestFoodMatchHeal = heal;
//					continue;
//				}
//			}
//		}
//
//		return Math.max(bestFoodMatchIndex, -1);
//	}
//
//	/**
//	 * Determines if the itemStack has a smelting recipe in the furnace.
//	 *
//	 * @param itemStack
//	 * @return
//	 */
//	private static boolean hasSmeltingRecipe(ItemStack itemStack) {
//		//TODO: add some kind of conditional exception for things that some popular mods do, like bread smelting into toast
//
//		//if this function ends up taking a long time to run we can save the values into our own Map (without meta values) for fast lookup
//		// The nice, IRecipeType only version of this method has become private, but there IS a way to avoid reflection, so let's try that instead
//		List<FurnaceRecipe> smeltingList = mc.player.world.getRecipeManager()
//		                                                  .getRecipes(
//			                                                  IRecipeType.SMELTING, mc.player.inventory,
//			                                                  mc.player.world
//		                                                  );
//
//		//if(smeltingList.containsKey(itemStack)) ... ;	//this doesn't work since the meta values for the item stacks are different
//
//		for (IRecipe<?> recipe : smeltingList) {
//			if (recipe.getIngredients().get(0).test(itemStack)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//
//	/**
//	 * Toggles the whether you eat golden food or not
//	 *
//	 * @return The state it was changed to
//	 */
//	public static boolean toggleEatingGoldenFood() {
//		return eatGoldenFood = !eatGoldenFood;
//	}
//
//	/**
//	 * Toggles the whether you eat raw (uncooked) food or not
//	 *
//	 * @return The state it was changed to
//	 */
//	public static boolean toggleEatingRawFood() {
//		return eatRawFood = !eatRawFood;
//	}
//
//	/**
//	 * Toggles the prioritizing food in hotbar
//	 *
//	 * @return The state it was changed to
//	 */
//	public static boolean togglePrioritizeFoodInHotbar() {
//		return prioritizeFoodInHotbar = !prioritizeFoodInHotbar;
//	}
//
//	/**
//	 * Toggles enabling using PvP Soup
//	 *
//	 * @return The state it was changed to
//	 */
//	public static boolean toggleUsePvPSoup() {
//		return usePvPSoup = !usePvPSoup;
//	}
//
//
//	private class StopEatingTimerTask extends TimerTask {
//		private Robot r;
//		private int hotbarIndexToBeSelected = -1;
//
//		/**
//		 * Helper class whose purpose is to release right click and set our status to not eating.
//		 *
//		 * @param r the r
//		 */
//		StopEatingTimerTask(Robot r) {
//			this.r = r;
//		}
//
//		/**
//		 * Helper class whose purpose is to release right click, set our status to not eating, and select a hotbar index.
//		 *
//		 * @param r                       the r
//		 * @param hotbarIndexToBeSelected the hotbar index to be selected
//		 */
//		StopEatingTimerTask(Robot r, int hotbarIndexToBeSelected) {
//			this.r = r;
//			this.hotbarIndexToBeSelected = hotbarIndexToBeSelected;
//		}
//
//		@Override
//		public void run() {
//			r.mouseRelease(InputEvent.BUTTON3_MASK); //release right click
//			isCurrentlyEating = false;
//
//			if (hotbarIndexToBeSelected > -1) {
//				mc.player.inventory.currentItem = hotbarIndexToBeSelected;
//			}
//		}
//	}
//}
