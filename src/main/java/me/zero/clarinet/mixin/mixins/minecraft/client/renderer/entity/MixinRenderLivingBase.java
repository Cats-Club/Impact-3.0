package me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.types.EventType;
import me.zero.clarinet.event.render.EventEntityRender;
import me.zero.clarinet.mixin.Fields;
import me.zero.clarinet.mod.render.ESP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase {

    Entity entity;

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At("HEAD"))
    public void doRender(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        EventEntityRender eventPre = new EventEntityRender(EventType.PRE, (RenderLivingBase) (Object) this, entity, x, y, z);
        EventManager.call(eventPre);
        this.entity = entity;
    }

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At("TAIL"))
    public void doRenderPost(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        EventEntityRender eventPost = new EventEntityRender(EventType.POST, (RenderLivingBase) (Object) this, entity, x, y, z);
        EventManager.call(eventPost);
        this.entity = null;
    }

    @Redirect(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableOutlineMode(I)V"))
    public void doRenderEOM(int color) {
        ESP esp = Impact.getInstance().getModManager().get(ESP.class);
        if (esp.isToggled() && esp.isSpectral() && esp.isCorrectEntity(entity)) {
            GlStateManager.enableOutlineMode(esp.getESPColorH(entity));
        } else {
            // bruh
            int i = 16777215;
            ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam) entity.getTeam();

            if (scoreplayerteam != null)
            {
                String s = FontRenderer.getFormatFromString(scoreplayerteam.getPrefix());

                if (s.length() >= 2)
                {
                    i = Minecraft.getMinecraft().fontRenderer.getColorCode(s.charAt(1));
                }
            }

            GlStateManager.enableOutlineMode(i);
        }

    }

    @Inject(method = "renderLayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerRenderer;doRenderLayer(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V"), cancellable = true)
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn, CallbackInfo ci) {
        if (!Fields.renderLayersRenderLivingBase)
            ci.cancel();
    }

}
