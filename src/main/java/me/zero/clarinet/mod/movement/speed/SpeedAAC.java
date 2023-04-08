package me.zero.clarinet.mod.movement.speed;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Speed;
import me.zero.clarinet.util.ClientUtils;

public class SpeedAAC extends ModMode<Speed> {

    private double speed = 1.0D;

	public SpeedAAC(Speed parent) {
		super(parent, "AAC");
	}
	
	@EventTarget
	private void onMotionUpdate(EventMotionUpdate event) {
		if (mc.player.isInWater() || mc.player.isOnLadder()) {
			return;
		}
		if (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) {
			if (mc.player.collidedHorizontally && mc.player.ticksExisted % 2 == 1 && mc.player.onGround) {
                mc.player.jump();
            } else {
                if (mc.player.fallDistance > 0.0F) {
                    return;
                }
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
                speed += 0.1;
                speed = Math.min(speed, 1.8);

                if (mc.player.ticksExisted % 2 == 0) {
                    event.y += 0.2;
                }

                ClientUtils.setMoveSpeed(ClientUtils.getBaseMoveSpeedNoBoost() * speed);
            }
		} else {
            speed = 0.0D;
        }
	}
	
	@Override
	public void onDisable() {
		mc.player.motionY = -4.0D;
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
        speed = 1.0D;
	}
}
