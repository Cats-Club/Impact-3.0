package net.minecraft.stats;

import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.ITextComponent;

public class StatBasic extends net.minecraft.stats.StatBase
{
    public StatBasic(String statIdIn, ITextComponent statNameIn, IStatType typeIn)
    {
        super(statIdIn, statNameIn, typeIn);
    }

    public StatBasic(String statIdIn, ITextComponent statNameIn)
    {
        super(statIdIn, statNameIn);
    }

    /**
     * Register the stat into StatList.
     */
    public StatBase registerStat()
    {
        super.registerStat();
        StatList.BASIC_STATS.add(this);
        return this;
    }
}
