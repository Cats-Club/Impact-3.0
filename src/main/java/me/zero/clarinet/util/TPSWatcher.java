package me.zero.clarinet.util;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.network.EventPacketReceive;
import net.minecraft.network.play.server.SPacketTimeUpdate;

import java.util.ArrayDeque;

public final class TPSWatcher {

    public static TPSWatcher INSTANCE;

    private static final int BUFFER_SIZE = 20;

    private ArrayDeque<Float> tpsBuffer = new ArrayDeque<>(BUFFER_SIZE);

    private float lastTPS;

    private long lastMS = 0L;

    public TPSWatcher() {
        INSTANCE = this;
    }

    public float getTPS() {
        return lastTPS;
    }

    @EventTarget
    public void onPacketReceive(EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            if (lastMS != 0L) {
                if (tpsBuffer.size() > BUFFER_SIZE){
                    tpsBuffer.poll();
                }
                tpsBuffer.add(20.0F * (1000.0F / (System.currentTimeMillis() - lastMS)));

                float total = 0.0F;
                for (Float f : tpsBuffer) {
                    total += Math.max(0.0F, Math.min(20.0F, f));
                }
                total /= tpsBuffer.size();
                lastTPS = total;
            }
            lastMS = System.currentTimeMillis();
        }
    }
}
