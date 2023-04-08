package me.zero.clarinet.manager.manager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.zero.clarinet.mod.misc.*;
import me.zero.clarinet.mod.movement.*;
import me.zero.clarinet.mod.player.*;
import me.zero.clarinet.mod.render.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import me.zero.clarinet.Impact;
import me.zero.clarinet.manager.Manager;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.combat.Aimbot;
import me.zero.clarinet.mod.combat.Aura;
import me.zero.clarinet.mod.combat.AutoArmor;
import me.zero.clarinet.mod.combat.AutoClicker;
import me.zero.clarinet.mod.combat.BowAimbot;
import me.zero.clarinet.mod.combat.Criticals;
import me.zero.clarinet.mod.combat.HitBox;
import me.zero.clarinet.mod.combat.Pot;
import me.zero.clarinet.mod.combat.SmoothAim;
import me.zero.clarinet.mod.combat.Soup;
import me.zero.clarinet.mod.combat.Velocity;
import me.zero.clarinet.mod.exploit.AntiHunger;
import me.zero.clarinet.mod.exploit.BedGodMode;
import me.zero.clarinet.mod.exploit.Firion;
import me.zero.clarinet.mod.exploit.Franky;
import me.zero.clarinet.mod.exploit.GhostHand;
import me.zero.clarinet.mod.exploit.Ignite;
import me.zero.clarinet.mod.exploit.PingSpoof;
import me.zero.clarinet.mod.minigame.CopsAndCrims;
import me.zero.clarinet.mod.minigame.Minestrike;
import me.zero.clarinet.mod.minigame.Murder;
import me.zero.clarinet.mod.minigame.PropHunt;
import me.zero.clarinet.mod.minigame.QuakeCraft;
import me.zero.clarinet.mod.minigame.SneakyAssassians;
import me.zero.clarinet.mod.world.AntiWeather;
import me.zero.clarinet.mod.world.Nuker;
import me.zero.clarinet.mod.world.Xray;
import me.zero.clarinet.util.ClientUtils;
import me.zero.values.ValueManager;
import me.zero.values.types.Value;

public class ModManager extends Manager<Mod> {
	
	public ModManager() {
		super("Module");
	}
	
	@Override
	public void load() {
		this.addData(
				new HUD(),
				new ClickGui(),
				new Glide(),
				new Brightness(),
				new Spider(),
				new Blink(),
				new AutoJump(),
				new AutoWalk(),
				new Sprint(),
				new NoFall(),
				new Aura(),
				new Flight(), 
				new AntiWeather(),
				new FastBreak(),
				new NoPush(),
				new Velocity(),
				new StorageESP(),
				new Criticals(),
				new Jesus(),
				new ESP(),
				new Xray(),
				new FastPlace(),
				new InventoryMove(),
				new Chams(),
				new MCF(),
				new AutoReconnect(),
				new Speed(),
				new Jetpack(),
				new NoSlowDown(),
				new AntiHunger(), 
				new Nuker(),
				new MineplexStaffDetector(),
				new QuakeCraft(),
				new Tracers(),
				new Nametags(),
				new AutoMine(),
				new Sneak(),
				new Freecam(), 
				new Aimbot(),
				new RainbowEnchant(),
				new Disable(),
				new AutoFish(),
				new Step(),
				new BowAimbot(),
				new GhostHand(),
				new Minestrike(),
				new Murder(),
				new Trajectories(),
				new NoRotate(),
				new CrosshairPlus(),
				new CopsAndCrims(),
				new SmoothAim(),
				new AutoClicker(),
				new Pot(),
				new Soup(),
				new SafeWalk(),
				new ElytraPlus(),
				new AntiAFK(),
				new AutoEat(),
				new Franky(),
				new LongJump(),
				new SneakyAssassians(),
				new PropHunt(),
				new PingSpoof(),
				new HitBox(),
				new Timer(),
				new DeathCoords(),
				new AutoRespawn(),
				new BoatFly(),
				new SkinBlinker(),
				new LiquidVision(),
				new Ignite(),
				new Waypoints(),
				new AutoSteal(),
				new Animations(),
				new LiquidInteract(),
				new AutoTool(),
				new NoHurtCam(),
				new AntiBlind(),
				new Wireframe(),
				new ScaffoldWalk(),
				new AntiBot(),
				new Phase(),
				new Firion(),
				new Radar(),
				new BedGodMode(),
				new AutoArmor(),
				new CameraClip(),
				new Unpack(),
				new Parkour(),	
				new ScreenshotUploader(),
				new ItemPhysics(),
				new AutoDisconnect(),
				new ItemSaver(),
				new FastFall(),
				new PCP(),
                new FastLadder(),
                new AntiCheat(),
                new Retard(),
                new NoRender(),
                new Breadcrumbs());
		
		ClientUtils.log("[Mod] Loaded " + this.getMods().size() + " mods!");
		
		getMods().sort((m1, m2) -> {
			return m1.compareTo(m2);
		});
		
		String folder = "./" + Impact.getInstance().getName();
		String file = folder + "/mods.json";
		Path folderP = Paths.get(folder);
		Path fileP = Paths.get(file);
		if (!Files.exists(folderP) || !Files.exists(fileP)) {
			this.save();
			return;
		}
		try {
			FileReader reader = new FileReader(file);
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject json = new JSONObject(tokener);
			for (Mod mod : getMods()) {
				if (json.has(mod.getName())) {
					JSONArray array = json.getJSONArray(mod.getName());
					if (!array.isNull(0)) {
						Object o = array.get(0);
						if (o instanceof JSONObject) {
							JSONObject obj = (JSONObject) o;
							Iterator<String> keys = obj.keys();
							while (keys.hasNext()) {
								String piece = keys.next();
								Object data = obj.get(piece);
								if (piece.equalsIgnoreCase("toggled")) {
									if (data instanceof Boolean) {
										if ((Boolean) data) {
											if (!mod.isToggled()) {
												mod.toggle();
											}
										}
									}
								} else if (piece.equalsIgnoreCase("visible")) {
									if (data instanceof Boolean) {
										mod.setDisplays((Boolean) data);
									}
								} else if (piece.equalsIgnoreCase("keybind")) {
									try {
										mod.setKeybind((int) data);
									} catch (Exception e) {
									}
								} else {
									for (Value value : ValueManager.INSTANCE.getValues(mod)) {
										if (value.getID().equalsIgnoreCase(piece)) {
											try{
												value.setValue(data);
											}catch(Exception e){}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (JSONException | FileNotFoundException e) {
		}
	}
	
	public ArrayList<Mod> getMods(Category cat) {
		ArrayList<Mod> modsInCat = new ArrayList<Mod>();
		for (Mod m : getMods()) {
			if (m.getCategory() == cat) {
				modsInCat.add(m);
			}
		}
		return modsInCat;
	}
	
	public Mod get(String string) {
		for (Mod m : getMods()) {
			if (m.getName().equalsIgnoreCase(string)) {
				return m;
			}
		}
		return null;
	}
	
	public List<Mod> getMods() {
		return this.getData();
	}

	@Override
	public void save() {
		JSONObject json = new JSONObject();
		for (Mod mod : getMods()) {
			JSONObject modData = new JSONObject();
			modData.put("toggled", mod.isToggled());
			modData.put("keybind", mod.getKeybind());
			modData.put("visible", mod.doesDisplay());
			for (Value value : ValueManager.INSTANCE.getValues(mod)) {
				modData.put(value.getID(), value.getValue());
			}
			json.append(mod.getName(), modData);
		}
		String folder = "./" + Impact.getInstance().getName();
		String file = folder + "/mods.json";
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
				FileWriter write = new FileWriter(file, false);
				PrintWriter printer = new PrintWriter(write);
				
				String gay = json.toString();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonElement je = new JsonParser().parse(gay);
				String cool = gson.toJson(je);
				
				printer.write(cool);
				printer.close();
			} catch (IOException e) {
			}
		}
	}
}
