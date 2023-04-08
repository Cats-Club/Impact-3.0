package me.zero.clarinet.mixin.mixins.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import me.zero.clarinet.Impact;
import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.network.EventSendChat;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.event.player.EventMove;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.movement.NoSlowDown;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow protected abstract void updateAutoJump(float p_189810_1_, float p_189810_2_);

    private EventMotionUpdate eventPre = null;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        EventManager.call(new EventUpdate());
    }

    // hell
    // region onUpdateWalkingPlayer

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    public void onUpdateWalkingPlayer(CallbackInfo ci) {
        eventPre = new EventMotionUpdate(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.isSprinting(), this.isSneaking(), this.onGround);
        EventManager.call(eventPre);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("TAIL"))
    public void onUpdateWalkingPlayerTail(CallbackInfo ci) {
        EventMotionUpdate eventPost = new EventMotionUpdate();
        EventManager.call(eventPost);
    }

    // region deltas
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posX:D", ordinal = 0))
    public double getPosXd(EntityPlayerSP instance) {
        return eventPre.x;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/AxisAlignedBB;minY:D", ordinal = 0))
    public double getPosYd(AxisAlignedBB instance) {
        return eventPre.y;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posZ:D", ordinal = 0))
    public double getPosZd(EntityPlayerSP instance) {
        return eventPre.z;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F", ordinal = 0))
    public float getRotYawd(EntityPlayerSP instance) {
        return eventPre.yaw;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F", ordinal = 0))
    public float getRotPitchd(EntityPlayerSP instance) {
        return eventPre.pitch;
    }
    // no onground check
    // endregion

    // region packet

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posX:D", ordinal = 1))
    public double getPosX1(EntityPlayerSP instance) {
        return eventPre.x;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/AxisAlignedBB;minY:D", ordinal = 1))
    public double getPosY1(AxisAlignedBB instance) {
        return eventPre.y;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posZ:D", ordinal = 1))
    public double getPosZ1(EntityPlayerSP instance) {
        return eventPre.z;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posX:D", ordinal = 2))
    public double getPosX2(EntityPlayerSP instance) {
        return eventPre.x;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/AxisAlignedBB;minY:D", ordinal = 2))
    public double getPosY2(AxisAlignedBB instance) {
        return eventPre.y;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posZ:D", ordinal = 2))
    public double getPosZ2(EntityPlayerSP instance) {
        return eventPre.z;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posX:D", ordinal = 3))
    public double getPosX3(EntityPlayerSP instance) {
        return eventPre.x;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/AxisAlignedBB;minY:D", ordinal = 3))
    public double getPosY3(AxisAlignedBB instance) {
        return eventPre.y;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posZ:D", ordinal = 3))
    public double getPosZ3(EntityPlayerSP instance) {
        return eventPre.z;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F", ordinal = 1))
    public float getRotYaw1(EntityPlayerSP instance) {
        return eventPre.yaw;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F", ordinal = 1))
    public float getRotPitch1(EntityPlayerSP instance) {
        return eventPre.pitch;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F", ordinal = 2))
    public float getRotYaw2(EntityPlayerSP instance) {
        return eventPre.yaw;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F", ordinal = 2))
    public float getRotPitch2(EntityPlayerSP instance) {
        return eventPre.pitch;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F", ordinal = 3))
    public float getRotYaw3(EntityPlayerSP instance) {
        return eventPre.yaw;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F", ordinal = 3))
    public float getRotPitch3(EntityPlayerSP instance) {
        return eventPre.pitch;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F", ordinal = 4))
    public float getRotYaw4(EntityPlayerSP instance) {
        return eventPre.yaw;
    }
    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F", ordinal = 4))
    public float getRotPitch4(EntityPlayerSP instance) {
        return eventPre.pitch;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onGround:Z"))
    public boolean getOnGround0(EntityPlayerSP instance) {
        return eventPre.onGround;
    }
    // endregion

    // endregion

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        EventSendChat event = new EventSendChat(message);
        EventManager.call(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isRiding()Z"))
    public boolean onLivingUpdate(EntityPlayerSP instance) {
        return instance.isRiding() || Impact.getInstance().getModManager().get(NoSlowDown.class).isToggled();
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(AbstractClientPlayer instance, MoverType type, double x, double y, double z) {
        EventMove event = new EventMove(x, y, z);
        EventManager.call(event);
        double d0 = this.posX;
        double d1 = this.posZ;
        super.move(type, event.x, event.y, event.z);
        this.updateAutoJump((float)(this.posX - d0), (float)(this.posZ - d1));
    }

}
