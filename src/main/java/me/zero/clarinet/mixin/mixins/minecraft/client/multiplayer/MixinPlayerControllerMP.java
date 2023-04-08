package me.zero.clarinet.mixin.mixins.minecraft.client.multiplayer;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.types.EventType;
import me.zero.clarinet.event.player.EventAttackEntity;
import me.zero.clarinet.mod.player.FastBreak;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Shadow private int blockHitDelay;

    @Inject(method = "clickBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;blockHitDelay:I", shift = At.Shift.AFTER))
    public void clickBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {

        blockHitDelay = Impact.getInstance().getModManager().get(FastBreak.class).isToggled() ? 0 : 5;

    }

    @Inject(method = "onPlayerDamageBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;blockHitDelay:I", shift = At.Shift.AFTER))
    public void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {

        blockHitDelay = Impact.getInstance().getModManager().get(FastBreak.class).isToggled() ? 0 : 5;

    }

    @Inject(method = "attackEntity", at = @At("HEAD"))
    public void attackEntity(EntityPlayer playerIn, Entity targetEntity, CallbackInfo ci) {

        EventManager.call(new EventAttackEntity(EventType.PRE, targetEntity));

    }

    @Inject(method = "attackEntity", at = @At("TAIL"))
    public void attackEntityTail(EntityPlayer playerIn, Entity targetEntity, CallbackInfo ci) {

        EventManager.call(new EventAttackEntity(EventType.POST, targetEntity));

    }

}
