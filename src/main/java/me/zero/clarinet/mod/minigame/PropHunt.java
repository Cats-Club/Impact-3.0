package me.zero.clarinet.mod.minigame;

import java.util.ArrayList;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.MathUtils;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.math.AxisAlignedBB;

public class PropHunt extends Mod {
	
	private ArrayList<Entity> props = new ArrayList<Entity>();
	
	private NumberValue r = new NumberValue(this, "Red", "red", 200D, 0D, 255D, 1D);
	private NumberValue g = new NumberValue(this, "Green", "green", 0D, 0D, 255D, 1D);
	private NumberValue b = new NumberValue(this, "Blue", "blue", 255D, 0D, 255D, 1D);
	private NumberValue a = new NumberValue(this, "Alpha", "alpha", 50D, 0D, 255D, 1D);
	
	public PropHunt() {
		super("PropHunt", "Reveals the location of props", Keyboard.KEY_NONE, Category.MINIGAME);
	}
	
	@Override
	public void onEnable() {
		props.clear();
	}
	
	@EventTarget
	public void onRender(EventRender3D event) {
		props.clear();
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityChicken) {
				EntityChicken chicken = (EntityChicken) e;
				if (chicken.isChild() && (chicken.posX - chicken.lastTickPosX == 0.0) && (chicken.posZ - chicken.lastTickPosZ == 0.0)) {
					props.add(chicken);
				}
			} else if (e instanceof EntityFallingBlock) {
				EntityFallingBlock fallingblock = (EntityFallingBlock) e;
				props.add(fallingblock);
			} else if (e instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) e;
				if ((animal.moveStrafing != 0.0F) || (animal.isSprinting()) || (animal.rotationPitch > 41) || (animal.rotationPitch < -41)) {
					props.add(animal);
				}
			}
		}
		for (Entity prop : props) {
			double x = (prop.lastTickPosX + (prop.posX - prop.lastTickPosX) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
			double y = (prop.lastTickPosY + (prop.posY - prop.lastTickPosY) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
			double z = (prop.lastTickPosZ + (prop.posZ - prop.lastTickPosZ) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
			AxisAlignedBB box = null;
			if (prop instanceof EntityChicken) {
				x = MathUtils.roundToPlace(prop.posX, 0) - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
				y = MathUtils.roundToPlace(prop.posY, 0) - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
				z = MathUtils.roundToPlace(prop.posZ, 0) - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
				box = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
			} else if (prop instanceof EntityFallingBlock) {
				x -= 0.5F;
				z -= 0.5F;
				box = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
			} else if (prop instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) prop;
				RenderUtils.drawEntityESP(animal, r.getValue().floatValue() / 255F, g.getValue().floatValue() / 255F, b.getValue().floatValue() / 255F, event.partialTicks);
			}
			if (box != null) {
				RenderUtils.drawSolidBlockESP(box, r.getValue().floatValue() / 255F, g.getValue().floatValue() / 255F, b.getValue().floatValue() / 255F, a.getValue().floatValue() / 255F);
				RenderUtils.drawOutlinedBlockESP(box, 0, 0, 0, 1, 1);
			}
		}
	}
}
