package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.event.player.EventStep;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.network.play.client.CPacketPlayer;

public class Step extends Mod {
	
	private float stepHeight = 1.0F;
	
	public Step() {
		super("Step", "Step up blocks", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.player.stepHeight = stepHeight;
	}
	
	@EventTarget
	public void onStep(EventStep event) {
		if (event.entity != mc.player) {
			return;
		}
		if (event.type == EventType.PRE) {
			if (this.stepHeight > 1.0D) {
				mc.player.stepHeight = (this.stepHeight);
				event.stepHeight = this.stepHeight;
			} else {
				mc.player.stepHeight = 0.6F;
				if ((mc.player.movementInput != null) & (!mc.player.movementInput.jump) && (mc.player.collidedVertically)) {
					event.stepHeight = 1.0D;
				}
			}
		} else {
			mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42D, mc.player.posZ, mc.player.onGround));
			mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.753D, mc.player.posZ, mc.player.onGround));
		}
	}
	
	@Override
	public void onDisable() {
		mc.player.stepHeight = 0.5F;
	}
}
