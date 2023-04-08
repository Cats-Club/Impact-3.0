package me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.render.ItemPhysics;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {

    @Inject(method = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V", at = @At("HEAD"), cancellable = true)
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        ItemPhysics physics = Impact.getInstance().getModManager().get(ItemPhysics.class);
        if (physics.isToggled()) {
            physics.doRender((RenderEntityItem) (Object) this, entity, x, y, z, entityYaw, partialTicks);
            ci.cancel();
        }
    }

}
