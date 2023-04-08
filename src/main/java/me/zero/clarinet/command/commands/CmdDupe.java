package me.zero.clarinet.command.commands;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.manager.manager.CommandManager;

public class CmdDupe extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        CommandManager commands = Impact.getInstance().getCommandManager();
        commands.get(CmdDrop.class).run("", new String[]{ "hotbar" });
        commands.get(CmdLeave.class).run("", new String[]{ "quit" });
        return true;
    }

    @Override
    protected String[] names() {
        return new String[] { "dupe" };
    }

    @Override
    protected String description() {
        return "Dupes your hotbar";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[0];
    }
}
