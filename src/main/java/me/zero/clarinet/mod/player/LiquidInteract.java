package me.zero.clarinet.mod.player;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class LiquidInteract extends Mod {
	
	public LiquidInteract() {
		super("LiquidInteract", "Place and Break on liquids", Keyboard.KEY_NONE, Category.PLAYER);
	}
}
