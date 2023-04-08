package me.zero.clarinet.command.commands;

import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.network.play.client.CPacketChatMessage;

public class CmdSay extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (args.length > 0) {
            String reallycoolmessage = "";
            for (String arg : args) {
                reallycoolmessage += " " + arg;
            }
            if (reallycoolmessage.length() > 0) {
                mc.player.connection.sendPacket(new CPacketChatMessage(reallycoolmessage));
            } else {
                ClientUtils.error("The message must be longer than 0 characters");
            }
            return true;
        }
        return false;
    }

    @Override
    protected String[] names() {
        return new String[]{ "say" };
    }

    @Override
    protected String description() {
        return "Sends a message in chat";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("<message>", "Sends a message in chat")
        };
    }
}
