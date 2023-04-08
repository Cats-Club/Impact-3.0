package me.zero.clarinet.mod.misc;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventRightClick;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.misc.MiscUtils;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public class Unpack extends Mod {
	
	public Unpack() {
		super("Unpack", "When you right click logs in your main hand, they turn into planks!", Keyboard.KEY_NONE, Category.MISC);
	}
	
	@EventTarget
	public void onClick(EventRightClick event) {
		if (canUnpack()) {
			if (isUnpackable(mc.player.getHeldItem(EnumHand.MAIN_HAND))) {
				unpack(mc.player.getHeldItem(EnumHand.MAIN_HAND));
			}
		}
	}
	
	public void unpack(ItemStack stack) {
		int id = mc.player.inventoryContainer.windowId;
		mc.playerController.windowClick(id, mc.player.inventory.currentItem + 36, 0, ClickType.PICKUP, mc.player);
		mc.playerController.windowClick(id, 1, 0, ClickType.PICKUP, mc.player);
		mc.playerController.windowClick(id, 0, 0, ClickType.QUICK_MOVE, mc.player);
	}
	
	private boolean isUnpackable(ItemStack stack) {
		if (stack == null || stack.getItem() == null) {
			return false;
		}
		Integer id = Item.getIdFromItem(stack.getItem());
		Integer[] ids = { 17 };
		return MiscUtils.arrayContains(ids, id);
	}
	
	private boolean canUnpack() {
		return mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY || mc.objectMouseOver.typeOfHit == RayTraceResult.Type.MISS;
	}
}
