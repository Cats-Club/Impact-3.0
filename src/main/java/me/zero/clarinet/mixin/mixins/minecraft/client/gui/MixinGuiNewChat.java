package me.zero.clarinet.mixin.mixins.minecraft.client.gui;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.network.EventReceiveChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"))
    public void printChatMessageWithOptionalDeletion(ITextComponent chatComponent, int chatLineId, CallbackInfo ci) {
        EventManager.call(new EventReceiveChat(chatComponent));
    }

}
