package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecartMobSpawner;

public class RenderMinecartMobSpawner extends RenderMinecart<EntityMinecartMobSpawner>
{
    public RenderMinecartMobSpawner(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    protected void renderCartContents(EntityMinecartMobSpawner p_188319_1_, float partialTicks, IBlockState p_188319_3_)
    {
        super.renderCartContents(p_188319_1_, partialTicks, p_188319_3_);
    }
}
