package de.m_marvin.cliutil.exception;

public class CommandArgumentException extends CommandException {
	
	private static final long serialVersionUID = 1140748566560328377L;

	public CommandArgumentException(String msg) {
		super(msg);
	}

	public CommandArgumentException(Throwable e, String msg) {
		super(e, msg);
	}
	
	public CommandArgumentException(String msg, Object... args) {
		super(String.format(msg, args));
	}

	public CommandArgumentException(Throwable e, String msg, Object... args) {
		super(e, String.format(msg, args));
	}

	public static CommandArgumentException args(String msg, String[] args, int off) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i <= off; i++) buf.append(i == off ? "[" + args[i] + "]" : args[i]).append(' ');
		return new CommandArgumentException("%s: %s<- HERE", msg, buf.toString());
	}

	public static CommandArgumentException args(Throwable e, String msg, String[] args, int off) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i <= off; i++) buf.append(i == off ? "[" + args[i] + "]" : args[i]).append(' ');
		return new CommandArgumentException(e, "%s: %s<- HERE", msg, buf.toString());
	}
	
}
