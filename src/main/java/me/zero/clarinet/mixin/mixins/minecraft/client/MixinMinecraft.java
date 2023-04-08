package me.zero.clarinet.mixin.mixins.minecraft.client;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.game.*;
import me.zero.clarinet.ui.screen.override.GuiOverrideHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow protected abstract boolean processKeyF3(int auxKey);

    @Inject(method = "getVersion", at = @At("HEAD"), cancellable = true)
    public void isDisabled(CallbackInfoReturnable<String> cir) {
        if (Impact.getInstance().isDisabled()) {
            cir.setReturnValue("1.10.2");
        }
    }

    @ModifyVariable(method = "displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", at = @At("HEAD"), index = 1, argsOnly = true)
    public GuiScreen displayGuiScreen(GuiScreen value) {
        if (!Impact.getInstance().isDisabled()) {
            value = GuiOverrideHandler.handle(value);
        }
        return value;
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    public void runGameLoop(CallbackInfo ci) {
        EventManager.call(new EventLoop());
    }

    @Inject(method = "clickMouse", at = @At("HEAD"))
    public void clickMouse(CallbackInfo ci) {
        EventManager.call(new EventLeftClick());
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"))
    public void rightClickMouse(CallbackInfo ci) {
        EventManager.call(new EventRightClick());
    }

    @Inject(method = "middleClickMouse", at = @At("HEAD"))
    public void middleClickMouse(CallbackInfo ci) {
        EventManager.call(new EventMiddleClick());
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void runTick(CallbackInfo ci) {
        EventManager.call(new EventTick());
    }

    // this is some wack ass shit impact, you happy with yourselves?
    int i = -1;

    @ModifyVariable(method = "runTickKeyboard", at = @At("STORE"), ordinal = 0)
    public int runTickKeyboard(int i) {
        return this.i = i;
    }

    @ModifyVariable(method = "runTickKeyboard", at = @At("STORE"), ordinal = 0)
    public boolean runTickKeyboard2(boolean value) {
        if (value)
            EventManager.call(new EventKey(i));
        return value;
    }

}
