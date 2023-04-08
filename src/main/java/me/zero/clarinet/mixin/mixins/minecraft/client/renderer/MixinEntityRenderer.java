package me.zero.clarinet.mixin.mixins.minecraft.client.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import me.zero.clarinet.Impact;
import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.render.EventHurtcam;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.event.render.EventRenderBrightness;
import me.zero.clarinet.event.render.EventRenderHands;
import me.zero.clarinet.mod.render.AntiBlind;
import me.zero.clarinet.mod.render.CameraClip;
import me.zero.clarinet.mod.render.LiquidVision;
import me.zero.clarinet.mod.render.Wireframe;
import net.minecraft.block.material.Material;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(net.minecraft.client.renderer.EntityRenderer.class)
public class MixinEntityRenderer {

    @Shadow private float thirdPersonDistancePrev;

    @Shadow private float smoothCamPartialTicks;

    @Shadow @Final private net.minecraft.client.Minecraft mc;

    @Shadow private int rendererUpdateCount;

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        EventHurtcam event = new EventHurtcam();
        EventManager.call(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;distanceTo(Lnet/minecraft/util/math/Vec3d;)D"))
    public double orientCamera(Vec3d instance, Vec3d vec) {

        double d3 = (double)(thirdPersonDistancePrev + (4.0F - this.thirdPersonDistancePrev) * Minecraft.getMinecraft().getRenderPartialTicks());

        if (Impact.getInstance().getModManager().get(CameraClip.class).isToggled())
            return d3;

        return instance.distanceTo(vec);

    }

    @Inject(method = "setupCameraTransform", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;orientCamera(F)V", shift = At.Shift.BEFORE))
    public void setupCameraTransform(float partialTicks, int pass, CallbackInfo ci) {
        float f1 = mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * partialTicks;
        if (f1 > 0.0F && (!Impact.getInstance().getModManager().get(AntiBlind.class).isToggled()))
        {
            int i = 20;

            if (this.mc.player.isPotionActive(MobEffects.NAUSEA))
            {
                i = 7;
            }

            float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
            f2 = f2 * f2;
            GlStateManager.rotate(((float)this.rendererUpdateCount + partialTicks) * (float)i, 0.0F, 1.0F, 1.0F);
            GlStateManager.scale(1.0F / f2, 1.0F, 1.0F);
            GlStateManager.rotate(-((float)this.rendererUpdateCount + partialTicks) * (float)i, 0.0F, 1.0F, 1.0F);
        }
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    public void renderHand(float partialTicks, int pass, CallbackInfo ci) {
        EventRenderHands event = new EventRenderHands();
        EventManager.call(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "updateLightmap", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;gammaSetting:F"))
    public float updateLightmap(GameSettings instance) {
        EventRenderBrightness event = new EventRenderBrightness();
        EventManager.call(event);
        return event.brightness;
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;matrixMode(I)V", ordinal = 4))
    public void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {

        Wireframe wireframe = Impact.getInstance().getModManager().get(Wireframe.class);
        if (wireframe.isToggled()) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(-1, -1);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glLineWidth(wireframe.width.getValue().floatValue());
        }

    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;shadeModel(I)V", ordinal = 1))
    public void renderWorldPass2(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {

        Wireframe wireframe = Impact.getInstance().getModManager().get(Wireframe.class);
        if (wireframe.isToggled()) {
            GL11.glPopAttrib();
        }

    }

    @Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE))
    public void renderWorldPass3(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {

        EventManager.call(new EventRender3D(partialTicks));

    }

    @Redirect(method = "updateFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    public boolean updateFogColor(EntityLivingBase instance, Potion potionIn) {
        return instance.isPotionActive(potionIn) && !Impact.getInstance().getModManager().get(AntiBlind.class).isToggled();
    }

    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z", ordinal = 0))
    public boolean setupFog(EntityLivingBase instance, Potion potionIn) {
        return instance.isPotionActive(potionIn) && !Impact.getInstance().getModManager().get(AntiBlind.class).isToggled();
    }

    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getMaterial()Lnet/minecraft/block/material/Material;", ordinal = 0))
    public Material setupFogWater(IBlockState instance) {
        // just needs to not be water
        return Impact.getInstance().getModManager().get(LiquidVision.class).isToggled()
                && Impact.getInstance().getModManager().get(LiquidVision.class).water.getValue()
                ? Material.AIR
                : instance.getMaterial();
    }

    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getMaterial()Lnet/minecraft/block/material/Material;", ordinal = 1))
    public Material setupFogLava(IBlockState instance) {
        // just needs to not be lava
        return Impact.getInstance().getModManager().get(LiquidVision.class).isToggled()
                && Impact.getInstance().getModManager().get(LiquidVision.class).lava.getValue()
                ? Material.AIR
                : instance.getMaterial();
    }

}
