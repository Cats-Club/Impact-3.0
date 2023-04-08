package me.zero.clarinet.ui.click.para.components;

public class LColorPicker extends Component {
	
	public LColorPicker(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this.mouseInside(mouseX, mouseY)) {
			if (mouseButton == 0) {
				// Aris kills himself
			}
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {}
}
