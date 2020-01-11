package com.zyin.zyinhud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class ZyinHUDSound {
	private static final Minecraft mc = Minecraft.getInstance();

	/**
	 * Plays a zyinhud sound with the given resource name.
	 *
	 * @param name the name
	 */
	public static void play(String name) {
		mc.getSoundHandler().play(new SimpleSound(
			new ResourceLocation("zyinhud:" + name),
			SoundCategory.MASTER,
			0.25F, 1.0F, false, 0,
			ISound.AttenuationType.NONE,
			0.0F, 0.0F, 0.0F,
			// Don't know if this should be false or true,
			// but since true indicates that a sound is NOT tied to a position in the world,
			// and this sound only plays for the local client, true seems like a safe assumption
			true
		));
		//SimpleSound.create(new ResourceLocation("zyinhud:" + name), 1.0F));
		//new SimpleSound(soundResource, 0.25F, pitch, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
	}

	/**
	 * Plays a zyinhud sound with the given resource name the specified volume
	 *
	 * @param name   the name
	 * @param volume 0-100% (0.0F to 1.0F) cannot go above 100%
	 */
	public static void play(String name, float volume) {
		mc.getSoundHandler().play(new SimpleSound(
			new ResourceLocation("zyinhud:" + name),
			SoundCategory.MASTER,
			volume, 1.0F, false, 0,
			ISound.AttenuationType.LINEAR,
			(float) mc.player.posX, (float) mc.player.posY, (float) mc.player.posZ,
			// Don't know if this should be false or true,
			// but since true indicates that a sound is NOT tied to a position in the world,
			// and this sound only plays for the local client, true seems like a safe assumption
			true
		));
	}

	/**
	 * Plays the sound that a GuiButton makes.
	 */
	public static void PlayButtonPress() {
		mc.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	/**
	 * Plays the "plop" sound that a chicken makes when laying an egg.
	 */
	public static void PlayPlopSound() {
		mc.getSoundHandler().play(SimpleSound.master(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F));
	}
}
