package me.zero.clarinet.waypoints;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import me.zero.clarinet.Impact;

public class Waypoint {
	
	private String name;
	
	private Double x, y, z;
	
	private Integer r, g, b;

	public Waypoint() {}
	
	public Waypoint(String name, double x, double y, double z, int r, int g, int b) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getR() {
		return this.r;
	}
	
	public int getG() {
		return this.g;
	}
	
	public int getB() {
		return this.b;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y == null ? 100 : y;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public boolean isComplete() {
		return x != null && r != null && g != null && b != null && name != null;
	}
	
	public static Waypoint create(File file) {
		Waypoint wp = new Waypoint();
		try {
			FileReader reader = new FileReader(file);
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject root = new JSONObject(tokener);
			Iterator<String> keyIterator = root.keys();
			while (keyIterator.hasNext()) {
				String next = keyIterator.next();
				Object obj = root.get(next);
				if (obj != null) {
					if (obj instanceof Integer) {
						if (next.equalsIgnoreCase("red")) {
							wp.r = (Integer) obj;
						} else if (next.equalsIgnoreCase("green")) {
							wp.g = (Integer) obj;
						} else if (next.equalsIgnoreCase("blue")) {
							wp.b = (Integer) obj;
						}
					} else if (obj instanceof Double) {
						if (next.equalsIgnoreCase("x")) {
							wp.x = (Double) obj;
						} else if (next.equalsIgnoreCase("y")) {
							wp.y = (Double) obj;
						} else if (next.equalsIgnoreCase("z")) {
							wp.z = (Double) obj;
						}
					} else if (obj instanceof String) {
						if (next.equalsIgnoreCase("name")) {
							wp.name = (String) obj;
						}
					}
				}
			}
		} catch (JSONException | FileNotFoundException e) {
		}
		
		if (wp.isComplete()) {
			return wp;
		} else {
			return null;
		}
	}
	
	public void save() {
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("red", r);
		obj.put("green", g);
		obj.put("blue", b);
		obj.put("x", x);
		obj.put("y", y);
		obj.put("z", z);
		String folder = "./" + Impact.getInstance().getName() + "/waypoints";
		String file = folder + "/" + getName() + ".json";
		Path folderP = Paths.get(folder);
		Path fileP = Paths.get(file);
		if (!Files.exists(folderP)) {
			try {
				Files.createDirectory(folderP);
			} catch (IOException e) {
			}
		}
		if (!Files.exists(fileP)) {
			try {
				Files.createFile(fileP);
			} catch (IOException e) {
			}
		}
		if (Files.exists(folderP) && Files.exists(fileP)) {
			try {
				FileWriter write;
				write = new FileWriter(file, false);
				PrintWriter print_line = new PrintWriter(write);
				print_line.println(obj.toString());
				print_line.close();
			} catch (IOException e) {
			}
		}
	}
}
