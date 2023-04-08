package me.zero.clarinet.command.executer;

import java.util.ArrayList;
import java.util.List;

public class CmdParser {

	public static String[] parse(String input) {
		String[] split = input.split(" ");
		List<String> parsed = new ArrayList<>();
		for (int i = 0; i < split.length; i++) {
			String arg = split[i];
			if (arg.length() != 0) {
				parsed.add(arg);
			}
		}
		return parsed.toArray(new String[0]);
	}
}
