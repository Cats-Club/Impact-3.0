package me.zero.clarinet.mod.render;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.BooleanValue;

public class LiquidVision extends Mod {
	
	public BooleanValue water = new BooleanValue(this, "Water", "water", true);
	
	public BooleanValue lava = new BooleanValue(this, "Lava", "lava", true);
	
	public LiquidVision() {
		super("LiquidVision", "See better in liquids", Keyboard.KEY_NONE, Category.RENDER);
	}
}
