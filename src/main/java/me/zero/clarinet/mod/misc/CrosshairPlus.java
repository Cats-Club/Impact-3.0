package me.zero.clarinet.mod.misc;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.render.EventCrosshairRender;
import me.zero.clarinet.event.render.EventRender2D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.client.gui.ScaledResolution;

public class CrosshairPlus extends Mod {
	
	private BooleanValue outline = new BooleanValue(this, "Crosshair Outline", "outline");
	
	private BooleanValue dot = new BooleanValue(this, "Crosshair Dot", "dot");
	
	private NumberValue gap = new NumberValue(this, "Crosshair Gap", "gap", 2D, 0D, 10D, 0.5D);
	private NumberValue size = new NumberValue(this, "Crosshair Size", "size", 10D, 1D, 40D, 0.5D);
	private NumberValue width = new NumberValue(this, "Crosshair Width", "width", 1D, 0D, 2D, 0.5D);
	private NumberValue r = new NumberValue(this, "Red", "red", 30D, 0D, 255D, 1D);
	private NumberValue g = new NumberValue(this, "Green", "green", 255D, 0D, 255D, 1D);
	private NumberValue b = new NumberValue(this, "Blue", "blue", 0D, 0D, 255D, 1D);
	
	public CrosshairPlus() {
		super("Crosshair+", "Have a crosshair like CS:GO", Keyboard.KEY_NONE, Category.MISC);
	}
	
	@EventTarget
	public void onRenderCrosshair(EventCrosshairRender event) {
		event.setCancelled(true);
	}
	
	@EventTarget
	public void onRenderHud(EventRender2D event) {
		ScaledResolution sr = new ScaledResolution(mc);
		int cX = sr.getScaledWidth() / 2;
		int cY = sr.getScaledHeight() / 2;
		int color = new Color(r.getValue().floatValue() / 255F, g.getValue().floatValue() / 255F, b.getValue().floatValue() / 255F, 1.0F).getRGB();
		float gap = this.gap.getValue().floatValue();
		float width = (float) Math.max(this.width.getValue().floatValue(), 0.5);
		float size = this.size.getValue().floatValue();
		if (outline.getValue().booleanValue()) {
			width += 0.5;
			RenderUtils.rectangleBordered(cX - gap - size, cY - width / 2.0, cX - gap, cY + width / 2.0, 0xFF000000, color);
			RenderUtils.rectangleBordered(cX + gap + size, cY - width / 2.0, cX + gap, cY + width / 2.0, 0xFF000000, color);
			RenderUtils.rectangleBordered(cX - width / 2.0, cY + gap + size, cX + width / 2.0, cY + gap, 0xFF000000, color);
			RenderUtils.rectangleBordered(cX - width / 2.0, cY - gap - size, cX + width / 2.0, cY - gap, 0xFF000000, color);
			if (dot.getValue().booleanValue()) {
				RenderUtils.rectangleBordered(cX - width / 2, cY - width / 2, cX + width / 2, cY + width / 2, 0xFF000000, color);
			}
		} else {
			RenderUtils.rectangle(cX - gap - size, cY - width / 2.0, cX - gap, cY + width / 2.0, color);
			RenderUtils.rectangle(cX + gap + size, cY - width / 2.0, cX + gap, cY + width / 2.0, color);
			RenderUtils.rectangle(cX - width / 2.0, cY + gap + size, cX + width / 2.0, cY + gap, color);
			RenderUtils.rectangle(cX - width / 2.0, cY - gap - size, cX + width / 2.0, cY - gap, color);
			if (dot.getValue().booleanValue()) {
				RenderUtils.rectangle(cX - width / 2, cY - width / 2, cX + width / 2, cY + width / 2, color);
			}
		}
	}
}
