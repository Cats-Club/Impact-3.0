package me.zero.clarinet.mod.movement.speed;

import org.apache.commons.lang3.ArrayUtils;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Speed;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.MathUtils;

public class SpeedVhop extends ModMode<Speed> {
	
	private double moveSpeed;
	private double lastDist;
	private int stage;
	
	public SpeedVhop(Speed parent) {
		super(parent, "Vhop");
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
		if (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) {
			
			double[] values = { 0.943, 0.443, 0.005, 0.880 };
			if (!mc.player.collidedHorizontally && ArrayUtils.contains(values, MathUtils.roundToPlace(mc.player.posY - (int) mc.player.posY, 3))) {
				mc.player.motionY = -0.2;
				event.y = -0.2;
			}
			
			switch (stage) {
				case 1: {
					stage = 2;
					moveSpeed = (2.149D * ClientUtils.getBaseMoveSpeed() - 0.01D);
					break;
				}
				case 2: {
					if (mc.player.onGround) {
						stage = 3;
						mc.player.motionY = 0.4D;
						event.y = 0.4D;
						moveSpeed *= 2.149D;
					}
					break;
				}
				case 3: {
					stage = 4;
					moveSpeed = (lastDist - 0.66D * (lastDist - ClientUtils.getBaseMoveSpeed()));
					break;
				}
				case 4: {
					if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, mc.player.motionY, 0.0D)).size() > 0) {
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
