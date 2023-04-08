package me.zero.clarinet.mixin.mixins.minecraft.block;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.movement.Jesus;
import me.zero.clarinet.mod.player.LiquidInteract;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
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
@Mixin(BlockLiquid.class)
public class MixinBlockLiquid {

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (Impact.getInstance().getModManager().get(Jesus.class).isToggled() && Impact.getInstance().getModManager().get(Jesus.class).isJesus) {
            cir.setReturnValue(Block.FULL_BLOCK_AABB);
        }
    }

    @Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    public void canCollideCheck(IBlockState state, boolean hitIfLiquid, CallbackInfoReturnable<Boolean> cir) {
        if (Impact.getInstance().getModManager().get(LiquidInteract.class).isToggled()) {
            cir.setReturnValue(true);
        }
    }

}
