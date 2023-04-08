package me.zero.clarinet.mixin.mixins.minecraft.client.gui;

import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Doogie13
 * @since 05/04/2023
 */
@Mixin(GuiChat.class)
public interface IGuiChat {

    @Accessor("defaultInputFieldText")
    String getDefaultInputFieldText();

}
