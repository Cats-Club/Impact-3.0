package me.zero.clarinet.mod.combat;

import java.util.Random;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.MathUtils;
import me.zero.clarinet.util.TimerUtil;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;

public class AutoClicker extends Mod {
	
	private TimerUtil timer = new TimerUtil();
	
	private int minCPS = 6;
	
	private int maxCPS = 12;
	
	private int curCPS = 9;

	private BooleanValue jitter = new BooleanValue(this, "Jitter", "jitter", true);
	
	public NumberValue range = new NumberValue(this, "Range", "range", 4.25D, 4D, 6D, 0.25D);
	public NumberValue fov = new NumberValue(this, "FOV", "fov", 30D, 0D, 90D, 1D);
	
	public AutoClicker() {
		super("AutoClicker", "Automatically clicks for you", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	@EventTarget
	public void onMotionUpdate(EventMotionUpdate event) {
		if (mc.player != null && mc.world != null && mc.currentScreen == null) {
			boolean held = false;
			int bind = mc.gameSettings.keyBindAttack.getKeyCode();
			if (bind <= -100) {
				held = Mouse.isButtonDown(bind + 100);
			} else {
				held = Keyboard.isKeyDown(bind);
			}
			if (held) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
				if (jitter.getValue()) {
					double jitter = 1;
					mc.player.rotationYaw += (float) ((Math.random() - 0.5) * jitter);
					mc.player.rotationPitch += (float) ((Math.random() - 0.5) * jitter);
				}
				if (timer.speed(curCPS)) {
					int lastCPS = curCPS;
					curCPS = MathUtils.randInt(minCPS, maxCPS);
					if (lastCPS == curCPS) {
						if (curCPS == minCPS) {
							curCPS++;
						} else if (curCPS == maxCPS) {
							curCPS--;
						} else {
							curCPS += (new Random().nextBoolean() ? 1 : -1);
						}
					}
					((IMinecraft) mc).callClickMouse();
					timer.reset();
				}
			}
		}
	}
}
