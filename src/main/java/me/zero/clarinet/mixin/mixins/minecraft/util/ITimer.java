package me.zero.clarinet.mixin.mixins.minecraft.util;

import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(Timer.class)
public interface ITimer {

    @Accessor("tickLength")
    float getTickLength();

    @Accessor("tickLength")
    void setTickLength(float tickLength);

}
