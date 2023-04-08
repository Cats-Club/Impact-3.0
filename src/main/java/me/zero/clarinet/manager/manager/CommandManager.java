package me.zero.clarinet.manager.manager;

import java.util.List;

import me.zero.clarinet.command.CommandOptions;
import me.zero.clarinet.command.commands.*;
import me.zero.clarinet.manager.Manager;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.values.ValueManager;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;
import me.zero.values.types.Value;
import net.minecraft.util.text.TextFormatting;

public class CommandManager extends Manager<AbstractCommand> {
	
	public CommandManager() {
		super("Command");
	}
	
	@Override
	public void load() {
		this.addData(
				new CmdHelp(),
				new CmdXRay(),
				new CmdBind(),
				new CmdFriend(),
				new CmdVClip(),
				new CmdSay(),
				new CmdToggle(),
				new CmdPlugins(),
				new CmdServer(),
				new CmdGhosthand(),
				new CmdDevMsg(),
				new CmdClear(),
				new CmdPanic(),
				new CmdPing(),
				new CmdNuker(),
				new CmdWaypoint(),
				new CmdPrefix(),
                new CmdMeme(),
                new CmdSniffer(),
                new CmdPizza(),
                new CmdLeave(),
                new CmdDrop(),
                new CmdDupe());
		
		/**
		Command command = new Command("mod", "mod <name> <option> <value>", "Manage settings for mods") {
			@Override
			public void onExecute(String[] args) {
				if (args.length > 0) {
					Mod mod = Impact.getInstance().getModManager().get(args[0]);
					if (mod != null) {
						String[] temp = new String[args.length - 1];
						for (int i = 1; i < args.length; i++) {
							temp[i - 1] = args[i];
						}
						args = temp;
						String[] response = handleMod(mod, args);
						if (response == null) {
							ClientUtils.error("Unable to set value!");
							return;
						} else {
							for (String str : response) {
								if (response.length > 0) {
									ClientUtils.message(str);
								}
							}
						}
					} else {
						ClientUtils.error("Invalid Mod Specified!");
					}
				} else {
					this.onThrow();
				}
			}
		};
		this.addData(command);
		*/
	}

    @Override
    public void save() {}

    private String[] handleMod(Mod mod, String[] args) {
		if (args.length > 0) {
			for (Value val : ValueManager.INSTANCE.getValues(mod)) {
				if (!val.getName().equalsIgnoreCase(args[0].replaceAll("_", " "))) {
					continue;
				}
				if (val instanceof BooleanValue) {
					BooleanValue bo = (BooleanValue) val;
					bo.setValue(!bo.getValue());
					return new String[] { "Set " + TextFormatting.BLUE + val.getName() + TextFormatting.GRAY + " to " + TextFormatting.BLUE + bo.getValue() };
				}
				if (val instanceof NumberValue) {
					NumberValue no = (NumberValue) val;
					if (args.length > 1) {
						try {
							Number value = Double.valueOf(args[1]);
							no.setValue(value);
							return new String[] { "Set " + TextFormatting.BLUE + val.getName() + TextFormatting.GRAY + " to " + TextFormatting.BLUE + no.getValue() };
						} catch (Exception e) {
							return null;
						}
					} else {
						return null;
					}
				}
				if (val instanceof MultiValue) {
					MultiValue so = (MultiValue) val;
					if (args.length > 1 && so.getValue() instanceof String) {
						boolean valid = false;
						for (Object o : so.getValues()) {
							String str = (String) o;
							if (args[1].equalsIgnoreCase(str)) {
								valid = true;
							}
						}
						if (valid) {
							so.setValue(args[1]);
							return new String[] { "Set " + TextFormatting.BLUE + val.getName() + TextFormatting.GRAY + " to " + TextFormatting.BLUE + so.getValue() };
						} else {
							return null;
						}
					} else {
						return null;
					}
				}
			}
		} else {
			String response = "";
			for (Value opt : ValueManager.INSTANCE.getValues(mod)) {
				response += CommandOptions.getPrefix() + mod.getName().toLowerCase() + " " + opt.getName() + ((ValueManager.INSTANCE.getValues(mod).indexOf(opt) != ValueManager.INSTANCE.getValues(mod).size() - 1) ? "\n" : "");
			}
			if (ValueManager.INSTANCE.getValues(mod).size() == 0) {
				response = "There are no availiable options for " + TextFormatting.BLUE + mod.getName();
			}
			return response.split("\n");
		}
		return null;
	}
	
	public List<AbstractCommand> getCommands() {
		return this.getData();
	}
}