package net.minecraft.client.renderer.block.statemap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

public class DefaultStateMapper extends StateMapperBase
{
    protected ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        return new ModelResourceLocation((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock()), this.getPropertyString(state.getProperties()));
    }
}
