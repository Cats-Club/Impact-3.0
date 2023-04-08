package me.zero.clarinet.command.commands;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.TimerUtil;
import me.zero.clarinet.util.io.FileUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CmdSniffer extends AbstractCommand {

    private boolean sniffing;

    private String currentPlayer;

    private List<String> buffer = new ArrayList<>();

    private TimerUtil timer = new TimerUtil();

    public CmdSniffer() {
        super();
        EventManager.register(this);
    }

    @Override
    public boolean run(String message, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("stop")) {
                if (sniffing) {
                    ClientUtils.message("Stopped Sniffing " + TextFormatting.BLUE + currentPlayer);

                    String pattern = "MdHms";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    Date currentDate = new Date();
                    String date = format.format(currentDate);

                    String file = "./" + Impact.getInstance().getName() + "/sniff/" + date + "_" + currentPlayer + ".log";
                    FileUtil.write(buffer, file);

                    try {
                        Desktop.getDesktop().open(new File(file));
                    } catch (Exception e) {}

                    sniffing = false;
                    currentPlayer = null;
                    buffer.clear();
                } else {
                    ClientUtils.error("Not Sniffing!");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("player")){
                if (args.length > 1) {
                    if (!sniffing) {
                        sniffing = true;
                        currentPlayer = args[1];
                        ClientUtils.message("Started Sniffing " + TextFormatting.BLUE + currentPlayer);
                        timer.reset();
                    } else {
                        ClientUtils.message("Already Sniffing " + TextFormatting.BLUE + currentPlayer);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected String[] names() {
        return new String[]{ "sniff" };
    }

    @Override
    protected String description() {
        return "Packet sniff a player";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("player <player>", "Starts sniffing player"),
                new CommandUsage("stop", "Stops sniffing")
        };
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (sniffing && currentPlayer != null) {
            for (Entity e : mc.world.loadedEntityList) {
                if (e.getName().equalsIgnoreCase(currentPlayer) && e instanceof EntityPlayer) {
                    String format = "%s - pos[X:%s Y:%s Z:%s Yaw:%s Pitch:%s], motion[X:%s Y:%s Z:%s]";

                    Object[] args = {
                            timer.getDiff(),
                            e.posX,
                            e.posY,
                            e.posZ,
                            MathHelper.wrapDegrees(e.rotationYaw),
                            MathHelper.wrapDegrees(e.rotationPitch),
                            e.motionX,
                            e.motionY,
                            e.motionZ
                    };

                    buffer.add(String.format(format, args));
                    break;
                }
            }
        }
    }
}
