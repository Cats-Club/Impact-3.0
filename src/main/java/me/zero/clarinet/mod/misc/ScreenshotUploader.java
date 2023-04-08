package me.zero.clarinet.mod.misc;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class ScreenshotUploader extends Mod {
	
	public ScreenshotUploader() {
		super("ScreenshotUploader", "When you take a screenshot, it is uploaded to Imgur", Keyboard.KEY_NONE, Category.MISC, false);
	}
}
