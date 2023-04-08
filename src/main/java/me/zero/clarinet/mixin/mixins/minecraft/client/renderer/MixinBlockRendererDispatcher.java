package me.zero.clarinet.mixin.mixins.minecraft.client.renderer;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.render.EventRenderBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(BlockRendererDispatcher.class)
public class MixinBlockRendererDispatcher {

    @Inject(method = "renderBlock", at = @At("HEAD"))
    public void renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn, CallbackInfoReturnable<Boolean> cir) {
        EventRenderBlock event = new EventRenderBlock(state, pos);
        EventManager.call(event);
    }

}
