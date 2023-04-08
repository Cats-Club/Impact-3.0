package net.minecraft.world.gen.structure.template;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class PlacementSettings
{
    private Mirror mirror = Mirror.NONE;
    private Rotation rotation = Rotation.NONE;
    private boolean ignoreEntities = false;
    @Nullable

    /**
     * the type of block in the world that will get replaced by the structure
     */
    private Block replacedBlock;
    @Nullable

    /** the chunk the structure is within */
    private ChunkPos chunk;
    @Nullable

    /** the bounds the structure is contained within */
    private StructureBoundingBox boundingBox;
    private boolean ignoreStructureBlock = true;
    private float field_189951_h = 1.0F;
    @Nullable
    private Random field_189952_i;
    @Nullable
    private Long field_189953_j;

    public PlacementSettings copy()
    {
        PlacementSettings placementsettings = new PlacementSettings();
        placementsettings.mirror = this.mirror;
        placementsettings.rotation = this.rotation;
        placementsettings.ignoreEntities = this.ignoreEntities;
        placementsettings.replacedBlock = this.replacedBlock;
        placementsettings.chunk = this.chunk;
        placementsettings.boundingBox = this.boundingBox;
        placementsettings.ignoreStructureBlock = this.ignoreStructureBlock;
        placementsettings.field_189951_h = this.field_189951_h;
        placementsettings.field_189952_i = this.field_189952_i;
        placementsettings.field_189953_j = this.field_189953_j;
        return placementsettings;
    }

    public PlacementSettings setMirror(Mirror mirrorIn)
    {
        this.mirror = mirrorIn;
        return this;
    }

    public PlacementSettings setRotation(Rotation rotationIn)
    {
        this.rotation = rotationIn;
        return this;
    }

    public PlacementSettings setIgnoreEntities(boolean ignoreEntitiesIn)
    {
        this.ignoreEntities = ignoreEntitiesIn;
        return this;
    }

    public PlacementSettings setReplacedBlock(Block replacedBlockIn)
    {
        this.replacedBlock = replacedBlockIn;
        return this;
    }

    public PlacementSettings setChunk(ChunkPos chunkPosIn)
    {
        this.chunk = chunkPosIn;
        return this;
    }

    public PlacementSettings setBoundingBox(StructureBoundingBox boundingBoxIn)
    {
        this.boundingBox = boundingBoxIn;
        return this;
    }

    public PlacementSettings func_189949_a(@Nullable Long p_189949_1_)
    {
        this.field_189953_j = p_189949_1_;
        return this;
    }

    public PlacementSettings func_189950_a(@Nullable Random p_189950_1_)
    {
        this.field_189952_i = p_189950_1_;
        return this;
    }

    public PlacementSettings func_189946_a(float p_189946_1_)
    {
        this.field_189951_h = p_189946_1_;
        return this;
    }

    public Mirror getMirror()
    {
        return this.mirror;
    }

    public PlacementSettings setIgnoreStructureBlock(boolean ignoreStructureBlockIn)
    {
        this.ignoreStructureBlock = ignoreStructureBlockIn;
        return this;
    }

    public Rotation getRotation()
    {
        return this.rotation;
    }

    public Random func_189947_a(@Nullable BlockPos p_189947_1_)
    {
        if (this.field_189952_i != null)
        {
            return this.field_189952_i;
        }
        else if (this.field_189953_j != null)
        {
            return this.field_189953_j.longValue() == 0L ? new Random(System.currentTimeMillis()) : new Random(this.field_189953_j.longValue());
        }
        else if (p_189947_1_ == null)
        {
            return new Random(System.currentTimeMillis());
        }
        else
        {
            int i = p_189947_1_.getX();
            int j = p_189947_1_.getZ();
            return new Random((long)(i * i * 4987142 + i * 5947611) + (long)(j * j) * 4392871L + (long)(j * 389711) ^ 987234911L);
        }
    }

    public float func_189948_f()
    {
        return this.field_189951_h;
    }

    public boolean getIgnoreEntities()
    {
        return this.ignoreEntities;
    }

    @Nullable
    public Block getReplacedBlock()
    {
        return this.replacedBlock;
    }

    @Nullable
    public StructureBoundingBox getBoundingBox()
    {
        if (this.boundingBox == null && this.chunk != null)
        {
            this.setBoundingBoxFromChunk();
        }

        return this.boundingBox;
    }

    public boolean getIgnoreStructureBlock()
    {
        return this.ignoreStructureBlock;
    }

    void setBoundingBoxFromChunk()
    {
        this.boundingBox = this.getBoundingBoxFromChunk(this.chunk);
    }

    @Nullable
    private StructureBoundingBox getBoundingBoxFromChunk(@Nullable ChunkPos pos)
    {
        if (pos == null)
        {
            return null;
        }
        else
        {
            int i = pos.chunkXPos * 16;
            int j = pos.chunkZPos * 16;
            return new StructureBoundingBox(i, 0, j, i + 16 - 1, 255, j + 16 - 1);
        }
    }
}
