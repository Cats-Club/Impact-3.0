package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class Parkour extends Mod {
	
	public Parkour() {
		super("Parkour", "Jumps when you hit the edge of blocks", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.player.onGround && !mc.player.isSneaking() && mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty()){
			mc.player.jump();
		}
	}
}
