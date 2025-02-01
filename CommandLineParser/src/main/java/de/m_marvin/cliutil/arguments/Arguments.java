package de.m_marvin.cliutil.arguments;

import java.util.List;
import java.util.Map;

import de.m_marvin.cliutil.exception.CommandArgumentException;

public class Arguments {
	
	private final Map<String, Object> options;
	private final List<String> args;
	
	public Arguments(Map<String, Object> options, List<String> args) {
		this.options = options;
		this.args = args;
	}
	
	public List<String> getArgList() {
		return args;
	}

	public String[] getArgs() {
		return args.toArray(String[]::new);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String name) throws CommandArgumentException {
		if (!this.options.containsKey(name)) {
			throw new CommandArgumentException("option not defined: %s", name);
		} else {
			Object val = this.options.get(name);
			try {
				return (T) val;
			} catch (ClassCastException e) {
				throw new CommandArgumentException("option '%s' has unexpected type %s", val.getClass().getName());
			}
		}
	}
	
	public boolean flag(String name) throws CommandArgumentException {
		return (boolean) get(name);
	}
	
	public Map<String, Object> getOptions() {
		return this.options;
	}
	
}
