package me.zero.clarinet.mixin.mixins.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.misc.Animations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Shadow @Final private Minecraft mc;

    @Shadow protected abstract void transformFirstPerson(EnumHandSide hand, float p_187453_2_);

    Animations animations;
    boolean _1dot8;
    boolean vanilla;
    boolean flags;

    float p_187457_7_new;
    float p_187457_5_;

    @Inject(
            method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", shift = At.Shift.BEFORE)
    )
    public void renderItemInFirstPerson(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack p_187457_6_, float p_187457_7_, CallbackInfo ci) {
        animations = Impact.getInstance().getModManager().get(Animations.class);
        _1dot8 = animations.isToggled() && animations.sword.getValue();
        vanilla = !animations.mode.getValue().equalsIgnoreCase("Fancy");
        flags = false;
        this.p_187457_5_ = p_187457_5_;
        if (_1dot8) {
            try {
                if (p_187457_6_.getItem() != null && p_187457_6_.getItem() instanceof ItemShield) {
                    return;
                }
            } catch (Exception e) {
            }

            if (mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                if (mc.player.getActiveItemStack() != null && mc.player.getActiveItemStack().getItem() instanceof ItemShield) {
                    flags = true;
                }
            }
        }
    }

    @Inject(
            method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal = 3)
    )
    public void renderItemInFirstPerson2(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo ci) {

        if (animations.isToggled()) {
            if (!animations.recharge.getValue()) {
                p_187457_7_new = 0.0F;
            }
            if (flags) {
                float f = -0.4F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * (float)Math.PI);
                float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * ((float)Math.PI * 2F));
                float f2 = -0.2F * MathHelper.sin(p_187457_5_ * (float)Math.PI);
                boolean flag1 = EnumHandSide.RIGHT == (hand == EnumHand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite());
                int i = flag1 ? 1 : -1;
                GlStateManager.translate(-(float) i * f, -f1, -f2);
            }
        }
    }

    @ModifyArgs(
            method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V", ordinal = 4)
    )
    public void renderItemInFirstPersonMArgs(Args args) {
        if (animations.isToggled()) {
            if (animations.recharge.getValue()) {
                args.set(1, p_187457_5_);
            }
        }
    }

    @Redirect(
            method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V", ordinal = 0)
    )
    public void renderItemInFirstPerson3(ItemRenderer instance, EnumHandSide hand, float p_187453_2_) {
        if (!flags || vanilla)
            transformFirstPerson(hand, p_187453_2_);

        if (flags) {
            if (vanilla) {
                boolean cool = animations.mode.getValue().equalsIgnoreCase("1.7");
                double amt = 0.25;
                double amt2 = 0.1;
                if (cool) {
                    amt = 0.2;
                    amt2 = 0.15;
                }
                GlStateManager.translate(cool ? -0.05 : 0.0, amt2, amt);
                GlStateManager.rotate(-25, 1, 0, 0);
                GlStateManager.rotate(80, 0, 1, 0);
                GlStateManager.rotate(-75, 1, 0, 0);
                GlStateManager.translate(0.0, -amt2, -amt);
                GlStateManager.translate(0.1, 0.2, 0.25);
                if (cool) {
                    GlStateManager.translate(-0.15, 0.05, 0.0);
                }
            } else {
                GlStateManager.rotate(93, 0, 1, 0);
                GlStateManager.rotate(-80, 1, 0, 0);
                GlStateManager.rotate(-13, 0, 0, 1);
                float sin = (float) Math.sin(Math.sqrt(p_187457_5_) * Math.PI);
                GlStateManager.translate(-0.05F, 0.2F, 0.0F);
                GlStateManager.rotate(-sin * 30.0F, 1.0F, -0.4F, 0.0F);
            }
        }
    }

}
