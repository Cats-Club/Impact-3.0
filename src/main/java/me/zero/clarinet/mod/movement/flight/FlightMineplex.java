package me.zero.clarinet.mod.movement.flight;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Flight;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.TimerUtil;

public class FlightMineplex extends ModMode<Flight> {
	
	private TimerUtil timer = new TimerUtil();
	
	public FlightMineplex(Flight parent) {
		super(parent, "Mineplex");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!mc.player.onGround) {
			if (mc.gameSettings.keyBindJump.isKeyDown() && timer.speed(5)) {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 2, mc.player.posZ);
				timer.reset();
			} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.motionY = -0.35F;
			} else {
				mc.player.motionY = -0.004F;
			}
		}
		mc.player.jumpMovementFactor = 0.04F;
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		ClientUtils.setMoveSpeed(event, ClientUtils.getBaseMoveSpeed() * ((ClientUtils.getDistanceToGround() > 0.1) ? Math.min(parent.speed.getValue().doubleValue(), 5) : 1));
	}
}
