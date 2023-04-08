package net.minecraft.src;

import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkUtils
{
    public static boolean hasEntities(Chunk p_hasEntities_0_, BlockPos p_hasEntities_1_)
    {
        ClassInheritanceMultiMap<Entity> classinheritancemultimap = p_hasEntities_0_.getEntityLists()[p_hasEntities_1_.getY() / 16];
        return !classinheritancemultimap.isEmpty();
    }
}
