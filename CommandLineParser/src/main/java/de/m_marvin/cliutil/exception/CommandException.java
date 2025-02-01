package de.m_marvin.cliutil.exception;

public class CommandException extends Exception {
	
	private static final long serialVersionUID = 3639109912829712954L;

	public CommandException(String msg) {
		super(msg, null, false, false);
	}

	public CommandException(Throwable e, String msg) {
		super(msg, e, false, false);
	}
	
	public CommandException(String msg, Object... args) {
		super(String.format(msg, args), null, false, false);
	}

	public CommandException(Throwable e, String msg, Object... args) {
		super(String.format(msg, args), e, false, false);
	}
	
	public static CommandException args(String[] args, int off) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i <= off; i++) buf.append(i == off ? "[" + args[i] + "]" : args[i]).append(' ');
		return new CommandException("command error: %s<- HERE", buf.toString());
	}

	public static CommandException args(Throwable e, String[] args, int off) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i <= off; i++) buf.append(i == off ? "[" + args[i] + "]" : args[i]).append(' ');
		return new CommandException(e, "command error: %s<- HERE", buf.toString());
	}
	
}
