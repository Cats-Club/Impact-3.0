package me.zero.clarinet.command.commands;

import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.command.CommandOptions;
import me.zero.clarinet.util.ClientUtils;

public class CmdPrefix extends AbstractCommand {

	@Override
	public boolean run(String message, String[] args) {
		if (args.length > 0) {
			ClientUtils.message(CommandOptions.setPrefix(args[0]));
            return true;
		}
		return false;
	}

    @Override
    protected String[] names() {
        return new String[]{ "prefix" };
    }

    @Override
    protected String description() {
        return "Manage the Command Prefix";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("<prefix>", "Set the Command Prefix")
        };
    }
}
