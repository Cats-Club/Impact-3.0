package me.zero.clarinet.mod.player;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.BooleanValue;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketEntityAction;

public class Sneak extends Mod {
	
	private BooleanValue packet = new BooleanValue(this, "Packet", "packet", true);
	
	public Sneak() {
		super("Sneak", "Automatically Sneak", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onMotionUpdate(EventMotionUpdate event) {
		if (isPacket()) {
			if (event.type == EventType.PRE) {
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
			} else if (event.type == EventType.POST) {
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
			}
		} else {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
		}
	}
	
	public void onDisable() {
		if (isPacket()) {
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
	}
	
	private boolean isPacket() {
		return this.packet.getValue();
	}
}