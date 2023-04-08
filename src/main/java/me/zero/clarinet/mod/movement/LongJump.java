package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.movement.longjump.LongJumpNoDamage;
import me.zero.clarinet.mod.movement.longjump.LongJumpNormal;
import me.zero.values.types.NumberValue;

public class LongJump extends Mod {
	
	public NumberValue boost;
	
	public NumberValue ndspeed;
	
	public LongJump() {
		super("LongJump", "Jump Farther", Keyboard.KEY_NONE, Category.MOVEMENT);
		
		this.setModes(
		        new LongJumpNormal(this),
                new LongJumpNoDamage(this)
        );
		
		boost = new NumberValue(this, "Boost", "boost", 15D, 1D, 20D, 0.25D);
		ndspeed = new NumberValue(this, "NoDamage Speed", "ndspeed", 3D, 3D, 15D, 0.25D);
	}
}
