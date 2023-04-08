package me.zero.clarinet.util;

import me.zero.clarinet.util.render.shader.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class DimensionConverter implements Helper {

    private static Map<Entity, Vec3d> entities = new HashMap<>();

    private static double gradualFOVModifier;

    public static void update() {
        entities.clear();
        for (Entity ent : mc.world.loadedEntityList) {
            double[] position = ShaderHelper.interpolate(ent);
            double x = position[0];
            double y = position[1] + ent.height + 0.3;
            double z = position[2];
            Vec3d pos = convertTo2D(x, y, z);
            if (pos != null) {
                if (pos.z >= 0.0D && pos.z < 1.0D) {
                    entities.put(ent, new Vec3d(pos.x, pos.y, pos.z));
                }
            }
        }
    }

    private static Vec3d convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        glGetFloat(GL_MODELVIEW_MATRIX, modelView);
        glGetFloat(GL_PROJECTION_MATRIX, projection);
        glGetInteger(GL_VIEWPORT, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new Vec3d(screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2));
        }
        return null;
    }

    public static void screenScale() {
        ScaledResolution scaledRes = new ScaledResolution(mc);
        double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0D);
        glScaled(twoDscale, twoDscale, twoDscale);
    }

    public static void renderScale(double scale) {
        double target = scale * (mc.gameSettings.fovSetting / (mc.gameSettings.fovSetting * mc.player.getFovModifier()));
        if (gradualFOVModifier == 0.0D || Double.isNaN(gradualFOVModifier)) {
            gradualFOVModifier = target;
        }
        gradualFOVModifier += (target - gradualFOVModifier) / (Minecraft.getDebugFPS() * 0.7D);
        scale = (float)(scale * gradualFOVModifier);
        // Doogie13 - Optifine
        //scale *= (mc.currentScreen == null && GameSettings.isKeyDown(mc.gameSettings.ofKeyBindZoom) ? 3 : 1);
        glScaled(scale, scale, scale);
    }

    public static Vec3d getEntityPosition(Entity ent) {
        return entities.get(ent);
    }
}
