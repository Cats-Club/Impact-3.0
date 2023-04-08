package net.minecraftforge.common.model;

import com.google.common.base.Optional;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.TRSRTransformation;

public interface IModelState
{
    Optional<TRSRTransformation> apply(Optional <? extends IModelPart> var1);
}
