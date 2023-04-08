package me.zero.clarinet.mod.combat;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.NumberValue;

public class HitBox extends Mod {
	
	public NumberValue amount = new NumberValue(this, "Expansion Amount", "amount", 0.35D, 0D, 0.5D, 0.05D);
	
	public HitBox() {
		super("HitBox", "Expands entity hitboxes", Keyboard.KEY_NONE, Category.COMBAT);
	}
}
