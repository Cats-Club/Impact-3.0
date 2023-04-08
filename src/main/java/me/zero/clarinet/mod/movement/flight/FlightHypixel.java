package me.zero.clarinet.mod.movement.flight;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Flight;
import me.zero.clarinet.util.ClientUtils;

public class FlightHypixel extends ModMode<Flight> {
	
	public FlightHypixel(Flight parent) {
		super(parent, "Hypixel");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.player.motionY *= 0.02D;
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.motionY -= 0.4D;
		}
		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.motionY += 0.4D;
		}
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		ClientUtils.setMoveSpeed(event, ClientUtils.getBaseMoveSpeed());
	}
}