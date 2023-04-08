package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import javax.annotation.Nullable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.multipart.ICondition;

public class ConditionAnd implements net.minecraft.client.renderer.block.model.multipart.ICondition
{
    private final Iterable<net.minecraft.client.renderer.block.model.multipart.ICondition> conditions;

    public ConditionAnd(Iterable<net.minecraft.client.renderer.block.model.multipart.ICondition> conditionsIn)
    {
        this.conditions = conditionsIn;
    }

    public Predicate<IBlockState> getPredicate(final BlockStateContainer blockState)
    {
        return Predicates.and(Iterables.transform(this.conditions, new Function<net.minecraft.client.renderer.block.model.multipart.ICondition, Predicate<IBlockState>>()
        {
            @Nullable
            public Predicate<IBlockState> apply(@Nullable ICondition p_apply_1_)
            {
                return p_apply_1_ == null ? null : p_apply_1_.getPredicate(blockState);
            }
        }));
    }
}
