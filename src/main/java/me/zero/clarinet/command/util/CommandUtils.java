package me.zero.clarinet.command.util;

import me.zero.clarinet.command.AbstractCommand;

public class CommandUtils {
	
	public static boolean isName(AbstractCommand command, String name) {
		for (int i = 0; i < command.getNames().length; i++) {
			if (name.equalsIgnoreCase(command.getNames()[i])) {
				return true;
			}
		}
		return false;
	}
}
