package me.zero.clarinet.mod.movement;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class AutoWalk extends Mod {
	
	public AutoWalk() {
		super("AutoWalk", "Automatically Walk", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!mc.gameSettings.keyBindForward.isKeyDown()) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(),true);
		}
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(),false);
	}
}
