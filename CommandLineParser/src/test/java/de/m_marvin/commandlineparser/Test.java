package de.m_marvin.commandlineparser;

import java.io.IOException;
import java.util.Optional;

public class Test {
	
	public static void main(String... args) throws InterruptedException, IOException {
		
		CommandLineParser parser = new CommandLineParser();
	
		parser.addOption("i", "test.txt", "The path to the input file");
		parser.addAlias("file-in", "i");
		parser.addAlias("file-in2", "i");
		parser.addOption("o", "test-out.txt", "The path to the output file");
		parser.addOption("f", false, "A flag that does something");
		parser.addAlias("flag", "f");
		parser.addOption("test_option_1234", "f", "TEST TEST TEST");
		
		String[] argsE = new String[] {"dsff", "-o", "test.txt", "--file-in", "\"test", "-2.txt\"", "-f" };
		
		System.out.println(parser.printHelp());
		
		parser.parseInput(argsE);
		
		System.out.println(parser.getOption("i"));
		System.out.println(parser.getOption("o"));
		System.out.println(parser.getOption("f"));
		System.out.println(parser.getFlag("f"));
		
		CommandLineReader console = new CommandLineReader(System.in);
		
		boolean run = true;
		while (run) {
			
			Optional<String> commandLine = console.readLine();
			if (commandLine.isPresent()) {
				
				System.out.println("Entered: " + commandLine.get());
				
				if (commandLine.get().equals("exit")) {
					run = false;
				} else if (commandLine.get().equals("test")) {
					System.out.println("TESTTEST");
				}
				
			}
			
			Thread.sleep(1000);
			
		}
		console.close();
		
	}
	
}
