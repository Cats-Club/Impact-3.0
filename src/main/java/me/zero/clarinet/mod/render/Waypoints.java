package me.zero.clarinet.mod.render;

import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.IEntityRenderer;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.RotationUtils;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.clarinet.waypoints.Waypoint;
import me.zero.values.types.BooleanValue;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Waypoints extends Mod {
	
	private BooleanValue distance = new BooleanValue(this, "Distance", "distance");
	
	private BooleanValue nametag = new BooleanValue(this, "Nametags", "nametag");
	
	private BooleanValue tracer = new BooleanValue(this, "Tracers", "tracer");
	
	private BooleanValue marker = new BooleanValue(this, "Marker", "marker");

	public Waypoints() {
		super("Waypoints", "Shows marked points", Keyboard.KEY_NONE, Category.RENDER);
	}

    @EventTarget
    public void onRender(EventRender3D event) {
        for (Waypoint w : Impact.getInstance().getWaypointManager().getWaypoints()) {
            double x = w.getX() - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
            double y = w.getY() - ((IRenderManager) mc.getRenderManager()).getRenderPosY() + 1;
            double z = w.getZ() - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
            if (tracer.getValue()) {
                RenderUtils.pre();
                GL11.glColor4f(w.getR() / 255F, w.getG() / 255F, w.getB() / 255F, 0.5F);
                GL11.glLineWidth(1.5F);
                GL11.glLoadIdentity();
                boolean bobbing = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                ((IEntityRenderer) mc.entityRenderer).callOrientCamera(event.partialTicks);
                Vec3d eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(mc.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(eyes.x, mc.player.getEyeHeight() + eyes.y, eyes.z);
                GL11.glVertex3d(x, y, z);
                GL11.glEnd();
                mc.gameSettings.viewBobbing = bobbing;
                RenderUtils.post();
            }
            double width = 0.25D;
            double height = 0.25D;
            if (marker.getValue() && mc.player.getDistance(w.getX(), w.getY(), w.getZ()) < 100) {
                RenderUtils.pre();
                GL11.glLineWidth(2F);
                RenderUtils.rotateY(-mc.getRenderManager().playerViewY, x, y, z);
                GL11.glColor4f(w.getR() / 255F, w.getG() / 255F, w.getB() / 255F, 0.5F);
                GL11.glBegin(GL11.GL_POLYGON);
                GL11.glVertex3d(x - width, y, z);
                GL11.glVertex3d(x, y + height, z);
                GL11.glVertex3d(x + width, y, z);
                GL11.glVertex3d(x, y - height, z);
                GL11.glEnd();
                GL11.glColor4f(0, 0, 0, 1);
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex3d(x - width, y, z);
                GL11.glVertex3d(x, y + height, z);
                GL11.glVertex3d(x + width, y, z);
                GL11.glVertex3d(x, y - height, z);
                GL11.glEnd();
                RenderUtils.post();
            }
            if (nametag.getValue()) {
                GL11.glPushMatrix();
                GL11.glLoadIdentity();
                ((IEntityRenderer) mc.entityRenderer).callOrientCamera(event.partialTicks);
                String renderText = w.getName() + (distance.getValue() ? " (" + (int) mc.player.getDistance(w.getX(), w.getY(), w.getZ()) + "m)" : "");
                float textWidth = mc.fontRenderer.getStringWidth(renderText) / 2;
                float scale = 1.6F / 500F;
                float[] rotations = RotationUtils.getRotations(w.getX(), w.getY(), w.getZ());
                float yaw = (float) Math.toRadians(rotations[0]);
                float pitch = (float) Math.toRadians(rotations[1]);
                float divisor = 1.2F;
                float dirX = -MathHelper.sin(yaw) * MathHelper.cos(pitch) / divisor;
                float dirY = -MathHelper.sin(pitch) / divisor;
                float dirZ = MathHelper.cos(yaw) * MathHelper.cos(pitch) / divisor;
                x = 0;
                y = 1.365;
                z = 0;
                for (int i = 0; i < 1; i++) {
                    x += dirX;
                    y += dirY;
                    z += dirZ;
                }
                float offset = (float) height + (scale * mc.fontRenderer.FONT_HEIGHT);
                GL11.glTranslated(x, y + offset, z);
                GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
                GL11.glScaled(-scale, -scale, scale);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                RenderUtils.rectangle(-textWidth - 1, -1, textWidth, mc.fontRenderer.FONT_HEIGHT, 0x60000000);
                mc.fontRenderer.drawStringWithShadow(renderText, (float) -textWidth, 0, -1);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glPopMatrix();
            }
        }
    }
}
