package net.minecraft.src;

public class RangeInt
{
    private int min;
    private int max;

    public RangeInt(int p_i69_1_, int p_i69_2_)
    {
        this.min = Math.min(p_i69_1_, p_i69_2_);
        this.max = Math.max(p_i69_1_, p_i69_2_);
    }

    public boolean isInRange(int p_isInRange_1_)
    {
        return p_isInRange_1_ < this.min ? false : p_isInRange_1_ <= this.max;
    }

    public int getMin()
    {
        return this.min;
    }

    public int getMax()
    {
        return this.max;
    }

    public String toString()
    {
        return "min: " + this.min + ", max: " + this.max;
    }
}
