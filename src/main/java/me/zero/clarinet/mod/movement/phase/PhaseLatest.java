package me.zero.clarinet.mod.movement.phase;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.network.EventPacketReceive;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mixin.mixins.minecraft.network.play.server.ISPacketPlayerPosLook;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Phase;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.text.TextComponentString;

public class PhaseLatest extends ModMode<Phase> {
	
	public PhaseLatest(Phase parent) {
		super(parent, "Latest");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (ClientUtils.isInsideBlock()) {
			mc.player.motionY = 0.0;
			double pX = mc.player.posX;
			double pY = mc.player.posY + (0.0625 * (mc.player.movementInput.jump ? 1 : mc.player.movementInput.sneak ? -1 : 0));
			double pZ = mc.player.posZ;
			mc.player.connection.sendPacket(new CPacketPlayer.Position(pX, pY, pZ, false));
			if (mc.player.collidedHorizontally) {
				double mx = Math.cos(Math.toRadians(mc.player.rotationYaw - 90));
				double mz = Math.sin(Math.toRadians(mc.player.rotationYaw - 90));
				double speed = 0.05D;
				double x1 = -(speed * mx - 0.0D * mz);
				double z1 = -(speed * mz - 0.0D * mx);
				mc.player.motionX = x1;
				mc.player.motionZ = z1;
				mc.player.connection.sendPacket(new CPacketPlayer.Position(pX + x1, pY, pZ + z1, false));
			}
			ClientUtils.breakNCP();
		} else {
			if ((int) mc.player.posY - mc.player.posY == -0.8590999984741217) {
				try {
					mc.getConnection().getNetworkManager().closeChannel(new TextComponentString("Successfully moved into block - Relog"));
				} catch (Exception e) {
				}
			}
		}
	}
	
	@EventTarget
	public void onReceivePacket(EventPacketReceive event) {
		if (event.getPacket() instanceof SPacketPlayerPosLook) {
			ISPacketPlayerPosLook packet = (ISPacketPlayerPosLook) event.getPacket();
			packet.setYaw(mc.player.rotationYaw);
			packet.setPitch(mc.player.rotationPitch);
			event.setPacket((Packet<?>) packet);
		}
	}
}