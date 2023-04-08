package net.minecraft.block;

import javax.annotation.Nullable;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.movement.NoSlowDown;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSoulSand extends Block
{
    protected static final AxisAlignedBB SOUL_SAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockSoulSand()
    {
        super(Material.SAND, MapColor.BROWN);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return SOUL_SAND_AABB;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        // TODON'T: Impact
        if (Impact.getInstance().getModManager().get(NoSlowDown.class).isToggled()) {
            return;
        }

        entityIn.motionX *= 0.4D;
        entityIn.motionZ *= 0.4D;
    }
}
