package me.zero.clarinet.ui.click.classic.elements;

public abstract class Element {
	
	protected Element parent;
	
	protected int x, y;
	
	protected int width, height;
	
	public abstract void drawElement(int mouseX, int mouseY, float partialTicks);
	
	public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);
	
	public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);
	
	public final boolean isInside(int mouseX, int mouseY) {
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
	
	public Element getParent() {
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
}
