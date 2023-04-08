package me.zero.clarinet.mixin.mixins.minecraft.network;

import io.netty.channel.ChannelHandlerContext;
import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.network.EventPacketReceive;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Shadow private INetHandler packetListener;

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V"), cancellable = true)
    public void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo ci) {

        EventPacketReceive event = new EventPacketReceive(p_channelRead0_2_);
        EventManager.call(event);

        if (!event.isCancelled())
            event.getPacket().processPacket(packetListener);

        ci.cancel();

    }

}
