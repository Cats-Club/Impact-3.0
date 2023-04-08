package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

public class BiomeCache
{
    /** Reference to the WorldChunkManager */
    private final BiomeProvider chunkManager;

    /** The last time this BiomeCache was cleaned, in milliseconds. */
    private long lastCleanupTime;
    private final Long2ObjectMap<Block> cacheMap = new Long2ObjectOpenHashMap(4096);
    private final List<Block> cache = Lists.<Block>newArrayList();

    public BiomeCache(BiomeProvider chunkManagerIn)
    {
        this.chunkManager = chunkManagerIn;
    }

    /**
     * Returns a biome cache block at location specified.
     */
    public Block getBiomeCacheBlock(int x, int z)
    {
        x = x >> 4;
        z = z >> 4;
        long i = (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
        Block biomecache$block = (Block)this.cacheMap.get(i);

        if (biomecache$block == null)
        {
            biomecache$block = new Block(x, z);
            this.cacheMap.put(i, biomecache$block);
            this.cache.add(biomecache$block);
        }

        biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
        return biomecache$block;
    }

    public net.minecraft.world.biome.Biome getBiome(int x, int z, net.minecraft.world.biome.Biome defaultValue)
    {
        net.minecraft.world.biome.Biome biome = this.getBiomeCacheBlock(x, z).getBiome(x, z);
        return biome == null ? defaultValue : biome;
    }

    /**
     * Removes BiomeCacheBlocks from this cache that haven't been accessed in at least 30 seconds.
     */
    public void cleanupCache()
    {
        long i = MinecraftServer.getCurrentTimeMillis();
        long j = i - this.lastCleanupTime;

        if (j > 7500L || j < 0L)
        {
            this.lastCleanupTime = i;

            for (int k = 0; k < this.cache.size(); ++k)
            {
                Block biomecache$block = (Block)this.cache.get(k);
                long l = i - biomecache$block.lastAccessTime;

                if (l > 30000L || l < 0L)
                {
                    this.cache.remove(k--);
                    long i1 = (long)biomecache$block.xPosition & 4294967295L | ((long)biomecache$block.zPosition & 4294967295L) << 32;
                    this.cacheMap.remove(i1);
                }
            }
        }
    }

    /**
     * Returns the array of cached biome types in the BiomeCacheBlock at the given location.
     */
    public net.minecraft.world.biome.Biome[] getCachedBiomes(int x, int z)
    {
        return this.getBiomeCacheBlock(x, z).biomes;
    }

    public class Block
    {
        public net.minecraft.world.biome.Biome[] biomes = new net.minecraft.world.biome.Biome[256];
        public int xPosition;
        public int zPosition;
        public long lastAccessTime;

        public Block(int x, int z)
        {
            this.xPosition = x;
            this.zPosition = z;
            BiomeCache.this.chunkManager.getBiomes(this.biomes, x << 4, z << 4, 16, 16, false);
        }

        public Biome getBiome(int x, int z)
        {
            return this.biomes[x & 15 | (z & 15) << 4];
        }
    }
}
