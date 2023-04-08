package me.zero.clarinet.mod.player.retard;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;
import me.zero.clarinet.event.api.types.Priority;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.player.Retard;
import me.zero.clarinet.util.MathUtils;

public class RetardSpinny extends ModMode<Retard> {

    public RetardSpinny(Retard parent) {
        super(parent, "Spinny");
    }

    @EventTarget(Priority.HIGHEST)
    public void onMotionUpdate(EventMotionUpdate event) {
        if (event.type != EventType.PRE) {
            return;
        }
        if (parent.yaw.getValue()) {
            event.yaw = MathUtils.randInt(-180, 180);
        }
        if (parent.pitch.getValue()) {
            event.pitch = MathUtils.randInt(-180, 180);
        }
    }
}
