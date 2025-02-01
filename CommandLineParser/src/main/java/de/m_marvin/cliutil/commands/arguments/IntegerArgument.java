package de.m_marvin.cliutil.commands.arguments;

import java.util.Collection;
import java.util.Collections;

import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandArgumentException;

public class IntegerArgument extends CommandArgument<Integer> {
	
	private final int radix;
	private final Integer maxValue;
	private final Integer minValue;
	
	public IntegerArgument(int radix, Integer minValue, Integer maxValue) {
		this.radix = radix;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	@Override
	public Integer parse(String str) throws CommandArgumentException {
		try {
			int value = Integer.parseInt(str, this.radix);
			if (this.minValue != null && value < this.minValue) throw new CommandArgumentException("interger argument out of bounds: %d !<= %d", this.minValue, value);
			if (this.maxValue != null && value > this.maxValue) throw new CommandArgumentException("interger argument out of bounds: %d !>= %d", this.maxValue, value);
			return value;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	@Override
	public Collection<String> getExampleInput() {
		if (this.maxValue != null && this.minValue != null) {
			return Collections.singleton(String.format("%s..%s", Integer.toString(this.minValue, this.radix), Integer.toString(this.maxValue, this.radix)));
		} else if (this.maxValue != null) {
			return Collections.singleton(String.format("..%s", Integer.toString(this.maxValue, this.radix)));
		} else if (this.minValue != null) {
			return Collections.singleton(String.format("%s..", Integer.toString(this.minValue, this.radix)));
		} else {
			return Collections.singleton("any number");
		}
	}
	
	public static IntegerArgument any() {
		return new IntegerArgument(10, null, null);
	}

	public static IntegerArgument min(int min) {
		return new IntegerArgument(10, min, null);
	}

	public static IntegerArgument max(int max) {
		return new IntegerArgument(10, null, max);
	}
	
	public static IntegerArgument range(int min, int max) {
		return new IntegerArgument(10, min, max);
	}

	public static IntegerArgument any(int radix) {
		return new IntegerArgument(radix, null, null);
	}

	public static IntegerArgument min(int radix, int min) {
		return new IntegerArgument(radix, min, null);
	}

	public static IntegerArgument max(int radix, int max) {
		return new IntegerArgument(radix, null, max);
	}
	
	public static IntegerArgument range(int radix, int min, int max) {
		return new IntegerArgument(radix, min, max);
	}
	
	public static <T extends CommandContext> int getValue(T context, String name) {
		return context.getArgument(name, Integer.class);
	}
	
}
