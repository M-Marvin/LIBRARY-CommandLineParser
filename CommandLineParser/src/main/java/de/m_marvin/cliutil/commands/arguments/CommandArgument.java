package de.m_marvin.cliutil.commands.arguments;

import java.util.Collection;
import java.util.Collections;

import de.m_marvin.cliutil.exception.CommandArgumentException;

public abstract class CommandArgument<T> {
	
	public abstract T parse(String str) throws CommandArgumentException;
	
	public Collection<String> getExampleInput() {
		return Collections.emptyList();
	}
	
	public Collection<String> getCompletitionSuggestions(String input) {
		return Collections.emptyList();
	}
	
}
