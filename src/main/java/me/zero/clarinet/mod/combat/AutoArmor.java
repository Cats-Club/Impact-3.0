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
import net.minecraft.entity.player.EntityPlayer;
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
			if (stack != null) {
				if (this.canEquip(stack)) {
					final double itemWeight = this.getItemWeight(stack);
					if (itemWeight >= maxProt) {
						slotID = i;
						maxProt = itemWeight;
					}
				} else if (autoReplace.getValue()) {
					if (getArmorType(stack) == "boots" && armorIsBetter(0, stack)) {
						this.mc.playerController.windowClick(0, 8, 0, ClickType.QUICK_MOVE, (EntityPlayer) this.mc.player);
					}
					if (getArmorType(stack) == "leggings" && armorIsBetter(1, stack)) {
						this.mc.playerController.windowClick(0, 7, 0, ClickType.QUICK_MOVE, (EntityPlayer) this.mc.player);
					}
					if (getArmorType(stack) == "chestplate" && armorIsBetter(2, stack)) {
						this.mc.playerController.windowClick(0, 6, 0, ClickType.QUICK_MOVE, (EntityPlayer) this.mc.player);
					}
					if (getArmorType(stack) == "helmet" && armorIsBetter(3, stack)) {
						this.mc.playerController.windowClick(0, 5, 0, ClickType.QUICK_MOVE, (EntityPlayer) this.mc.player);
					}
				}
			}
		}
		
		if (slotID != -1 && timer.delay(delay)) {
			this.mc.playerController.windowClick(0, slotID, 0, ClickType.QUICK_MOVE, (EntityPlayer) this.mc.player);
			timer.reset();
		}
	}
	
	private boolean canEquip(ItemStack stack) {
		return (this.mc.player.inventory.armorInventory.get(0) == null && getArmorType(stack) == "boots") || (this.mc.player.inventory.armorInventory.get(1) == null && getArmorType(stack) == "leggings") || (this.mc.player.inventory.armorInventory.get(2) == null && getArmorType(stack) == "chestplate") || (this.mc.player.inventory.armorInventory.get(3) == null && getArmorType(stack) == "helmet");
	}
	
	private String getArmorType(ItemStack stack1) {

		if (!(stack1.getItem() instanceof ItemArmor))
			return "";

		ItemArmor stack = (ItemArmor) stack1.getItem();

		if (stack.getEquipmentSlot().equals(EntityEquipmentSlot.FEET)) {
			return "boots";
		} else if (stack.getEquipmentSlot().equals(EntityEquipmentSlot.LEGS)) {
			return "leggings";
		} else if (stack.getEquipmentSlot().equals(EntityEquipmentSlot.CHEST)) {
			return "chestplate";
		} else if (stack.getEquipmentSlot().equals(EntityEquipmentSlot.HEAD)) {
			return "helmet";
		}
		
		return "";
	}
	
	private double getItemWeight(ItemStack stack) {
		
		int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack);
		int itemDura = ((ItemArmor) stack.getItem()).damageReduceAmount;
		
		return itemDura + (100 - itemDura * 4) * protectionLevel;
	}
	
	public boolean armorIsBetter(int slot, ItemStack armor) {
		if (this.mc.player.inventory.armorInventory.get(slot) != null && getItemWeight(this.mc.player.inventory.armorInventory.get(slot)) < getItemWeight(armor)) {
			return true;
		}
		
		return false;
	}
	
}