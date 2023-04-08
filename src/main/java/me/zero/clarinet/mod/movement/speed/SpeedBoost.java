package me.zero.clarinet.mod.movement.speed;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Speed;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.anticheat.util.NCP;

public class SpeedBoost extends ModMode<Speed> implements NCP {
	
	public SpeedBoost(Speed parent) {
		super(parent, "Boost");
	}
	
	@Override
	public void onDisable() {
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		if (event.type == EventType.PRE) {
            if (!mc.player.isInWater() && !mc.player.isInLava()) {
                if (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) {
                    if (!mc.player.movementInput.jump && mc.player.onGround) {
                        if (mc.player.ticksExisted % 2 == 0) {
                            event.y += ClientUtils.isUnderBlock() ? 0.2 : MIN_JUMP_HEIGHT;
                        }
                        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / (mc.player.ticksExisted % 3 == 0 ? 1.275F : 1.0F));

                        mc.player.motionX *= mc.player.ticksExisted % 2 != 0 ? MAX_SPEED_BOOST : MAX_SPEED_DECLINE;
                        mc.player.motionZ *= mc.player.ticksExisted % 2 != 0 ? MAX_SPEED_BOOST : MAX_SPEED_DECLINE;
                    }
                } else {
                    mc.player.motionX = 0.0;
                    mc.player.motionZ = 0.0;
                }
            }
        }
	}
}
