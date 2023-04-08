package me.zero.clarinet.command.commands;

import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.command.AbstractCommand;

public class CmdClear extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        mc.ingameGUI.getChatGUI().clearChatMessages(true);
        ClientUtils.message("Cleared chat!");
        return true;
    }

    @Override
    public String[] names() {
        return new String[] { "clear", "cls" };
    }

    @Override
    public String description() {
        return "Clears chat";
    }

    @Override
    public CommandUsage[] usage() {
        return new CommandUsage[0];
    }
}
