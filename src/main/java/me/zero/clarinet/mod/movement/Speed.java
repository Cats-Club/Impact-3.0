package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.movement.speed.SpeedAAC;
import me.zero.clarinet.mod.movement.speed.SpeedBhop;
import me.zero.clarinet.mod.movement.speed.SpeedFloat;
import me.zero.clarinet.mod.movement.speed.SpeedBoost;
import me.zero.clarinet.mod.movement.speed.SpeedLowHop;
import me.zero.clarinet.mod.movement.speed.SpeedVanilla;
import me.zero.clarinet.mod.movement.speed.SpeedVhop;
import me.zero.clarinet.mod.movement.speed.SpeedYPort;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;

public class Speed extends Mod {
	
	public BooleanValue antishake;
	
	public NumberValue speed, boost;
	
	public Speed() {
		super("Speed", "Go faster", Keyboard.KEY_V, Category.MOVEMENT);

		this.setModes(
		        new SpeedBhop(this),
                new SpeedVhop(this),
                new SpeedYPort(this),
                new SpeedLowHop(this),
                new SpeedVanilla(this),
                new SpeedAAC(this),
                new SpeedBoost(this),
                new SpeedFloat(this)
        );
		
		antishake = new BooleanValue(this, "Vanilla Antishake", "antishake");
		speed = new NumberValue(this, "Vanilla Speed", "speed", 1D, 1D, 5D, 0.25D);
		boost = new NumberValue(this, "Timer Boost", "boost", 1D, 0.5D, 3D, 0.1D);
	}
}
