package me.zero.clarinet.mod.misc;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.TimerUtil;
import me.zero.values.types.NumberValue;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinBlinker extends Mod {
	
	private TimerUtil loop = new TimerUtil();
	
	private NumberValue delay = new NumberValue(this, "Blinker Delay", "delay", 200D, 0D, 1000D, 50D);
	
	public SkinBlinker() {
		super("SkinBlinker", "Your jacket layer", Keyboard.KEY_NONE, Category.MISC);
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if (loop.delay(delay)) {
			for (EnumPlayerModelParts part : mc.gameSettings.getModelParts()) {
				mc.gameSettings.setModelPartEnabled(part, new Random().nextBoolean());
			}
			loop.reset();
		}
	}
}
