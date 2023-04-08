package me.zero.clarinet.mod.world;

import me.zero.clarinet.event.game.EventLoop;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class AntiWeather extends Mod {
	
	public AntiWeather() {
		super("AntiWeather", "No Rain or Snow", Keyboard.KEY_NONE, Category.WORLD);
	}
	
	@EventTarget
	public void onLoop(EventLoop event) {
		mc.world.setRainStrength(0.0F);
	}
}
