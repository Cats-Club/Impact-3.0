package me.zero.clarinet.mod.misc;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemSaver extends Mod {
	
	public ItemSaver() {
		super("ItemSaver", "Moves items out of your hand that have 1 durability", Keyboard.KEY_NONE, Category.MISC);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!isInventoryGood()) {
			return;
		}
		EnumHand hands[] = EnumHand.values();
		for (int i = 0; i < hands.length; i++) {
			EnumHand hand = hands[i];
			ItemStack stack = mc.player.getHeldItem(hand);
			if (stack != null && stack.getItem() != null) {
				Item item = stack.getItem();
				if (stack.isItemStackDamageable() && stack.getItemDamage() == item.getMaxDamage()) {
					switch (hand) {
						case MAIN_HAND: {
							mc.playerController.windowClick(mc.player.inventoryContainer.windowId, mc.player.inventory.currentItem + 36, 0, ClickType.QUICK_MOVE, mc.player);
							break;
						}
						case OFF_HAND: {
							mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 1, ClickType.QUICK_MOVE, mc.player);
							break;
						}
					}
				}
			}
		}
	}
	
	private boolean isInventoryGood() {
		for (int i = 0; i < 36; i++) {
			if (!mc.player.inventoryContainer.getSlot(i).getHasStack()) {
				return true;
			}
		}
		return false;
	}
}
