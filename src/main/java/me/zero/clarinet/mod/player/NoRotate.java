package me.zero.clarinet.mod.player;

import me.zero.clarinet.mixin.mixins.minecraft.network.play.server.ISPacketPlayerPosLook;
import net.minecraft.network.Packet;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.network.EventPacketReceive;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class NoRotate extends Mod {
	
	public NoRotate() {
		super("NoRotate", "Discard Server Rotation Packets", Keyboard.KEY_NONE, Category.PLAYER);
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
