package me.zero.clarinet.mixin.mixins.minecraft.entity;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.types.EventType;
import me.zero.clarinet.event.player.EventGround;
import me.zero.clarinet.event.player.EventStep;
import me.zero.clarinet.mod.combat.HitBox;
import me.zero.clarinet.mod.movement.Flight;
import me.zero.clarinet.mod.movement.SafeWalk;
import me.zero.clarinet.mod.player.Freecam;
import me.zero.clarinet.mod.render.ESP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow public boolean noClip;

    @Shadow public abstract void setEntityBoundingBox(AxisAlignedBB bb);

    @Shadow public abstract AxisAlignedBB getEntityBoundingBox();

    @Shadow public abstract void resetPositionToBB();

    @Shadow public float stepHeight;

    @Shadow public boolean onGround;

    @Shadow public abstract boolean isSneaking();

    boolean shouldStep = true;
    private EventStep eventStep;

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MoverType type, double x, double y, double z, CallbackInfo ci) {

        Freecam freecam = Impact.getInstance().getModManager().get(Freecam.class);

        if (this.noClip || freecam.isToggled()) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.resetPositionToBB();

            if (freecam.isToggled()) {
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x * 10, y, z * 10));
            }
            ci.cancel();
        }


    }

    @Redirect(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;onGround:Z", ordinal = 0))
    public boolean move2(Entity instance) {
        boolean flag = this.onGround && this.isSneaking() && ((Entity) (Object) this) instanceof EntityPlayer;
        boolean flag2 = this.onGround && Impact.getInstance().getModManager().get(SafeWalk.class).isToggled() && ((Entity) (Object) this) instanceof EntityPlayerSP;
        Flight flight = Impact.getInstance().getModManager().get(Flight.class);
        boolean flightCheck = flight.isToggled() && flight.getMode().getName().equalsIgnoreCase("Vanilla");

        return (flag || flag2) && !flightCheck;
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean move3(Entity instance) {
        return true; // vanilla logic incl sneaking handled already above
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getEntityBoundingBox()Lnet/minecraft/util/math/AxisAlignedBB;", ordinal = 12))
    public void move3(MoverType type, double x, double y, double z, CallbackInfo ci) {
        eventStep = new EventStep(((Entity) (Object) this), this.stepHeight, EventType.PRE);
        EventManager.call(eventStep);
    }

    @Redirect(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F", ordinal = 4))
    public float move4(Entity instance) {
        return (float) eventStep.stepHeight;
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setEntityBoundingBox(Lnet/minecraft/util/math/AxisAlignedBB;)V", ordinal = 8))
    public void move4(MoverType type, double x, double y, double z, CallbackInfo ci) {
        shouldStep = false;
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", ordinal = 0))
    public void move5(MoverType type, double x, double y, double z, CallbackInfo ci) {
        if (shouldStep) {
            EventManager.call(new EventStep(((Entity) (Object) this), this.stepHeight, EventType.POST));
        }

        shouldStep = false;
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(D)I", ordinal = 1))
    public void move6(MoverType type, double x, double y, double z, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof EntityPlayerSP) {
            EventGround event = new EventGround(onGround);
            EventManager.call(event);
            onGround = event.onGround;
        }
    }

    @Inject(method = "getCollisionBorderSize", at = @At("HEAD"), cancellable = true)
    public void getCollisionBorderSize(CallbackInfoReturnable<Float> cir) {
        HitBox hitbox = Impact.getInstance().getModManager().get(HitBox.class);
        if (hitbox.isToggled()) {
            cir.setReturnValue(hitbox.amount.getValue().floatValue());
        }
    }

    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    public void isGlowing(CallbackInfoReturnable<Boolean> cir) {
            ESP esp = Impact.getInstance().getModManager().get(ESP.class);
            if (esp.isToggled() && esp.isSpectral() && esp.isCorrectEntity(this)) {
                cir.setReturnValue(true);
            }
    }

}
