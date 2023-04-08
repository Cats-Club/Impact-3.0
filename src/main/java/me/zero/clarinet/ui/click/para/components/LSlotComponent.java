package me.zero.clarinet.ui.click.para.components;

import java.util.ArrayList;

public abstract class LSlotComponent extends Component {
	
	protected ArrayList<Component> components = new ArrayList<Component>();
	
	protected boolean open = false;
	
	public void addComponent(Component component) {
		components.add(component);
	}
	
	public ArrayList<Component> getComponents() {
		return this.components;
	}
	
	public boolean isOpen() {
		return this.open;
	}
}
