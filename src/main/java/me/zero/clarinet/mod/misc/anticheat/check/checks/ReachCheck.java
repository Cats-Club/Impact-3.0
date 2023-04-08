package me.zero.clarinet.mod.misc.anticheat.check.checks;

import me.zero.clarinet.mod.misc.anticheat.check.Check;
import me.zero.clarinet.mod.misc.anticheat.data.types.CombatData;

public class ReachCheck extends Check<CombatData> {

    public ReachCheck() {
        super("FIGHT_REACH");
    }

    @Override
    public boolean check(CombatData data) {
        double dist = data.getPlayer().getDistance(data.getTarget());
        if (dist >= 4.25) {
            return true;
        }
        return false;
    }
}
