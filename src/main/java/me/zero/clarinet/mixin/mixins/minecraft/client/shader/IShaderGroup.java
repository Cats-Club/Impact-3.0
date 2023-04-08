package me.zero.clarinet.mixin.mixins.minecraft.client.shader;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * @author Doogie13
 * @since 05/04/2023
 */
@Mixin(ShaderGroup.class)
public interface IShaderGroup {

    @Accessor("listFramebuffers")
    List<Framebuffer> getFrameBuffers();

    @Accessor("listShaders")
    List<Shader> getShaders();

}
