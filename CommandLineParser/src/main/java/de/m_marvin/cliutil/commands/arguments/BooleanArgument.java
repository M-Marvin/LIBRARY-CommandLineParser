package de.m_marvin.cliutil.commands.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandArgumentException;

public class BooleanArgument extends CommandArgument<Boolean> {
	
	private final static Pattern BOOL_FILTER = Pattern.compile("true|false");
	
	public BooleanArgument() {}
	
	@Override
	public Boolean parse(String str) throws CommandArgumentException {
		if (!BOOL_FILTER.matcher(str).matches()) return null;
		boolean value = Boolean.parseBoolean(str);
		return value;
	}
	
	@Override
	public Collection<String> getExampleInput() {
		return Arrays.asList("true", "false");
	}
	
	public static BooleanArgument bool() {
		return new BooleanArgument();
	}

	public static <T extends CommandContext> boolean getValue(T context, String name) {
		return context.getArgument(name, Boolean.class);
	}
	
}
