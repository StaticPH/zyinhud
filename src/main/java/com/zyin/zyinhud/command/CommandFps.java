package com.zyin.zyinhud.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.zyin.zyinhud.mods.Fps;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class CommandFps {
	/**
	 * Registers the command, and calls
	 * {@link #registerAliases(CommandDispatcher, LiteralCommandNode, List) registerAliases}
	 * to add alternative command names.
	 *
	 * @param dispatcher The command dispatcher
	 */
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralCommandNode<CommandSource> commandLiteral = dispatcher.register(
			Commands.literal("fps").executes((command) -> {
				execute(command);
				command.getSource().sendFeedback((new TranslationTextComponent("commands.doneMessage")), true);
				return 0;
			})
		);
		registerAliases(
			dispatcher, commandLiteral,
			Collections.singletonList("f")
		);
	}

	/**
	 * What the command does when successful
	 *
	 * @param command
	 * @return 1 in the absence of catastrophic failures (crashes)
	 */
	public static int execute(CommandContext<CommandSource> command) {
		Fps.ToggleEnabled();
		return 1;
	}

	/**
	 * Register aliases for a command
	 *
	 * @param dispatcher     The command dispatcher
	 * @param commandLiteral The command to register the aliases for
	 * @param aliases        A List of Strings, one for each alias to register
	 */
	public static void registerAliases(
		CommandDispatcher<CommandSource> dispatcher,
		LiteralCommandNode<CommandSource> commandLiteral,
		List<String> aliases
	) {
		aliases.forEach(
			(str) -> dispatcher.register(Commands.literal(str).redirect(commandLiteral))
		);
	}
}
