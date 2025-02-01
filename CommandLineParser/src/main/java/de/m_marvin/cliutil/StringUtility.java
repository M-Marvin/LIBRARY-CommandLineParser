package de.m_marvin.cliutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StringUtility {

	public static String[] resplit(String... args) {
		Objects.requireNonNull(args);
		StringBuffer buf = new StringBuffer();
		for (String s : args) {
			if (!buf.isEmpty()) buf.append(' ');
			buf.append(s);
		}
		
		List<String> seg = new ArrayList<String>();
		
		int l = 0;
		int e = 0;
		boolean quoted = false;
		for (int i = 0; i < buf.length(); i++) {
			int c = buf.charAt(i);
			if (c == '\\') {
				e++;
				continue;
			} if (c == '"' && e % 2 == 0) {
				quoted = !quoted;
			}
			if (buf.charAt(i) == ' ' && !quoted) {
				seg.add(buf.substring(l, i));
				l = i + 1;
			}
			e = 0;
		}
		if (l == buf.length()) {
			seg.add("");
		} else {
			seg.add(buf.substring(l));
		}
		
		return seg.toArray(String[]::new);
	}
	
	/** Escape Sequences
	Escape Sequence		Description
	\t					Insert a tab in the text at this point.
	\b					Insert a backspace in the text at this point.
	\n					Insert a newline in the text at this point.
	\r					Insert a carriage return in the text at this point.
	\f					Insert a form feed in the text at this point.
	\'					Insert a single quote character in the text at this point.
	\"					Insert a double quote character in the text at this point.
	\\					Insert a backslash character in the text at this point.
	 */
	
	public static String escape(String str) {
		if (str == null) return null;
		StringBuffer buf = new StringBuffer();
		int l = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '\t' || c == '\b' || c == '\n' || c == '\r' || c == '\f' || c == '\'' || c == '\"' || c == '\\') {
				buf.append(str.substring(l, i));
				buf.append('\\');
				switch (c) {
				case '\t': buf.append('t'); break;
				case '\b': buf.append('b'); break;
				case '\n': buf.append('n'); break;
				case '\r': buf.append('r'); break;
				case '\f': buf.append('f'); break;
				}
				l = i + 1;
			}
		}
		if (l != str.length())
			buf.append(str.substring(l));
		return buf.toString();
	}
	
	public static String unescape(String str) {
		if (str == null) return null;
		StringBuffer buf = new StringBuffer();
		int l = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\\') {
				buf.append(str.substring(l, i));
				if (str.length() == i + 1)
					throw new StringFormatException("unexpected exacpe sequence end: %s <- HERE", str);
				char c = str.charAt(i + 1);
				switch (c) {
				case 't': buf.append('\t'); break;
				case 'b': buf.append('\b'); break;
				case 'n': buf.append('\n'); break;
				case 'f': buf.append('\f'); break;
				case 'r': buf.append('\r'); break;
				case '\'': buf.append('\''); break;
				case '\"': buf.append('\"'); break;
				case '\\': buf.append('\\'); break;
				default: throw new StringFormatException("illegal excape sequence: %s <- HERE", str.substring(0, i + 2));
				}
				i++;
				l = i + 1;
			}
		}
		if (l != str.length())
			buf.append(str.substring(l));
		return buf.toString();
	}
	
	public static String quote(String str) {
		return "\"" + escape(str) + "\"";
	}
	
	public static String unquote(String str) {
		if (str.startsWith("\"") && str.endsWith("\"")  && str.length() > 1)
			return unescape(str.substring(1, str.length() - 1));
		else
			return str;
	}
	
	public static String join(String... args) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			if (i != 0) buf.append(' ');
			buf.append(args[i]);
		}
		return buf.toString();
	}
	
}
