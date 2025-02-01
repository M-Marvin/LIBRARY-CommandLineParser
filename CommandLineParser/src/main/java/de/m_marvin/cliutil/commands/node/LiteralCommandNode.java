package de.m_marvin.cliutil.commands.node;

import java.util.Collection;
import java.util.Collections;

import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandException;

public class LiteralCommandNode<T extends CommandContext> extends CommandNode<T> {
	
	private final String name;
	
	public LiteralCommandNode(String name) {
		this.name = name;
	}
	
	@Override
	public NodeResult accept(T context, String[] args, int off, boolean suggest) throws CommandException {
		if (suggest) {
			if (!this.name.startsWith(args[off])) return NodeResult.noMatch();
		} else {
			if (!args[off].equals(this.name)) return NodeResult.noMatch();
		}
		return super.accept(context, args, off, suggest);
	}

	@Override
	protected Collection<String> getSuggestions(T context, String input) {
		return Collections.singleton(this.name);
	}
	
}
