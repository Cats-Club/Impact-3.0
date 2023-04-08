package me.zero.clarinet.command.commands;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.ClientUtils;

public class CmdPanic extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        for (Mod m : Impact.getInstance().getModManager().getMods()) {
            if (m.isToggled()) {
                m.toggle();
            }
        }
        ClientUtils.message("All mods were disabled!");
        return true;
    }

    @Override
    protected String[] names() {
        return new String[]{ "panic" };
    }

    @Override
    protected String description() {
        return "Disables all mods";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[0];
    }
}
