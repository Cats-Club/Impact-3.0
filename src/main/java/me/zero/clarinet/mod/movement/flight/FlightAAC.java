package me.zero.clarinet.mod.movement.flight;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.network.EventPacketSend;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.mixin.mixins.minecraft.network.play.client.ICPacketPlayer;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.Flight;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.network.play.client.CPacketPlayer;

public class FlightAAC extends ModMode<Flight> {
	
	public FlightAAC(Flight parent) {
		super(parent, "AAC");
	}
	
	@EventTarget
	public void onMotion(EventMove event) {
		if (!mc.player.onGround && !mc.player.isInWater()) {
			ClientUtils.setMoveSpeed(event, 0.4621096658284778F);
		}
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if (mc.player.hurtTime == 10.0f) {
			mc.player.motionY = 0.83;
		}
	}
	
	@EventTarget
	public void onPacketSend(EventPacketSend event) {
		if (mc.player.fallDistance > 3.8f) {
			if (event.getPacket() instanceof CPacketPlayer) {
				((ICPacketPlayer) event.getPacket()).setOnGround(true);
				mc.player.fallDistance = 0.0f;
			}
		}
	}
}
