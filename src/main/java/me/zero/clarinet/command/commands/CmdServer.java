package me.zero.clarinet.command.commands;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.TextFormatting;

public class CmdServer extends AbstractCommand {

	@Override
	public boolean run(String message, String[] args) {
		if (args.length == 0) {
			ServerData server = ClientUtils.getCurrentServer();
			if (server != null) {
				ClientUtils.message(TextFormatting.GRAY + "Server IP: " + TextFormatting.BLUE + server.serverIP);
				ClientUtils.message(TextFormatting.GRAY + "Server Version: " + TextFormatting.BLUE + server.gameVersion);
				Impact.getInstance().getCommandManager().get(CmdPlugins.class).run("", new String[0]);
			} else {
				ClientUtils.error("The server command only works on online servers!");
			}
			return true;
		}
		return false;
	}

    @Override
    protected String[] names() {
        return new String[]{ "server" };
    }

    @Override
    protected String description() {
        return "Lists server information";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[0];
    }
}
