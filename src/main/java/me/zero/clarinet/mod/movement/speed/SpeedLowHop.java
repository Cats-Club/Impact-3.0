package me.zero.clarinet.mod.movement.speed;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Speed;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.MathUtils;

public class SpeedLowHop extends ModMode<Speed> {
	
	private double moveSpeed;
	private double lastDist;
	private int timerDelay;
	private int stage;
	
	public SpeedLowHop(Speed parent) {
		super(parent, "LowHop");
	}
	
	@Override
	public void onEnable() {
		this.stage = 1;
		this.moveSpeed = ClientUtils.getBaseMoveSpeedNoBoost();
	}
	
	@Override
	public void onDisable() {
		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		double xDist = mc.player.posX - mc.player.prevPosX;
		double zDist = mc.player.posZ - mc.player.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		int downIndex = 1;
		double[][] downMemes = { { 0.086, 0.1310812D }, { 0.941, 0.081 } };
		
		double up = 0.39936D;
		double down = downMemes[downIndex][1];
		
		this.timerDelay += 1;
		this.timerDelay %= 5;
		if (timerDelay != 0) {
			((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
		} else {
			if (mc.player.motionX != 0.0D && mc.player.motionZ != 0.0D && mc.player.motionY != 0.0D) {
				((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.5F);
			}
		}
		if (MathUtils.roundToPlace(mc.player.posY - (int) mc.player.posY, 3) == downMemes[downIndex][0]) {
			mc.player.motionY = -down;
		}
		if (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) {
			switch (stage) {
				case 1: {
					if (mc.player.onGround) {
						stage = 2;
						mc.player.motionY = up;
						event.y = up;
						moveSpeed *= 2.149D;
					}
					break;
				}
				case 2: {
					stage = 3;
					double difference = 0.66D * (lastDist - ClientUtils.getBaseMoveSpeed());
					moveSpeed = lastDist - difference;
					break;
				}
				case 3: {
					if ((mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, mc.player.motionY, 0.0D)).size() > 0) || (mc.player.collidedVertically)) {
						stage = 1;
					}
					moveSpeed = (lastDist - lastDist / 159.0D);
					break;
				}
			}
		}
		ClientUtils.setMoveSpeed(event, Math.max(moveSpeed, ClientUtils.getBaseMoveSpeed()));
	}
}
