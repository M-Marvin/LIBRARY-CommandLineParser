package de.m_marvin.cliutil.commands.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.m_marvin.cliutil.StringUtility;
import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.exception.CommandException;
import de.m_marvin.cliutil.exception.CommandSynthaxException;

public abstract class CommandNode<T extends CommandContext> {
	
	@FunctionalInterface
	public static interface Command<T extends CommandContext> {
		public boolean run(T context) throws CommandException;
	}
	
	@FunctionalInterface
	public static interface ContextModifier<T extends CommandContext> {
		public void modify(T context);
	}
	
	public static record NodeResult(boolean commandResult, boolean nodeResult, Collection<String> suggestions) {
		
		public static NodeResult command(boolean success) {
			return new NodeResult(success, true, null);
		}
		
		public static NodeResult noMatch() {
			return new NodeResult(false, false, null);
		}
		
		public static NodeResult suggest(Collection<String> suggestion) {
			return new NodeResult(false, true, suggestion);
		}
		
	}
	
	private ContextModifier<T> modifier = null;
	private Command<T> command = null;
	private List<CommandNode<T>> children = new ArrayList<>();
	
	protected NodeResult accept(T context, String[] args, int off, boolean suggest) throws CommandException {
		if (!suggest && this.modifier != null) {
			this.modifier.modify(context);
		}
		if (args.length == off + 1) {
			if (suggest) {
				return NodeResult.suggest(getSuggestions(context, args[off]));
			} else {
				if (this.command != null) {
					return NodeResult.command(this.command.run(context));
				} else {
					throw CommandSynthaxException.args("incomplete command", args, off);
				}
			}
		} else {
			if (suggest) {
				List<String> suggestions = new ArrayList<String>();
				for (CommandNode<T> node : this.children) {
					NodeResult result = node.accept(context, args, off + 1, true);
					if (result.nodeResult()) {
						suggestions.addAll(result.suggestions());
					}
				}
				return NodeResult.suggest(suggestions);
			} else {
				for (CommandNode<T> node : this.children) {
					NodeResult result = node.accept(context, args, off + 1, false);
					if (result.nodeResult()) return result;
				}
			}
		}
		throw CommandSynthaxException.args("invalid command", args, off + 1);
	}
	
	protected abstract Collection<String> getSuggestions(T context, String input);
	
	public CommandNode<T> then(CommandNode<T> node) {
		this.children.add(node);
		return this;
	}
	
	public CommandNode<T> fork(CommandNode<T> node) {
		this.children = node.children;
		return this;
	}
	
	public CommandNode<T> execute(Command<T> command) {
		this.command = command;
		return this;
	}
	
	public CommandNode<T> modifies(ContextModifier<T> modifier) {
		this.modifier = modifier;
		return this;
	}
	
	public boolean parse(T context, String... args) throws CommandException {
		args = StringUtility.resplit(args);
		return accept(context, args, 0, false).commandResult();
	}
	
	public Collection<String> suggest(T context, String... args) throws CommandException {
		args = StringUtility.resplit(args);
		return accept(context, args, 0, true).suggestions();
	}
	
}
