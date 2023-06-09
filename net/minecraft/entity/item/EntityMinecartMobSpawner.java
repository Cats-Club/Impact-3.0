package net.minecraft.entity.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartMobSpawner extends net.minecraft.entity.item.EntityMinecart
{
    /** Mob spawner logic for this spawner minecart. */
    private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic()
    {
        public void broadcastEvent(int id)
        {
            EntityMinecartMobSpawner.this.worldObj.setEntityState(EntityMinecartMobSpawner.this, (byte)id);
        }
        public World getSpawnerWorld()
        {
            return EntityMinecartMobSpawner.this.worldObj;
        }
        public BlockPos getSpawnerPosition()
        {
            return new BlockPos(EntityMinecartMobSpawner.this);
        }
    };

    public EntityMinecartMobSpawner(World worldIn)
    {
        super(worldIn);
    }

    public EntityMinecartMobSpawner(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void func_189672_a(DataFixer p_189672_0_)
    {
        func_189669_a(p_189672_0_, "MinecartSpawner");
        p_189672_0_.registerWalker(FixTypes.ENTITY, new IDataWalker()
        {
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn)
            {
                if ("MinecartSpawner".equals(compound.getString("id")))
                {
                    compound.setString("id", "MobSpawner");
                    fixer.process(FixTypes.BLOCK_ENTITY, compound, versionIn);
                    compound.setString("id", "MinecartSpawner");
                }

                return compound;
            }
        });
    }

    public Type getType()
    {
        return Type.SPAWNER;
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.MOB_SPAWNER.getDefaultState();
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.mobSpawnerLogic.readFromNBT(compound);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        this.mobSpawnerLogic.writeToNBT(compound);
    }

    public void handleStatusUpdate(byte id)
    {
        this.mobSpawnerLogic.setDelayToMin(id);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.mobSpawnerLogic.updateSpawner();
    }
}
