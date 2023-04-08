package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class SafeWalk extends Mod {
	
	public SafeWalk() {
		super("SafeWalk", "Don't walk off ledges", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
}
