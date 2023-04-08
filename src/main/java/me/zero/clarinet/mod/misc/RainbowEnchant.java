package me.zero.clarinet.mod.misc;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class RainbowEnchant extends Mod {
	
	public RainbowEnchant() {
		super("RainbowEnchant", "Turns enchant color from purple to rainbow", Keyboard.KEY_NONE, Category.MISC, false);
	}
}
