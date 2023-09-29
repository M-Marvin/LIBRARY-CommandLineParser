package de.m_marvin.commandlineparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommandLineParser {
	
	public static record Option(Object defaultValue, String description)  {}
	public static final Option NONE = new Option("", "");

	protected Map<String, String> alias = new HashMap<String, String>();
	protected Map<String, Option> options = new HashMap<String, Option>();
	protected Map<String, Object> values = new HashMap<String, Object>();
	
	public void addOption(String name, Object defaultValue) {
		options.put(name, new Option(defaultValue, ""));
	}

	public void addOption(String name, Object defaultValue, String description) {
		options.put(name, new Option(defaultValue, description));
	}

	public void addAlias(String name, String option) {
		alias.put(name, option);
	}
	
	public String resolveAlias(String name) {
		return this.alias.getOrDefault(name, name);
	}

	public String getOption(String name) {
		name = resolveAlias(name);
		Object value = this.values.getOrDefault(name, options.getOrDefault(name, NONE).defaultValue);
		return value instanceof String ? (String) value : Boolean.toString((boolean) value);
	}
	
	public boolean getFlag(String name) {
		name = resolveAlias(name);
		Object value = this.values.getOrDefault(name, options.getOrDefault(name, NONE).defaultValue);
		if (value instanceof String) throw new IllegalArgumentException("The option " + name + " is a string argument!");
		return (boolean) value;
	}
	
	public void parseInput(String[] args) {
		
		if (args.length == 0) return;
		
		String[] currentOption = null;
		StringBuilder value = new StringBuilder();
		
		args = Stream.of(args).reduce((a, b) -> a + " " + b).get().split(" ");
		for (int i = 0; i < args.length; i++) {
			
			String argument = args[i];
			String[] optionName = parseOptionName(argument);
			
			if (optionName != null) {
				if (currentOption != null) {
					if (value.isEmpty()) {
						values.put(resolveAlias(currentOption[0]), true);
					} else {
						values.put(resolveAlias(currentOption[0]), value.toString());
					}
				}
				currentOption = optionName;
				value = new StringBuilder();
				if (currentOption.length > 1) value.append(currentOption[1]);
				continue;
			} else {
				value.append((value.isEmpty() ? "" : " ") + argument);
			}
			
		}
		
		if (currentOption != null) {
			if (value.isEmpty()) {
				values.put(resolveAlias(currentOption[0]), true);
			} else {
				values.put(resolveAlias(currentOption[0]), value.toString());
			}
		}
		
	}
	
	public String[] parseOptionName(String s) {
		String option = null;
		if (s.startsWith("--")) {
			option = s.substring(2);
		} else if (s.startsWith("-")) {
			option = s.substring(1);
		}
		if (option == null) {
			return null;
		} else {
			return isOptionValid(option.split("=")[0]) ? option.split("=") : null;
		}
	}
	
	public boolean isOptionValid(String optionName) {
		return alias.containsKey(optionName) || options.containsKey(optionName);
	}
	
	public String printHelp() {
		StringBuilder builder = new StringBuilder();
		int longestName = this.options.keySet().stream().mapToInt(String::length).max().getAsInt();
		this.options.forEach((name, option) -> {;
			int spaces = longestName - name.length() + 1;
			builder
				.append("-")
				.append(name);
			for (int i = 0; i < spaces; i++) builder
				.append(" ");
			builder
				.append("| ")
				.append(option.description)
				.append("\n");
			List<String> aliases = this.alias.keySet().stream().filter(alias -> this.alias.get(alias).equals(name)).toList();
			aliases.forEach(alias -> {
				builder
					.append("  --")
					.append(alias)
					.append("\n");
			});

		});
		return builder.toString();
	}
	
	public void reset() {
		this.values.clear();
	}
	
}
