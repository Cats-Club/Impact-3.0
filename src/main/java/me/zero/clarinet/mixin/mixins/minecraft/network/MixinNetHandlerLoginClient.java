package me.zero.clarinet.mixin.mixins.minecraft.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.mcleaks.MCLeaks;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(NetHandlerLoginClient.class)
public class MixinNetHandlerLoginClient {

    // breaks multiplayer, MCLeaks is deprecated
    /*
    String string = null;

    @Shadow @Final private NetworkManager networkManager;

    @Redirect(method = "handleEncryptionRequest", at = @At(value = "INVOKE", target = "Ljava/math/BigInteger;toString(I)Ljava/lang/String;"))
    public String handleEncryptionRequestBI(BigInteger instance, int i) {
        // we need this to set the string to be used in the later mixin
        return string = instance.toString(i);
    }

    @Inject(method = "handleEncryptionRequest", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/minecraft/MinecraftSessionService;joinServer(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/lang/String;)V"), cancellable = true, remap = false)
    public void handleEncryptionRequest(SPacketEncryptionRequest packetIn, CallbackInfo ci) {

        if (MCLeaks.isAltActive()) {
            String mcLeaksSession = MCLeaks.getMCLeaksSession();
            String mcName = MCLeaks.getMCName();
            String server = ((InetSocketAddress) networkManager.getRemoteAddress()).getHostName() + ":" + ((InetSocketAddress) networkManager.getRemoteAddress()).getPort();
            try {
                String jsonBody = "{\"session\":\"" + mcLeaksSession + "\",\"mcname\":\"" + mcName + "\",\"serverhash\":\"" + *//*s1*//* string + "\",\"server\":\"" + server + "\"}";

                HttpURLConnection connection = (HttpURLConnection) new URL("http://auth.mcleaks.net/v1/joinserver").openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(jsonBody.getBytes(StandardCharsets.UTF_8)); // "UTF-8"
                outputStream.flush();
                outputStream.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                reader.close();

                JsonElement jsonElement = new Gson().fromJson(out.toString(), JsonElement.class);
                if ((!jsonElement.isJsonObject()) || (!jsonElement.getAsJsonObject().has("success"))) {
                    networkManager.closeChannel(new TextComponentString("Invalid response from MCLeaks API"));
                    ci.cancel();
                }
                if (!jsonElement.getAsJsonObject().get("success").getAsBoolean()) {
                    String errorMessage = "Received success=false from MCLeaks API";
                    if (jsonElement.getAsJsonObject().has("errorMessage")) {
                        errorMessage = jsonElement.getAsJsonObject().get("errorMessage").getAsString();
                    }
                    networkManager.closeChannel(new TextComponentString(errorMessage));
                    ci.cancel();
                }
            } catch (Exception e) {
                networkManager.closeChannel(new TextComponentString("Error whilst contacting MCLeaks API: " + e));
                ci.cancel();
            }
        }
        ci.cancel();
    }*/
    
}
