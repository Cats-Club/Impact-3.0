package me.zero.clarinet.command.commands;

import me.zero.clarinet.command.AbstractCommand;
import net.minecraft.util.text.TextComponentString;

public class CmdDevMsg extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (mc.player != null) {
            StringBuilder builder = new StringBuilder();
            for (String s : args) {
                builder.append(s).append(" ");
            }
            this.mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(builder.toString()));
            return true;
        }
        return false;
    }

    @Override
    public String[] names() {
        return new String[] { "devmsg" };
    }

    @Override
    public String description() {
        return "Displays a message in chat";
    }

    @Override
    public CommandUsage[] usage() {
        return new CommandUsage[0];
    }
}
