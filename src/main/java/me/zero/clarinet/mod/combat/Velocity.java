package me.zero.clarinet.mod.combat;

import me.zero.clarinet.mixin.mixins.minecraft.network.play.server.ISPacketEntityVelocity;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.network.EventPacketReceive;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.NumberValue;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public class Velocity extends Mod {
	
	private NumberValue reduce = new NumberValue(this, "Amount", "amount", 100D, 0D, 100D, 1D);
	
	public Velocity() {
		super("Velocity", "Reduces the amount of knockback taken", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.suffix = (int) reduce.getValue().doubleValue() + "%";
	}
	
	@EventTarget
	public void onPacketReceive(EventPacketReceive event) {
		double multiplier = reduce.getValue().doubleValue() / 100.0;
		if (event.getPacket() instanceof SPacketEntityVelocity) {
			SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();
			ISPacketEntityVelocity iPacket = (ISPacketEntityVelocity) event.getPacket();
			if (packet.getEntityID() == mc.player.getEntityId()) {
				iPacket.setMotionX((int) (packet.getMotionX() * multiplier));
				iPacket.setMotionY((int) (packet.getMotionY() * multiplier));
				iPacket.setMotionZ((int) (packet.getMotionZ() * multiplier));
			}
		}
	}
}
