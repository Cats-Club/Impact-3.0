package me.zero.clarinet.mixin.mixins.minecraft.network.play.server;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(SPacketEntityVelocity.class)
public interface ISPacketEntityVelocity {

    @Accessor("motionX")
    void setMotionX(int motionX);

    @Accessor("motionY")
    void setMotionY(int motionY);

    @Accessor("motionZ")
    void setMotionZ(int motionZ);

}
