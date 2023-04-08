package me.zero.clarinet.mod.misc;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.misc.anticheat.AntiCheatHandler;
import me.zero.clarinet.mod.misc.anticheat.data.types.MovementData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class AntiCheat extends Mod {

    public AntiCheat() {
        super("AntiCheat", "Client-Sided AntiCheat", Keyboard.KEY_NONE, Category.MISC);
    }

    @EventTarget
    public void onTick(EventTick event) {
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) e;

                MovementData data = new MovementData(player);
                AntiCheatHandler.execute(data);
            }
        }
    }
}
