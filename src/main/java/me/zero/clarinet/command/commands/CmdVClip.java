package me.zero.clarinet.command.commands;

import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;

public class CmdVClip extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (args.length == 1) {
            try {
                Double amt = Double.valueOf(args[0]);
                mc.player.setPosition(mc.player.posX, mc.player.posY + amt, mc.player.posZ);
            } catch (Exception e) {
                ClientUtils.error(args[0] + " isn't a valid number");
            }
            return true;
        }
        return false;
    }

    @Override
    public String[] names() {
        return new String[] { "vclip" };
    }

    @Override
    public String description() {
        return "VClip a specified amount";
    }

    @Override
    public CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("<amount>", "VClip specified amount")
        };
    }
}
