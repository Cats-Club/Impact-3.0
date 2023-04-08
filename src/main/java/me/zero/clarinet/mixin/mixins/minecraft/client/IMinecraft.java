package me.zero.clarinet.mixin.mixins.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(Minecraft.class)
public interface IMinecraft {

    @Accessor("session")
    Session getSession();

    @Accessor("session")
    void setSession(Session session);

    @Accessor("timer")
    Timer getTimer();

    @Invoker void callClickMouse();

    @Invoker void callRightClickMouse();

    @Accessor("rightClickDelayTimer")
    int getRightClickDelayTimer();

    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimer(int rightClickDelayTimer);

}
