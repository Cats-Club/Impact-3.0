package me.zero.clarinet.command.commands;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.event.network.EventPacketReceive;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.util.text.TextFormatting;

public class CmdPlugins extends AbstractCommand {
	
	private long start = 0L;

    @Override
    public boolean run(String message, String[] args) {
        start = System.currentTimeMillis();
        mc.player.connection.sendPacket(new CPacketTabComplete("/", null, false));
        ClientUtils.message("Finding Possible Plugins...");
        EventManager.register(this);
        return true;
    }

    @Override
    protected String[] names() {
        return new String[]{ "plugins" };
    }

    @Override
    protected String description() {
        return "View possible server plugins";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[0];
    }

    @EventTarget
	public void onReceivePacket(EventPacketReceive event) {
		if (System.currentTimeMillis() - start > 5000) {
			ClientUtils.message("Request timed out after 5s");
			EventManager.unregister(this);
		}
		if (event.getPacket() instanceof SPacketTabComplete) {
			EventManager.unregister(this);
			SPacketTabComplete packet = (SPacketTabComplete) event.getPacket();
			String result = "";
			int count = 0;
			for (int i = 0; i < packet.getMatches().length; i++) {
				String s = packet.getMatches()[i];
				if (s.contains(":")) {
					String pluginName = s.split(":")[0].replaceAll("/", "");
					if (!result.contains(pluginName)) {
						if (!pluginName.equalsIgnoreCase("bukkit") && !pluginName.equalsIgnoreCase("minecraft")) {
							count++;
							result += TextFormatting.GRAY + pluginName + ", ";
						}
					}
				}
			}
			ClientUtils.message("Listing Plugins " + TextFormatting.BLUE + "(" + count + ")");
			StringBuilder builder = new StringBuilder(result);
			builder.replace(result.lastIndexOf(", "), result.length() - 1, ".");
			ClientUtils.message(builder.toString());
		}
	}
}
