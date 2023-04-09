package me.zero.clarinet.mod.combat;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.TimerUtil;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Mod {

	private NumberValue delay = new NumberValue(this, "Delay", "delay", 250D, 0D, 1000D, 10D);
	
	private BooleanValue autoReplace = new BooleanValue(this, "Auto Replace", "replace", true);
	
	TimerUtil timer;
	
	public AutoArmor() {
		super("AutoArmor", "Automatically equips armor", 0, Category.COMBAT);
		timer = new TimerUtil();
	}
	
	@EventTarget
	private void onUpdate(EventUpdate event) {
		
		int slotID = -1;
		double maxProt = -1.0;
		
		for (int i = 9; i < 45; ++i) {

			final ItemStack stack = this.mc.player.inventoryContainer.getSlot(i).getStack();

			if (!(stack.getItem() instanceof ItemArmor))
				continue;

			ItemArmor item = (ItemArmor) stack.getItem();

			if (this.canEquip(stack)) {
				final double itemWeight = this.getItemWeight(stack);
				if (itemWeight >= maxProt) {
					slotID = i;
					maxProt = itemWeight;
				}
			} else if (autoReplace.getValue()) {
				if (item.getEquipmentSlot().equals(EntityEquipmentSlot.FEET) && armorIsBetter(0, stack)) {
					this.mc.playerController.windowClick(0, 8, 0, ClickType.QUICK_MOVE, this.mc.player);
				}
				if (item.getEquipmentSlot().equals(EntityEquipmentSlot.LEGS) && armorIsBetter(1, stack)) {
					this.mc.playerController.windowClick(0, 7, 0, ClickType.QUICK_MOVE, this.mc.player);
				}
				if (item.getEquipmentSlot().equals(EntityEquipmentSlot.CHEST) && armorIsBetter(2, stack)) {
					this.mc.playerController.windowClick(0, 6, 0, ClickType.QUICK_MOVE, this.mc.player);
				}
				if (item.getEquipmentSlot().equals(EntityEquipmentSlot.HEAD) && armorIsBetter(3, stack)) {
					this.mc.playerController.windowClick(0, 5, 0, ClickType.QUICK_MOVE, this.mc.player);
				}
			}

		}
		
		if (slotID != -1 && timer.delay(delay)) {
			this.mc.playerController.windowClick(0, slotID, 0, ClickType.QUICK_MOVE, this.mc.player);
			timer.reset();
		}
	}
	
	private boolean canEquip(ItemStack stack) {
		if (!(stack.getItem() instanceof ItemArmor))
			return false;
		ItemArmor item = (ItemArmor) stack.getItem();
		return !this.mc.player.inventory.armorInventory.get(0).getItem().equals(Items.AIR) && item.getEquipmentSlot().equals(EntityEquipmentSlot.FEET) || !this.mc.player.inventory.armorInventory.get(1).getItem().equals(Items.AIR) && item.getEquipmentSlot().equals(EntityEquipmentSlot.LEGS) || !this.mc.player.inventory.armorInventory.get(2).getItem().equals(Items.AIR) && item.getEquipmentSlot().equals(EntityEquipmentSlot.CHEST) || !this.mc.player.inventory.armorInventory.get(3).getItem().equals(Items.AIR) && item.getEquipmentSlot().equals(EntityEquipmentSlot.CHEST);
	}
	
	private double getItemWeight(ItemStack stack) {
		
		int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack);
		int itemDura = ((ItemArmor) stack.getItem()).damageReduceAmount;
		
		return itemDura + (100 - itemDura * 4) * protectionLevel;
	}
	
	public boolean armorIsBetter(int slot, ItemStack armor) {
		return this.mc.player.inventory.armorInventory.get(slot).getItem().equals(Items.AIR) || getItemWeight(this.mc.player.inventory.armorInventory.get(slot)) < getItemWeight(armor);
	}
	
}