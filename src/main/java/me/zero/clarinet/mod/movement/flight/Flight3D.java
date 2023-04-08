package me.zero.clarinet.mod.movement.flight;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Flight;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.util.math.MathHelper;

public class Flight3D extends ModMode<Flight> {
	
	public Flight3D(Flight parent) {
		super(parent, "3D");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		double speed = ClientUtils.getBaseMoveSpeed() * parent.speed.getValue().doubleValue();
		double forward = mc.player.moveForward;
		double strafe = mc.player.moveStrafing;
		float yaw = mc.player.rotationYaw;
		float pitch = mc.player.rotationPitch;
		if ((forward == 0.0D) && (strafe == 0.0D)) {
			mc.player.motionX = 0;
			mc.player.motionY = 0;
			mc.player.motionZ = 0;
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}
			double strafeX = Math.sin(Math.toRadians(yaw + 90.0F));
			double strafeZ = Math.cos(Math.toRadians(yaw + 90.0F));
			yaw = (float) Math.toRadians(yaw);
			pitch = (float) Math.toRadians(pitch);
			double dirX = -Math.sin(yaw) * Math.cos(pitch);
			double dirZ = Math.cos(yaw) * Math.cos(pitch);
			double dirY = -MathHelper.sin(pitch);
			mc.player.motionX = (forward * speed * dirX) + (strafe * speed * strafeX);
			mc.player.motionZ = (forward * speed * dirZ) - (strafe * speed * strafeZ);
			mc.player.motionY = (forward * speed * dirY);
		}
	}
}
