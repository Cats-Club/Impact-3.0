package me.zero.clarinet.mixin.mixins.minecraft.entity.player;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(EntityPlayer.class)
public interface IEntityPlayer {

    @Accessor("sleeping")
    boolean isPlayerSleeping();

    @Accessor("sleeping")
    void setPlayerSleeping(boolean sleeping);

    @Accessor("sleepTimer")
    int getSleepTimer();

    @Accessor("sleepTimer")
    void setSleepTimer(int timer);

}
