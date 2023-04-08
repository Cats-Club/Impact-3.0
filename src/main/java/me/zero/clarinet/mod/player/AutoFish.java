package me.zero.clarinet.mod.player;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.entity.projectile.EntityFishHook;

public class AutoFish extends Mod {
	
	private boolean catching = false;
	
	public AutoFish() {
		super("AutoFish", "Automatically Fish", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.player.fishEntity != null && isHooked(mc.player.fishEntity) && !catching) {
			catching = true;
			((IMinecraft) mc).callRightClickMouse();
			new Thread("AutoFish Thread") {
				@Override
				public void run() {
					try {
						Thread.sleep(750);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					((IMinecraft) mc).callRightClickMouse();
					catching = false;
				}
			}.start();
		}
	}
	
	private boolean isHooked(EntityFishHook hook) {
		return hook.motionX == 0.0D && hook.motionZ == 0.0D && hook.motionY != 0.0D;
	}
}
