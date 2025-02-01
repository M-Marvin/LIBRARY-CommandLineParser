package de.m_marvin.cliutil.exception;

public class CommandSynthaxException extends CommandException {
	
	private static final long serialVersionUID = 3639109912829712954L;

	public CommandSynthaxException(String msg) {
		super(msg);
	}

	public CommandSynthaxException(Throwable e, String msg) {
		super(e, msg);
	}
	
	public CommandSynthaxException(String msg, Object... args) {
		super(String.format(msg, args));
	}

	public CommandSynthaxException(Throwable e, String msg, Object... args) {
		super(e, String.format(msg, args));
	}
	
	public static CommandSynthaxException args(String msg, String[] args, int off) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i <= off; i++) buf.append(i == off ? "[" + args[i] + "]" : args[i]).append(' ');
		return new CommandSynthaxException("%s: %s<- HERE", msg, buf.toString());
	}

	public static CommandSynthaxException args(Throwable e, String msg, String[] args, int off) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i <= off; i++) buf.append(i == off ? "[" + args[i] + "]" : args[i]).append(' ');
		return new CommandSynthaxException(e, "%s: %s<- HERE", msg, buf.toString());
	}
	
}
