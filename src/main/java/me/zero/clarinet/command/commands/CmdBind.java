package me.zero.clarinet.command.commands;

import java.lang.reflect.Field;

import me.zero.clarinet.command.AbstractCommand;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.render.ClickGui;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.util.text.TextFormatting;

public class CmdBind extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (args.length == 2) {
            Mod mod = Impact.getInstance().getModManager().get(args[0]);
            if (mod != null) {
                if (args[1].equalsIgnoreCase("none")) {
                    mod.setKeybind(0);
                    ClientUtils.message(TextFormatting.GRAY + "Unbound " + TextFormatting.BLUE + mod.getName());
                    return true;
                } else {
                    args[1] = args[1].toUpperCase();
                    int key = getKeybind(args[1]);
                    if (key != 0) {
                        mod.setKeybind(key);
                        ClientUtils.message(TextFormatting.GRAY + "Bound " + TextFormatting.BLUE + mod.getName() + TextFormatting.GRAY + " to " + TextFormatting.BLUE + args[1]);
                        return true;
                    } else {
                        ClientUtils.message(TextFormatting.RED + "Unable to bind the key \"" + args[1] + "\"");
                    }
                }
            } else {
                ClientUtils.message(TextFormatting.RED + "Invalid Mod!");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reset")) {
                for (Mod m : Impact.getInstance().getModManager().getMods()) {
                    if (!(m instanceof ClickGui)) {
                        m.setKeybind(0);
                    }
                }
                ClientUtils.message(TextFormatting.GRAY + "Unbound all mods!");
                return true;
            }
        }
        return false;
    }

    @Override
    public String[] names() {
        return new String[] { "bind" };
    }

    @Override
    public String description() {
        return "Bind mods";
    }

    @Override
    public CommandUsage[] usage() {
        return new CommandUsage[] {
                new CommandUsage("<mod> <key|none>", "Bind or unbind a mod"),
                new CommandUsage("reset", "Reset all binds")
        };
    }
	
	public int getKeybind(String name) {
		Field[] fields = Keyboard.class.getFields();
		for (Field field : fields) {
			if (field.getName().equalsIgnoreCase("KEY_" + name)) {
				try {
					return field.getInt(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}
}
