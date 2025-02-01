package de.m_marvin.cliutil;

public class StringFormatException extends IllegalArgumentException {
	
	private static final long serialVersionUID = -6534062321695294488L;

	public StringFormatException(String msg) {
		super(msg);
	}

	public StringFormatException(Exception e, String msg) {
		super(msg, e);
	}
	
	public StringFormatException(String msg, Object... args) {
		super(String.format(msg, args));
	}

	public StringFormatException(Exception e, String msg, Object... args) {
		super(String.format(msg, args), e);
	}
	
}
