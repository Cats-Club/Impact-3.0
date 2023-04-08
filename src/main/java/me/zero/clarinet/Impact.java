package me.zero.clarinet;

import me.zero.clarinet.command.executer.CmdExecuter;
import me.zero.clarinet.event.Handler;
import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.game.EventKey;
import me.zero.clarinet.manager.Manager;
import me.zero.clarinet.manager.manager.*;
import me.zero.clarinet.ui.TabGui;
import me.zero.clarinet.ui.font.CFontRenderer;
import me.zero.clarinet.ui.font.GlobalFontRenderer;
import me.zero.clarinet.ui.screen.GuiUpdate;
import me.zero.clarinet.user.UserManager;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.TPSWatcher;
import me.zero.clarinet.waypoints.WaypointManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

@Mod(modid = "impact", name = Impact.CLIENT_NAME, version = "3.0")
public class Impact {
	
	private static Minecraft mc = Minecraft.getMinecraft();

	// originally private final, now public static final
	/* private */ public static final String CLIENT_NAME = "Impact";
	private final String[] CLIENT_AUTHORS = { "Zero", "Whispers", "Dinavi" };
	private final Double CLIENT_BUILD = 3.0D;
	private static Impact INSTANCE;

	public FontRenderer baseFontRenderer;

	private TabGui tabgui;
	
	private ModManager modmanager;
	private AltManager altManager;
	private CommandManager commandManager;
	private ConfigManager configManager;
	private FriendManager friendManager;
	private XrayManager xrayManager;
	private MacroManager macroManager;
	private NukerManager nukerManager;
	private WaypointManager waypointManager;
    private PluginManager pluginManager;

	private boolean disabled = false;
	
	public Impact() {
		// moved to onStart

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		Logger.getAnonymousLogger().info("Impact 3.0 for 1.12.2 Loaded");

		baseFontRenderer = mc.fontRenderer; // mc.fontRenderer;

		EventManager.start();
		FontManager.load();

		INSTANCE = this;

		configManager = new ConfigManager();
		configManager.load();

		modmanager = new ModManager();
		modmanager.load();

		commandManager = new CommandManager();
		commandManager.load();

		waypointManager = new WaypointManager();
		waypointManager.load();

		pluginManager = new PluginManager();
		pluginManager.load();

		altManager = new AltManager();
		altManager.load();

		friendManager = new FriendManager();
		friendManager.load();

		xrayManager = new XrayManager();
		xrayManager.load();

		macroManager = new MacroManager();
		macroManager.load();

		nukerManager = new NukerManager();
		nukerManager.load();

		tabgui = new TabGui();

		UserManager.load();
		GuiUpdate.checkForUpdate();
		registerEvents();

		String gfont_font = Impact.getInstance().getConfigManager().getValue("globalfont-font");
		boolean gfont = Impact.getInstance().getConfigManager().getValueBoolean("globalfont", false);
		int gfont_style = Impact.getInstance().getConfigManager().getValueInteger("globalfont-style", Font.PLAIN);
		if (gfont_font != null && gfont) {
			mc.fontRenderer = new GlobalFontRenderer(new CFontRenderer(new Font(gfont_font, gfont_style, GlobalFontRenderer.SIZE), true, false));
		}

		Runtime.getRuntime().addShutdownHook(new Thread(this::saveFiles));
	}

	private void registerEvents() {
		ClientUtils.log("[Init] Registering Events");
		EventManager.register(new Handler());
		EventManager.register(new CmdExecuter());
        EventManager.register(new TPSWatcher());
		EventManager.register(friendManager);
		EventManager.register(tabgui);
	}

	private void saveFiles() {
		Manager.saveAll();
	}
	
	public void disable() {
		saveFiles();
		disabled = true;
		EventManager.stop();
	}
	
	public String getName() {
		return this.CLIENT_NAME;
	}
	
	public String[] getClientAuthors() {
		return this.CLIENT_AUTHORS;
	}
	
	public Double getBuild() {
		return this.CLIENT_BUILD;
	}
	
	public static Impact getInstance() {
		return INSTANCE;
	}
	
	public ModManager getModManager() {
		return this.modmanager;
	}
	
	public ConfigManager getConfigManager() {
		return this.configManager;
	}
	
	public AltManager getAltManager() {
		return this.altManager;
	}
	
	public CommandManager getCommandManager() {
		return this.commandManager;
	}

	public FriendManager getFriendManager() {
		return this.friendManager;
	}
	
	public MacroManager getMacroManager() {
		return this.macroManager;
	}
	
	public NukerManager getNukerManager() {
		return this.nukerManager;
	}
	
	public WaypointManager getWaypointManager() {
		return this.waypointManager;
	}

	public PluginManager getPluginManager() {
        return this.pluginManager;
	}
	
	public boolean isDisabled() {
		return this.disabled;
	}
	
	public XrayManager getXrayManager() {
		return this.xrayManager;
	}
	
	public TabGui getTabGui() {
		return this.tabgui;
	}

    public String getFile(String name) {
        return "./" + CLIENT_NAME + "/" + name + ".cfg";
    }
	
	public static URL getURL(String data) {
		try {
			return new URL("http://impactdevelopment.weebly.com/files/theme/data_" + data + ".js");
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public static boolean isObfuscated() {
		return !Impact.class.getSimpleName().equalsIgnoreCase("Impact");
	}
}
