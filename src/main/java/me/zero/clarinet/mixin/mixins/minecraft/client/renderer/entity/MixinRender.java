package me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.render.EventNametag;
import me.zero.clarinet.mixin.Fields;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(Render.class)
public class MixinRender {

    String str = null;

    @Inject(method = "renderLivingLabel", at = @At("HEAD"), cancellable = true)
    protected void renderLivingLabel(Entity entityIn, String str, double x, double y, double z, int maxDistance, CallbackInfo ci) {
        EventNametag event = new EventNametag(entityIn, str);
        EventManager.call(event);
        this.str = event.getRenderName();
        if (!Fields.renderTagsRender || event.isCancelled()) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "renderLivingLabel", at = @At(value = "HEAD"), index = 2, argsOnly = true)
    public String renderLivingLabel(String value) {
        return str;
    }

}
