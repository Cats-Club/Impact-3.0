package me.zero.clarinet.mod.render;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.network.EventPacketReceive;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.server.SPacketSpawnObject;
import org.lwjgl.input.Keyboard;

public class NoRender extends Mod {

    public NoRender() {
        super("NoRender", "Doesn't render Item Entities", Keyboard.KEY_NONE, Category.RENDER);
    }

    @Override
    public void onEnable() {
        if(mc.world != null){
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityItem) {
                    mc.world.removeEntity(entity);
                }
            }
        }else{
            this.toggle();
        }
    }

    @EventTarget
    public void onPacketReceiveTick(EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            event.setCancelled(true);
        }
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                EntityItem.setRenderDistanceWeight(0.0D);
            }
        }
    }

    @Override
    public void onDisable() {
        if(mc.world != null) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityItem) {
                    EntityItem.setRenderDistanceWeight(1.0D);
                }
            }
        }
    }
}
