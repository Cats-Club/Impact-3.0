package me.zero.clarinet.plugin;

import me.zero.clarinet.util.Helper;

public class PluginWrapper implements Helper {

    public static void setPlayerMotionX(float amount) {
        setPlayerMotion(Axis.X, amount);
    }

    public static void setPlayerMotionY(float amount) {
        setPlayerMotion(Axis.Y, amount);
    }

    public static void setPlayerMotionZ(float amount) {
        setPlayerMotion(Axis.Z, amount);
    }

    public static void setPlayerMotion(Axis axis, float amount) {
        mc.player.motionX = (axis == Axis.X ? amount : mc.player.motionX);
        mc.player.motionY = (axis == Axis.Y ? amount : mc.player.motionY);
        mc.player.motionZ = (axis == Axis.Z ? amount : mc.player.motionZ);
    }

    public enum Axis {
        X, Y, Z
    }
}
