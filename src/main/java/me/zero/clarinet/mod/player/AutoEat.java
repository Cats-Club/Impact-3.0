package me.zero.clarinet.mod.player;

import java.util.Random;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.TimerUtil;
import me.zero.values.types.NumberValue;
import me.zero.values.types.MultiValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class AutoEat extends Mod {
	
	private TimerUtil timer = new TimerUtil();
	private int prevItem;
	private int stage = 0;
	
	private MultiValue<String> priority = new MultiValue<String>(this, "Priority", "priority", "Saturation", new String[] { "Saturation", "Slot" });
	
	private NumberValue hunger = new NumberValue(this, "Hunger", "hunger", 6D, 1D, 9.5D, 0.5D);
	
	private NumberValue speed = new NumberValue(this, "Speed", "speed", 60D, 30D, 100D, 5D);
	
	private NumberValue random = new NumberValue(this, "Random", "random", 20D, 5D, 40D, 5D);
	
	public AutoEat() {
		super("AutoEat", "Automatically eat food when you reach a certain hunger amount", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@Override
	public void onEnable() {
		stage = 0;
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		int food = getFoodFromHotbar();
		if (food != -1) {
			switch (stage) {
				case 0:
					if (mc.player.getFoodStats().getFoodLevel() <= (hunger.getValue().floatValue() * 2)) {
						prevItem = mc.player.inventory.currentItem;
						mc.player.inventory.currentItem = food;
						timer.reset();
						stage++;
					}
					break;
				case 1:
					if (timer.delay(new Random().nextInt(random.getValue().intValue()) + speed.getValue().intValue())) {
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
						if (mc.player.getFoodStats().getFoodLevel() > (hunger.getValue().floatValue() * 2)) {
							stage++;
							timer.reset();
						}
					}
					break;
				case 2:
					if (timer.delay(new Random().nextInt(random.getValue().intValue()) + speed.getValue().intValue())) {
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
						mc.player.inventory.currentItem = prevItem;
						timer.reset();
						stage = 0;
					}
					break;
			}
		}
	}
	
	private int getFoodFromHotbar() {
		Float saturation = null;
		int slot = -1;
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.player.inventory.mainInventory.get(i);
			if (stack != null && stack.getItem() != null) {
				Item hand = stack.getItem();
				if (hand instanceof ItemFood) {
					ItemFood food = (ItemFood) hand;
					if (priority.getValue().equalsIgnoreCase("Slot")) {
						return i;
					}
					if (saturation == null || food.getSaturationModifier(stack) > saturation) {
						saturation = food.getSaturationModifier(stack);
						slot = i;
					}
				}
			}
		}
		return slot;
	}
}
