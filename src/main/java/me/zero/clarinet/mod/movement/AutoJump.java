package me.zero.clarinet.mod.movement;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class AutoJump extends Mod {
	
	public AutoJump() {
		super("AutoJump", "Automatically Jump", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(),true);
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(),false);
	}
}
