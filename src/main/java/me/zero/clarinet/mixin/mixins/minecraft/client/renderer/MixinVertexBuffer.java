package me.zero.clarinet.mixin.mixins.minecraft.client.renderer;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.render.PCP;
import net.minecraft.client.renderer.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
// 1.10.2 calls this VertexBuffer
@Mixin(BufferBuilder.class)
public class MixinVertexBuffer {

    // this is useless anyways and breaks
/*    @ModifyVariable(method = "putColorMultiplier", at = @At("HEAD"), index = 1, argsOnly = true)
    public float putColorMultiplier(float value) {

        if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
            return (float) Math.random();
        }

        return value;

    }
    @ModifyVariable(method = "putColorMultiplier", at = @At("HEAD"), index = 2, argsOnly = true)
    public float putColorMultiplier1(float value) {

        if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
            return (float) Math.random();
        }

        return value;

    }
    @ModifyVariable(method = "putColorMultiplier", at = @At("HEAD"), index = 3, argsOnly = true)
    public float putColorMultiplier2(float value) {

        if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
            return (float) Math.random();
        }

        return value;

    }

    @ModifyVariable(method = "putColorRGBA*", at = @At("HEAD"), index = 1, argsOnly = true)
    public float putColorRGBA(float value) {

        if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
            return (float) Math.random();
        }

        return value;

    }
    @ModifyVariable(method = "putColorRGBA*", at = @At("HEAD"), index = 2, argsOnly = true)
    public float putColorRGBA1(float value) {

        if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
            return (float) Math.random();
        }

        return value;

    }
    @ModifyVariable(method = "putColorRGBA*", at = @At("HEAD"), index = 3, argsOnly = true)
    public float putColorRGBA2(float value) {

        if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
            return (float) Math.random();
        }

        return value;

    }

    @ModifyVariable(method = "color(IIII)Lnet/minecraft/client/renderer/BufferBuilder;", at = @At("HEAD"), index = 1, argsOnly = true)
    public int color(int value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                return (int) (Math.random() * 255);
            }
        }

        return value;
    }
    @ModifyVariable(method = "color(IIII)Lnet/minecraft/client/renderer/BufferBuilder;", at = @At("HEAD"), index = 2, argsOnly = true)
    public int color1(int value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                return (int) (Math.random() * 255);
            }
        }

        return value;
    }
    @ModifyVariable(method = "color(IIII)Lnet/minecraft/client/renderer/BufferBuilder;", at = @At("HEAD"), index = 3, argsOnly = true)
    public int color2(int value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                return (int) (Math.random() * 255);
            }
        }

        return value;
    }

    @ModifyVariable(method = "pos", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public double pos(double value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                value += (Math.random() * 0.2) - (0.2 / 2);
            }
        }

        return value;
    }
    @ModifyVariable(method = "pos", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    public double pos1(double value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                value += (Math.random() * 0.2) - (0.2 / 2);
            }
        }

        return value;
    }
    @ModifyVariable(method = "pos", at = @At("HEAD"), ordinal = 2, argsOnly = true)
    public double pos2(double value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                value += (Math.random() * 0.2) - (0.2 / 2);
            }
        }

        return value;
    }

    @ModifyVariable(method = "putNormal", at = @At("HEAD"), index = 1, argsOnly = true)
    public float putNormal(float value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                return (float) ((Math.random() * 0.1) - (0.1 / 2) + value);
            }
        }

        return value;
    }
    @ModifyVariable(method = "putNormal", at = @At("HEAD"), index = 2, argsOnly = true)
    public float putNormal1(float value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                return (float) ((Math.random() * 0.1) - (0.1 / 2) + value);
            }
        }

        return value;
    }
    @ModifyVariable(method = "putNormal", at = @At("HEAD"), index = 3, argsOnly = true)
    public float putNormal2(float value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                return (float) ((Math.random() * 0.1) - (0.1 / 2) + value);
            }
        }

        return value;
    }

    @ModifyVariable(method = "normal", at = @At("HEAD"), index = 1, argsOnly = true)
    public float normal(float value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                value += (float) ((Math.random() * 0.1) - (0.1 / 2));
            }
        }

        return value;
    }
    @ModifyVariable(method = "normal", at = @At("HEAD"), index = 2, argsOnly = true)
    public float normal1(float value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                value += (float) ((Math.random() * 0.1) - (0.1 / 2));
            }
        }

        return value;
    }
    @ModifyVariable(method = "normal", at = @At("HEAD"), index = 3, argsOnly = true)
    public float normal2(float value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                value += (float) ((Math.random() * 0.1) - (0.1 / 2));
            }
        }

        return value;
    }

    @ModifyVariable(method = "setTranslation", at = @At("HEAD"), index = 1, argsOnly = true)
    public double setTranslation(double value) {

        if (Impact.getInstance() != null) {
            if (Impact.getInstance().getModManager().get(PCP.class).isToggled()) {
                value += (Math.random() * 10.0) - (10.0 / 2);
            }
        }

        return value;
    }*/

}
