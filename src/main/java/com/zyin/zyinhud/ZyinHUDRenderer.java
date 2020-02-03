package com.zyin.zyinhud;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import com.zyin.zyinhud.helper.HUDEntityTrackerHelper;
import com.zyin.zyinhud.helper.RenderEntityTrackerHelper;
import com.zyin.zyinhud.modules.AnimalInfo;
import com.zyin.zyinhud.modules.DistanceMeasurer;
import com.zyin.zyinhud.modules.DurabilityInfo;
import com.zyin.zyinhud.modules.InfoLine;
import com.zyin.zyinhud.modules.ItemSelector;
//import com.zyin.zyinhud.modules.PotionTimers;
import com.zyin.zyinhud.modules.SafeOverlay;

/**
 * This class is in charge of rendering things onto the HUD and into the game world.
 */
public class ZyinHUDRenderer {
	public static final ZyinHUDRenderer instance = new ZyinHUDRenderer();
	private static Minecraft mc = Minecraft.getInstance();

	/**
	 * Event fired at various points during the GUI rendering process.
	 * We render anything that need to be rendered onto the HUD in this method.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
		//render everything onto the screen
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			InfoLine.RenderOntoHUD();
			DistanceMeasurer.RenderOntoHUD();
			DurabilityInfo.RenderOntoHUD();
//            PotionTimers.RenderOntoHUD();

			//Call other modules that need to render things on the HUD near entities
			HUDEntityTrackerHelper.RenderEntityInfo(event.getPartialTicks());

			ItemSelector.RenderOntoHUD(event.getPartialTicks());
		}
		else if (event.getType() == RenderGameOverlayEvent.ElementType.DEBUG) {
			AnimalInfo.RenderOntoDebugMenu();
		}


		//change how the inventories are rendered (this has to be done on every game tick)
//    	if (mc.currentScreen instanceof DisplayEffectsScreen)
//    	{
//            PotionTimers.DisableInventoryPotionEffects((DisplayEffectsScreen)mc.currentScreen);
//        }
	}


	/**
	 * Event fired when the world gets rendered.
	 * We render anything that need to be rendered into the game world in this method.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	public void RenderWorldLastEvent(RenderWorldLastEvent event) {
		//render unsafe positions (cache calculations are done from this render method)
		SafeOverlay.instance.RenderAllUnsafePositionsMultithreaded(event.getPartialTicks());

		//calls other modules that need to render things in the game world nearby other entities
		RenderEntityTrackerHelper.RenderEntityInfo(event.getPartialTicks());

		//store world render transform matrices for later use when rendering HUD
		HUDEntityTrackerHelper.StoreMatrices();
	}


	/**
	 * Renders an Item icon in the 3D world at the specified coordinates
	 *
	 * @param x               the x
	 * @param y               the y
	 * @param z               the z
	 * @param item            the item
	 * @param partialTickTime the partial tick time
	 */
	public static void RenderFloatingItemIcon(float x, float y, float z, Item item, float partialTickTime) {
		GL11BeforeDrawInWorld(x, y, z, 0.025f, 0.75f, partialTickTime);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		RenderItemTexture(-8, -8, new ItemStack(item), 16, 16);

		GL11AfterDrawInWorld();
	}

   /* @SubscribeEvent
    public void RenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.POTION_ICONS) && !PotionTimers.ShowVanillaStatusEffectHUD) {
            event.setCanceled(true);
        }
    }*/

	/**
	 * Renders a texture at the specified location
	 *
	 * @param x      the x
	 * @param y      the y
	 * @param item   the item
	 * @param width  the width
	 * @param height the height
	 */
	public static void RenderItemTexture(int x, int y, Item item, int width, int height) {
		// Don't use the deprecated IBakedModel.getParticleTexture method
//        IBakedModel iBakedModel = mc.getItemRenderer().getItemModelMesher().getItemModel(new ItemStack(item));
//        TextureAtlasSprite textureAtlasSprite = mc.getTextureMap().getAtlasSprite(iBakedModel.getParticleTexture().getName().toString());

		RenderItemTexture(x, y, new ItemStack(item), width, height);
	}

	/**
	 * Renders the texture of an ItemStack at the specified location
	 * More or less copy/pasted static version of what used to be Gui.func_175175_a()
	 *
	 * @param x         the x
	 * @param y         the y
	 * @param itemStack the ItemStack
	 * @param width     the width
	 * @param height    the height
	 */
	public static void RenderItemTexture(int x, int y, ItemStack itemStack, int width, int height) {
		TextureAtlasSprite textureAtlasSprite = mc.getItemRenderer().getItemModelMesher().getParticleIcon(itemStack);
		mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

		RenderTexture(x, y, textureAtlasSprite, width, height, 0);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param block
	 * @param width
	 * @param height
	 */
    /*public static void RenderBlockTexture(int x, int y, Block block, int width, int height)
    {
        TextureAtlasSprite textureAtlasSprite = mc.getBlockRendererDispatcher().func_175023_a().func_178122_a(block.getDefaultState());
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        RenderTexture(x, y, textureAtlasSprite, width, height, 0);
    }*/


	/**
	 * Draws a texture at the specified 2D coordinates
	 *
	 * @param x                X coordinate
	 * @param y                Y coordinate
	 * @param u                X coordinate of the texture inside of the .png
	 * @param v                Y coordinate of the texture inside of the .png
	 * @param width            width of the texture
	 * @param height           height of the texture
	 * @param resourceLocation A reference to the texture's ResourceLocation. If null, it'll use the last used resource.
	 * @param scale            How much to scale the texture by when rendering it
	 */
	public static void RenderCustomTexture(
		int x, int y, int u, int v, int width, int height,
		ResourceLocation resourceLocation, float scale
	) {
		x /= scale;
		y /= scale;

		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, scale);

		if (resourceLocation != null) { mc.getTextureManager().bindTexture(resourceLocation); }

		mc.ingameGUI.blit(x, y, u, v, width, height);

		GL11.glPopMatrix();
	}

	/**
	 * Renders a previously bound texture (with mc.getTextureManager().bindTexture())
	 *
	 * @param x
	 * @param y
	 * @param textureAtlasSprite
	 * @param width
	 * @param height
	 * @param zLevel
	 */
	@SuppressWarnings("SameParameterValue")
	private static void RenderTexture(
		int x, int y, TextureAtlasSprite textureAtlasSprite,
		int width, int height, double zLevel
	) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();

		worldrenderer.begin(
			GL11.GL_QUADS,
			DefaultVertexFormats.POSITION_TEX
		);    //I have no clue what the DefaultVertexFormats are, but POSITION_TEX(=field_181707_g) works

		worldrenderer.pos((double) (x), (double) (y + height), (double) zLevel)
		             .tex((double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMaxV())
		             .endVertex();
		worldrenderer.pos((double) (x + width), (double) (y + height), (double) zLevel)
		             .tex((double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMaxV())
		             .endVertex();
		worldrenderer.pos((double) (x + width), (double) (y), (double) zLevel)
		             .tex((double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMinV())
		             .endVertex();
		worldrenderer.pos((double) (x), (double) (y), (double) zLevel)
		             .tex((double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMinV())
		             .endVertex();

		tessellator.draw();
	}


	/**
	 * Renders floating text in the 3D world at a specific position.
	 *
	 * @param text                  The text to render
	 * @param x                     X coordinate in the game world
	 * @param y                     Y coordinate in the game world
	 * @param z                     Z coordinate in the game world
	 * @param color                 0xRRGGBB text color
	 * @param renderBlackBackground render a pretty black border behind the text?
	 * @param partialTickTime       Usually taken from RenderWorldLastEvent.partialTicks variable
	 */
	public static void RenderFloatingText(
		String text, float x, float y, float z, int color, boolean renderBlackBackground, float partialTickTime
	) {
		RenderFloatingText(new String[]{text}, x, y, z, color, renderBlackBackground, partialTickTime);
	}

	/**
	 * Renders floating lines of text in the 3D world at a specific position.
	 *
	 * @param text                  The string array of text to render
	 * @param x                     X coordinate in the game world
	 * @param y                     Y coordinate in the game world
	 * @param z                     Z coordinate in the game world
	 * @param color                 0xRRGGBB text color
	 * @param renderBlackBackground render a pretty black border behind the text?
	 * @param partialTickTime       Usually taken from RenderWorldLastEvent.partialTicks variable
	 */
	public static void RenderFloatingText(
		String[] text, float x, float y, float z, int color, boolean renderBlackBackground, float partialTickTime
	) {
		//TODO: See how much of this can be replaced with EntityRenderer:renderLivingLabel and GameRenderer.drawNameplate
		//Thanks to Electric-Expansion mod for the majority of this code
		//https://github.com/Alex-hawks/Electric-Expansion/blob/master/src/electricexpansion/client/render/RenderFloatingText.java
		GL11BeforeDrawInWorld(x, y, z, 0.03f, 0.5f, partialTickTime);
		GlStateManager.disableDepthTest();
		GlStateManager.disableTexture();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		int textWidth = 0;
		for (String thisMessage : text) {
			int thisMessageWidth = mc.fontRenderer.getStringWidth(thisMessage);

			if (thisMessageWidth > textWidth) { textWidth = thisMessageWidth; }
		}

		int lineHeight = 10;

		if (renderBlackBackground && text.length > 0) {
			int stringMiddle = textWidth / 2;

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder worldrenderer = tessellator.getBuffer();

			//GL11.glDisable(GL11.GL_TEXTURE_2D);
			GlStateManager.disableTexture();
            
            /* OLD 1.8 rendering code
            //worldrenderer.startDrawingQuads();
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);	//field_181707_g maybe?
            
            GlStateManager.color(0.0F, 0.0F, 0.0F, 0.5F);
            worldrenderer.putPosition(-stringMiddle - 1, -1 + 0, 0.0D);
            worldrenderer.putPosition(-stringMiddle - 1, 8 + lineHeight*text.length-lineHeight, 0.0D);
            worldrenderer.putPosition(stringMiddle + 1, 8 + lineHeight*text.length-lineHeight, 0.0D);
            worldrenderer.putPosition(stringMiddle + 1, -1 + 0, 0.0D);
            */

			//This code taken from 1.8.8 net.minecraft.client.renderer.entity.Render.renderLivingLabel()
			worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			worldrenderer.pos((double) (-stringMiddle - 1), (double) -1, 0.0D)
			             .color(0.0F, 0.0F, 0.0F, 0.25F)
			             .endVertex();
			worldrenderer.pos((double) (-stringMiddle - 1), (double) (8 + lineHeight * (text.length - 1)), 0.0D)
			             .color(0.0F, 0.0F, 0.0F, 0.25F)
			             .endVertex();
			worldrenderer.pos((double) (stringMiddle + 1), (double) (8 + lineHeight * (text.length - 1)), 0.0D)
			             .color(0.0F, 0.0F, 0.0F, 0.25F)
			             .endVertex();
			worldrenderer.pos((double) (stringMiddle + 1), (double) -1, 0.0D)
			             .color(0.0F, 0.0F, 0.0F, 0.25F)
			             .endVertex();
			tessellator.draw();
			//GL11.glEnable(GL11.GL_TEXTURE_2D);
			GlStateManager.enableTexture();
		}

		int i = 0;
		for (String message : text) {
			mc.fontRenderer.drawString(message, -textWidth / 2, i * lineHeight, color);
			i++;
		}
		GlStateManager.enableDepthTest();
		GlStateManager.enableTexture();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GL11AfterDrawInWorld();
	}


	/**
	 * Displays a short notification to the user. Uses the Minecraft code to display messages.
	 *
	 * @param message the message to be displayed
	 */
	public static void DisplayNotification(String message) {
		mc.ingameGUI.setOverlayMessage(message, false);
	}

	private static void GL11BeforeDrawInWorld(
		float x, float y, float z, float scale, float alpha, float partialTickTime
	) {
		EntityRendererManager renderManager = mc.getRenderManager();

		float playerX = (float) (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * partialTickTime);
		float playerY = (float) (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * partialTickTime);
		float playerZ = (float) (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * partialTickTime);

		GL11.glColor4f(1f, 1f, 1f, alpha);
		GL11.glPushMatrix();
		GL11.glTranslatef(x - playerX, y - playerY, z - playerZ);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-scale, -scale, scale);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
	}

	private static void GL11AfterDrawInWorld() {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}
}
