package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class SwimNodeProcessor extends NodeProcessor
{
    public net.minecraft.pathfinding.PathPoint getStart()
    {
        return this.openPoint(MathHelper.floor_double(this.entity.getEntityBoundingBox().minX), MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(this.entity.getEntityBoundingBox().minZ));
    }

    /**
     * Returns PathPoint for given coordinates
     */
    public net.minecraft.pathfinding.PathPoint getPathPointToCoords(double x, double y, double z)
    {
        return this.openPoint(MathHelper.floor_double(x - (double)(this.entity.width / 2.0F)), MathHelper.floor_double(y + 0.5D), MathHelper.floor_double(z - (double)(this.entity.width / 2.0F)));
    }

    public int findPathOptions(net.minecraft.pathfinding.PathPoint[] pathOptions, net.minecraft.pathfinding.PathPoint currentPoint, net.minecraft.pathfinding.PathPoint targetPoint, float maxDistance)
    {
        int i = 0;

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            net.minecraft.pathfinding.PathPoint pathpoint = this.getWaterNode(currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());

            if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint;
            }
        }

        return i;
    }

    public net.minecraft.pathfinding.PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn)
    {
        return net.minecraft.pathfinding.PathNodeType.WATER;
    }

    public net.minecraft.pathfinding.PathNodeType getPathNodeType(IBlockAccess x, int y, int z, int p_186330_4_)
    {
        return net.minecraft.pathfinding.PathNodeType.WATER;
    }

    @Nullable
    private PathPoint getWaterNode(int p_186328_1_, int p_186328_2_, int p_186328_3_)
    {
        net.minecraft.pathfinding.PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return pathnodetype == net.minecraft.pathfinding.PathNodeType.WATER ? this.openPoint(p_186328_1_, p_186328_2_, p_186328_3_) : null;
    }

    private net.minecraft.pathfinding.PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_)
    {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int i = p_186327_1_; i < p_186327_1_ + this.entitySizeX; ++i)
        {
            for (int j = p_186327_2_; j < p_186327_2_ + this.entitySizeY; ++j)
            {
                for (int k = p_186327_3_; k < p_186327_3_ + this.entitySizeZ; ++k)
                {
                    IBlockState iblockstate = this.blockaccess.getBlockState(blockpos$mutableblockpos.setPos(i, j, k));

                    if (iblockstate.getMaterial() != Material.WATER)
                    {
                        return net.minecraft.pathfinding.PathNodeType.BLOCKED;
                    }
                }
            }
        }

        return PathNodeType.WATER;
    }
}
