package me.zero.clarinet.mixin.mixins.minecraft.client.gui;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.render.EventNameprotect;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    @ModifyVariable(method = "renderString", at = @At("HEAD"), index = 1, argsOnly = true)
    public String renderString(String value) {

        // Doogie13
        if (value == null)
            return null;

        EventNameprotect event = new EventNameprotect(value);
        EventManager.call(event);

        return event.getMessage();
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), index = 1, argsOnly = true)
    public String getStringWidth(String value) {

        // Doogie13
        if (value == null)
            return null;

        EventNameprotect event = new EventNameprotect(value);
        EventManager.call(event);

        return event.getMessage();

    }

}
