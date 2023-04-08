package me.zero.clarinet.mixin.mixins.minecraft.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Doogie13
 * @since 05/04/2023
 */
@Mixin(GuiChest.class)
public interface IGuiChest {

    @Accessor("lowerChestInventory")
    IInventory getLowerChestInventory();

    @Accessor("upperChestInventory")
    IInventory getUpperChestInventory();

    @Accessor("inventoryRows")
    int getInventoryRows();

}
