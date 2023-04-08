package me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author Doogie13
 * @since 05/04/2023
 */
@Mixin(Render.class)
public interface IRender {

    @Accessor("renderOutlines")
    boolean getRenderOutlines();

    @Invoker int callGetTeamColor(Entity entityIn);

}
