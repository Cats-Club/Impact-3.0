package me.zero.clarinet.mixin.mixins.minecraft.block;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.movement.NoSlowDown;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(BlockSoulSand.class)
public class MixinBlockSoulSand {

    // 1.10.2 -> 1.12.2 mapping change
    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn, CallbackInfo ci) {
        if (Impact.getInstance().getModManager().get(NoSlowDown.class).isToggled()) {
            ci.cancel();
        }
    }

}
