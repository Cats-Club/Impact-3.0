package me.zero.clarinet.ui.click.impact.component;

import java.util.ArrayList;

public abstract class ClickSlotComponent extends ClickComponent {
	
	protected ArrayList<ClickComponent> components = new ArrayList<ClickComponent>();
	
	protected boolean open = false;
	
	public void addComponent(ClickComponent component) {
        component.setParent(this);
		components.add(component);
	}
	
	public ArrayList<ClickComponent> getComponents() {
		return this.components;
	}
	
	public boolean isOpen() {
		return this.open;
	}
}
