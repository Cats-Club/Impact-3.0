package me.zero.clarinet.ui.click.para.components;

import net.minecraft.client.gui.FontRenderer;

public abstract class Component {
	
	public static FontRenderer font;
	
	protected Component parent;
	
	protected String title;
	
	protected int x, y;
	
	protected int width, height;
	
	public abstract void drawElement(int mouseX, int mouseY, float partialTicks);
	
	public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);
	
	public abstract void mouseReleased(int mouseX, int mouseY, int state);
	
	public abstract void keyTyped(char typedChar, int keyCode);
	
	public final boolean mouseInside(int mouseX, int mouseY) {
		return (mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height);
	}
	
	public final boolean mouseInside(int mouseX, int mouseY, int x, int y, int width, int height) {
		return (mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Component getParent() {
		return parent;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void onUpdate() {
	}
}
