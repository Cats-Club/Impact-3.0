package me.zero.clarinet.mod.render;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class CameraClip extends Mod {
	
	public CameraClip() {
		super("CameraClip", "Blocks don't effect the 3rd person view", Keyboard.KEY_NONE, Category.RENDER);
	}
}
