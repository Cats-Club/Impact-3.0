package me.zero.clarinet.manager.manager;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

import java.time.OffsetDateTime;

/**
 * @author Doogie13
 * @since 24/04/2023
 */
@SuppressWarnings("resource")
public enum DiscordRPCManager {

    INSTANCE;


    public void init() {

        // NOT Impact's RPC, it's custom for this
        IPCClient client = new IPCClient(1100112127215280249L);

        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client, User user) {
                client.sendRichPresence(
                        new RichPresence.Builder()
                                .setState("https://github.com/Cats-Club/Impact-3.0")
                                .setStartTimestamp(OffsetDateTime.now())
                                .setLargeImage("impact", "Impact Utility Mod")
                                .build()
                );

            }
        });

        try {
            client.connect();
        } catch (NoDiscordClientException ignored) {
        }

    }

}
