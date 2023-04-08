package me.zero.clarinet.mod.misc;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;

public class Animations extends Mod {

	// Doogie13 - todo: this isn't how it should be but its close enough lol

	public MultiValue<String> mode = new MultiValue<>(this, "Block Mode", "mode", "1.7", new String[] { "1.7", "1.8", "Fancy" });
	
	public BooleanValue sword = new BooleanValue(this, "Sword Blocking", "block", true);
	public BooleanValue recharge = new BooleanValue(this, "Recharge", "recharge");
	public BooleanValue shield = new BooleanValue(this, "Better Shield", "shield");

	public NumberValue lower = new NumberValue(this, "Lowered Amount", "low_amount", 0.05D, 0D, 0.2D, 0.01D);
	
	public Animations() {
		super("Animations", "Changes Animations", Keyboard.KEY_NONE, Category.MISC, false);
	}
}
