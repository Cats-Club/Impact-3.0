package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.movement.phase.PhaseLatest;
import me.zero.clarinet.mod.movement.phase.PhaseNoClip;
import me.zero.clarinet.mod.movement.phase.PhaseSkipClip;

public class Phase extends Mod {
	
	public Phase() {
		super("Phase", "Allows you to go through blocks", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.setModes(
		        new PhaseSkipClip(this),
                new PhaseLatest(this),
                new PhaseNoClip(this)
        );
	}
}
