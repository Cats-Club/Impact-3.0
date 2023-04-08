package me.zero.clarinet.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.user.UserManager;
import net.minecraft.util.Session;

public final class MojangUtils implements Helper {
	
	public static void login(String username, String password) throws Exception {
		YggdrasilAuthenticationService authService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		YggdrasilUserAuthentication userAuth = (YggdrasilUserAuthentication) authService.createUserAuthentication(Agent.MINECRAFT);
		userAuth.setUsername(username);
		userAuth.setPassword(password);
		userAuth.logIn();
		((IMinecraft) mc).setSession(new Session(userAuth.getSelectedProfile().getName(), userAuth.getSelectedProfile().getId().toString(), userAuth.getAuthenticatedToken(), "mojang"));
	}
	
	public static void stealSession(String input) {
		if ((input.length() != 65) || (!input.substring(32, 33).equals(":")) || (input.split(":").length != 2)) {
			return;
		}
		String uuid = input.split(":")[1];
		if (uuid.contains("-")) {
			return;
		}
		JsonElement rawJson;
		try {
			rawJson = new JsonParser().parse(new InputStreamReader(new URL("https://api.mojang.com/user/profiles/" + uuid + "/names").openConnection().getInputStream()));
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace();
			return;
		}
		if (!rawJson.isJsonArray()) {
			return;
		}
		JsonArray json = rawJson.getAsJsonArray();
		String name = json.get(json.size() - 1).getAsJsonObject().get("name").getAsString();
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("https://authserver.mojang.com/validate").openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			String content = "{\"accessToken\":\"" + input.split(":")[0] + "\"}";
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			output.writeBytes(content);
			output.flush();
			output.close();
			if (connection.getResponseCode() != 204) {
				throw new IOException();
			}
		} catch (IOException e) {
			return;
		}
		((IMinecraft) mc).setSession(new Session(name, uuid, input.split(":")[0], "mojang"));
	}
}
