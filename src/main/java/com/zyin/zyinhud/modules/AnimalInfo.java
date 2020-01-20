package com.zyin.zyinhud.modules;

import com.zyin.zyinhud.ZyinHUDConfig;
import com.zyin.zyinhud.ZyinHUDRenderer;
import com.zyin.zyinhud.modules.ZyinHUDModuleModes.AnimalInfoOptions;
import com.zyin.zyinhud.util.Localization;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Shows information about horses in the F3 menu.
 */
@SuppressWarnings({"RedundantCast", "FieldCanBeLocal", "RedundantSuppression"})
public class AnimalInfo extends ZyinHUDModuleBase {
	/**
	 * Enables/Disables this module
	 */
	public static boolean Enabled = ZyinHUDConfig.EnableAnimalInfo.get();

	/**
	 * Toggles this module on or off
	 *
	 * @return The state the module was changed to
	 */
	public static boolean ToggleEnabled() {
		ZyinHUDConfig.EnableAnimalInfo.set(!Enabled);
		ZyinHUDConfig.EnableAnimalInfo.save();    //Temp: will eventually move to something in a UI, likely connected to a "DONE" button
		return Enabled = !Enabled;
	}

	/**
	 * The current mode for this module
	 */
	public static AnimalInfoOptions.AnimalInfoModes Mode = ZyinHUDConfig.AnimalInfoMode.get();

//_CHECK: It occurs to me, this sort of thing may not play nicely with dynamic config changes...
// Maybe I'll want a public method that can be called whenever the config is updated, to update the values here?
	/**
	 * Animals that are farther away than this will not have their info shown
	 */
	public static int viewDistanceCutoff = ZyinHUDConfig.AnimalInfoMaxViewDistance.get();        //how far away we will render the overlay
	// the min cutoff is not actually used here at the moment, but it's here for consistency if nothing else
	private static int minViewDistanceCutoff = AnimalInfoOptions.minViewDistanceCutoff;
	private static int maxViewDistanceCutoff = AnimalInfoOptions.maxViewDistanceCutoff;

	/**
	 * Sets the number of decimal places that will be rendered when displaying horse stats
	 */
	public static int numberOfDecimalsDisplayed = ZyinHUDConfig.AnimalInfoNumberOfDecimalsDisplayed.get();

	private static boolean ShowBreedingIcons = ZyinHUDConfig.ShowBreedingIcons.get();
	//private static boolean ShowBreedingTimers;
	private static boolean ShowHorseStatsOnF3Menu = ZyinHUDConfig.ShowHorseStatsOnF3Menu.get();
	private static boolean ShowHorseStatsOverlay = ZyinHUDConfig.ShowHorseStatsOverlay.get();
	private static boolean ShowTextBackgrounds = ZyinHUDConfig.ShowTextBackgrounds.get();

	private static PlayerEntity me;

	//TODO: verify these values
	//values above the perfect value are aqua
	//values between the perfect and good values are green
	//values between the good and bad values are white
	//values below the bad value are red
	private static double perfectHorseSpeedThreshold = 13;    //max: 14.1?
	private static double goodHorseSpeedThreshold = 11;
	private static double badHorseSpeedThreshold = 9.5;        //min: ~7?

	private static double perfectHorseJumpThreshold = 5;    //max: 5.5?
	private static double goodHorseJumpThreshold = 4;
	private static double badHorseJumpThreshold = 2.5;        //min: 1.2

	private static int perfectHorseHPThreshold = 28;        //max: 30
	private static int goodHorseHPThreshold = 24;
	private static int badHorseHPThreshold = 20;            //min: 15

	//UNUSED?
//	private static final int verticalSpaceBetweenLines = 10;    //space between the overlay lines (because it is more than one line)
//	public static final int maxNumberOfOverlays = 200;    //render only the first nearest 50 overlays

	private static DecimalFormat decimalFormat = GetDecimalFormat();
	private static DecimalFormat twoDigitFormat = new DecimalFormat("00");


	/**
	 * Gets the amount of decimals that should be displayed with a DecimalFormat object.
	 *
	 * @return
	 */
	private static DecimalFormat GetDecimalFormat() {
		if (numberOfDecimalsDisplayed < 1) { return new DecimalFormat("#"); }

		StringBuilder format = new StringBuilder("#.");
		for (int i = 1; i <= numberOfDecimalsDisplayed; i++) { format.append('#'); }

		return new DecimalFormat(format.toString());
	}

	/**
	 * Gets the number of deciamls used to display the animal stats.
	 *
	 * @return int
	 */
	public static int GetNumberOfDecimalsDisplayed() {
		return numberOfDecimalsDisplayed;
	}

	/**
	 * Sets the number of deciamls used to display the animal stats.
	 *
	 * @param numDecimals the num decimals
	 * @return
	 */
	public static void SetNumberOfDecimalsDisplayed(int numDecimals) {
		numberOfDecimalsDisplayed = numDecimals;
		decimalFormat = GetDecimalFormat();
	}

	/**
	 * Renders an animal's speed, hit points, jump strength, and other relevant information
	 * on the F3 menu when the player is riding it.
	 */
	public static void RenderOntoDebugMenu() {
		//if F3 is shown
		if (AnimalInfo.Enabled && ShowHorseStatsOnF3Menu && mc.gameSettings.showDebugInfo) {
			if (mc.player.isRidingHorse() || mc.player.isPassenger() && mc.player.getRidingEntity() instanceof LlamaEntity) {
				AbstractHorseEntity horse = (AbstractHorseEntity) mc.player.getRidingEntity();
				String horseSpeedMessage =
					Localization.get("animalinfo.debug.speed") + ' ' + GetHorseSpeedText(horse) + " m/s";
				String horseJumpMessage =
					Localization.get("animalinfo.debug.jump") + ' ' + GetHorseJumpText(horse) + " blocks";
				String horseHPMessage = Localization.get("animalinfo.debug.hp") + ' ' + GetHorseHPText(horse);

				ArrayList<String> list = new ArrayList<String>();
				list.add(horseSpeedMessage);
				list.add(horseJumpMessage);
				list.add(horseHPMessage);
				if (horse instanceof LlamaEntity) {
					String llamaStrength =
						Localization.get("animalinfo.debug.strength") + ' ' + GetLlamaStrength((LlamaEntity) horse);
					list.add(llamaStrength);
				}
				else if (horse instanceof HorseEntity) {
					String horseColor = Localization.get("animalinfo.debug.color") + ' ' + GetHorseColoringText(horse);
					String horseMarking =
						Localization.get("animalinfo.debug.markings") + ' ' + GetHorseMarkingText(horse);
					list.add(horseColor);
					list.add(horseMarking);
				}
				for (int i = 0; i < list.size(); ++i) {
					String s = list.get(i);

					int height = mc.fontRenderer.FONT_HEIGHT;
					int width = mc.fontRenderer.getStringWidth(s);
					int y = 2 + height * i + 144 + height + height;
					AbstractGui.fill(1, y - 1, 2 + width + 1, y + height - 1, -0xa9AFAFB0);
					mc.fontRenderer.drawString(s, 2, y, 0xE0E0E0);
				}
			}
		}
	}

	/**
	 * Renders information about an entity into the game world.
	 *
	 * @param entity          the entity
	 * @param partialTickTime the partial tick time
	 */
	public static void RenderEntityInfoInWorld(Entity entity, float partialTickTime) {
		//we only care about ageable entities
		if (!(entity instanceof AgeableEntity)) { return; }//|| entity.isBeingRidden()

		//if the player is in the world
		//and not looking at a menu
		//and F3 not pressed
		if (AnimalInfo.Enabled && Mode == AnimalInfoOptions.AnimalInfoModes.ON &&
		    (mc.mouseHelper.isMouseGrabbed() || mc.currentScreen == null || mc.currentScreen instanceof ChatScreen)
		    && !mc.gameSettings.showDebugInfo) {

			AgeableEntity animal = (AgeableEntity) entity;

			//don't render stats of the horse/animal we are currently riding
			if (animal.isBeingRidden()) { return; }

			//only show entities that are close by
			double distanceFromMe = mc.player.getDistance(animal);

			if (distanceFromMe > maxViewDistanceCutoff || distanceFromMe > viewDistanceCutoff) { return; }

			RenderAnimalOverlay(animal, partialTickTime);
		}
	}

	/**
	 * Renders an overlay in the game world for the specified animal.
	 *
	 * @param animal          the animal
	 * @param partialTickTime the partial tick time
	 */
	protected static void RenderAnimalOverlay(AgeableEntity animal, float partialTickTime) {
		float x = (float) animal.posX;
		float y = (float) animal.posY;
		float z = (float) animal.posZ;

		//a positive value means the horse has bred recently
		int animalGrowingAge = animal.getGrowingAge();

		ArrayList<String> multilineOverlayArrayList = new ArrayList<>();

		if (ShowHorseStatsOverlay && animal instanceof AbstractHorseEntity) {
			AbstractHorseEntity horse = (AbstractHorseEntity) animal;

			multilineOverlayArrayList.add(
				GetHorseSpeedText(horse) + ' ' + Localization.get("animalinfo.overlay.speed"));
			multilineOverlayArrayList.add(GetHorseHPText(horse) + ' ' + Localization.get("animalinfo.overlay.hp"));
			multilineOverlayArrayList.add(GetHorseJumpText(horse) + ' ' + Localization.get("animalinfo.overlay.jump"));
			if (animal instanceof LlamaEntity) {
				multilineOverlayArrayList.add(
					GetLlamaStrength((LlamaEntity) animal) + " " + Localization.get("animalinfo.overlay.strength")
				);
			}

			//if (animalGrowingAge < 0)
			//    multilineOverlayArrayList.add(GetHorseBabyGrowingAgeAsPercent(horse) + "%");
		}
        /* Breeding timer info no longer available on client in 1.8
    	if(ShowBreedingTimers && animal instanceof EntityAgeable)
        {
            if (animalGrowingAge > 0)	//if the animal has recently bred
                multilineOverlayArrayList.add(GetTimeUntilBreedAgain(animal));
        }
        */

		String[] multilineOverlayMessage = new String[1];
		multilineOverlayMessage = multilineOverlayArrayList.toArray(multilineOverlayMessage);

		if (multilineOverlayMessage[0] != null) {
			//render the overlay message
			ZyinHUDRenderer.RenderFloatingText(
				multilineOverlayMessage, x, y, z, 0xFFFFFF, ShowTextBackgrounds, partialTickTime);
		}

		if (ShowBreedingIcons &&
		    !animal.isChild() &&            //animal is an adult that is ready to breed
		    animal instanceof AnimalEntity &&    //animal is not a villager
		    !((AnimalEntity) animal).isInLove())    //animal is not currently breeding
		{
			//render the overlay icon
			if (animal instanceof AbstractHorseEntity && ((AbstractHorseEntity) animal).isTame()) {
				if (animal instanceof LlamaEntity) {
					ZyinHUDRenderer.RenderFloatingItemIcon(
						x, y + animal.getHeight(), z, Blocks.HAY_BLOCK.asItem(), partialTickTime
					);
				}
				else {
					ZyinHUDRenderer.RenderFloatingItemIcon(
						x, y + animal.getHeight(), z, Items.GOLDEN_CARROT, partialTickTime
					);
				}
			}
			else if (animal instanceof CowEntity) {
				ZyinHUDRenderer.RenderFloatingItemIcon(x, y + animal.getHeight(), z, Items.WHEAT, partialTickTime);
			}
			else if (animal instanceof SheepEntity) {
				ZyinHUDRenderer.RenderFloatingItemIcon(x, y + animal.getHeight(), z, Items.WHEAT, partialTickTime);
			}
			else if (animal instanceof PigEntity) {
				ZyinHUDRenderer.RenderFloatingItemIcon(x, y + animal.getHeight(), z, Items.CARROT, partialTickTime);
			}
			else if (animal instanceof ChickenEntity) {
				ZyinHUDRenderer.RenderFloatingItemIcon(
					x, y + animal.getHeight(), z, Items.WHEAT_SEEDS, partialTickTime
				);
			}
			else if (animal instanceof RabbitEntity) {
				ZyinHUDRenderer.RenderFloatingItemIcon(x, y + animal.getHeight(), z, Items.CARROT, partialTickTime);
			}
			else if (animal instanceof WolfEntity && ((WolfEntity) animal).isTamed()) {
				ZyinHUDRenderer.RenderFloatingItemIcon(x, y + animal.getHeight(), z, Items.BEEF, partialTickTime);
			}
			else if (animal instanceof WolfEntity && !((WolfEntity) animal).isTamed()) {
				ZyinHUDRenderer.RenderFloatingItemIcon(x, y + animal.getHeight(), z, Items.BONE, partialTickTime);
			}
			else if (animal instanceof OcelotEntity) {
				ZyinHUDRenderer.RenderFloatingItemIcon(
					x, y + animal.getHeight(), z, Items.TROPICAL_FISH, partialTickTime
				);
			}
		}
		
		
        /* EntityVillager profession is not available on client
        if(animal instanceof EntityVillager)
        {
			String getDisplayName = ((EntityVillager)animal).getDisplayName().getFormattedText();
            ZyinHUDRenderer.RenderFloatingText(getDisplayName, x, y + animal.getEyeHeight() + 0.8f, z, 0xFFFFFF, ShowTextBackgrounds, partialTickTime);
			
        }
		*/
	}

	/**
	 * Gets the status of the Animal Info
	 *
	 * @return the string "animals" if Animal Info is enabled, otherwise "".
	 */
	public static String CalculateMessageForInfoLine() {
		if (Mode == AnimalInfoOptions.AnimalInfoModes.OFF || !AnimalInfo.Enabled) { return ""; }
		else if (Mode == AnimalInfoOptions.AnimalInfoModes.ON) {
			return TextFormatting.WHITE + Localization.get("animalinfo.infoline");
		}
		else { return TextFormatting.WHITE + "???"; }
	}

	/**
	 * Gets the baby "horse's" age ranging from 0 to 100.
	 *
	 * @param horse
	 * @return
	 */
	private static int GetHorseBabyGrowingAgeAsPercent(AbstractHorseEntity horse) {
		float horseGrowingAge = horse.getRenderScale();     //horse size ranges from 0.5 to 1
		return (int) ((horseGrowingAge - 0.5f) * 2.0f * 100f);
	}

	/**
	 * Gets the time remaining before this animal can breed again
	 *
	 * @param animal
	 * @return null if the animal ready to breed or is a baby, otherwise "#:##" formatted string
	 */
	private static String GetTimeUntilBreedAgain(AgeableEntity animal) {
		int animalBreedingTime = animal.getGrowingAge();

		if (animalBreedingTime <= 0) { return null; }

		int seconds = animalBreedingTime / 20;
		int minutes = seconds / 60;

		return minutes + ':' + twoDigitFormat.format(seconds % 60);
	}

	/**
	 * Gets a "horse's" speed, colored based on how good it is.
	 *
	 * @param horse
	 * @return e.x.:<br>aqua "13.5"<br>green "12.5"<br>white "11.3"<br>red "7.0"
	 */
	private static String GetHorseSpeedText(AbstractHorseEntity horse) {
		double horseSpeed = GetEntityMaxSpeed(horse);
		String horseSpeedString = decimalFormat.format(horseSpeed);

		if (horseSpeed > perfectHorseSpeedThreshold) {
			horseSpeedString = TextFormatting.AQUA + horseSpeedString + TextFormatting.WHITE;
		}
		else if (horseSpeed > goodHorseSpeedThreshold) {
			horseSpeedString = TextFormatting.GREEN + horseSpeedString + TextFormatting.WHITE;
		}
		else if (horseSpeed < badHorseSpeedThreshold) {
			horseSpeedString = TextFormatting.RED + horseSpeedString + TextFormatting.WHITE;
		}

		return horseSpeedString;
	}

	/**
	 * Gets a "horse's" HP, colored based on how good it is.
	 *
	 * @param horse
	 * @return e.x.:<br>aqua "28"<br>green "26"<br>white "22"<br>red "18"
	 */
	private static String GetHorseHPText(AbstractHorseEntity horse) {
		int horseHP = GetEntityMaxHP(horse);
		@SuppressWarnings("DuplicatedCode")
		String horseHPString = decimalFormat.format(GetEntityMaxHP(horse));

		if (horseHP > perfectHorseHPThreshold) {
			horseHPString = TextFormatting.AQUA + horseHPString + TextFormatting.WHITE;
		}
		else if (horseHP > goodHorseHPThreshold) {
			horseHPString = TextFormatting.GREEN + horseHPString + TextFormatting.WHITE;
		}
		else if (horseHP < badHorseHPThreshold) {
			horseHPString = TextFormatting.RED + horseHPString + TextFormatting.WHITE;
		}

		return horseHPString;
	}

	/**
	 * Gets a "horse's" hearts, colored based on how good it is.
	 *
	 * @param horse
	 * @return e.x.:<br>aqua "15"<br>green "13"<br>white "11"<br>red "9"
	 */
	private static String GetHorseHeartsText(AbstractHorseEntity horse) {
		int horseHP = GetEntityMaxHP(horse);
		int horseHearts = GetEntityMaxHearts(horse);
		String horseHeartsString = "" + horseHearts;

		if (horseHP > perfectHorseHPThreshold) {
			horseHeartsString = TextFormatting.AQUA + horseHeartsString + TextFormatting.WHITE;
		}
		else if (horseHP > goodHorseHPThreshold) {
			horseHeartsString = TextFormatting.GREEN + horseHeartsString + TextFormatting.WHITE;
		}
		else if (horseHP < badHorseHPThreshold) {
			horseHeartsString = TextFormatting.RED + horseHeartsString + TextFormatting.WHITE;
		}

		return horseHeartsString;
	}

	/**
	 * Gets a "horse's" jump height, colored based on how good it is.
	 *
	 * @param horse
	 * @return e.x.:<br>aqua "5.4"<br>green "4"<br>white "3"<br>red "1.5"
	 */
	private static String GetHorseJumpText(AbstractHorseEntity horse) {
		double horseJump = GetHorseMaxJump(horse);
		@SuppressWarnings("DuplicatedCode")
		String horseJumpString = decimalFormat.format(horseJump);

		if (horseJump > perfectHorseJumpThreshold) {
			horseJumpString = TextFormatting.AQUA + horseJumpString + TextFormatting.WHITE;
		}
		else if (horseJump > goodHorseJumpThreshold) {
			horseJumpString = TextFormatting.GREEN + horseJumpString + TextFormatting.WHITE;
		}
		else if (horseJump < badHorseJumpThreshold) {
			horseJumpString = TextFormatting.RED + horseJumpString + TextFormatting.WHITE;
		}

		return horseJumpString;
	}

	/**
	 * Gets a "horse's" primary coloring
	 *
	 * @param horse
	 * @return empty string if there is no coloring (like for donkeys)
	 */
	private static String GetHorseColoringText(AbstractHorseEntity horse) {
		String texture = "";
		if (horse instanceof HorseEntity) { texture = ((HorseEntity) horse).getVariantTexturePaths()[0]; }
		if (texture == null || texture.isEmpty()) { return ""; }

		String[] textureArray = texture.split("/");            //"textures/entity/horse/horse_creamy.png"
		texture = textureArray[textureArray.length - 1];        //"horse_creamy.png"
		texture = texture.substring(6, texture.length() - 4);    //"creamy"
		texture = WordUtils.capitalize(texture);            //"Creamy"
		// NOTE: WordUtils in commons-lang has been deprecated in favor of its counterpart in commons-text

		return texture;
	}

	/**
	 * Gets a "horse's" secondary coloring
	 *
	 * @param horse
	 * @return empty string if there is no secondary coloring (like for donkeys)
	 */
	private static String GetHorseMarkingText(AbstractHorseEntity horse) {
		String texture = "";
		if (horse instanceof HorseEntity) { texture = ((HorseEntity) horse).getVariantTexturePaths()[1]; }
		if (texture == null || texture.isEmpty()) { return ""; }

		String[] textureArray = texture.split(
			"/");                //"textures/entity/horse/horse_markings_blackdots.png"
		texture = textureArray[textureArray.length - 1];            //"horse_markings_blackdots.png"
		texture = texture.substring(15, texture.length() - 4);    //"blackdots"
		texture = WordUtils.capitalize(texture);                //"Blackdots"
		// NOTE: WordUtils in commons-lang has been deprecated in favor of its counterpart in commons-text

		return texture;
	}

	/**
	 * Gets the max height a "horse" can jump when the jump bar is fully charged.
	 *
	 * @param horse
	 * @return e.x. 1.2?-5.5?
	 */
	private static double GetHorseMaxJump(AbstractHorseEntity horse) {
		double jumpPower = 1.0D; //see AbstractHorse.setJumpPower()
		double maxJumpStrength = horse.getHorseJumpStrength() * jumpPower;
		return (-0.1817584952 * Math.pow(maxJumpStrength, 3)) +
		       (3.689713992 * Math.pow(maxJumpStrength, 2)) +
		       (2.128599134 * maxJumpStrength) - 0.343930367;
	}

	private static int GetLlamaStrength(LlamaEntity llama) {
		return llama.getStrength();
	}

	/**
	 * Gets an entity's max hit points
	 *
	 * @param entity
	 * @return e.x. Steve = 20 hit points
	 */
	private static int GetEntityMaxHP(LivingEntity entity) {
		return (int) entity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue();
	}

	/**
	 * Gets the max hearts an entity has
	 *
	 * @param entity
	 * @return e.x. Steve = 20 hit points
	 */
	private static int GetEntityMaxHearts(LivingEntity entity) {
		return (int) Math.round(entity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue() / 2.0d);
	}

	/**
	 * Gets an entity's max run speed in meters(blocks) per second
	 *
	 * @param entity
	 * @return e.x. Steve = 4.3 m/s. Horses ~7-13
	 */
	private static double GetEntityMaxSpeed(LivingEntity entity) {
		//Steve has a movement speed of 0.1 and walks 4.3 blocks per second,
		//so multiply this result by 43 to convert to blocks per second
		return entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() * 43;
	}

	/**
	 * Toggle showing "horse" stats on the F3 menu
	 *
	 * @return the new F3 render boolean
	 */
	public static boolean ToggleShowHorseStatsOnF3Menu() {
		return ShowHorseStatsOnF3Menu = !ShowHorseStatsOnF3Menu;
	}

	/**
	 * Toggle showing "horse" stats on the overlay
	 *
	 * @return the new overlay render boolean
	 */
	public static boolean ToggleShowHorseStatsOverlay() {
		return ShowHorseStatsOverlay = !ShowHorseStatsOverlay;
	}

	/**
	 * Toggle showing black text backgrounds on overlayed text
	 *
	 * @return the new text background boolean
	 */
	public static boolean ToggleShowTextBackgrounds() {
		return ShowTextBackgrounds = !ShowTextBackgrounds;
	}

	/**
	 * Toggles showing breeding icons
	 *
	 * @return the new boolean
	 */
	public static boolean ToggleShowBreedingIcons() {
		return ShowBreedingIcons = !ShowBreedingIcons;
	}

	/**
	 * Toggles showing breeding timers
	 * @return the new boolean
	 */
    /*
    public static boolean ToggleShowBreedingTimers()
    {
    	return ShowBreedingTimers = !ShowBreedingTimers;
    }
    */
}
