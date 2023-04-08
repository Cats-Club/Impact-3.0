package me.zero.clarinet.mod.movement.speed;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Speed;
import me.zero.clarinet.util.ClientUtils;

public class SpeedYPort extends ModMode<Speed> {
	
	private double moveSpeed;
	private double lastDist;
	private int stage;
	
	public SpeedYPort(Speed parent) {
		super(parent, "YPort");
	}
	
	@Override
	public void onEnable() {
		this.stage = 1;
		this.moveSpeed = ClientUtils.getBaseMoveSpeedNoBoost();
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		double xDist = mc.player.posX - mc.player.prevPosX;
		double zDist = mc.player.posZ - mc.player.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		if (mc.player.fallDistance >= 1.0F) {
			return;
		}
		
		double amt = 0.327244884939;
		double uamt = 0.399039995003033D;

		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
		stage = mc.player.onGround ? 1 : stage;
		if (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) {
			switch (stage) {
				case 1: {
					if (mc.player.onGround) {
						moveSpeed *= 0.901467505241D;
						stage = 2;
						mc.player.motionY = uamt;
						event.y = uamt;
						this.moveSpeed *= 2.385D;
						mc.player.motionY = -amt;
					}
					break;
				}
				case 2: {
					stage = 3;
					double difference = 0.66D * (this.lastDist - ClientUtils.getBaseMoveSpeed());
					moveSpeed = (this.lastDist - difference);
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
