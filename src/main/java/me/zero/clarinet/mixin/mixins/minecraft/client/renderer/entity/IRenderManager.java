package me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(RenderManager.class)
public interface IRenderManager {

    @Accessor("renderPosX")
    double getRenderPosX();

    @Accessor("renderPosY")
    double getRenderPosY();

    @Accessor("renderPosZ")
    double getRenderPosZ();

}
