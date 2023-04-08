package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class Jetpack extends Mod {
	
	public Jetpack() {
		super("Jetpack", "Hold space and fly up", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.motionY += 0.3D;
			mc.player.capabilities.isFlying = false;
		}
	}
}
