package me.zero.clarinet.command.commands;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.mod.exploit.GhostHand;
import me.zero.clarinet.util.ClientUtils;

public class CmdGhosthand extends AbstractCommand {
	
	@Override
	public boolean run(String message, String[] args) {
		if (args.length == 1) {
			try {
				int id = Integer.valueOf(args[0]);
				Impact.getInstance().getModManager().get(GhostHand.class).id = id;
			} catch (Exception e) {
				ClientUtils.error(args[0] + " is not a valid number!");
			}
			return true;
		}
		return false;
	}

    @Override
    protected String[] names() {
        return new String[]{ "ghosthand", "gh" };
    }

    @Override
    protected String description() {
        return "Manage GhostHand mod";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("<id>", "Set the GhostHand ID")
        };
    }
}
