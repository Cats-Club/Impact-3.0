package me.zero.clarinet.mod.render;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class AntiBlind extends Mod {
	
	public AntiBlind() {
		super("AntiBlind", "Removes Blindness and Nausea", Keyboard.KEY_NONE, Category.RENDER);
	}
}
