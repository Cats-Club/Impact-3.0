package me.zero.clarinet.mixin.mixins.minecraft.entity.player;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.movement.Sprint;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    @ModifyArgs(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;setSprinting(Z)V"))
    public void attackTargetEntityWithCurrentItem(Args args) {
        Sprint sprint = Impact.getInstance().getModManager().get(Sprint.class);
        if ((sprint.isToggled() && sprint.keep.getValue()) && (boolean) args.get(0)) {
            EntityPlayer instance = (EntityPlayer) (Object) this;
            instance.motionX /= 0.6D;
            instance.motionZ /= 0.6D;
            args.set(0, true);
        }
    }

}
