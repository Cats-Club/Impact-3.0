package me.zero.clarinet.mixin.mixins.minecraft.client.renderer;

import me.zero.clarinet.mixin.Fields;
import me.zero.clarinet.mod.misc.RainbowEnchant;
import me.zero.clarinet.util.render.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.misc.Animations;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(RenderItem.class)
public class MixinRenderItem {

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableRescaleNormal()V", shift = At.Shift.BEFORE))
    public void renderItem(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        if (Impact.getInstance().getModManager().get(Animations.class).isToggled() && Impact.getInstance().getModManager().get(Animations.class).shield.getValue()) {
            GlStateManager.translate(0.0F, -Impact.getInstance().getModManager().get(Animations.class).lower.getValue().floatValue(), 0.0F);
        }
    }

    @Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasEffect()Z"))
    public boolean renderItem2(ItemStack instance) {
        if (!Fields.renderEnchantsRenderItem)
            return false;
        return instance.hasEffect();
    }

    @ModifyArgs(method = "renderEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"))
    public void renderEffect(Args args) {
        if (Impact.getInstance().getModManager().get(RainbowEnchant.class).isToggled()) {
            args.set(1, ColorUtils.getRainbowH());
        }
    }

}
