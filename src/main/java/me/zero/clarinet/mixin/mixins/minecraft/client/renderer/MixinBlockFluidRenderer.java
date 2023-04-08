package me.zero.clarinet.mixin.mixins.minecraft.client.renderer;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.world.Xray;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
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
@Mixin(BlockFluidRenderer.class)
public class MixinBlockFluidRenderer {

    @Inject(method = "renderFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER), cancellable = true)
    public void renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder bufferBuilderIn, CallbackInfoReturnable<Boolean> cir) {
        BlockLiquid blockliquid = (BlockLiquid)blockStateIn.getBlock();
        if(Impact.getInstance().getModManager().get(Xray.class).isToggled()){
            if(!Impact.getInstance().getXrayManager().isBlock(Block.getIdFromBlock(blockliquid))){
                cir.setReturnValue(false);
            }
        }
    }

}
