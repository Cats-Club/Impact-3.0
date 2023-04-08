package me.zero.clarinet.mod.player;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.NumberValue;

public class AutoSteal extends Mod {
	
	private NumberValue delay = new NumberValue(this, "Delay", "delay", 250D, 0D, 500D, 25D);
	
	public AutoSteal() {
		super("AutoSteal", "Automatically steal things from chests", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	public int getDelay() {
		return delay.getValue().intValue();
	}
}
