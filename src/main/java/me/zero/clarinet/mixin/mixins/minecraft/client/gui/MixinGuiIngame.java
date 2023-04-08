package me.zero.clarinet.mixin.mixins.minecraft.client.gui;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.render.EventCrosshairRender;
import me.zero.clarinet.event.render.EventRender2D;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    // see GuiIngameForge - this is never executed!
    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V", ordinal = 3, shift = At.Shift.BEFORE))
    public void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        EventManager.call(new EventRender2D(partialTicks));
    }

    @Inject(method = "renderAttackIndicator", at = @At("HEAD"), cancellable = true)
    private void renderAttackIndicator(float partialTicks, ScaledResolution p_184045_2_, CallbackInfo ci) {
        EventCrosshairRender event = new EventCrosshairRender();
        EventManager.call(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}
