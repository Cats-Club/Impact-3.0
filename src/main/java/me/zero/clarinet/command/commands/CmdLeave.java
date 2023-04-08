package me.zero.clarinet.command.commands;

import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.network.play.client.CPacketChatMessage;

public class CmdLeave extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (args.length == 1) {
            String type = args[0].toLowerCase();
            switch (type) {
                case "quit" : {
                    mc.world.sendQuittingDisconnectingPacket();
                    mc.loadWorld(null);
                    break;
                }
                case "damageself" : {
                    mc.playerController.attackEntity(mc.player, mc.player);
                    break;
                }
                case "spam" : {
                    for (int i = 0; i < 69/*xd*/; i++) {
                        mc.player.connection.sendPacket(new CPacketChatMessage(""));
                    }
                    break;
                }
                case "invalid" : {
                    mc.player.connection.sendPacket(new CPacketChatMessage("§"));
                    break;
                }
                default : {
                    ClientUtils.error("Invalid kick type supplied!");
                    break;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected String[] names() {
        return new String[]{ "leave" };
    }

    @Override
    protected String description() {
        return "Leave server for various reasons";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("quit", "Leave server for generic reason"),
                new CommandUsage("damageself", "Leave server for Interacting with Self"),
                new CommandUsage("spam", "Leave server for Spam"),
                new CommandUsage("invalid", "Leave server for Illegal Characters"),
        };
    }
}
