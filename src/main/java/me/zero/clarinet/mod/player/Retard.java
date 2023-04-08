package me.zero.clarinet.mod.player;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.player.retard.RetardHeadless;
import me.zero.clarinet.mod.player.retard.RetardSpinny;
import me.zero.values.types.BooleanValue;
import org.lwjgl.input.Keyboard;

public class Retard extends Mod {

    public BooleanValue yaw, pitch;

    public Retard() {
        super("Retard", "Makes your head spin", Keyboard.KEY_NONE, Category.MOVEMENT);

        this.setModes(
                new RetardSpinny(this),
                new RetardHeadless(this)
        );

        yaw = new BooleanValue(this, "Yaw", "yaw", true);
        pitch = new BooleanValue(this, "Pitch", "pitch", true);
    }
}
