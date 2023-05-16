package de.m_marvin.commandlineparser;

public class Test {
	
	public static void main(String... args) {
		
		CommandLineParser parser = new CommandLineParser();
	
		parser.addOption("i", "test.txt", "The path to the input file");
		parser.addAlias("file-in", "i");
		parser.addOption("o", "test-out.txt", "The path to the output file");
		parser.addOption("f", false, "A flag that does something");
		parser.addAlias("flag", "f");
		
		String[] argsE = new String[] {"dsff", "-o", "test.txt", "--file-in", "\"test", "-2.txt\"", "-f" };
		
		System.out.println(parser.printHelp());
		
		parser.parseInput(argsE);
		
		System.out.println(parser.getOption("i"));
		System.out.println(parser.getOption("o"));
		System.out.println(parser.getOption("f"));
		System.out.println(parser.getFlag("f"));
		
	}
	
}
