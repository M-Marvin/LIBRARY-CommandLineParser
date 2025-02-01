package de.m_marvin.cliutil.commands.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import de.m_marvin.cliutil.StringUtility;
import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandArgumentException;

public class StringArgument extends CommandArgument<String> {
	
	private Collection<String> examples;
	private Pattern filter;
	
	public StringArgument(String filter, String... examples) {
		this.filter = filter == null ? null :Pattern.compile(filter);
		this.examples = Arrays.asList(examples);
	}
	
	@Override
	public String parse(String str) throws CommandArgumentException {
		try {
			String value = StringUtility.unquote(str);
			if (this.filter != null && !this.filter.matcher(value).matches()) throw new CommandArgumentException("string argument invalid: %s does not match %s", str, StringUtility.escape(this.filter.pattern()));
			return value;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	@Override
	public Collection<String> getExampleInput() {
		return this.examples;
	}
	
	public static StringArgument any() {
		return new StringArgument(null, "any_text_with_no_spaces", "\"any text with spaces in quotes\"");
	}
	
	public static StringArgument singleLine() {
		return new StringArgument("[^\n\r]", "any_text_with_no_spaces", "\"any text with spaces in quotes\"");
	}

	public static StringArgument noSpaces() {
		return new StringArgument("[^\s]", "example", "words_with_underscores", "numbers_123", "\"in_quotes\"", "!?4%&\\/[]()");
	}
	
	public static StringArgument singleWord() {
		return new StringArgument("\\w+", "example", "\"in_quotes\"", "word_with_underscores");
	}
	
	public static StringArgument matches(String regex, String examples) {
		return new StringArgument(regex, examples);
	}

	public static <T extends CommandContext> String getValue(T context, String name) {
		return context.getArgument(name, String.class);
	}
	
}
