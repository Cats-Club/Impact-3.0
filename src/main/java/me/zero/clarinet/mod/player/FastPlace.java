package me.zero.clarinet.mod.player;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class FastPlace extends Mod {
	
	public FastPlace() {
		super("FastPlace", "You can place blocks a lot faster then usual.", 0, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		((IMinecraft) mc).setRightClickDelayTimer(0);
	}
	
	@Override
	public void onDisable() {
		((IMinecraft) mc).setRightClickDelayTimer(4);
	}
}
