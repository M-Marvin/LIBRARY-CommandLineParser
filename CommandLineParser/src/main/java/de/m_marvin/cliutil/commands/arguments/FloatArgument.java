package de.m_marvin.cliutil.commands.arguments;

import java.util.Collection;
import java.util.Collections;

import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandArgumentException;

public class FloatArgument extends CommandArgument<Float> {
	
	private final Float maxValue;
	private final Float minValue;
	
	public FloatArgument(Float minValue, Float maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	@Override
	public Float parse(String str) throws CommandArgumentException {
		try {
			float value = Float.parseFloat(str);
			if (this.minValue != null && value < this.minValue) throw new CommandArgumentException("float argument out of bounds: %d !<= %d", this.minValue, value);
			if (this.maxValue != null && value > this.maxValue) throw new CommandArgumentException("float argument out of bounds: %d !>= %d", this.maxValue, value);
			return value;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	@Override
	public Collection<String> getExampleInput() {
		if (this.maxValue != null && this.minValue != null) {
			return Collections.singleton(String.format("%s..%s", Float.toString(this.minValue), Float.toString(this.maxValue)));
		} else if (this.maxValue != null) {
			return Collections.singleton(String.format("..%s", Float.toString(this.maxValue)));
		} else if (this.minValue != null) {
			return Collections.singleton(String.format("%s..", Float.toString(this.minValue)));
		} else {
			return Collections.singleton("any number");
		}
	}
	
	public static FloatArgument any() {
		return new FloatArgument(null, null);
	}

	public static FloatArgument min(float min) {
		return new FloatArgument(min, null);
	}

	public static FloatArgument max(float max) {
		return new FloatArgument(null, max);
	}
	
	public static FloatArgument range(float min, float max) {
		return new FloatArgument(min, max);
	}

	public static <T extends CommandContext> float getValue(T context, String name) {
		return context.getArgument(name, Float.class);
	}
	
}
