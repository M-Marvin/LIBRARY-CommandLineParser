package de.m_marvin.cliutil.commands.arguments;

import java.util.Collection;
import java.util.Collections;

import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandArgumentException;

public class DoubleArgument extends CommandArgument<Double> {
	
	private final Double maxValue;
	private final Double minValue;
	
	public DoubleArgument(Double minValue, Double maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	@Override
	public Double parse(String str) throws CommandArgumentException {
		try {
			double value = Double.parseDouble(str);
			if (this.minValue != null && value < this.minValue) throw new CommandArgumentException("double argument out of bounds: %d !<= %d", this.minValue, value);
			if (this.maxValue != null && value > this.maxValue) throw new CommandArgumentException("double argument out of bounds: %d !>= %d", this.maxValue, value);
			return value;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	@Override
	public Collection<String> getExampleInput() {
		if (this.maxValue != null && this.minValue != null) {
			return Collections.singleton(String.format("%s..%s", Double.toString(this.minValue), Double.toString(this.maxValue)));
		} else if (this.maxValue != null) {
			return Collections.singleton(String.format("..%s", Double.toString(this.maxValue)));
		} else if (this.minValue != null) {
			return Collections.singleton(String.format("%s..", Double.toString(this.minValue)));
		} else {
			return Collections.singleton("any number");
		}
	}
	
	public static DoubleArgument any() {
		return new DoubleArgument(null, null);
	}

	public static DoubleArgument min(double min) {
		return new DoubleArgument(min, null);
	}

	public static DoubleArgument max(double max) {
		return new DoubleArgument(null, max);
	}
	
	public static DoubleArgument range(double min, double max) {
		return new DoubleArgument(min, max);
	}

	public static <T extends CommandContext> double getValue(T context, String name) {
		return context.getArgument(name, Double.class);
	}
	
}