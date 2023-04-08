package me.zero.clarinet.mod.movement.flight;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.network.EventPacketReceive;
import me.zero.clarinet.mixin.mixins.minecraft.network.play.server.ISPacketPlayerPosLook;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Flight;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class FlightNCP extends ModMode<Flight> {
	
	public FlightNCP(Flight parent) {
		super(parent, "NCP");
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if (mc.gameSettings.keyBindForward.isKeyDown()) {
			double mx = Math.cos(Math.toRadians(mc.player.rotationYaw - 90));
			double mz = Math.sin(Math.toRadians(mc.player.rotationYaw - 90));
			double speed = 0.026D;
			double x1 = -(speed * mx - 0.0D * mz);
			double z1 = -(speed * mz - 0.0D * mx);
			mc.player.motionX = x1;
			mc.player.motionZ = z1;
		}
		mc.player.motionY = 0.0;
		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX * 11, mc.player.posY + (0.0625 * (mc.player.movementInput.jump ? 1 : mc.player.movementInput.sneak ? -1 : 0)), mc.player.posZ + mc.player.motionZ * 11, false));
		ClientUtils.breakNCP();
	}
	
	@EventTarget
	public void onReceivePacket(EventPacketReceive event) {
		if (event.getPacket() instanceof SPacketPlayerPosLook) {
			SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
			((ISPacketPlayerPosLook) packet).setYaw(mc.player.rotationYaw);
			((ISPacketPlayerPosLook) packet).setPitch(mc.player.rotationPitch);
			event.setPacket(packet);
		}
	}
}
