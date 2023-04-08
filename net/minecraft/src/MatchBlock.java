package net.minecraft.src;

import net.minecraft.block.state.BlockStateBase;

public class MatchBlock
{
    private int blockId = -1;
    private int[] metadatas = null;

    public MatchBlock(int p_i52_1_)
    {
        this.blockId = p_i52_1_;
    }

    public MatchBlock(int p_i53_1_, int p_i53_2_)
    {
        this.blockId = p_i53_1_;

        if (p_i53_2_ >= 0 && p_i53_2_ <= 15)
        {
            this.metadatas = new int[] {p_i53_2_};
        }
    }

    public MatchBlock(int p_i54_1_, int[] p_i54_2_)
    {
        this.blockId = p_i54_1_;
        this.metadatas = p_i54_2_;
    }

    public int getBlockId()
    {
        return this.blockId;
    }

    public int[] getMetadatas()
    {
        return this.metadatas;
    }

    public boolean matches(BlockStateBase p_matches_1_)
    {
        return p_matches_1_.getBlockId() != this.blockId ? false : Matches.metadata(p_matches_1_.getMetadata(), this.metadatas);
    }

    public boolean matches(int p_matches_1_, int p_matches_2_)
    {
        return p_matches_1_ != this.blockId ? false : Matches.metadata(p_matches_2_, this.metadatas);
    }

    public void addMetadata(int p_addMetadata_1_)
    {
        if (this.metadatas != null)
        {
            if (p_addMetadata_1_ >= 0 && p_addMetadata_1_ <= 15)
            {
                for (int i = 0; i < this.metadatas.length; ++i)
                {
                    if (this.metadatas[i] == p_addMetadata_1_)
                    {
                        return;
                    }
                }

                this.metadatas = Config.addIntToArray(this.metadatas, p_addMetadata_1_);
            }
        }
    }

    public String toString()
    {
        return "" + this.blockId + ":" + Config.arrayToString(this.metadatas);
    }
}
