package me.zero.clarinet.mod.movement.flight;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Flight;
import me.zero.clarinet.util.ClientUtils;

public class FlightCubecraft extends ModMode<Flight> {
	
	public FlightCubecraft(Flight parent) {
		super(parent, "Cubecraft");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.player.motionY *= 0.02D;
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.motionY -= 0.3D;
		} else if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.motionY += 0.3D;
		} else {
			mc.player.motionY -= 0.04;
		}
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		ClientUtils.setMoveSpeed(event, ClientUtils.getBaseMoveSpeed());
	}
}
