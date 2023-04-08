package me.zero.clarinet.mod.movement;

import me.zero.clarinet.event.api.types.Priority;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class FastFall extends Mod {

	public FastFall() {
		super("FastFall", "Fall faster", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@Override
	public void onDisable() {
		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
	}

	@EventTarget(Priority.HIGHEST)
	public void onTick(EventTick event) {
		if (mc.player.fallDistance > 0.0F) {
			((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 3.0F);
			mc.player.motionX = 0.0F;
			mc.player.motionZ = 0.0F;
		} else {
			((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
		}
	}
}
