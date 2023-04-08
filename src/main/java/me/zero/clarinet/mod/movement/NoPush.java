package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class NoPush extends Mod {
	
	private float oldReduction;
	
	public NoPush() {
		super("NoPush", "Don't get pushed", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@Override
	public void onEnable() {
		if (mc.player != null) {
			oldReduction = mc.player.entityCollisionReduction;
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.player.entityCollisionReduction = 1.0F;
	}
	
	@Override
	public void onDisable() {
		if (mc.player != null) {
			mc.player.entityCollisionReduction = oldReduction;
		}
	}
}
