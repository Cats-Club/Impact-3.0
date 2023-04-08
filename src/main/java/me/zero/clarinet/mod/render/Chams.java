package me.zero.clarinet.mod.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.event.render.EventEntityRender;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.entity.EntityFilter;
import me.zero.values.types.BooleanValue;

public class Chams extends Mod {
	
	private BooleanValue players = new BooleanValue(this, "Players", "players", true);
	
	private BooleanValue mobs = new BooleanValue(this, "Mobs", "mobs", true);
	
	private BooleanValue animals = new BooleanValue(this, "Animals", "animals", true);
	
	public ChamsEntityFilter filter = new ChamsEntityFilter();
	
	public Chams() {
		super("Chams", "Renders players through walls", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@EventTarget
	public void onEntityRender(EventEntityRender event) {
		if (!filter.isValidEntity(event.entity)) {
			return;
		}
		if (event.type == EventType.PRE) {
			GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glPolygonOffset(1.0F, -1000000.0F);
		} else if (event.type == EventType.POST) {
			GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glPolygonOffset(1.0F, 1000000.0F);
		}
	}
	
	public class ChamsEntityFilter extends EntityFilter {
		
		@Override
		public boolean walls() {
			return true;
		}
		
		@Override
		public boolean sleeping() {
			return true;
		}
		
		@Override
		public boolean invisibles() {
			return true;
		}
		
		@Override
		public boolean teammates() {
			return true;
		}
		
		@Override
		public boolean friends() {
			return true;
		}
		
		@Override
		public boolean players() {
			return players.getValue();
		}
		
		@Override
		public boolean animals() {
			return animals.getValue();
		}
		
		@Override
		public boolean hostiles() {
			return mobs.getValue();
		}
		
		@Override
		public boolean passives() {
			return false;
		}
	}
}
