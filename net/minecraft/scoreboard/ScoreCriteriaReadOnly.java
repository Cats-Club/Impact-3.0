package net.minecraft.scoreboard;

import net.minecraft.scoreboard.ScoreCriteria;

public class ScoreCriteriaReadOnly extends ScoreCriteria
{
    public ScoreCriteriaReadOnly(String name)
    {
        super(name);
    }

    public boolean isReadOnly()
    {
        return true;
    }
}
