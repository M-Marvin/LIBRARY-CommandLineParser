package de.m_marvin.cliutil.commands.node;

import java.util.Collection;

import de.m_marvin.cliutil.StringUtility;
import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandException;

public class RootCommandNode<T extends CommandContext> extends CommandNode<T> {
	
	@Override
	public boolean parse(T context, String... args) throws CommandException {
		args = StringUtility.resplit(args);
		return accept(context, args, -1, false).commandResult();
	}
	
	@Override
	public Collection<String> suggest(T context, String... args) throws CommandException {
		args = StringUtility.resplit(args);
		return accept(context, args, -1, true).suggestions();
	}

	@Override
	protected Collection<String> getSuggestions(T context, String input) {
		throw new UnsupportedOperationException();
	}
	
}
