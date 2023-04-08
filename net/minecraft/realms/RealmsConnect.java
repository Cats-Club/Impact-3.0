package net.minecraft.realms;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsConnect
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final net.minecraft.realms.RealmsScreen onlineScreen;
    private volatile boolean aborted;
    private NetworkManager connection;

    public RealmsConnect(RealmsScreen onlineScreenIn)
    {
        this.onlineScreen = onlineScreenIn;
    }

    public void connect(final String p_connect_1_, final int p_connect_2_)
    {
        net.minecraft.realms.Realms.setConnectedToRealms(true);
        (new Thread("Realms-connect-task")
        {
            public void run()
            {
                InetAddress inetaddress = null;

                try
                {
                    inetaddress = InetAddress.getByName(p_connect_1_);

                    if (RealmsConnect.this.aborted)
                    {
                        return;
                    }

                    RealmsConnect.this.connection = NetworkManager.createNetworkManagerAndConnect(inetaddress, p_connect_2_, Minecraft.getMinecraft().gameSettings.isUsingNativeTransport());

                    if (RealmsConnect.this.aborted)
                    {
                        return;
                    }

                    RealmsConnect.this.connection.setNetHandler(new NetHandlerLoginClient(RealmsConnect.this.connection, Minecraft.getMinecraft(), RealmsConnect.this.onlineScreen.getProxy()));

                    if (RealmsConnect.this.aborted)
                    {
                        return;
                    }

                    RealmsConnect.this.connection.sendPacket(new C00Handshake(210, p_connect_1_, p_connect_2_, EnumConnectionState.LOGIN));

                    if (RealmsConnect.this.aborted)
                    {
                        return;
                    }

                    RealmsConnect.this.connection.sendPacket(new CPacketLoginStart(Minecraft.getMinecraft().getSession().getProfile()));
                }
                catch (UnknownHostException unknownhostexception)
                {
                    net.minecraft.realms.Realms.clearResourcePack();

                    if (RealmsConnect.this.aborted)
                    {
                        return;
                    }

                    RealmsConnect.LOGGER.error((String)"Couldn\'t connect to world", (Throwable)unknownhostexception);
                    net.minecraft.realms.Realms.setScreen(new DisconnectedRealmsScreen(RealmsConnect.this.onlineScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] {"Unknown host \'" + p_connect_1_ + "\'"})));
                }
                catch (Exception exception)
                {
                    net.minecraft.realms.Realms.clearResourcePack();

                    if (RealmsConnect.this.aborted)
                    {
                        return;
                    }

                    RealmsConnect.LOGGER.error((String)"Couldn\'t connect to world", (Throwable)exception);
                    String s = exception.toString();

                    if (inetaddress != null)
                    {
                        String s1 = inetaddress + ":" + p_connect_2_;
                        s = s.replaceAll(s1, "");
                    }

                    Realms.setScreen(new DisconnectedRealmsScreen(RealmsConnect.this.onlineScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] {s})));
                }
            }
        }).start();
    }

    public void abort()
    {
        this.aborted = true;

        if (this.connection != null && this.connection.isChannelOpen())
        {
            this.connection.closeChannel(new TextComponentTranslation("disconnect.genericReason", new Object[0]));
            this.connection.checkDisconnected();
        }
    }

    public void tick()
    {
        if (this.connection != null)
        {
            if (this.connection.isChannelOpen())
            {
                this.connection.processReceivedPackets();
            }
            else
            {
                this.connection.checkDisconnected();
            }
        }
    }
}
