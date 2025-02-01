package de.m_marvin.cliutil.commands.node;

import java.util.Collection;
import java.util.Collections;

import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.commands.arguments.CommandArgument;
import de.m_marvin.cliutil.exception.CommandArgumentException;
import de.m_marvin.cliutil.exception.CommandException;

public class ArgumentCommandNode<T extends CommandContext, V> extends CommandNode<T> {
	
	private final String name;
	private final CommandArgument<V> argument;
	
	public ArgumentCommandNode(String name, CommandArgument<V> argument) {
		this.name = name;
		this.argument = argument;
	}
		
	@Override
	public NodeResult accept(T context, String[] args, int off, boolean suggest) throws CommandException {
		// Do suggestions if nothing is entered yet, even if this would cause the parse to fail
		if (!suggest || !args[off].isEmpty()) {
			try {
				V value = this.argument.parse(args[off]);
				if (value == null) return NodeResult.noMatch();
				context.setArgument(name, value);
			} catch (CommandArgumentException e) {
				// Do not throw an error if we just want to collect suggestions, just say that this does not match
				if (!suggest) {
					throw CommandArgumentException.args(e, "argument error", args, off);
				} else {
					return NodeResult.noMatch();
				}
			}
		}
		return super.accept(context, args, off, suggest);
	}

	@Override
	protected Collection<String> getSuggestions(T context, String input) {
		// Return example inputs if nothing was typed yet, return name of this property otherwise
		if (input.isEmpty()) {
			return this.argument.getExampleInput();
		} else {
			return Collections.singleton(String.format("[%s]", this.name));
		}
	}
	
}
