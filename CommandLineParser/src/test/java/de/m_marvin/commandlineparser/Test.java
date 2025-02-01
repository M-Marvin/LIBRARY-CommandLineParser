package de.m_marvin.commandlineparser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;

import de.m_marvin.cliutil.CommandLineReader;
import de.m_marvin.cliutil.arguments.Arguments;
import de.m_marvin.cliutil.arguments.CommandArgumentParser;
import de.m_marvin.cliutil.commands.CommandContext;
import de.m_marvin.cliutil.commands.Commands;
import de.m_marvin.cliutil.commands.arguments.IntegerArgument;
import de.m_marvin.cliutil.commands.arguments.StringArgument;
import de.m_marvin.cliutil.commands.node.CommandNode;
import de.m_marvin.cliutil.exception.CommandArgumentException;
import de.m_marvin.cliutil.exception.CommandException;

public class Test {
	
	public static void main(String... args) throws InterruptedException, IOException {
		
		String test = "1.23 test1 -testt false -test=true -test2 \"ggg test2 \\\"test3 -out test4\\\" 244 \\\"test5 \\\\\"test6\\\\\"\\\"\"";
		
		CommandArgumentParser p = new CommandArgumentParser();

		p.addOption("testt", false, "Test Option1");
		p.addOption("test", false, "Test Option2");
		p.addOption("test2", "a", "Test Option3");
		p.addOption("path", new File("./"), "Test Option3");
		p.addOption("url", new URL("https://test.net//"), "URL TEST");
		p.addShort('t', "test");
		
		try {
			Arguments s = p.parse(test);
			for (var e : s.getOptions().entrySet()) {
				System.out.println(e.getKey() + " = " + e.getValue());
			}
			
			String[] cli = p.print(s);
			
			System.out.print("\n->");
			for (String s1 : cli)
				System.out.print(" " + s1);
			
		} catch (CommandArgumentException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n----");
		
		p.printHelp(System.out);
		
		CommandArgumentParser parser = new CommandArgumentParser();
	
		parser.addOption("file-in", "test.txt", "The path to the input file");
		parser.addShort('i', "file-in");
		parser.addOption("o", "test-out.txt", "The path to the output file");
		parser.addOption("flag", false, "A flag that does something");
		parser.addShort('f', "flag");
		parser.addOption("test_option_1234", "f", "TEST TEST TEST");
		
		String[] argsE = new String[] {"dsff", "-o", "test.txt", "--file-in", "\"test", "-2.txt\"", "-f" };
		
		System.out.println(parser.printHelp());
		
		Arguments arg;
		try {
			arg = parser.parse(argsE);

			System.out.println(arg.get("file-in").toString());
			System.out.println(arg.get("o").toString());
			System.out.println(arg.get("flag").toString());
			
		} catch (CommandArgumentException e) {
			e.printStackTrace();
		}
		
		
		
		
		CommandNode<CommandContext> croot = Commands.root();
		
		croot.then(
				Commands.literal("command")
				.then(
						Commands.literal("test")
						.modifies(
								context -> context.setArgument("test", true)
						)
						.fork(croot)
				)
				.then(
						Commands.literal("arg")
						.then(
								Commands.argument("argument", IntegerArgument.range(1, 10))
								.execute(
										context -> {
											
											System.out.println("test: " + context.getArgument("test", Boolean.class));
											System.out.println("arg: " + context.getArgument("argument", Integer.class));
											return true;
											
										}
								)
						)
				)
				.then(
						Commands.literal("text")
						.then(
								Commands.argument("argument", StringArgument.singleLine())
								.execute(
										context -> {
											
											System.out.println("test: " + context.getArgument("test", Boolean.class));
											System.out.println("arg: " + StringArgument.getValue(context, "argument"));
											return true;
											
										}
								)
						)
				)
		);
		
		try {
			boolean result1 = croot.parse(new CommandContext(), "command test command arg 2");
			System.out.println("result: " + result1);

			boolean result2 = croot.parse(new CommandContext(), "command test command text \"one two three\"");
			System.out.println("result: " + result2);
			
			Collection<String> suggestions = croot.suggest(new CommandContext(), "command text ");
			System.out.println(suggestions);
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CommandLineReader console = new CommandLineReader(System.in);
		
		boolean run = true;
		while (run) {
			
			Optional<String> commandLine = console.readLine();
			if (commandLine.isPresent()) {
				
				System.out.println("Entered: " + commandLine.get());
				
				try {
					boolean result = croot.parse(new CommandContext(), commandLine.get());
					System.out.println(result ? "success" : "error");
				} catch (CommandException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			Thread.sleep(1000);
			
		}
		console.close();
		
	}
	
}
