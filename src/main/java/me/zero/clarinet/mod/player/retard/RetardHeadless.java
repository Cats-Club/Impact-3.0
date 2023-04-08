package me.zero.clarinet.mod.player.retard;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;
import me.zero.clarinet.event.api.types.Priority;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.player.Retard;

public class RetardHeadless extends ModMode<Retard> {

    public RetardHeadless(Retard parent) {
        super(parent, "Headless");
    }

    @EventTarget(Priority.HIGHEST)
    public void onMotionUpdate(EventMotionUpdate event) {
        if (event.type != EventType.PRE) {
            return;
        }
        event.pitch = 180F;
    }
}
