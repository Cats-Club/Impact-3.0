package me.zero.clarinet.mixin.mixins.minecraft.client.gui;

import me.zero.clarinet.util.ServerHook;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(GuiScreenServerList.class)
public class MixinGuiScreenServerList {

    @Shadow @Final private GuiScreen lastScreen;

    @Shadow @Final private ServerData serverData;

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;getText()Ljava/lang/String;", shift = At.Shift.AFTER))
    protected void actionPerformed(GuiButton button, CallbackInfo ci) {
        ServerHook.updateLastServerFromDirectConnect((GuiMultiplayer) lastScreen, serverData);
    }

}
