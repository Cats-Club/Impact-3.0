package me.zero.clarinet.ui.screen.override.screen;

import java.io.IOException;

import me.zero.clarinet.mixin.mixins.minecraft.client.gui.inventory.IGuiChest;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.player.AutoSteal;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class OverrideGuiChest extends GuiChest {
	
	private static boolean hide = false;
	
	public OverrideGuiChest(IInventory upperInv, IInventory lowerInv) {
		super(upperInv, lowerInv);
	}
	
	protected void keyTyped(char c, int i) {
		if (i == Keyboard.KEY_RSHIFT) {
			hide = !hide;
			initGui();
		}
		try {
			super.keyTyped(c, i);
		} catch (IOException e) {
		}
	}
	
	public void initGui() {
		this.buttonList.clear();
		super.initGui();
		if (!hide) {
			GuiButton stealButton = new GuiButton(0, this.guiLeft + this.xSize - 57, this.guiTop + 4, 50, 12, "Steal");
			this.buttonList.add(stealButton);
			if (Impact.getInstance().getModManager().get(AutoSteal.class).isToggled()) {
				actionPerformed(stealButton);
			}
		}
	}
	
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			new Thread() {
				@Override
				public void run() {
					try {
						for (int i = 0; i < ((IGuiChest) OverrideGuiChest.this).getInventoryRows() * 9; i++) {
							Slot slot = (Slot) OverrideGuiChest.this.inventorySlots.inventorySlots.get(i);
							if (slot.getStack() != null) {
								if (Impact.getInstance().getModManager().get(AutoSteal.class).getDelay() > 0) {
									Thread.sleep(Impact.getInstance().getModManager().get(AutoSteal.class).getDelay());
								}
								OverrideGuiChest.this.handleMouseClick(slot, slot.slotNumber, 0, ClickType.QUICK_MOVE);
								OverrideGuiChest.this.handleMouseClick(slot, slot.slotNumber, 0, ClickType.PICKUP_ALL);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
}
