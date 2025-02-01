package de.m_marvin.cliutil.arguments;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import de.m_marvin.cliutil.StringUtility;
import de.m_marvin.cliutil.exception.CommandArgumentException;

public class CommandArgumentParser {
	
	private record Option<T>(Function<String, T> parser, Function<T, String> printer, T value, String description) {
		public String valueStr() {
			return this.printer.apply(this.value);
		}
	}
	
	private Map<Integer, String> shorts = new HashMap<>();
	private Map<String, Option<?>> options = new HashMap<>();
	
	public <T> void addOption(String name, T value, Function<String, T> parser, Function<T, String> printer, String description) {
		this.options.put(name, new Option<T>(parser, printer, value, description));
	}
	
	public void addOption(String name, String value, String description) {
		addOption(name, value, StringUtility::unquote, StringUtility::quote, description);
	}
	
	public void addOption(String name, int value, String description) {
		addOption(name, value, Integer::parseInt, i -> Integer.toString(i), description);
	}

	public void addOption(String name, long value, String description) {
		addOption(name, value, Long::parseLong, l -> Long.toString(l), description);
	}

	public void addOption(String name, float value, String description) {
		addOption(name, value, Float::parseFloat, f -> Float.toString(f), description);
	}

	public void addOption(String name, double value, String description) {
		addOption(name, value, Double::parseDouble, d -> Double.toString(d), description);
	}

	public void addOption(String name, File value, String description) {
		addOption(name, value, s -> new File(StringUtility.unquote(s)), f -> StringUtility.quote(f.getPath()), description);
	}
	
	public void addOption(String name, URL value, String description) {
		addOption(name, value, s -> {
			try {
				return new URL(StringUtility.unquote(s));
			} catch (MalformedURLException e) {
				return null;
			}
		}, u -> StringUtility.quote(u.toString()), description);
	}
	
	public void addOption(String name, boolean value, String description) {
		addOption(name, value, s -> {
			if (s.isEmpty()) return true;
			return Boolean.parseBoolean(s);
		}, b -> {
			if (b) return "";
			return Boolean.toString(b);
		}, description);
	}
	
	public void addShort(char character, String option) {
		this.shorts.put((int) character, option);
	}
	
	public Arguments parse(String... args) throws CommandArgumentException {
		args = StringUtility.resplit(args);
		
		List<String> arguments = new ArrayList<String>();
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		boolean endOptions = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].length() >= 2 && args[i].charAt(0) == '-' && !endOptions) {
				
				// Check if contains equal sign or is explicitly a long option name
				int eq = args[i].indexOf('=');
				if (eq < 0) eq = args[i].length();
				boolean el = args[i].charAt(1) == '-';
				
				// If '--' flag, end option parsing
				if (el && eq == 2) {
					endOptions = true;
					continue;
				}
				
				// Get option name(s)
				String optname = args[i].substring(el ? 2 : 1, eq);
				List<String> opts;
				
				if (!el) {
					List<String> shortoptlist = optname.chars().mapToObj(this.shorts::get).toList();
					List<String> shortoptlist1 = shortoptlist.stream().filter(o -> o != null).distinct().toList();
					if (shortoptlist.size() != shortoptlist1.size()) {
						opts = Collections.singletonList(optname);
					} else {
						opts = shortoptlist1;
					}
				} else {
					opts = Collections.singletonList(optname);
				}
				
				// Get option(s) value (concat with spaces until hit next option)
				StringBuffer val = new StringBuffer();
				
				if (eq < args[i].length() - 1) {
					val.append(args[i].substring(eq + 1));
				}
				
				for (; i < args.length - 1; i++) {
					if (args[i + 1].charAt(0) == '-') break;
					if (!val.isEmpty()) val.append(' ');
					val.append(args[i + 1]);
				}
				
				// Set option value
				for (String o : opts) {
					options.put(o, val.toString());
				}
				
			} else {
				arguments.add(args[i]);
			}
		}
		
		Map<String, Object> optionsParsed = new LinkedHashMap<String, Object>();
		
		// Set defaults
		for (var opt : this.options.entrySet()) {
			if (!options.containsKey(opt.getKey()))
				optionsParsed.put(opt.getKey(), opt.getValue().value());
		}
		
		// Set parsed
		for (var opt : options.entrySet()) {
			Option<?> optcfg = this.options.get(opt.getKey());
			if (optcfg == null)
				throw new CommandArgumentException("undefined option: %s", opt.getKey());
			try {
				optionsParsed.put(opt.getKey(), optcfg.parser().apply(opt.getValue()));
			} catch (Throwable e) {
				throw new CommandArgumentException(e, "failed to parse option: %s", opt.getKey());
			}
		}
		
		return new Arguments(optionsParsed, arguments);
	}

	@SuppressWarnings("unchecked")
	public String[] print(Arguments arguments) throws CommandArgumentException {
		
		// Add non option arguments
		List<String> args = new ArrayList<String>();
		for (String arg : arguments.getArgs())
			args.add(arg);
		
		// Add options
		for (var e : arguments.getOptions().entrySet()) {
			Option<?> optcfg = this.options.get(e.getKey());
			if (optcfg == null)
				throw new CommandArgumentException("invalid option: %s", e.getKey());
			if (e.getValue() == optcfg.value()) continue;
			args.add("-" + e.getKey());
			try {
				String valstr = ((Function<Object, String>) optcfg.printer()).apply(e.getValue());
				if (!valstr.isEmpty()) args.add(valstr);
			} catch (Throwable e1) {
				throw new CommandArgumentException(e1, "failed to cast option value: %s", e.getKey());
			}
		}
		
		return args.toArray(String[]::new);
	}
	
	public void printHelp(Consumer<String> writer) {
		int al = 0;
		int dl = 0;
		for (var e : this.options.entrySet()) {
			int kl = e.getKey().length();
			kl += this.shorts.entrySet().stream().filter(e1 -> e1.getValue().equals(e.getKey())).count() * 3;
			if (kl > al) al = kl;
			int vl = e.getValue().valueStr().length();
			if (vl > dl) dl = vl;
		}
		dl = Math.max(dl, 7) + 1;
		al += 6;
		
		StringBuffer buf1 = new StringBuffer();
		buf1.append("option");
		for (int i = 6; i < al; i++)
			buf1.append(' ');
		buf1.append("default");
		writer.accept(buf1.toString());
		
		for (var e : this.options.entrySet()) {
			StringBuffer buf = new StringBuffer();
			List<Integer> so = this.shorts.entrySet().stream()
					.filter(e1 -> e1.getValue().equals(e.getKey()))
					.map(e1 -> e1.getKey())
					.toList();
			buf.append("--").append(e.getKey());
			so.forEach(e1 -> buf.append(" -").append((char) e1.intValue()));
			for (int i = buf.length(); i < al; i++)
				buf.append(' ');
			String defstr = e.getValue().valueStr();
			buf.append(defstr);
			for (int i = defstr.length(); i < dl; i++)
				buf.append(' ');
			buf.append("| ");
			buf.append(e.getValue().description());
			writer.accept(buf.toString());
		}
	}
	
	public String printHelp() {
		StringBuffer buf = new StringBuffer();
		printHelp(s -> buf.append(s).append('\n'));
		return buf.toString();
	}
	
	public void printHelp(PrintStream writer) throws IOException {
		printHelp(writer, StandardCharsets.UTF_8);
	}
	
	public void printHelp(PrintStream writer, Charset charset) throws IOException {
		String text = printHelp();
		writer.write(text.getBytes(charset));
	}
	
	public void printHelp(PrintWriter writer) {
		String text = printHelp();
		writer.write(text);
	}
	
}
