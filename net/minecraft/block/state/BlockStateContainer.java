package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.BlockModelUtils;
import net.minecraft.src.Reflector;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MapPopulator;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Cartesian;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockStateContainer
{
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    private static final Function < IProperty<?>, String > GET_NAME_FUNC = new Function < IProperty<?>, String > ()
    {
        @Nullable
        public String apply(@Nullable IProperty<?> p_apply_1_)
        {
            return p_apply_1_ == null ? "<NULL>" : p_apply_1_.getName();
        }
    };
    private final Block block;
    private final ImmutableSortedMap < String, IProperty<? >> properties;
    private final ImmutableList<StateImplementation> validStates;

    public BlockStateContainer(Block blockIn, IProperty<?>... properties)
    {
        this(blockIn, properties, (ImmutableMap < IUnlistedProperty<?>, Optional<? >>)null);
    }

    protected BlockStateContainer.StateImplementation createState(Block p_createState_1_, ImmutableMap < IProperty<?>, Comparable<? >> p_createState_2_, ImmutableMap < IUnlistedProperty<?>, Optional<? >> p_createState_3_)
    {
        return new BlockStateContainer.StateImplementation(p_createState_1_, p_createState_2_);
    }

    protected BlockStateContainer(Block p_i8_1_, IProperty<?>[] p_i8_2_, ImmutableMap < IUnlistedProperty<?>, Optional<? >> p_i8_3_)
    {
        this.block = p_i8_1_;
        Map < String, IProperty<? >> map = Maps. < String, IProperty<? >> newHashMap();

        for (IProperty<?> iproperty : p_i8_2_)
        {
            validateProperty(p_i8_1_, iproperty);
            map.put(iproperty.getName(), iproperty);
        }

        this.properties = ImmutableSortedMap. < String, IProperty<? >> copyOf(map);
        Map < Map < IProperty<?>, Comparable<? >> , BlockStateContainer.StateImplementation > map2 = Maps. < Map < IProperty<?>, Comparable<? >> , BlockStateContainer.StateImplementation > newLinkedHashMap();
        List<BlockStateContainer.StateImplementation> list = Lists.<BlockStateContainer.StateImplementation>newArrayList();

        for (List < Comparable<? >> list1 : Cartesian.cartesianProduct(this.getAllowedValues()))
        {
            Map < IProperty<?>, Comparable<? >> map1 = MapPopulator. < IProperty<?>, Comparable<? >> createMap(this.properties.values(), list1);
            BlockStateContainer.StateImplementation blockstatecontainer$stateimplementation = this.createState(p_i8_1_, ImmutableMap. < IProperty<?>, Comparable<? >> copyOf(map1), p_i8_3_);
            map2.put(map1, blockstatecontainer$stateimplementation);
            list.add(blockstatecontainer$stateimplementation);
        }

        for (BlockStateContainer.StateImplementation blockstatecontainer$stateimplementation1 : list)
        {
            blockstatecontainer$stateimplementation1.buildPropertyValueTable(map2);
        }

        this.validStates = ImmutableList.copyOf(list);
    }

    public static <T extends Comparable<T>> String validateProperty(Block block, IProperty<T> property)
    {
        String s = property.getName();

        if (!NAME_PATTERN.matcher(s).matches())
        {
            throw new IllegalArgumentException("Block: " + block.getClass() + " has invalidly named property: " + s);
        }
        else
        {
            for (T t : property.getAllowedValues())
            {
                String s1 = property.getName(t);

                if (!NAME_PATTERN.matcher(s1).matches())
                {
                    throw new IllegalArgumentException("Block: " + block.getClass() + " has property: " + s + " with invalidly named value: " + s1);
                }
            }

            return s;
        }
    }

    public ImmutableList<StateImplementation> getValidStates()
    {
        return this.validStates;
    }

    private List < Iterable < Comparable<? >>> getAllowedValues()
    {
        List < Iterable < Comparable<? >>> list = Lists. < Iterable < Comparable<? >>> newArrayList();

        for (IProperty<?> iproperty : this.properties.values())
        {
            list.add(((IProperty)iproperty).getAllowedValues());
        }

        return list;
    }

    public IBlockState getBaseState()
    {
        return (IBlockState)this.validStates.get(0);
    }

    public Block getBlock()
    {
        return this.block;
    }

    public Collection < IProperty<? >> getProperties()
    {
        return this.properties.values();
    }

    public String toString()
    {
        return Objects.toStringHelper(this).add("block", Block.REGISTRY.getNameForObject(this.block)).add("properties", Iterables.transform(this.properties.values(), GET_NAME_FUNC)).toString();
    }

    @Nullable
    public IProperty<?> getProperty(String propertyName)
    {
        return (IProperty)this.properties.get(propertyName);
    }

    public static class Builder
    {
        private final Block block;
        private final List < IProperty<? >> listed = Lists. < IProperty<? >> newArrayList();
        private final List < IUnlistedProperty<? >> unlisted = Lists. < IUnlistedProperty<? >> newArrayList();

        public Builder(Block p_i10_1_)
        {
            this.block = p_i10_1_;
        }

        public BlockStateContainer.Builder add(IProperty<?>... p_add_1_)
        {
            for (IProperty<?> iproperty : p_add_1_)
            {
                this.listed.add(iproperty);
            }

            return this;
        }

        public BlockStateContainer.Builder add(IUnlistedProperty<?>... p_add_1_)
        {
            for (IUnlistedProperty<?> iunlistedproperty : p_add_1_)
            {
                this.unlisted.add(iunlistedproperty);
            }

            return this;
        }

        public BlockStateContainer build()
        {
            IProperty<?>[] iproperty = new IProperty[this.listed.size()];
            iproperty = (IProperty[])this.listed.toArray(iproperty);

            if (this.unlisted.size() == 0)
            {
                return new BlockStateContainer(this.block, iproperty);
            }
            else
            {
                IUnlistedProperty<?>[] iunlistedproperty = new IUnlistedProperty[this.unlisted.size()];
                iunlistedproperty = (IUnlistedProperty[])this.unlisted.toArray(iunlistedproperty);
                return (BlockStateContainer)Reflector.newInstance(Reflector.ExtendedBlockState_Constructor, new Object[] {this.block, iproperty, iunlistedproperty});
            }
        }
    }

    static class StateImplementation extends BlockStateBase
    {
        private final Block block;
        private final ImmutableMap < IProperty<?>, Comparable<? >> properties;
        private ImmutableTable < IProperty<?>, Comparable<?>, IBlockState > propertyValueTable;

        private StateImplementation(Block blockIn, ImmutableMap < IProperty<?>, Comparable<? >> propertiesIn)
        {
            this.block = blockIn;
            this.properties = propertiesIn;
        }

        protected StateImplementation(Block p_i7_1_, ImmutableMap < IProperty<?>, Comparable<? >> p_i7_2_, ImmutableTable < IProperty<?>, Comparable<?>, IBlockState > p_i7_3_)
        {
            this.block = p_i7_1_;
            this.properties = p_i7_2_;
            this.propertyValueTable = p_i7_3_;
        }

        public Collection < IProperty<? >> getPropertyNames()
        {
            return Collections. < IProperty<? >> unmodifiableCollection(this.properties.keySet());
        }

        public <T extends Comparable<T>> T getValue(IProperty<T> property)
        {
            Comparable<?> comparable = (Comparable)this.properties.get(property);

            if (comparable == null)
            {
                throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.block.getBlockState());
            }
            else
            {
                return (T)((Comparable)property.getValueClass().cast(comparable));
            }
        }

        public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value)
        {
            Comparable<?> comparable = (Comparable)this.properties.get(property);

            if (comparable == null)
            {
                throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.block.getBlockState());
            }
            else if (comparable == value)
            {
                return this;
            }
            else
            {
                IBlockState iblockstate = (IBlockState)this.propertyValueTable.get(property, value);

                if (iblockstate == null)
                {
                    throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.REGISTRY.getNameForObject(this.block) + ", it is not an allowed value");
                }
                else
                {
                    return iblockstate;
                }
            }
        }

        public ImmutableMap < IProperty<?>, Comparable<? >> getProperties()
        {
            return this.properties;
        }

        public Block getBlock()
        {
            return this.block;
        }

        public boolean equals(Object p_equals_1_)
        {
            return this == p_equals_1_;
        }

        public int hashCode()
        {
            return this.properties.hashCode();
        }

        public void buildPropertyValueTable(Map < Map < IProperty<?>, Comparable<? >> , BlockStateContainer.StateImplementation > map)
        {
            if (this.propertyValueTable != null)
            {
                throw new IllegalStateException();
            }
            else
            {
                Table < IProperty<?>, Comparable<?>, IBlockState > table = HashBasedTable. < IProperty<?>, Comparable<?>, IBlockState > create();

                for (Entry < IProperty<?>, Comparable<? >> entry : this.properties.entrySet())
                {
                    IProperty<?> iproperty = (IProperty)entry.getKey();

                    for (Comparable<?> comparable : iproperty.getAllowedValues())
                    {
                        if (comparable != entry.getValue())
                        {
                            table.put(iproperty, comparable, map.get(this.getPropertiesWithValue(iproperty, comparable)));
                        }
                    }
                }

                this.propertyValueTable = ImmutableTable. < IProperty<?>, Comparable<?>, IBlockState > copyOf(table);
            }
        }

        private Map < IProperty<?>, Comparable<? >> getPropertiesWithValue(IProperty<?> property, Comparable<?> value)
        {
            Map < IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newHashMap(this.properties);
            map.put(property, value);
            return map;
        }

        public Material getMaterial()
        {
            return this.block.getMaterial(this);
        }

        public boolean isFullBlock()
        {
            return this.block.isFullBlock(this);
        }

        public boolean func_189884_a(Entity p_189884_1_)
        {
            return this.block.func_189872_a(this, p_189884_1_);
        }

        public int getLightOpacity()
        {
            return this.block.getLightOpacity(this);
        }

        public int getLightValue()
        {
            return this.block.getLightValue(this);
        }

        public boolean isTranslucent()
        {
            return this.block.isTranslucent(this);
        }

        public boolean useNeighborBrightness()
        {
            return this.block.getUseNeighborBrightness(this);
        }

        public MapColor getMapColor()
        {
            return this.block.getMapColor(this);
        }

        public IBlockState withRotation(Rotation rot)
        {
            return this.block.withRotation(this, rot);
        }

        public IBlockState withMirror(Mirror mirrorIn)
        {
            return this.block.withMirror(this, mirrorIn);
        }

        public boolean isFullCube()
        {
            return this.block.isFullCube(this);
        }

        public EnumBlockRenderType getRenderType()
        {
            return this.block.getRenderType(this);
        }

        public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos)
        {
            return this.block.getPackedLightmapCoords(this, source, pos);
        }

        public float getAmbientOcclusionLightValue()
        {
            return this.block.getAmbientOcclusionLightValue(this);
        }

        public boolean isBlockNormalCube()
        {
            return this.block.isBlockNormalCube(this);
        }

        public boolean isNormalCube()
        {
            return this.block.isNormalCube(this);
        }

        public boolean canProvidePower()
        {
            return this.block.canProvidePower(this);
        }

        public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
        {
            return this.block.getWeakPower(this, blockAccess, pos, side);
        }

        public boolean hasComparatorInputOverride()
        {
            return this.block.hasComparatorInputOverride(this);
        }

        public int getComparatorInputOverride(World worldIn, BlockPos pos)
        {
            return this.block.getComparatorInputOverride(this, worldIn, pos);
        }

        public float getBlockHardness(World worldIn, BlockPos pos)
        {
            return this.block.getBlockHardness(this, worldIn, pos);
        }

        public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos)
        {
            return this.block.getPlayerRelativeBlockHardness(this, player, worldIn, pos);
        }

        public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
        {
            return this.block.getStrongPower(this, blockAccess, pos, side);
        }

        public EnumPushReaction getMobilityFlag()
        {
            return this.block.getMobilityFlag(this);
        }

        public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos)
        {
            return this.block.getActualState(this, blockAccess, pos);
        }

        public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
        {
            return this.block.getSelectedBoundingBox(this, worldIn, pos);
        }

        public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing)
        {
            return this.block.shouldSideBeRendered(this, blockAccess, pos, facing);
        }

        public boolean isOpaqueCube()
        {
            return this.block.isOpaqueCube(this);
        }

        @Nullable
        public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos)
        {
            return this.block.getCollisionBoundingBox(this, worldIn, pos);
        }

        public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB p_185908_3_, List<AxisAlignedBB> p_185908_4_, @Nullable Entity p_185908_5_)
        {
            this.block.addCollisionBoxToList(this, worldIn, pos, p_185908_3_, p_185908_4_, p_185908_5_);
        }

        public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos)
        {
            Block.EnumOffsetType block$enumoffsettype = this.block.getOffsetType();

            if (block$enumoffsettype != Block.EnumOffsetType.NONE)
            {
                AxisAlignedBB axisalignedbb = this.block.getBoundingBox(this, blockAccess, pos);
                axisalignedbb = BlockModelUtils.getOffsetBoundingBox(axisalignedbb, block$enumoffsettype, pos);
                return axisalignedbb;
            }
            else
            {
                return this.block.getBoundingBox(this, blockAccess, pos);
            }
        }

        public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
        {
            return this.block.collisionRayTrace(this, worldIn, pos, start, end);
        }

        public boolean isFullyOpaque()
        {
            return this.block.isFullyOpaque(this);
        }

        public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param)
        {
            return this.block.eventReceived(this, worldIn, pos, id, param);
        }

        public void neighborChanged(World worldIn, BlockPos pos, Block p_189546_3_)
        {
            this.block.neighborChanged(this, worldIn, pos, p_189546_3_);
        }

        public ImmutableTable < IProperty<?>, Comparable<?>, IBlockState > getPropertyValueTable()
        {
            return this.propertyValueTable;
        }

        public int getLightOpacity(IBlockAccess p_getLightOpacity_1_, BlockPos p_getLightOpacity_2_)
        {
            return Reflector.callInt(this.block, Reflector.ForgeBlock_getLightOpacity, new Object[] {this, p_getLightOpacity_1_, p_getLightOpacity_2_});
        }

        public int getLightValue(IBlockAccess p_getLightValue_1_, BlockPos p_getLightValue_2_)
        {
            return Reflector.callInt(this.block, Reflector.ForgeBlock_getLightValue, new Object[] {this, p_getLightValue_1_, p_getLightValue_2_});
        }

        public boolean isSideSolid(IBlockAccess p_isSideSolid_1_, BlockPos p_isSideSolid_2_, EnumFacing p_isSideSolid_3_)
        {
            return Reflector.callBoolean(this.block, Reflector.ForgeBlock_isSideSolid, new Object[] {this, p_isSideSolid_1_, p_isSideSolid_2_, p_isSideSolid_3_});
        }

        public boolean doesSideBlockRendering(IBlockAccess p_doesSideBlockRendering_1_, BlockPos p_doesSideBlockRendering_2_, EnumFacing p_doesSideBlockRendering_3_)
        {
            return Reflector.callBoolean(this.block, Reflector.ForgeBlock_doesSideBlockRendering, new Object[] {this, p_doesSideBlockRendering_1_, p_doesSideBlockRendering_2_, p_doesSideBlockRendering_3_});
        }
    }
}
