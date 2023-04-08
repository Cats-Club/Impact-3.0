package me.zero.clarinet.mod.render;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

public class Breadcrumbs extends Mod {

    private BooleanValue walls = new BooleanValue(this, "Walls", "walls");

    private NumberValue r = new NumberValue(this, "Red", "red", 255D, 0D, 255D, 1D);
    private NumberValue g = new NumberValue(this, "Green", "green", 0D, 0D, 255D, 1D);
    private NumberValue b = new NumberValue(this, "Blue", "blue", 0D, 0D, 255D, 1D);
    private NumberValue a = new NumberValue(this, "Alpha", "alpha", 255D, 0D, 255D, 1D);
    private NumberValue width = new NumberValue(this, "Outline Width", "width", 1D, 0.1D, 4D, 0.1D);

    private List<Vec3d> positions = new ArrayList<>();

    public Breadcrumbs() {
        super("Breadcrumbs", "A trail is left behind you", Keyboard.KEY_NONE, Category.RENDER);
    }

    @Override
    public void onEnable() {
        positions.clear();
    }

    @Override
    public void onDisable() {
        positions.clear();
    }

    @EventTarget
    public void onTick(EventTick event) {
        this.suffix = String.valueOf(positions.size());

        if (positions.isEmpty()) {
            addPos();
        } else {
            Vec3d last = positions.get(positions.size() - 1);
            if (mc.player.getDistance(last.x, last.y, last.z) > 1.0D) {
                addPos();
            }
        }
    }

    private void addPos() {
        positions.add(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ));
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        RenderUtils.pre();

        if (!walls.getValue()) {
            glEnable(GL_DEPTH_TEST);
            glDepthMask(true);
        }

        float red = r.getValue().floatValue() / 255F;
        float green = g.getValue().floatValue() / 255F;
        float blue = b.getValue().floatValue() / 255F;
        float alpha = a.getValue().floatValue() / 255F;
        glColor4f(red, green, blue, alpha);

        glLineWidth(width.getValue().floatValue());

        glBegin(GL_LINE_STRIP);
        for (Vec3d pos : positions) {
            IRenderManager renderManager = (IRenderManager) mc.getRenderManager();
            glVertex3d(pos.x - renderManager.getRenderPosX(), pos.y - renderManager.getRenderPosY() + 0.1, pos.z - renderManager.getRenderPosZ());
        }
        glEnd();

        RenderUtils.post();
    }
}
