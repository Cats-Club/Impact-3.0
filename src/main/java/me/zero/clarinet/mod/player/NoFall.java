package me.zero.clarinet.mod.player;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.manager.manager.ModManager;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.exploit.AntiHunger;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Mod {
	
	public NoFall() {
		super("NoFall", "Don't take fall damage", Keyboard.KEY_O, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.player.fallDistance > 2F) {
			mc.player.connection.sendPacket(new CPacketPlayer(true));
		}
	}
	
	@Override
	public Mod[] getConflictingMods() {
		ModManager m = Impact.getInstance().getModManager();
		return new Mod[] { m.get(AntiHunger.class) };
	}
}
