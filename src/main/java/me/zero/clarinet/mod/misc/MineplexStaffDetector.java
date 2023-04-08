package me.zero.clarinet.mod.misc;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.commands.CmdPanic;
import me.zero.clarinet.event.network.EventReceiveChat;
import me.zero.clarinet.event.render.EventRender2D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.ClientUtils;
import me.zero.values.types.BooleanValue;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.util.text.TextFormatting;

public class MineplexStaffDetector extends Mod {
	
	private static HashMap<String, MineplexRank> staff = new HashMap<String, MineplexRank>();
	
	private BooleanValue panic = new BooleanValue(this, "Panic", "panic");
	
	public MineplexStaffDetector() {
		super("MineplexStaffDetector", "Displays a notification when a staff member joins your game", Keyboard.KEY_NONE, Category.MISC);
		new Thread("Mineplex Staff Retriever") {
			@Override
			public void run() {
				retrieveStaff();
			}
		}.start();
	}
	
	public enum MineplexRank {
		
		TRAINEE(TextFormatting.DARK_AQUA, "Trainee"),
		MOD(TextFormatting.GOLD, "Mod"),
		SR_MOD(TextFormatting.GOLD, "Sr Mod"),
		ADMIN(TextFormatting.RED, "Admin"),
		OWNER(TextFormatting.GREEN, "Owner"),
		YOUTUBE(TextFormatting.DARK_PURPLE, "YouTube"),
		BUILDER(TextFormatting.BLUE, "Builder");
		
		private TextFormatting color;
		private String name;
		
		private MineplexRank(TextFormatting color, String name) {
			this.color = color;
			this.name = name;
		}
		
		public TextFormatting getColor() {
			return this.color;
		}
		
		public String getName() {
			return this.name;
		}
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		ScaledResolution sr = new ScaledResolution(mc);
		int y = sr.getScaledHeight() - 12;
		int x = sr.getScaledWidth() - 2;
		for (Entity ent : mc.world.loadedEntityList) {
			if (ent instanceof EntityPlayer) {
				String username = ent.getName();
				for (String name : staff.keySet()) {
					if (username.equalsIgnoreCase(name)) {
						MineplexRank eRank = staff.get(name);
						String rank = eRank.getName();
						TextFormatting rankColor = eRank.getColor();
						String msg = rankColor + "" + TextFormatting.BOLD + rank + " " + TextFormatting.GRAY + username;
						mc.fontRenderer.drawStringWithShadow(msg, x - mc.fontRenderer.getStringWidth(msg), y, 0xFFFFFFFF);
						y -= 12;
					}
				}
			}
		}
	}
	
	@EventTarget
	public void onReceiveMessage(EventReceiveChat event) {
		String message = event.chatComponent.getUnformattedText();
		if (message.startsWith("Join>")) {
			for (String name : staff.keySet()) {
				if (message.startsWith("Join> " + name) || message.equalsIgnoreCase("Join> " + name)) {
					warn(staff.get(name), name);
				}
			}
		}
	}
	
	@EventTarget
	public void onPacketReceive(SPacketSpawnPlayer event) {
		String username = mc.world.getEntityByID(event.getEntityID()).getName();
		for (String name : staff.keySet()) {
			if (username.equals(name)) {
				warn(staff.get(name), name);
			}
		}
	}
	
	public void retrieveStaff() {
		String grabbed = "";
		MineplexRank curr = null;
		URL url = null;
		url = Impact.getURL("mineplexstaff");
		if (url != null) {
			ClientUtils.log("[Data] Grabbing Mineplex Staff List");
			try {
				Scanner scr = new Scanner(url.openStream());
				while (scr.hasNext()) {
					grabbed += scr.nextLine();
				}
				scr.close();
				ClientUtils.log("[Data] Grabbed Mineplex Staff List");
			} catch (IOException e) {
				ClientUtils.log("[Data] Unable to grab Mineplex Staff List");
			}
		}
		String[] rankSplit = grabbed.split("<div class=\"mask\">");
		for (int i = 0; i < rankSplit.length; i++) {
			String currentRank = rankSplit[i].split("</div></div></div>")[0];
			if (currentRank.equalsIgnoreCase("Owners")) {
				curr = MineplexRank.OWNER;
			}
			if (currentRank.equalsIgnoreCase("Executive Staff")) {
				curr = MineplexRank.ADMIN;
			}
			if (currentRank.equalsIgnoreCase("Leadership Team")) {
				curr = MineplexRank.SR_MOD;
			}
			if (currentRank.equalsIgnoreCase("Official YouTuber")) {
				curr = MineplexRank.YOUTUBE;
			}
			if (currentRank.equalsIgnoreCase("Admin")) {
				curr = MineplexRank.ADMIN;
			}
			if (currentRank.equalsIgnoreCase("Clans Management")) {
				curr = MineplexRank.SR_MOD;
			}
			if (currentRank.equalsIgnoreCase("Community Management")) {
				curr = MineplexRank.SR_MOD;
			}
			if (currentRank.equalsIgnoreCase("Customer Support")) {
				curr = MineplexRank.SR_MOD;
			}
			if (currentRank.equalsIgnoreCase("Forum Ninja")) {
				curr = MineplexRank.SR_MOD;
			}
			if (currentRank.equalsIgnoreCase("Mod Coord")) {
				curr = MineplexRank.SR_MOD;
			}
			if (currentRank.equalsIgnoreCase("Quality Assurance")) {
				curr = MineplexRank.SR_MOD;
			}
			if (currentRank.equalsIgnoreCase("Social Media")) {
				curr = MineplexRank.SR_MOD;
			}
			if (currentRank.equalsIgnoreCase("Trainee Management")) {
				curr = MineplexRank.SR_MOD;
			}
			if (currentRank.equalsIgnoreCase("Mod")) {
				curr = MineplexRank.MOD;
			}
			if (currentRank.equalsIgnoreCase("Trainee")) {
				curr = MineplexRank.TRAINEE;
			}
			if (currentRank.equalsIgnoreCase("Builder")) {
				curr = MineplexRank.BUILDER;
			}
			String[] users = rankSplit[i].split("<a data-minitooltip=\"");
			for (int j = 0; j < users.length; j++) {
				String username = users[j].split("\"")[0];
				if (curr != null && !username.contains("</div>")) {
					addStaff(username.replaceAll(" ", ""), curr);
				}
			}
		}
	}
	
	private static void addStaff(String name, MineplexRank rank) {
		staff.put(name, rank);
	}
	
	public void warn(MineplexRank eRank, String name) {
		mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_WITHER_DEATH, 0.0F));
		String rank = eRank.getName();
		TextFormatting rankColor = eRank.getColor();
		ClientUtils.message(rankColor + "" + TextFormatting.BOLD + rank + " " + TextFormatting.GRAY + name + TextFormatting.WHITE + " has joined your game!");
		if (panic.getValue()) {
			Impact.getInstance().getCommandManager().get(CmdPanic.class).run("", new String[0]);
		}
	}
}
