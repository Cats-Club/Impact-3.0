package me.zero.clarinet.command.commands;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.util.text.TextFormatting;

public class CmdToggle extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (args.length == 1) {
            Mod mod = null;
            for (Mod m : Impact.getInstance().getModManager().getMods()) {
                if (m.getName().equalsIgnoreCase(args[0])) {
                    mod = m;
                    break;
                }
            }
            if (mod != null) {
                mod.toggle();
                ClientUtils.message("Toggled " + TextFormatting.BLUE + mod.getName());
            } else {
                ClientUtils.error("Invalid Mod Specified!");
            }
            return true;
        }
        return false;
    }

    @Override
    public String[] names() {
        return new String[] { "t", "toggle" };
    }

    @Override
    public String description() {
        return "Toggles a mod";
    }

    @Override
    public CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("<mod>", "Toggles specified mod")
        };
    }
}
