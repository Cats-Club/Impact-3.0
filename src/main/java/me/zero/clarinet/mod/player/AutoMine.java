package me.zero.clarinet.mod.player;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.client.settings.KeyBinding;

public class AutoMine extends Mod {
	
	public AutoMine() {
		super("AutoMine", "Automatically mines for you!", 0, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventMove event) {
		if (this.isToggled()) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
		}
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
	}
	
}
