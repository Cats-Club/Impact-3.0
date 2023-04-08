package me.zero.clarinet.mod.minigame;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;

public class SneakyAssassians extends Mod {
	
	private ArrayList<EntityVillager> assassians = new ArrayList<>();
	
	private NumberValue r = new NumberValue(this, "Red", "red", 0D, 0D, 255D, 1D);
	private NumberValue g = new NumberValue(this, "Green", "green", 199D, 0D, 255D, 1D);
	private NumberValue b = new NumberValue(this, "Blue", "blue", 3D, 0D, 255D, 1D);
	
	public SneakyAssassians() {
		super("SneakyAssassians", "Reveals the assassians", Keyboard.KEY_NONE, Category.MINIGAME);
	}
	
	@Override
	public void onEnable() {
		assassians.clear();
	}
	
	@EventTarget
	public void onRender(EventRender3D event) {
		assassians.clear();
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityVillager) {
				EntityVillager villager = (EntityVillager) e;
				if ((villager.rotationPitch != 0.0F) || (villager.isSprinting()) || (villager.moveStrafing != 0.0F)) {
					assassians.add(villager);
				}
			}
		}
		for (EntityVillager villager : assassians) {
			RenderUtils.drawEntityESP(villager, r.getValue().floatValue() / 255F, g.getValue().floatValue() / 255F, b.getValue().floatValue() / 255F, event.partialTicks);
		}
	}
}
