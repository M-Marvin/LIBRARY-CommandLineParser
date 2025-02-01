package de.m_marvin.cliutil.commands.arguments;

import java.util.Collection;
import java.util.Collections;

import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandArgumentException;

public class LongArgument extends CommandArgument<Long> {
	
	private final int radix;
	private final Long maxValue;
	private final Long minValue;
	
	public LongArgument(int radix, Long minValue, Long maxValue) {
		this.radix = radix;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	@Override
	public Long parse(String str) throws CommandArgumentException {
		try {
			Long value = Long.parseLong(str, this.radix);
			if (this.minValue != null && value < this.minValue) throw new CommandArgumentException("long argument out of bounds: %d !<= %d", this.minValue, value);
			if (this.maxValue != null && value > this.maxValue) throw new CommandArgumentException("long argument out of bounds: %d !>= %d", this.maxValue, value);
			return value;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	@Override
	public Collection<String> getExampleInput() {
		if (this.maxValue != null && this.minValue != null) {
			return Collections.singleton(String.format("%s..%s", Long.toString(this.minValue, this.radix), Long.toString(this.maxValue, this.radix)));
		} else if (this.maxValue != null) {
			return Collections.singleton(String.format("..%s", Long.toString(this.maxValue, this.radix)));
		} else if (this.minValue != null) {
			return Collections.singleton(String.format("%s..", Long.toString(this.minValue, this.radix)));
		} else {
			return Collections.singleton("any number");
		}
	}
	
	public static LongArgument any() {
		return new LongArgument(10, null, null);
	}

	public static LongArgument min(Long min) {
		return new LongArgument(10, min, null);
	}

	public static LongArgument max(Long max) {
		return new LongArgument(10, null, max);
	}
	
	public static LongArgument range(Long min, Long max) {
		return new LongArgument(10, min, max);
	}

	public static LongArgument any(int radix) {
		return new LongArgument(radix, null, null);
	}

	public static LongArgument min(int radix, Long min) {
		return new LongArgument(radix, min, null);
	}

	public static LongArgument max(int radix, Long max) {
		return new LongArgument(radix, null, max);
	}
	
	public static LongArgument range(int radix, Long min, Long max) {
		return new LongArgument(radix, min, max);
	}

	public static <T extends CommandContext> long getValue(T context, String name) {
		return context.getArgument(name, Long.class);
	}
	
}
