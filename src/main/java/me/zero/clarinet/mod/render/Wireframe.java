package me.zero.clarinet.mod.render;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.NumberValue;

public class Wireframe extends Mod {
	
	public NumberValue width = new NumberValue(this, "LineWidth", "width", 0.1D, 0.1D, 3D, 0.1D);
	
	public Wireframe() {
		super("Wireframe", "Makes blocks turn into outlines", Keyboard.KEY_NONE, Category.RENDER);
	}
}
