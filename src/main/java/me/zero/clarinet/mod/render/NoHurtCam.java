package me.zero.clarinet.mod.render;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.render.EventHurtcam;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class NoHurtCam extends Mod {
	
	public NoHurtCam() {
		super("NoHurtCam", "Removes the Hurt Cam effect", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@EventTarget
	public void onHurtcam(EventHurtcam event) {
		event.setCancelled(true);
	}
}
