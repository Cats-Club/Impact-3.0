package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.AchievementList;

public class ContainerBrewingStand extends Container
{
    private final net.minecraft.inventory.IInventory tileBrewingStand;

    /** Instance of Slot. */
    private final net.minecraft.inventory.Slot theSlot;

    /**
     * Used to cache the brewing time to send changes to ICrafting listeners.
     */
    private int prevBrewTime;

    /**
     * Used to cache the fuel remaining in the brewing stand to send changes to ICrafting listeners.
     */
    private int prevFuel;

    public ContainerBrewingStand(InventoryPlayer playerInventory, net.minecraft.inventory.IInventory tileBrewingStandIn)
    {
        this.tileBrewingStand = tileBrewingStandIn;
        this.addSlotToContainer(new Potion(playerInventory.player, tileBrewingStandIn, 0, 56, 51));
        this.addSlotToContainer(new Potion(playerInventory.player, tileBrewingStandIn, 1, 79, 58));
        this.addSlotToContainer(new Potion(playerInventory.player, tileBrewingStandIn, 2, 102, 51));
        this.theSlot = this.addSlotToContainer(new Ingredient(tileBrewingStandIn, 3, 79, 17));
        this.addSlotToContainer(new Fuel(tileBrewingStandIn, 4, 17, 17));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new net.minecraft.inventory.Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new net.minecraft.inventory.Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public void addListener(net.minecraft.inventory.IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBrewingStand);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            net.minecraft.inventory.IContainerListener icontainerlistener = (IContainerListener)this.listeners.get(i);

            if (this.prevBrewTime != this.tileBrewingStand.getField(0))
            {
                icontainerlistener.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
            }

            if (this.prevFuel != this.tileBrewingStand.getField(1))
            {
                icontainerlistener.sendProgressBarUpdate(this, 1, this.tileBrewingStand.getField(1));
            }
        }

        this.prevBrewTime = this.tileBrewingStand.getField(0);
        this.prevFuel = this.tileBrewingStand.getField(1);
    }

    public void updateProgressBar(int id, int data)
    {
        this.tileBrewingStand.setField(id, data);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileBrewingStand.isUseableByPlayer(playerIn);
    }

    @Nullable

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        net.minecraft.inventory.Slot slot = (net.minecraft.inventory.Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if ((index < 0 || index > 2) && index != 3 && index != 4)
            {
                if (!this.theSlot.getHasStack() && this.theSlot.isItemValid(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false))
                    {
                        return null;
                    }
                }
                else if (Potion.canHoldPotion(itemstack))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 3, false))
                    {
                        return null;
                    }
                }
                else if (Fuel.isValidBrewingFuel(itemstack))
                {
                    if (!this.mergeItemStack(itemstack1, 4, 5, false))
                    {
                        return null;
                    }
                }
                else if (index >= 5 && index < 32)
                {
                    if (!this.mergeItemStack(itemstack1, 32, 41, false))
                    {
                        return null;
                    }
                }
                else if (index >= 32 && index < 41)
                {
                    if (!this.mergeItemStack(itemstack1, 5, 32, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(itemstack1, 5, 41, false))
                {
                    return null;
                }
            }
            else
            {
                if (!this.mergeItemStack(itemstack1, 5, 41, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }

    static class Fuel extends net.minecraft.inventory.Slot
    {
        public Fuel(net.minecraft.inventory.IInventory iInventoryIn, int index, int xPosition, int yPosition)
        {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        public boolean isItemValid(@Nullable ItemStack stack)
        {
            return isValidBrewingFuel(stack);
        }

        public static boolean isValidBrewingFuel(@Nullable ItemStack itemStackIn)
        {
            return itemStackIn != null && itemStackIn.getItem() == Items.BLAZE_POWDER;
        }

        public int getSlotStackLimit()
        {
            return 64;
        }
    }

    static class Ingredient extends net.minecraft.inventory.Slot
    {
        public Ingredient(net.minecraft.inventory.IInventory iInventoryIn, int index, int xPosition, int yPosition)
        {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        public boolean isItemValid(@Nullable ItemStack stack)
        {
            return stack != null && PotionHelper.isReagent(stack);
        }

        public int getSlotStackLimit()
        {
            return 64;
        }
    }

    static class Potion extends Slot
    {
        private final EntityPlayer player;

        public Potion(EntityPlayer playerIn, IInventory inventoryIn, int index, int xPosition, int yPosition)
        {
            super(inventoryIn, index, xPosition, yPosition);
            this.player = playerIn;
        }

        public boolean isItemValid(@Nullable ItemStack stack)
        {
            return canHoldPotion(stack);
        }

        public int getSlotStackLimit()
        {
            return 1;
        }

        public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
        {
            if (PotionUtils.getPotionFromItem(stack) != PotionTypes.WATER)
            {
                this.player.addStat(AchievementList.POTION);
            }

            super.onPickupFromSlot(playerIn, stack);
        }

        public static boolean canHoldPotion(@Nullable ItemStack stack)
        {
            if (stack == null)
            {
                return false;
            }
            else
            {
                Item item = stack.getItem();
                return item == Items.POTIONITEM || item == Items.GLASS_BOTTLE || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION;
            }
        }
    }
}
