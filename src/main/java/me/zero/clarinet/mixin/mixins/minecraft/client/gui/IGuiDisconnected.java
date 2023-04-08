package me.zero.clarinet.mixin.mixins.minecraft.client.gui;

import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * @author Doogie13
 * @since 05/04/2023
 */
@Mixin(GuiDisconnected.class)
public interface IGuiDisconnected {

    @Accessor("reason")
    String getReason();

    @Accessor("message")
    ITextComponent getMessage();

    @Accessor("parentScreen")
    GuiScreen getParentScreen();

    @Accessor("textHeight")
    int getTextHeight();

}
