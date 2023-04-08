package me.zero.clarinet.mod.player;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.TimerUtil;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.util.EnumHand;

public class AntiAFK extends Mod {
	
	private TimerUtil swingTimer = new TimerUtil();
	
	private BooleanValue jump = new BooleanValue(this, "Jump", "jump", true);
	
	private BooleanValue spin = new BooleanValue(this, "Spin", "spin", true);
	
	private BooleanValue walk = new BooleanValue(this, "Walk", "walk", true);
	
	private BooleanValue swing = new BooleanValue(this, "Swing", "swing", true);
	
	private NumberValue speed = new NumberValue(this, "Spin Speed", "speed", 10D, 5D, 50D, 5D);
	
	private NumberValue swingSpeed = new NumberValue(this, "Swing Speed", "swingSpeed", 1D, 1D, 10D, 0.25D);
	
	private boolean unsetJump = false;
	
	private boolean unsetWalk = false;
	
	public AntiAFK() {
		super("AntiAFK", "Prevents you from being kicked by AFK plugins", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (jump.getValue()) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
			unsetJump = false;
		} else {
			if (!unsetJump) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
				unsetJump = true;
			}
		}
		if (walk.getValue()) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
			unsetWalk = false;
		} else {
			if (!unsetWalk) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
				unsetWalk = true;
			}
		}
		if (swing.getValue()) {
			if (swingTimer.speed(swingSpeed)) {
				mc.player.swingArm(EnumHand.MAIN_HAND);
				swingTimer.reset();
			}
		}
		if (spin.getValue()) {
			mc.player.rotationYaw += speed.getValue().floatValue();
		}
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
	}
}
