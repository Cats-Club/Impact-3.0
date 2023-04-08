package me.zero.clarinet.mixin.mixins.minecraft.client.gui;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Doogie13
 * @since 05/04/2023
 */
@Mixin(GuiMultiplayer.class)
public interface IGuiMultiplayer {

    @Accessor("serverListSelector")
    ServerSelectionList getServerListSelector();

}
