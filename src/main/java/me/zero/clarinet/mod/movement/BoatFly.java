package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.entity.Entity;

public class BoatFly extends Mod {
	
	private NumberValue up = new NumberValue(this, "Up Speed", "up", 3D, 1D, 10D, 0.5D);
	
	private NumberValue down = new NumberValue(this, "Down Speed", "down", 3D, 1D, 10D, 0.5D);
	
	private BooleanValue stability = new BooleanValue(this, "Stability", "stability");
	
	public BoatFly() {
		super("BoatFly", "Fly with boats", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if (mc.player.isRiding()) {
			Entity ent = mc.player.getRidingEntity();
			mc.player.getRidingEntity().rotationYaw = mc.player.rotationYaw;
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				ent.motionY = up.getValue().doubleValue() / 10.0;
			} else if (mc.gameSettings.keyBindSprint.isKeyDown()) {
				ent.motionY = -(down.getValue().doubleValue() / 10.0);
			} else {
				ent.motionY = stability.getValue() ? 0.4 : 0;
			}
		}
	}
}
