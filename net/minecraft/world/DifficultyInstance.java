package net.minecraft.world;

import javax.annotation.concurrent.Immutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;

@Immutable
public class DifficultyInstance
{
    private final net.minecraft.world.EnumDifficulty worldDifficulty;
    private final float additionalDifficulty;

    public DifficultyInstance(net.minecraft.world.EnumDifficulty worldDifficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor)
    {
        this.worldDifficulty = worldDifficulty;
        this.additionalDifficulty = this.calculateAdditionalDifficulty(worldDifficulty, worldTime, chunkInhabitedTime, moonPhaseFactor);
    }

    public float getAdditionalDifficulty()
    {
        return this.additionalDifficulty;
    }

    public boolean func_190083_c()
    {
        return this.additionalDifficulty >= (float) net.minecraft.world.EnumDifficulty.HARD.ordinal();
    }

    public float getClampedAdditionalDifficulty()
    {
        return this.additionalDifficulty < 2.0F ? 0.0F : (this.additionalDifficulty > 4.0F ? 1.0F : (this.additionalDifficulty - 2.0F) / 2.0F);
    }

    private float calculateAdditionalDifficulty(net.minecraft.world.EnumDifficulty difficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor)
    {
        if (difficulty == net.minecraft.world.EnumDifficulty.PEACEFUL)
        {
            return 0.0F;
        }
        else
        {
            boolean flag = difficulty == net.minecraft.world.EnumDifficulty.HARD;
            float f = 0.75F;
            float f1 = MathHelper.clamp_float(((float)worldTime + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
            f = f + f1;
            float f2 = 0.0F;
            f2 = f2 + MathHelper.clamp_float((float)chunkInhabitedTime / 3600000.0F, 0.0F, 1.0F) * (flag ? 1.0F : 0.75F);
            f2 = f2 + MathHelper.clamp_float(moonPhaseFactor * 0.25F, 0.0F, f1);

            if (difficulty == EnumDifficulty.EASY)
            {
                f2 *= 0.5F;
            }

            f = f + f2;
            return (float)difficulty.getDifficultyId() * f;
        }
    }
}
