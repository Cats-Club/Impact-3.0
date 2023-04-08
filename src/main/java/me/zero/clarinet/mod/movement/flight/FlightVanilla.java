package me.zero.clarinet.mod.movement.flight;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventGround;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Flight;
import me.zero.clarinet.util.ClientUtils;

public class FlightVanilla extends ModMode<Flight> {
	
	public FlightVanilla(Flight parent) {
		super(parent, "Vanilla");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		float divisor = 3.5F;
		if (mc.player.movementInput.jump) {
			mc.player.motionY = parent.speed.getValue().doubleValue() / divisor;
		} else if (mc.player.movementInput.sneak) {
			mc.player.motionY = -parent.speed.getValue().doubleValue() / divisor;
		} else {
			mc.player.motionY = 0.0D;
		}
	}

	@EventTarget
    public void onGround(EventGround event) {
        event.onGround = true;
    }
	
	@EventTarget
	public void onMotion(EventMove event) {
		ClientUtils.setMoveSpeed(event, ClientUtils.getBaseMoveSpeed() * parent.speed.getValue().doubleValue());
	}
}
