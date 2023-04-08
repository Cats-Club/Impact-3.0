package me.zero.clarinet.mixin.mixins.minecraft.client.gui;

import me.zero.clarinet.util.ServerHook;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer {

    @Shadow private ServerSelectionList serverListSelector;

    @Inject(method = "connectToSelected", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ServerSelectionList;getSelected()I", ordinal = 1, shift = At.Shift.AFTER))
    public void connectToSelected(CallbackInfo ci) {
        ServerHook.updateLastServerFromServerlist(
                // Doogie13
                serverListSelector.getSelected() < 0 ? null : serverListSelector.getListEntry(this.serverListSelector.getSelected()), (GuiMultiplayer) (Object) this
        );
    }

}
