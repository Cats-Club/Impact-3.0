package me.zero.clarinet.mixin.mixins.minecraft.entity;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.player.EventSprint;
import me.zero.clarinet.mixin.Fields;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase {

    @Inject(method = "swingArm", at = @At("HEAD"))
    public void swingArm(EnumHand hand, CallbackInfo ci) {
        if (((EntityLivingBase) (Object) this) instanceof EntityPlayer)
            Fields.didSwingEntityPlayer = true;
    }

    @ModifyVariable(method = "setSprinting", at = @At("HEAD"), index = 1, argsOnly = true)
    public boolean setSprinting(boolean value) {
        EventSprint event = new EventSprint(value);
        EventManager.call(event);
        return event.sprinting;
    }

}
