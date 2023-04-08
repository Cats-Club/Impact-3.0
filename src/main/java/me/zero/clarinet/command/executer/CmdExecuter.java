package me.zero.clarinet.command.executer;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.command.CommandOptions;
import me.zero.clarinet.command.util.CommandUtils;
import me.zero.clarinet.event.network.EventSendChat;
import me.zero.clarinet.util.ClientUtils;

public class CmdExecuter {

    @EventTarget
    public void onSendChat(EventSendChat event) {
        if (run(event.getMessage())) {
            event.setCancelled(true);
        }
    }
	
	public static boolean run(String message) {
		String prefix = String.valueOf(CommandOptions.getPrefix());
		if (message.startsWith(prefix)) {
			message = message.replaceFirst(prefix, "");
			String name = message.split(" ")[0];
			
			String[] args = new String[0];
			if (message.split(" ").length > 1) {
				args = CmdParser.parse(message.replaceFirst(name + " ", ""));
			}
			
			for (AbstractCommand command : Impact.getInstance().getCommandManager().getCommands()) {
				if (CommandUtils.isName(command, name)) {
					if (!command.run(message, args)) {
                        ClientUtils.error("Invalid Arguments");
                    }
				}
			}
			return true;
		}
		return false;
	}
}
