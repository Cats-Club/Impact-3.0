package me.zero.clarinet.mod.movement.phase;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Phase;

public class PhaseNoClip extends ModMode<Phase> {
	
	public PhaseNoClip(Phase parent) {
		super(parent, "NoClip");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.player.noClip = true;
		mc.player.fallDistance = 0.0f;
		mc.player.onGround = false;
		mc.player.capabilities.isFlying = false;
		mc.player.motionX = 0.0;
		mc.player.motionY = 0.0;
		mc.player.motionZ = 0.0;
		float speed = 0.4F;
		mc.player.jumpMovementFactor = speed;
		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.player.motionY += speed;
		}
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.player.motionY -= speed;
		}
	}
}
