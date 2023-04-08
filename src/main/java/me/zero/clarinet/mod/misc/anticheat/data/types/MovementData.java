package me.zero.clarinet.mod.misc.anticheat.data.types;

import me.zero.clarinet.mod.misc.anticheat.data.CheckData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class MovementData extends CheckData {

    private Vec3d from, to;

    public MovementData(EntityPlayer player) {
        super(player);
        this.to = new Vec3d(player.posX, player.posY, player.posZ);
        this.from = new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
    }

    public Vec3d getFrom() {
        return this.from;
    }

    public Vec3d getTo() {
        return this.to;
    }
}
