package de.m_marvin.cliutil.commands;

import de.m_marvin.cliutil.commands.arguments.CommandArgument;
import de.m_marvin.cliutil.commands.node.ArgumentCommandNode;
import de.m_marvin.cliutil.commands.node.CommandNode;
import de.m_marvin.cliutil.commands.node.LiteralCommandNode;
import de.m_marvin.cliutil.commands.node.RootCommandNode;

public class Commands {
	
	public static <T extends CommandContext> CommandNode<T> root() {
		return new RootCommandNode<T>();
	}
	
	public static <T extends CommandContext> LiteralCommandNode<T> literal(String name) {
		return new LiteralCommandNode<T>(name);
	}
	
	public static <V, T extends CommandContext> ArgumentCommandNode<T, V> argument(String name, CommandArgument<V> argument) {
		return new ArgumentCommandNode<T, V>(name, argument);
	}
	
}
