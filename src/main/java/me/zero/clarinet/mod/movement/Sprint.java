package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventSprint;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.BooleanValue;

public class Sprint extends Mod {
	
	public BooleanValue multiDirection = new BooleanValue(this, "MultiDirectional", "multiDir", true);
	
	public BooleanValue keep = new BooleanValue(this, "KeepSprint", "keep", true);
	
	public Sprint() {
		super("Sprint", "Better sprinting", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (canSprint()) {
			mc.player.setSprinting(true);
		}
	}
	
	@EventTarget
	public void onSprint(EventSprint event) {
		if (canSprint()) {
			event.sprinting = true;
		}
	}
	
	private boolean canSprint() {
		if (mc.player.getFoodStats().getFoodLevel() <= 4) {
			return false;
		}
		if (!mc.player.collidedHorizontally && !mc.player.isSneaking()/* && this.mc.player.getActiveItemStack() == null*/) {
			if (multiDirection.getValue()) {
				if (this.mc.player.movementInput.moveForward == 0.0f && this.mc.player.movementInput.moveStrafe == 0.0f) {
					return false;
				}
				return true;
			}
			if (mc.player.moveForward > 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onDisable() {
		mc.player.setSprinting(false);
	}
}
