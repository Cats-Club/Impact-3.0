package me.zero.clarinet.mod.movement.speed;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Speed;
import me.zero.clarinet.util.ClientUtils;

public class SpeedStrafe extends ModMode<Speed> {

	private double moveSpeed;
	private double lastDist;
	private int stage;
	private boolean jump = false;

	public SpeedStrafe(Speed parent) {
		super(parent, "Strafe");
	}
	
	@Override
	public void onEnable() {
		this.stage = 1;
		this.moveSpeed = ClientUtils.getBaseMoveSpeedNoBoost();
	}

	@Override
	public void onDisable() {
		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f);
	}

	@EventTarget
	public void onTick(EventTick event) {
		double xDist = mc.player.posX - mc.player.prevPosX;
		double zDist = mc.player.posZ - mc.player.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0888f);
		if (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) {
			switch (stage) {
				case 1: {
					stage = 2;
					moveSpeed = (1.35D * ClientUtils.getBaseMoveSpeed() - 0.01D);
					break;
				}
				case 2: {
					if (mc.player.onGround) {
						stage = 3;
						mc.player.motionY = 0.4D;
						event.y = 0.4D;
						moveSpeed *= jump ? 1.68 : 1.40;
						jump = !jump;
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
