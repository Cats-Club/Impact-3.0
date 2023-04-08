package me.zero.clarinet.mod.misc.anticheat.check.checks;

import me.zero.clarinet.mod.misc.anticheat.check.Check;
import me.zero.clarinet.mod.misc.anticheat.data.types.MovementData;
import net.minecraft.util.math.Vec3d;

public class FastLadderCheck extends Check<MovementData> {

    public FastLadderCheck() {
        super("FAST_LADDER");
    }

    @Override
    public boolean check(MovementData data) {
        Vec3d from = data.getFrom();
        Vec3d to = data.getTo();

        double yDistance = to.y - from.y;

        if (data.getPlayer().isOnLadder()) {
            if (yDistance > 0.2) {
                return true;
            }
        }

        return false;
    }
}
