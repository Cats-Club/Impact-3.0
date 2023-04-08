package me.zero.clarinet.mod.combat;

import java.util.Random;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.TimerUtil;
import me.zero.values.types.NumberValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;

public class Soup extends Mod {
	
private NumberValue speed = new NumberValue(this, "Speed", "speed", 60D, 30D, 100D, 5D);
	private NumberValue random = new NumberValue(this, "Random", "random", 20D, 5D, 40D, 5D);
	
	private TimerUtil timer = new TimerUtil();
	private int prevItem;
	private int stage = 0;
	
	public Soup() {
		super("Soup", "Swaps to a soup in your hotbar and drinks it", Keyboard.KEY_NONE, Category.COMBAT, false);
	}
	
	@Override
	public void onEnable() {
		stage = 0;
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		int soup = getSoupFromHotbar();
		if (soup != -1) {
			switch (stage) {
				case 0:
					prevItem = mc.player.inventory.currentItem;
					mc.player.inventory.currentItem = soup;
					timer.reset();
					stage++;
					break;
				case 1:
					if (timer.delay(new Random().nextInt(random.getValue().intValue()) + speed.getValue().intValue())) {
						((IMinecraft) mc).callRightClickMouse();
						timer.reset();
						stage++;
					}
					break;
				case 2:
					if (timer.delay(new Random().nextInt(random.getValue().intValue()) + speed.getValue().intValue())) {
						mc.player.inventory.currentItem = prevItem;
						this.toggle();
						timer.reset();
						stage = 0;
					}
					break;
			}
		} else {
			this.toggle();
		}
	}
	
	private int getSoupFromHotbar() {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.player.inventory.mainInventory.get(i);
			if (stack != null && stack.getItem() != null) {
				Item hand = stack.getItem();
				if (hand instanceof ItemSoup) {
					return i;
				}
			}
		}
		return -1;
	}
}
