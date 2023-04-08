package me.zero.clarinet.command;

import me.zero.clarinet.Impact;
import net.minecraft.util.text.TextFormatting;

public class CommandOptions {
	
	private static final char PREFIX_DEFAULT = '.';

	private static char PREFIX = PREFIX_DEFAULT;
	
	public static char getPrefix() {
		return PREFIX;
	}

    public static String setPrefix(String prefix) {
        if (prefix.length() == 1) {
            CommandOptions.PREFIX = prefix.charAt(0);
            Impact.getInstance().getConfigManager().setValue("cmdprefix", CommandOptions.PREFIX);
            return "Set the Command Prefix to " + TextFormatting.BLUE + "\"" + prefix + "\"";
        } else {
            return TextFormatting.RED + "The Command Prefix cannot be longer than 1 character!";
        }
    }
}
