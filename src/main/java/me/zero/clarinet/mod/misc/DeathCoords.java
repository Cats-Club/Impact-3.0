package me.zero.clarinet.mod.misc;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventPlayerDeath;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.util.text.TextFormatting;

public class DeathCoords extends Mod {
	
	public DeathCoords() {
		super("DeathCoords", "Tells you where you died", Keyboard.KEY_NONE, Category.MISC);
	}
	
	@EventTarget
	public void onPlayerDeath(EventPlayerDeath event) {
		TextFormatting c1 = TextFormatting.BLUE;
		TextFormatting c2 = TextFormatting.GRAY;
		ClientUtils.message("You Died At: " + c1 + "X: " + c2 + ((int) mc.player.posX) + ", " + c1 + "Y: " + c2 + ((int) mc.player.posY) + ", " + c1 + "Z: " + c2 + ((int) mc.player.posZ));
	}
}
