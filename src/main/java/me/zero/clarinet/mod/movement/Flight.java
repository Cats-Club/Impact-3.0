package me.zero.clarinet.mod.movement;

import me.zero.clarinet.mod.movement.flight.*;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;

public class Flight extends Mod {
	
	public NumberValue speed;
	
	public Flight() {
		super("Flight", "You can fly", Keyboard.KEY_F, Category.MOVEMENT);
		
		this.setModes(
		        new FlightVanilla(this),
                new FlightHypixel(this),
                new FlightCubecraft(this),
                new FlightMineplex(this),
                new Flight3D(this),
                new FlightAAC(this),
                new FlightNCP(this)
        );
		
		speed = new NumberValue(this, "Speed", "speed", 1D, 1D, 20D, 0.5D);
	}
}
