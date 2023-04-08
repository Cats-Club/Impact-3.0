package me.zero.clarinet.mod.movement.longjump;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.LongJump;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.MathUtils;

public class LongJumpNormal extends ModMode<LongJump> {
	
	private double moveSpeed;
	private double lastDist;
	private int stage;
	
	public LongJumpNormal(LongJump parent) {
		super(parent, "Normal");
	}
	
	@Override
	public void onEnable() {
		if (this.mc.player != null) {
			this.moveSpeed = ClientUtils.getBaseMoveSpeed();
			this.lastDist = 0.0D;
			this.stage = 1;
		} else {
			parent.toggle();
		}
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		double xDist = mc.player.posX - mc.player.prevPosX;
		double zDist = mc.player.posZ - mc.player.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		if (mc.player.moveStrafing <= 0.0F && mc.player.moveForward <= 0.0F) {
			stage = 1;
		}
		if (MathUtils.roundToPlace(mc.player.posY - (int) mc.player.posY, 3) == MathUtils.roundToPlace(0.943D, 3)) {
			mc.player.motionY -= 0.03F;
			event.y -= 0.03F;
		}
		if (stage == 1 && (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F)) {
			stage = 2;
			moveSpeed = (parent.boost.getValue().floatValue() * ClientUtils.getBaseMoveSpeed() - 0.01D);
		} else if (stage == 2) {
			stage = 3;
			mc.player.motionY = 0.424D;
			event.y = 0.424D;
			moveSpeed *= 2.149802D;
		} else if (stage == 3) {
			stage = 4;
			double difference = 0.66D * (lastDist - ClientUtils.getBaseMoveSpeed());
			moveSpeed = (lastDist - difference);
		} else {
			if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, mc.player.motionY, 0.0D)).size() > 0) {
				stage = 1;
			}
			moveSpeed = (lastDist - lastDist / 159.0D);
		}
		moveSpeed = Math.max(moveSpeed, ClientUtils.getBaseMoveSpeed());
		ClientUtils.setMoveSpeed(event, moveSpeed);
	}
}
