package me.zero.clarinet.mixin.mixins.minecraft.block;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.exploit.GhostHand;
import me.zero.clarinet.mod.player.Freecam;
import me.zero.clarinet.mod.world.Xray;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
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
@Mixin(Block.class)
public class MixinBlock {

    @Inject(method = "isFullCube", at = @At("HEAD"), cancellable = true)
    public void isFullCube(IBlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (Impact.getInstance().getModManager().get(Xray.class).isToggled() || Impact.getInstance().getModManager().get(Freecam.class).isToggled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getPackedLightmapCoords", at = @At("HEAD"), cancellable = true)
    public void getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (Impact.getInstance().getModManager().get(Xray.class).isToggled()) {
            cir.setReturnValue(1000000000);
        }
    }

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    public void shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        if (Impact.getInstance().getModManager().get(Xray.class).isToggled()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    public void canCollideCheck(IBlockState state, boolean hitIfLiquid, CallbackInfoReturnable<Boolean> cir)
    {
        if (Impact.getInstance().getModManager().get(GhostHand.class).isToggled()) {
            cir.setReturnValue(state.getBlock() == Block.getBlockById(Impact.getInstance().getModManager().get(GhostHand.class).id));
        }
    }

}
