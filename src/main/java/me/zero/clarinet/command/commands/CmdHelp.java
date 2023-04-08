package me.zero.clarinet.command.commands;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;

public class CmdHelp extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        for (AbstractCommand cmd : Impact.getInstance().getCommandManager().getCommands()) {
            if (cmd != this) {
                ClientUtils.message("§9" + cmd.getNames()[0] + "§f - " + cmd.getDescription());
            }
        }
        return true;
    }

    @Override
    protected String[] names() {
        return new String[]{ "help", "h" };
    }

    @Override
    protected String description() {
        return "Gives a list of commands and descriptions";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[0];
    }
}
