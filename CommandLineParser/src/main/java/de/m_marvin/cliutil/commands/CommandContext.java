package de.m_marvin.cliutil.commands;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandContext {
	
	private Map<String, Object> arguments = new LinkedHashMap<>();
	
	public <T> T getArgument(String name, Class<T> type) {
		Object val = this.arguments.get(name);
		if (type.isInstance(val)) {
			return type.cast(val);
		}
		return null;
	}
	
	public void setArgument(String name, Object value) {
		this.arguments.put(name, value);
	}
	
}
