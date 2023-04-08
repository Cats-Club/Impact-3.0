package me.zero.clarinet.mod.misc.anticheat;

import me.zero.clarinet.mod.misc.anticheat.check.Check;
import me.zero.clarinet.mod.misc.anticheat.check.checks.FastLadderCheck;
import me.zero.clarinet.mod.misc.anticheat.check.checks.ReachCheck;
import me.zero.clarinet.mod.misc.anticheat.check.checks.SpeedCheck;
import me.zero.clarinet.mod.misc.anticheat.data.CheckData;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class AntiCheatHandler {

    private static List<Check<?>> checks = new ArrayList<>();

    static {
        checks.add(new SpeedCheck());
        checks.add(new FastLadderCheck());
        checks.add(new ReachCheck());
    }

    @SuppressWarnings("unchecked")
    public static <T extends CheckData> boolean execute(T data) {
        for (Check<?> check : checks) {
            if (check.getType().equals(data.getClass())) {
                Check<T> match = (Check<T>) check;
                if (match.check(data)) {
                    ClientUtils.message(data.getPlayer().getName() + " has violated " + TextFormatting.BLUE + match.getID() + TextFormatting.WHITE + " (" + data.getPlayer().ticksExisted + ")");
                    return true;
                }
            }
        }
        return false;
    }
}
