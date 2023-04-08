package me.zero.clarinet.mixin.mixins.minecraft.network.play.client;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(CPacketPlayer.class)
public interface ICPacketPlayer {

    @Accessor("onGround")
    void setOnGround(boolean onGround);

}
