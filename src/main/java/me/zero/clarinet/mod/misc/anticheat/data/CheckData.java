package me.zero.clarinet.mod.misc.anticheat.data;

import net.minecraft.entity.player.EntityPlayer;

public class CheckData {

    private EntityPlayer player;

    public CheckData(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }
}
