package me.zero.clarinet.mod.movement.speed;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Speed;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.anticheat.util.NCP;

public class SpeedVanilla extends ModMode<Speed> implements NCP {
	
	private double moveSpeed;
	private double lastDist;
	private int stage;
	
	public SpeedVanilla(Speed parent) {
		super(parent, "Vanilla");
	}
	
	@Override
	public void onEnable() {
		this.stage = 2;
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
	public void onUpdate(EventMotionUpdate event) {
		if (event.type != EventType.PRE) {
			return;
		}
		if (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) {

			((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / (stage == 2 ? 1.0F : parent.boost.getValue().floatValue()));
		}
		if (parent.antishake.getValue()) {
			mc.player.distanceWalkedModified = 0.0F;
		}
		if (this.stage == 2) {
			event.y += ClientUtils.isUnderBlock() ? 0.2 : BASIC_JUMP_HEIGHT;
		}
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		if (mc.player.onGround) {
			if ((!mc.player.collidedHorizontally && mc.player.moveForward != 0.0F) || mc.player.moveStrafing != 0.0F) {
				switch (stage) {
					case 1: {
						double max = 2.149;
						double min = 1.149;
						double mult = (parent.speed.getValue().floatValue() / 5.0F) * (max - min) + min;
						moveSpeed *= mult;
						stage = 2;
						break;
					}
					case 2: {
						stage = 1;
						double difference = 0.66 * (lastDist - ClientUtils.getBaseMoveSpeed());
						moveSpeed = lastDist - difference;
						break;
					}
				}
			} else {
				((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
			}
			ClientUtils.setMoveSpeed(event, moveSpeed = Math.max(moveSpeed, ClientUtils.getBaseMoveSpeed()));
		}
	}
}
