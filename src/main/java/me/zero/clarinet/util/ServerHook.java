package me.zero.clarinet.util;

import me.zero.clarinet.mixin.mixins.minecraft.client.gui.IGuiMultiplayer;
import me.zero.clarinet.ui.screen.override.screen.OverrideGuiMultiplayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ServerHook {
	
	private static String currentServerIP = "127.0.0.1:25565";
	private static ServerListEntryNormal lastServer;
	
	public static void reconnectToLastServer(GuiScreen prevScreen) {
		if (lastServer == null) {
			return;
		}
		currentServerIP = lastServer.getServerData().serverIP;
		if (!currentServerIP.contains(":")) currentServerIP += ":25565";
		
		Minecraft mc = Minecraft.getMinecraft();
		mc.displayGuiScreen(new GuiConnecting(prevScreen, mc, lastServer.getServerData()));
	}
	
	public static void setCurrentIpToSingleplayer() {
		currentServerIP = "127.0.0.1:25565";
	}
	
	public static void updateLastServerFromServerlist(IGuiListEntry entry, GuiMultiplayer guiMultiplayer) {
		if (entry instanceof ServerListEntryNormal) {
			currentServerIP = ((ServerListEntryNormal) entry).getServerData().serverIP;
			if (!currentServerIP.contains(":")) {
				currentServerIP += ":25565";
			}
			lastServer = (ServerListEntryNormal) (((IGuiMultiplayer) guiMultiplayer).getServerListSelector().getSelected() < 0 ? null : ((IGuiMultiplayer) guiMultiplayer).getServerListSelector().getListEntry(((IGuiMultiplayer) guiMultiplayer).getServerListSelector().getSelected()));
		} else if (entry instanceof ServerListEntryLanDetected) {
			currentServerIP = ((ServerListEntryLanDetected) entry).getServerData().getServerIpPort();
			lastServer = createServerListEntry(guiMultiplayer, new ServerData("LAN-Server", currentServerIP, false));
		}
	}

	private static ServerListEntryNormal createServerListEntry(GuiMultiplayer guiMultiplayer, ServerData currentServerIP) {

		try {
			Constructor<ServerListEntryNormal> constructor = ServerListEntryNormal.class.getDeclaredConstructor(GuiMultiplayer.class, ServerData.class);
			boolean was = constructor.isAccessible();
			constructor.setAccessible(true);
			ServerListEntryNormal serverListEntryNormal = constructor.newInstance(guiMultiplayer, currentServerIP);
			constructor.setAccessible(was);
			return serverListEntryNormal;
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	public static void updateLastServerFromDirectConnect(GuiMultiplayer guiMultiplayer, ServerData serverData) {
		currentServerIP = serverData.serverIP;
		if (!currentServerIP.contains(":")) {
			currentServerIP += ":25565";
		}
		lastServer = createServerListEntry(guiMultiplayer, serverData);
	}
	
	public static String getCurrentServer() {
		return currentServerIP;
	}
	
	public static ServerListEntryNormal getLastServer() {
		return lastServer;
	}
}
