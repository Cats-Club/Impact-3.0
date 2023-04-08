package me.zero.clarinet.mod.movement.speed;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Speed;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.MathUtils;
import me.zero.clarinet.util.anticheat.util.NCP;

public class SpeedFloat extends ModMode<Speed> implements NCP {

	public SpeedFloat(Speed parent) {
		super(parent, "Float");
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		if (!(mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F)) {
			double offset = 0.1;
			event.x += Math.random() * (offset * 2) - offset;
			event.z += Math.random() * (offset * 2) - offset / 2;
		}
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		double height = 0.425;
		double dist = MathUtils.roundToPlace(mc.player.posY - (int) mc.player.posY, 3);

		if (dist == height) {
			event.y *= -0.005;
		} else if (dist < height) {
			event.y *= MIN_MOTIONY_DECREASE;
		}

		System.out.println(event.y);
		
		if (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) {
			if (mc.player.onGround) {
				event.y = height;
			}
		}
		mc.player.motionY = event.y;
		ClientUtils.setMoveSpeed(event, ClientUtils.getBaseMoveSpeed());
	}
}
