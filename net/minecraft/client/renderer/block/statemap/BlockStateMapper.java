package net.minecraft.client.renderer.block.statemap;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.util.ResourceLocation;

public class BlockStateMapper
{
    private final Map<Block, net.minecraft.client.renderer.block.statemap.IStateMapper> blockStateMap = Maps.<Block, net.minecraft.client.renderer.block.statemap.IStateMapper>newIdentityHashMap();
    private final Set<Block> setBuiltInBlocks = Sets.<Block>newIdentityHashSet();

    public void registerBlockStateMapper(Block blockIn, net.minecraft.client.renderer.block.statemap.IStateMapper stateMapper)
    {
        this.blockStateMap.put(blockIn, stateMapper);
    }

    public void registerBuiltInBlocks(Block... blockIn)
    {
        Collections.addAll(this.setBuiltInBlocks, blockIn);
    }

    public Map<IBlockState, ModelResourceLocation> putAllStateModelLocations()
    {
        Map<IBlockState, ModelResourceLocation> map = Maps.<IBlockState, ModelResourceLocation>newIdentityHashMap();

        for (Block block : Block.REGISTRY)
        {
            map.putAll(this.getVariants(block));
        }

        return map;
    }

    public Set<ResourceLocation> getBlockstateLocations(Block blockIn)
    {
        if (this.setBuiltInBlocks.contains(blockIn))
        {
            return Collections.<ResourceLocation>emptySet();
        }
        else
        {
            net.minecraft.client.renderer.block.statemap.IStateMapper istatemapper = (net.minecraft.client.renderer.block.statemap.IStateMapper)this.blockStateMap.get(blockIn);

            if (istatemapper == null)
            {
                return Collections.<ResourceLocation>singleton(Block.REGISTRY.getNameForObject(blockIn));
            }
            else
            {
                Set<ResourceLocation> set = Sets.<ResourceLocation>newHashSet();

                for (ModelResourceLocation modelresourcelocation : istatemapper.putStateModelLocations(blockIn).values())
                {
                    set.add(new ResourceLocation(modelresourcelocation.getResourceDomain(), modelresourcelocation.getResourcePath()));
                }

                return set;
            }
        }
    }

    public Map<IBlockState, ModelResourceLocation> getVariants(Block blockIn)
    {
        return this.setBuiltInBlocks.contains(blockIn) ? Collections.<IBlockState, ModelResourceLocation>emptyMap() : ((IStateMapper)Objects.firstNonNull(this.blockStateMap.get(blockIn), new DefaultStateMapper())).putStateModelLocations(blockIn);
    }
}
