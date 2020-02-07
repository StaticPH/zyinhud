package com.zyin.zyinhud.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.zyin.zyinhud.modules.Fps;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class CommandFps implements ICommandBase{
	/**
	 * Registers the command, and calls
	 * {@link #registerAliasesStatic(CommandDispatcher, LiteralCommandNode, List) registerAliases}
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
		ICommandBase.registerAliasesStatic(
			dispatcher, commandLiteral,
			Collections.singletonList("f")
		);
	}

	/**
	 * What the command does when successful
	 *
	 * @param command
	 * @return 0 in the absence of catastrophic failures (crashes)
	 */
	public static int execute(CommandContext<CommandSource> command) {
		Fps.toggleEnabled();
		return 0;
	}
}
