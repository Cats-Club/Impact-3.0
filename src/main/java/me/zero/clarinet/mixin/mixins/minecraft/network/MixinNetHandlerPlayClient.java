package me.zero.clarinet.mixin.mixins.minecraft.network;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.network.EventPacketSend;
import me.zero.clarinet.event.player.EventPlayerDeath;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    boolean doReturnSendPacket = false;

    @ModifyVariable(method = "sendPacket", at = @At("HEAD"), index = 1, argsOnly = true)
    public Packet<?> sendPacket(Packet<?> value) {
        EventPacketSend event = new EventPacketSend(value);
        EventManager.call(event);
        if (event.isCancelled()) {
            doReturnSendPacket = true;
            return null;
        }
        return event.getPacket();
    }

    @Inject(method = "sendPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;sendPacket(Lnet/minecraft/network/Packet;)V"), cancellable = true)
    public void sendPacketCancel(Packet<?> packetIn, CallbackInfo ci) {
        if (doReturnSendPacket)
            ci.cancel();

        doReturnSendPacket = false;
    }

    @Inject(method = "handleCombatEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"), cancellable = true)
    public void handleCombatEvent(SPacketCombatEvent packetIn, CallbackInfo ci) {
        EventPlayerDeath event = new EventPlayerDeath();
        EventManager.call(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}
