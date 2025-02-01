package de.m_marvin.cliutil.commands.arguments;

import java.util.Collection;
import java.util.stream.Stream;

import de.m_marvin.cliutil.StringUtility;
import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandArgumentException;

public class EnumArgument<T extends Enum<T>> extends CommandArgument<T> {

	private final Class<T> enumClass;
	
	public EnumArgument(Class<T> enumClass) {
		this.enumClass = enumClass;
	}
	
	@Override
	public T parse(String str) throws CommandArgumentException {
		try {
			T value = Enum.valueOf(this.enumClass, StringUtility.unquote(str).toUpperCase());
			return value;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public Collection<String> getExampleInput() {
		return Stream.of(this.enumClass.getEnumConstants()).map(e -> e.name().toLowerCase()).toList();
	}

	public static <T extends Enum<T>> EnumArgument<T> type(Class<T> enumType) {
		return new EnumArgument<T>(enumType);
	}
	
	public static <V extends Enum<V>, T extends CommandContext> V getValue(T context, String name, Class<V> enumType) {
		return context.getArgument(name, enumType);
	}
	
}
