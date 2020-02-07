package com.zyin.zyinhud.helper;

import com.zyin.zyinhud.util.ModCompatibility;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ToolItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Function;

@SuppressWarnings({"SameParameterValue"})
public class TagHelper {
	static class ZHItemTags extends TagHelper {
		public static final Tag<Item> tag_TORCHES = forgeTag("torches");
		public static final Tag<Item> tag_TORCH_PLACERS = forgeTag("torchplacers");

		static Tag<Item> forgeTag(String name) {
			return tag("forge", name);
		}

		static Tag<Item> tag(String namespace, String name) {
			return tag(ItemTags.Wrapper::new, namespace, name);
		}
	}

	static class ZHBlockTags extends TagHelper {
		static Tag<Block> forgeTag(String name) {
			return tag("forge", name);
		}

		static Tag<Block> tag(String namespace, String name) {
			return tag(BlockTags.Wrapper::new, namespace, name);
		}
	}

	public static class ItemLike {
		//TODO: Should I use Predicates, boolean methods, or both?
		//TODO: Item.isIn(<Tag>) or Tag.contains(<Item>) ?
		public static boolean isTorchLike(Item item) {
			return item == Blocks.TORCH.asItem() ||
			       ZHItemTags.tag_TORCHES.contains(item) ||
			       ZHItemTags.tag_TORCH_PLACERS.contains(item);
		}

		public static boolean isSpecialArrowLike(Item item) {
			return item == Items.SPECTRAL_ARROW || item == Items.TIPPED_ARROW;
		}

		public static boolean isArrowLike(Item item) {
			return item == Items.ARROW || ItemTags.ARROWS.contains(item);
		}

		public static boolean isArrowLike(Item item, boolean excludeSpecialArrows) {
			if (!excludeSpecialArrows) {return isArrowLike(item);}
			return !isSpecialArrowLike(item) && isArrowLike(item);
		}

		public static boolean isCrossbowAmmo(Item item) {
			// Yes, ShootableItem has a predicate that checks if an ItemStack is an arrow or firework rocket,
			// but this method explicitly checks Forge's arrow Tag as opposed to vanilla's
			return item == Items.FIREWORK_ROCKET || isArrowLike(item);
		}

		public static boolean isCrossbowAmmo(Item item, boolean excludeFireworks) {
			if (!excludeFireworks) {return isCrossbowAmmo(item);}
			return isArrowLike(item);
		}

		public static boolean isEnderpearlLike(Item item) {
			return Tags.Items.ENDER_PEARLS.contains(item);
		}

		// No, YOU'RE the tool!
		public static boolean isToolLike(Item item) {
			//This one is going to just balloon if I'm not conservative here, isn't it?
			return item instanceof ToolItem ||
			       item instanceof HoeItem ||
			       item instanceof ShearsItem ||
			       ModCompatibility.TConstruct.isTConstructHarvestTool(item);
		}

		public static boolean canItemPlaceTorches(Item item) {
			return item instanceof ToolItem ||
			       ModCompatibility.TConstruct.isTConstructToolWithoutARightClickAction(item);

		}
	}

	/**
	 * Convenience method for creating new Tags in a given namespace.
	 * Always uses the corresponding Wrapper class when creating Tags,
	 * which should cause the Tag to update following a reload.
	 * <p>
	 * One should typically avoid creating new Tags in the "minecraft" namespace.
	 * Modifying Tags in the "minecraft" namespace is okay, but be aware that doing so may break things.
	 */
	static <T extends Tag<?>> T tag(Function<ResourceLocation, T> creator, String namespace, String name) {
		return creator.apply(new ResourceLocation(namespace, name));
	}

	/**
	 * Convenience method for creating new Tags in the "forge" namespace.
	 * Always uses the corresponding Wrapper class when creating Tags,
	 * which should cause the Tag to update following a reload.
	 */
	static <T extends Tag<?>> T forgeTag(Function<ResourceLocation, T> creator, String name) {
		return creator.apply(new ResourceLocation("forge", name));
	}
}