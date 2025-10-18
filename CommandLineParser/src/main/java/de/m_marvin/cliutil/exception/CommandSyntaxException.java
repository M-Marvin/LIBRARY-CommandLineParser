package de.m_marvin.cliutil.exception;

public class CommandSyntaxException extends CommandException {
	
	private static final long serialVersionUID = 3639109912829712954L;

	public CommandSyntaxException(String msg) {
		super(msg);
	}

	public CommandSyntaxException(Throwable e, String msg) {
		super(e, msg);
	}
	
	public CommandSyntaxException(String msg, Object... args) {
		super(String.format(msg, args));
	}

	public CommandSyntaxException(Throwable e, String msg, Object... args) {
		super(e, String.format(msg, args));
	}
	
	public static CommandSyntaxException args(String msg, String[] args, int off) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i <= off; i++) buf.append(i == off ? "[" + args[i] + "]" : args[i]).append(' ');
		return new CommandSyntaxException("%s: %s<- HERE", msg, buf.toString());
	}

	public static CommandSyntaxException args(Throwable e, String msg, String[] args, int off) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i <= off; i++) buf.append(i == off ? "[" + args[i] + "]" : args[i]).append(' ');
		return new CommandSyntaxException(e, "%s: %s<- HERE", msg, buf.toString());
	}
	
}
