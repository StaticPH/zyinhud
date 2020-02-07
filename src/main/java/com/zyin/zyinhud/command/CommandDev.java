package com.zyin.zyinhud.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"CodeBlock2Expr", "UnnecessaryInterfaceModifier", "RedundantSuppression"})
public class CommandDev implements ICommandBase {
	//TODO: Implement datapack subcommand(s) to
	//      List Tags in a datapack/namespace
	//      Find which namespace(s) contain a particular Tag name

	private static final SuggestionProvider<CommandSource> SUGGEST_ANY_PACK = (srcContext, suggestionBuilder) -> {
		return ISuggestionProvider.suggest(
			packNames(getResourcepacks(srcContext).getAllPacks()), suggestionBuilder
		);
	};
	private static final SuggestionProvider<CommandSource> SUGGEST_AVAILABLE_PACK = (srcContext, suggestionBuilder) -> {
		return ISuggestionProvider.suggest(
			packNames(getResourcepacks(srcContext).getAvailablePacks()), suggestionBuilder
		);
	};
	private static final SuggestionProvider<CommandSource> SUGGEST_ENABLED_PACK = (srcContext, suggestionBuilder) -> {
		return ISuggestionProvider.suggest(
			packNames(getResourcepacks(srcContext).getEnabledPacks()), suggestionBuilder
		);
	};
	private static final SuggestionProvider<CommandSource> SUGGEST_TAGS = (srcContext, suggestionBuilder) -> {
		return ISuggestionProvider.suggest(
			ItemTags.getCollection().getTagMap().values().stream().map(Object::toString), suggestionBuilder
		);
	};

	private static ResourcePackList<ResourcePackInfo> getResourcepacks(CommandContext<CommandSource> ctx) {
		return ctx.getSource().getServer().getResourcePacks();
	}

	private static Stream<String> packNames(Collection<ResourcePackInfo> packs) {
		return packs.stream().map(ResourcePackInfo::getName).map(StringArgumentType::escapeIfRequired);
	}

	private static String tryFindWithTag(String itemTagName) {
		Collection<Tag<Item>> allItemTags = ItemTags.getCollection().getTagMap().values();

		// We don't care about tags that don't match
		allItemTags.removeIf(tag -> {
			if (itemTagName.contains(":")) {
				return !(tag.toString().equals(itemTagName));
			}
			else {
				// If itemTagName lacks a namespace, assume it is part of the "minecraft" namespace
				return !(tag.toString().equals("minecraft:" + itemTagName));
			}
		});

		if (allItemTags.isEmpty()) {return "";} // If no tags matched, return a zero-length string

		else {
			//Return a comma-separated list of all items tagged with itemTagName
			return allItemTags.stream()
			                  .flatMap(itemTag -> itemTag.getAllElements().stream())
			                  .map(Item::toString)
			                  .collect(Collectors.joining(", "));
		}
	}

	static <T extends Tag<?>> T getTagFromResource(Function<ResourceLocation, T> creator, ResourceLocation tag) {
		return creator.apply(tag);
	}

	private static class SubCommands {
		public static final ArgumentBuilder<CommandSource, ?> t2_showTagsOnHeldItem = showTagsOnHeldItem();
		public static final ArgumentBuilder<CommandSource, ?> t2_showItemsWithTag = showItemsWithTag();
		public static final ArgumentBuilder<CommandSource, ?> t2_listKnownItemTags = listKnownItemTags();
		public static final ArgumentBuilder<CommandSource, ?> t2_listKnownBlockTags = listKnownBlockTags();
		public static final ArgumentBuilder<CommandSource, ?> t1_tags = cmd_t1_Tags();
		public static final ArgumentBuilder<CommandSource, ?> t1_firstThingInTestWorld = cmd_t1_TestWorldRules();
		private static final String[] firstCommandsInTestWorld = {
			"/gamerule doDaylightCycle false", "/gamerule doFireTick false", "/gamerule doLimitedCrafting false",
			"/gamerule doWeatherCycle false", "/gamerule keepInventory true", "/gamerule mobGriefing false"
		};

		public static ArgumentBuilder<CommandSource, ?> cmd_t1_Tags() {
			return Commands.literal("tags")
			               .then(t2_showTagsOnHeldItem)
			               .then(t2_showItemsWithTag)
			               .then(t2_listKnownBlockTags)
			               .then(t2_listKnownItemTags);
		}

		public static ArgumentBuilder<CommandSource, ?> cmd_t1_TestWorldRules() {
			return Commands.literal("world_rules")
			               .requires(src -> src.hasPermissionLevel(2))
			               .executes(ctx -> {
				               for (String cmd : firstCommandsInTestWorld) {
//				                    ctx.getSource().asPlayer().getServer().sendMessage(new StringTextComponent(cmd));
					               Minecraft.getInstance().player.sendChatMessage(cmd);
				               }
				               return 0;
			               });
		}

		private static ArgumentBuilder<CommandSource, ?> showTagsOnHeldItem() {
			return Commands.literal("onHeld").executes(ctx -> {
				CommandSource source = ctx.getSource();
				PlayerEntity caller = source.asPlayer();    //yes, asPlayer returns a ServerPlayerEntity, but remember that singleplayer is internally a server these days
				Item heldItem = caller.getHeldItemMainhand().getItem();
				Set tags = heldItem.getTags();

				if (tags.isEmpty()) {
					respond(source, heldItem + " has 0 DataTags");
				}
				else {
					respond(source, heldItem + " has " + tags.size() + " DataTags:\n" + tags);
				}
				return 0;
			});
		}

		private static ArgumentBuilder<CommandSource, ?> showItemsWithTag() {
			return Commands.literal("withTag")
			               .then(tagNameArg())
			               .executes(ctx -> {
				               respond(ctx.getSource(), "No ItemTag name provided");
				               return 1;
			               });
		}

		private static ArgumentBuilder<CommandSource, ?> tagNameArg() {
			return Commands.argument("ItemTagName", StringArgumentType.string())
			               .suggests(SUGGEST_TAGS)
			               .executes(ctx -> {
				               String argument = StringArgumentType.getString(ctx, "ItemTagName");
				               String results = tryFindWithTag(argument);
				               String reply = results.isEmpty() ?
				                              "Unable to find Items with Tag \"" + argument + "\"" :
				                              "Found " + results.length() + " Items tagged with \"" + argument + "\"  :  " + results;
				               respond(ctx.getSource(), reply);
				               return results.length();
			               });
		}

		private static ArgumentBuilder<CommandSource, ?> listKnownItemTags() {
			return Commands.literal("allItemTags").executes(ctx -> {
				StringTextComponent response = new StringTextComponent(
					ItemTags.getCollection().getRegisteredTags().stream()
					        .map(ResourceLocation::toString)
					        .collect(Collectors.joining(", "))
				);
				respond(ctx.getSource(), response);
				return 0;
			});
		}

		private static ArgumentBuilder<CommandSource, ?> listKnownBlockTags() {
			return Commands.literal("allBlockTags").executes(ctx -> {
				StringTextComponent response = new StringTextComponent(
					BlockTags.getCollection().getRegisteredTags().stream()
					         .map(ResourceLocation::toString)
					         .collect(Collectors.joining(", "))
				);
				respond(ctx.getSource(), response);
				return 0;
			});
		}
	}

	/**
	 * Registers the command, and calls
	 * {@link #registerAliasesStatic(CommandDispatcher, LiteralCommandNode, List) registerAliases}
	 * to add alternative command names.
	 *
	 * @param dispatcher The command dispatcher
	 */
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		//Permission levels are confusing >.<
		// OpPermissions=4;
		// According to DefaultPermissionLevel, ALL=0, OP=1, NONE=2
		// But the CommandGenerate checks for a permission level of 4, and DebugCommand looks for 3,
		//      so who the hell knows?
		// According to ServerProperties, DedicatedServers set Op permission level at 4,
		//      and something called function-permission-level at 2
		//      While IntegratedServer says that Op permission level is 2, and lacks a function-permission-level
		// Based on the fact that DeOpCommand and KickCommand require a permission level of 3 though, it seems safe to assume
		//      that 3 or higher is at least Operator level
		// That seems somewhat reasonable, until you learn from PlayerEntity that any creative mode player with
		//		permission level >= 2 can use command blocks, which you certainly dont want non-Ops using.
		// And from the fact that commands like GameRuleCommand, which definitely requires elevated permissions,
		//      only looks for a permission level of 2, it sure as hell doesn't look like 2=NONE
		// At the same time, if 2=NONE, then it would make sense for something like SayCommand to set its permission
		//      requirement at 2

		//TODO ADD SHORTCUT COMMAND
		LiteralCommandNode<CommandSource> commandLiteral = dispatcher.register(
			Commands.literal("zhdevhelp")
//			        .requires(commandSource -> commandSource.hasPermissionLevel(2))
                    .then(SubCommands.t1_tags)
                    .then(SubCommands.t1_firstThingInTestWorld)
                    .executes((command) -> {
	                    respond(command.getSource(), "No operation provided.");
	                    return 1;
                    })
		);
		//FIXME: completion doesnt work for the /zhd alias
		ICommandBase.registerAliasesStatic(
			dispatcher, commandLiteral,
			Collections.singletonList("zhd")
		);
	}

	private static <T extends TextComponent> void respond(CommandSource source, T component) {
		source.sendFeedback(component, true);
	}

	private static <T extends TextComponent> void respond(PlayerEntity player, T component) {
		player.sendMessage(component);
	}

	private static void respond(CommandSource source, String component) {
		respond(source, new StringTextComponent(component));
	}

	private static void respond(PlayerEntity player, String component) {
		respond(player, new StringTextComponent(component));
	}
}
