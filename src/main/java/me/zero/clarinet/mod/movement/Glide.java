package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.block.material.Material;

public class Glide extends Mod {

	public Glide() {
		super("Glide", "Fall down slower", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
        if (mc.player.motionY < 0 && mc.player.isAirBorne && !mc.player.isInWater() && !mc.player.isOnLadder() && !mc.player.isInsideOfMaterial(Material.LAVA)) {
            mc.player.motionY = -0.125f;
        }
	}
}
