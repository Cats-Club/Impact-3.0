package me.zero.clarinet.mixin.mixins.minecraft.client.multiplayer;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(PlayerControllerMP.class)
public interface IPlayerControllerMP {

    @Accessor("curBlockDamageMP")
    float getCurBlockDamageMP();

    @Accessor("curBlockDamageMP")
    void setCurBlockDamageMP(float curBlockDamageMP);

}
