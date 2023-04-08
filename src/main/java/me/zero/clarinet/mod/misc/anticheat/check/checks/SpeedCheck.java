package me.zero.clarinet.mod.misc.anticheat.check.checks;

import me.zero.clarinet.mod.misc.anticheat.check.Check;
import me.zero.clarinet.mod.misc.anticheat.data.types.MovementData;
import me.zero.clarinet.util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.util.math.Vec3d;

public class SpeedCheck extends Check<MovementData> {

    public SpeedCheck() {
        super("MOVEMENT_SPEED");
    }

    @Override
    public boolean check(MovementData data) {
        Vec3d from = data.getFrom();
        Vec3d to = data.getTo();

        double xDistance = (to.x - from.x);
        double zDistance = (to.z - from.z);

        double hDistance = (xDistance * xDistance) + (zDistance * zDistance);

        double MAX_HDISTANCE = 0.3747467140677868;

        if (data.getPlayer().capabilities.isFlying) {
            MAX_HDISTANCE = 1.3D;
        }

        Block block = BlockUtils.getBlockUnderEntity(data.getPlayer(), 1);

        if (block instanceof BlockIce || block instanceof BlockPackedIce) {
            MAX_HDISTANCE = 0.9;
        }

        if (data.getPlayer().isInWater()) {
            MAX_HDISTANCE = 0.02;
        }

        if (hDistance > MAX_HDISTANCE) {
            return true;
        }

        return false;
    }
}
