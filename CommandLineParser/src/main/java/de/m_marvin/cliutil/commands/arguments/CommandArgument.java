package de.m_marvin.cliutil.commands.arguments;

import java.util.Collection;

import de.m_marvin.cliutil.exception.CommandArgumentException;

public abstract class CommandArgument<T> {
	
	public abstract T parse(String str) throws CommandArgumentException;
	
	public abstract Collection<String> getExampleInput();
	
}
