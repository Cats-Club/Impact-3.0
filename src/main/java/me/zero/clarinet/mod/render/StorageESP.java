package me.zero.clarinet.mod.render;

import java.awt.*;
import java.util.ArrayList;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import me.zero.clarinet.util.render.ColorUtils;
import me.zero.clarinet.util.render.MCStencil;
import me.zero.clarinet.util.render.Stencil;
import me.zero.values.types.MultiValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.math.AxisAlignedBB;

public class StorageESP extends Mod {

    private MultiValue<String> mode = new MultiValue<>(this, "Mode", "mode", "Box", new String[] { "Box", "Outline" });
	
	private BooleanValue fade = new BooleanValue(this, "Fade", "fade", true);
	
	private NumberValue fadeDist = new NumberValue(this, "Fade Start", "dist", 20D, 5D, 30D, 1D);
	private NumberValue lineWidth = new NumberValue(this, "Line Width", "width", 1D, 0D, 3D, 0.25D);
	
	private NumberValue cr = new NumberValue(this, "Chest R", "cr", 51D, 0D, 255D, 1D);
	private NumberValue cg = new NumberValue(this, "Chest G", "cg", 51D, 0D, 255D, 1D);
	private NumberValue cb = new NumberValue(this, "Chest B", "cb", 127D, 0D, 255D, 1D);
	
	private NumberValue tr = new NumberValue(this, "Trapped Chest R", "tr", 179D, 0D, 255D, 1D);
	private NumberValue tg = new NumberValue(this, "Trapped Chest G", "tg", 51D, 0D, 255D, 1D);
	private NumberValue tb = new NumberValue(this, "Trapped Chest B", "tb", 51D, 0D, 255D, 1D);
	
	private NumberValue er = new NumberValue(this, "Ender Chest R", "er", 179D, 0D, 255D, 1D);
	private NumberValue eg = new NumberValue(this, "Ender Chest G", "eg", 0D, 0D, 255D, 1D);
	private NumberValue eb = new NumberValue(this, "Ender Chest B", "eb", 255D, 0D, 255D, 1D);

    private final AxisAlignedBB chestBox = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);;
	
	public StorageESP() {
		super("StorageESP", "Draws boxes around storage containers", Keyboard.KEY_H, Category.RENDER);
	}
	
	@EventTarget
	public void onRender(EventRender3D event) {
        this.suffix = mode.getValue();

        if (mode.getValue().equalsIgnoreCase("Outline")) {
            ColorUtils.setColor(new Color(255, 255, 255));
            MCStencil.checkSetupFBO();
            int list = glGenLists(1);
            Stencil.getInstance().startLayer();
            RenderUtils.pre();
            Stencil.getInstance().setBuffer(true);
            glNewList(list, GL_COMPILE);
            glLineWidth(lineWidth.getValue().floatValue());
            for (TileEntity te : mc.world.loadedTileEntityList) {
                if (true) { // Doogie13 - wtf? this was like this in the original code I didn't write this
                    RenderUtils.pre();
                    double posX = te.getPos().getX() - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
                    double posY = te.getPos().getY() - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
                    double posZ = te.getPos().getZ() - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
                    TileEntitySpecialRenderer<TileEntity> render = TileEntityRendererDispatcher.instance.getRenderer(te);
                    if (render != null) {
                        float[] color = getColor(te);
                        float r = color[0];
                        float g = color[1];
                        float b = color[2];
                        glColor4f(r, g, b, 1.0F);
                        glDisable(GL_BLEND);
                        GlStateManager.disableLighting();
                        render.render(te, posX, posY, posZ, mc.getRenderPartialTicks(), -420, -69);
                        GlStateManager.enableLighting();
                        glEnable(GL_BLEND);
                    }
                    RenderUtils.post();
                }
            }
            glEndList();
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            glCallList(list);
            Stencil.getInstance().setBuffer(false);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            glCallList(list);
            Stencil.getInstance().cropInside();
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            glCallList(list);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            Stencil.getInstance().stopLayer();
            glDeleteLists(list, 1);
            RenderUtils.post();
        } else if (mode.getValue().equalsIgnoreCase("Box")) {
            ArrayList<TileEntity> ignoring = new ArrayList<>();
            for (TileEntity te : mc.world.loadedTileEntityList) {
                if (ignoring.contains(te)) {
                    continue;
                }
                double x = te.getPos().getX() - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
                double y = te.getPos().getY() - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
                double z = te.getPos().getZ() - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
                float[] color = getColor(te);
                float r = color[0];
                float g = color[1];
                float b = color[2];
                if (te instanceof TileEntityChest || te instanceof TileEntityEnderChest) {
                    float a = 90F;
                    float oa = 255F;
                    if (fade.getValue()) {
                        double dist = mc.player.getDistance(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
                        if (dist <= fadeDist.getValue().doubleValue()) {
                            double perc = dist / fadeDist.getValue().doubleValue();
                            a = (float) (perc * 90F);
                            oa = (float) (perc * 255F);
                        }
                    }

                    AxisAlignedBB box = chestBox;
                    if (te instanceof TileEntityChest) {
                        box = getChestBoundingBox((TileEntityChest) te);
                    }
                    if (te instanceof TileEntityEnderChest) {
                        r = er.getValue().floatValue();
                        g = eg.getValue().floatValue();
                        b = eb.getValue().floatValue();
                    }

                    a /= 255F;
                    oa /= 255F;
                    box = box.offset(x, y, z);

                    RenderUtils.drawSolidBlockESP(box, r, g, b, a);
                    RenderUtils.drawOutlinedBlockESP(box, r, g, b, oa, lineWidth.getValue().floatValue());
                }
            }
            for (Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityMinecartChest) {
                    double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * mc.getRenderPartialTicks() - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
                    double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * mc.getRenderPartialTicks() - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
                    double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * mc.getRenderPartialTicks() - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
                    AxisAlignedBB box = new AxisAlignedBB(x - 0.35, y + 0.35, z - 0.35, x + 0.35, y + 1.05, z + 0.35);
                    glPushMatrix();
                    RenderUtils.rotateY(-(e.rotationYaw), x, y, z);
                    float a = 90F;
                    float oa = 255F;
                    if (fade.getValue()) {
                        double dist = mc.player.getDistance(e.posX, e.posY, e.posZ);
                        if (dist <= fadeDist.getValue().doubleValue()) {
                            double max = 90F;
                            double perc = dist / fadeDist.getValue().doubleValue();
                            a = (float) (perc * max);
                            oa = (float) (perc * 255F);
                        }
                    }
                    RenderUtils.drawSolidBlockESP(box, cr.getValue().floatValue() / 255F, cg.getValue().floatValue() / 255F, cb.getValue().floatValue() / 255F, a / 255F);
                    RenderUtils.drawOutlinedBlockESP(box, cr.getValue().floatValue() / 255F, cg.getValue().floatValue() / 255F, cb.getValue().floatValue() / 255F, oa / 255F, 1);
                    glPopMatrix();
                }
            }
		}
	}

	public float[] getColor(TileEntity te) {
        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;

        if (te.getBlockType().equals(Blocks.TRAPPED_CHEST)) {
            r = tr.getValue().floatValue();
            g = tg.getValue().floatValue();
            b = tb.getValue().floatValue();
        } else {
            r = cr.getValue().floatValue();
            g = cg.getValue().floatValue();
            b = cb.getValue().floatValue();
        }
        if (te instanceof TileEntityEnderChest) {
            r = er.getValue().floatValue();
            g = eg.getValue().floatValue();
            b = eb.getValue().floatValue();
        }

        r /= 255F;
        g /= 255F;
        b /= 255F;

        return new float[]{ r, g, b };
    }

    private AxisAlignedBB getChestBoundingBox(TileEntityChest tec) {
        AxisAlignedBB box = chestBox;
        if (tec.adjacentChestXNeg != null) {
            box = new AxisAlignedBB(-0.9375D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
        } else if (tec.adjacentChestXPos != null) {
            box = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D + 0.9375D + 0.0625D, 0.875D, 0.9375D);
        } else if (tec.adjacentChestZNeg != null) {
            box = new AxisAlignedBB(0.0625D, 0.0D, -0.9375D, 0.9375D, 0.875D, 0.9375D);
        } else if (tec.adjacentChestZPos != null) {
            box = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D + 0.9375D + 0.0625D);
        }
        return box;
    }

    private boolean hasConnectingChest(TileEntityChest tec) {
        return tec.adjacentChestXNeg != null || tec.adjacentChestXPos != null || tec.adjacentChestZNeg != null || tec.adjacentChestZPos != null;
    }
}
