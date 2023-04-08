package me.zero.clarinet.command.commands;

import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.util.text.TextFormatting;

public class CmdPing extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        ClientUtils.message("Your ping is " + TextFormatting.BLUE + ClientUtils.getLatency(mc.player) + "ms");
        return true;
    }

    @Override
    protected String[] names() {
        return new String[]{ "ping" };
    }

    @Override
    protected String description() {
        return "Shows your ping";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[0];
    }
}
