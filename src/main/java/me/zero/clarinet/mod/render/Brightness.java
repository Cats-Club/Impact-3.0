package me.zero.clarinet.mod.render;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.render.EventRenderBrightness;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.BooleanValue;
import net.minecraft.util.math.MathHelper;

public class Brightness extends Mod {
	
	private BooleanValue fade = new BooleanValue(this, "Fade", "fade", true);
	
	public Brightness() {
		super("Brightness", "Brighten up your world!", Keyboard.KEY_B, Category.RENDER);
		EventManager.register(new FullbrightHandler());
	}
	
	public class FullbrightHandler {
		
		private float brightness;
		
		public FullbrightHandler() {
			this.brightness = Brightness.this.mc.gameSettings.gammaSetting;
		}
		
		@EventTarget
		public void onBrightnessRender(EventRenderBrightness event) {
			event.brightness = this.brightness;
		}
		
		@EventTarget
		public void onUpdate(EventTick event) {
			float max = 10F;
			float speed = 1.4F;
			if (fade.getValue()) {
				if (Brightness.this.isToggled()) {
					this.brightness *= speed;
				} else {
					this.brightness /= speed;
				}
			} else {
				if (Brightness.this.isToggled()) {
					this.brightness = max;
				} else {
					this.brightness = Brightness.this.mc.gameSettings.gammaSetting;
				}
			}
			this.brightness = MathHelper.clamp(this.brightness, Brightness.this.mc.gameSettings.gammaSetting, max);
		}
	}
}
