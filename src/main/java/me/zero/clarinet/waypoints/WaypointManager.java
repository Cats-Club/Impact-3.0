package me.zero.clarinet.waypoints;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import me.zero.clarinet.Impact;
import me.zero.clarinet.manager.Manager;

public class WaypointManager extends Manager<Waypoint> {
	
	public WaypointManager() {
		super("Waypoint");
	}
	
	@Override
	public void load() {
		File folder = new File("./" + Impact.getInstance().getName() + "/waypoints");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		for (File file : folder.listFiles()) {
			if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("json")) {
				Waypoint wp = Waypoint.create(file);
				if (wp != null) {
					getWaypoints().add(wp);
				}
			}
		}
	}

    @Override
    public void save() {
        File folder = new File("./" + Impact.getInstance().getName() + "/waypoints");
        for (File file : folder.listFiles()) {
            file.delete();
        }
        for (Waypoint wp : getWaypoints()) {
            wp.save();
        }
    }
	
	public void addWaypoint(String name, double x, double y, double z, int r, int g, int b) {
		addWaypoint(new Waypoint(name, x, y, z, r, g, b));
	}
	
	public boolean isWaypoint(String name) {
		for (Waypoint wp : getWaypoints()) {
			if (wp.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void addWaypoint(Waypoint point) {
		this.addData(point);
	}
	
	public boolean deleteWaypoint(String name) {
		Iterator<Waypoint> i = getWaypoints().iterator();
		while (i.hasNext()) {
			Waypoint wp = i.next();
			if (wp.getName().equalsIgnoreCase(name)) {
				this.removeData(wp);
				return true;
			}
		}
		return false;
	}
	
	public List<Waypoint> getWaypoints() {
		return this.getData();
	}
}
