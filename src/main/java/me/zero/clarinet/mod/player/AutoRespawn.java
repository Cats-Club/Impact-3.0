package me.zero.clarinet.mod.player;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventPlayerDeath;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.BooleanValue;

public class AutoRespawn extends Mod {
	
	private BooleanValue home = new BooleanValue(this, "Home", "home");
	
	public AutoRespawn() {
		super("AutoRespawn", "Automatically Respawn", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@EventTarget
	public void onPlayerDeath(EventPlayerDeath event) {
		event.setCancelled(true);
		mc.player.respawnPlayer();
		if (home.getValue()) {
			mc.player.sendChatMessage("/home");
		}
	}
}
