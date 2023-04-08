package me.zero.clarinet.mod.player;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.NumberValue;

public class Timer extends Mod {
	
	private NumberValue speed = new NumberValue(this, "Speed", "speed", 2D, 0.1D, 5D, 0.1D);
	
	public Timer() {
		super("Timer", "Change the speed of your game", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.suffix = String.valueOf(speed.getValue().floatValue());
		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / speed.getValue().floatValue());
	}
	
	@Override
	public void onDisable() {
		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
	}
}
