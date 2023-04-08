package net.mcleaks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.text.TextFormatting;

public class MCLeaks {
	
	public static Session savedSession = null;
	private static String mcLeaksSession;
	private static String mcName;
	
	public static boolean isAltActive() {
		return mcLeaksSession != null;
	}
	
	public static String getMCLeaksSession() {
		return mcLeaksSession;
	}
	
	public static String getMCName() {
		return mcName;
	}
	
	public static void refresh(String session, String name) {
		mcLeaksSession = session;
		mcName = name;
	}
	
	public static void remove() {
		mcLeaksSession = null;
		mcName = null;
	}
	
	public static String getStatus() {
		String status = TextFormatting.GOLD + "No Token redeemed. Using " + TextFormatting.YELLOW + Minecraft.getMinecraft().getSession().getUsername() + TextFormatting.GOLD + " to login!";
		if (mcLeaksSession != null) {
			status = TextFormatting.GREEN + "Token active. Using " + TextFormatting.AQUA + mcName + TextFormatting.GREEN + " to login!";
		}
		return status;
	}
}
