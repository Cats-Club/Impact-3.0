package me.zero.clarinet.mod.movement.phase;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Phase;
import me.zero.clarinet.util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;

public class PhaseSkipClip extends ModMode<Phase> {
	
	public PhaseSkipClip(Phase parent) {
		super(parent, "SkipClip");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.player.collidedHorizontally) {
			double x = mc.player.posX;
			double y = mc.player.posY;
			double z = mc.player.posZ;
			double yaw = Math.toRadians(mc.player.rotationYaw);
			double pitch = Math.toRadians(mc.player.rotationPitch);
			double divisor = 1.6;
			double dirX = -Math.sin(yaw) / divisor;
			double dirZ = Math.cos(yaw) / divisor;
			x += dirX;
			z += dirZ;
			Block block = BlockUtils.getBlock(x, y, z);
			if (block instanceof BlockPane) {
				mc.player.setPosition(mc.player.posX + dirX, mc.player.posY, mc.player.posZ + dirZ);
			}
		}
	}
}
