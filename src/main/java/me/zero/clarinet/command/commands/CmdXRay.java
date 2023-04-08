package me.zero.clarinet.command.commands;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.block.Block;

public class CmdXRay extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                Block b = Block.getBlockFromName(args[1]);
                if (b != null) {
                    Impact.getInstance().getXrayManager().addBlock(b);
                } else {
                    ClientUtils.error(args[1] + " isn't a valid block!");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("del")) {
                Block b = Block.getBlockFromName(args[1]);
                if (b != null) {
                    Impact.getInstance().getXrayManager().removeBlock(b);
                } else {
                    ClientUtils.error(args[1] + " isn't a valid block!");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected String[] names() {
        return new String[]{ "xray" };
    }

    @Override
    protected String description() {
        return "Manage blocks for XRay!";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("add <block>", "Add block to XRay"),
                new CommandUsage("del <block>", "Remove block from XRay")
        };
    }
}
