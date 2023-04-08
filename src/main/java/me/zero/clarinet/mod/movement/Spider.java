package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.ClientUtils;
import me.zero.values.types.MultiValue;
import net.minecraft.network.play.client.CPacketPlayer;

public class Spider extends Mod {
	
	private MultiValue<String> mode = new MultiValue<String>(this, "Mode", "mode", "Normal", new String[] { "Normal", "NCP" });
	
	public Spider() {
		super("Spider", "Climb up walls", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.suffix = mode.getValue();
		
		if (mode.getValue().equalsIgnoreCase("Normal")) {
			if (mc.player.collidedHorizontally) {
				mc.player.motionY = 0.2;
			}
		}
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if (mc.player == null || mc.world == null) {
			return;
		}
		if (mode.getValue().equalsIgnoreCase("NCP")) {
			if (mc.player.collidedHorizontally) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625, mc.player.posZ, false));
				ClientUtils.breakNCP();
			}
		}
	}
}
