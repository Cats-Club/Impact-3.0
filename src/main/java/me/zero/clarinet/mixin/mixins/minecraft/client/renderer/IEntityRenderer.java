package me.zero.clarinet.mixin.mixins.minecraft.client.renderer;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(EntityRenderer.class)
public interface IEntityRenderer {

    @Invoker void callOrientCamera(float partialTicks);

    @Invoker void callSetupCameraTransform(float partialTicks, int pass);

    @Invoker void callUpdateFogColor(float partialTicks);

    @Accessor("renderEndNanoTime")
    long getRenderEndNanoTime();

}
