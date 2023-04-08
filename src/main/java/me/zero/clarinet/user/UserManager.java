package me.zero.clarinet.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import me.zero.clarinet.Impact;
import me.zero.clarinet.util.io.OnlineFileReader;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.client.Minecraft;

public class UserManager {
	
	private static ArrayList<UserHandler> managers = new ArrayList<>();
	private static boolean premium = false;

	public static UserHandler dmanager;
	public static UserHandler pmanager;
    public static UserHandler pepsi;
	
	public static void load() {
		ClientUtils.log("[Data] Loading User Lists");

        managers.add(dmanager = new UserHandler(Impact.getURL("developer"), "https://i.imgur.com/ywZHbwX.png"));
        managers.add(pepsi = new UserHandler(Impact.getURL("pepsi"), "http://i.imgur.com/adiTjUM.png"));
        managers.add(pmanager = new UserHandler(Impact.getURL("premium"), "https://i.imgur.com/JPmtDcO.png"));

		isPremium();
	}
	
	public static String getCapeURL(UUID uuid) {
		for (UserHandler handler : managers) {
			for (UUID user : handler.users) {
				if (user.equals(uuid)) {
					return handler.capeURL;
				}
			}
		}
		return null;
	}
	
	public static boolean hasCape(UUID uuid) {
		return getCapeURL(uuid) != null;
	}
	
	public static boolean isPremium() {
		if (pmanager.isUser(Minecraft.getMinecraft().getSession().getPlayerID()) || dmanager.isUser(Minecraft.getMinecraft().getSession().getPlayerID()) || !Impact.isObfuscated()) {
			premium = true;
		}
		return premium;
	}

	public static boolean isPepsi() {
        return pepsi.isUser(Minecraft.getMinecraft().getSession().getPlayerID());
    }

    public static class UserHandler {

        private ArrayList<UUID> users = new ArrayList<>();

        public final String capeURL;

        private UserHandler(URL url, String capeURL) {
            this.capeURL = capeURL;
            OnlineFileReader reader = new OnlineFileReader(url);
            reader.read();
            for (String str : reader.getRead()) {
                UUID uuid = UUID.fromString(str);
                if (uuid != null) {
                    users.add(uuid);
                }
            }
        }

        public boolean isUser(String uuid) {
            for (UUID user : users) {
                if (user.toString().replaceAll("-", "").equalsIgnoreCase(uuid.replaceAll("-", ""))) {
                    return true;
                }
            }
            return false;
        }
    }
}
