package me.zero.clarinet.command.commands;

import java.util.Random;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.waypoints.Waypoint;
import net.minecraft.util.text.TextFormatting;

public class CmdWaypoint extends AbstractCommand {


    @Override
    public boolean run(String message, String[] args) {
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length > 0) {
                char[] identifiers = { 'r', 'g', 'b', 'x', 'y', 'z' };
                Double[] values = new Double[identifiers.length];
                for (String arg : args) {
                    if (!arg.equalsIgnoreCase("add")) {
                        for (int i = 0; i < identifiers.length; i++) {
                            char ch = identifiers[i];
                            String header = ch + ":";
                            if (arg.startsWith(header)) {
                                try {
                                    Double val = Double.valueOf(arg.replaceFirst(header, ""));
                                    values[i] = val;
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }
                for (int i = 3; i < values.length; i++) {
                    if (values[i] == null) {
                        values[3] = mc.player.posX;
                        values[4] = mc.player.posY;
                        values[5] = mc.player.posZ;
                        break;
                    }
                }
                for (int i = 0; i < 3; i++) {
                    if (values[i] == null) {
                        values[i] = (double) new Random().nextInt(255);
                    }
                }
                if (!Impact.getInstance().getWaypointManager().isWaypoint(args[1])) {
                    try {
                        Waypoint wp = new Waypoint(args[1], values[3], values[4], values[5], (int) (double) values[0], (int) (double) values[1], (int) (double) values[2]);
                        Impact.getInstance().getWaypointManager().addWaypoint(wp);
                        ClientUtils.message("Added Waypoint, " + TextFormatting.BLUE + args[1]);
                    } catch (Exception e) {
                        ClientUtils.error("Unable to add Waypoint!");
                    }
                } else {
                    ClientUtils.error("A Waypoint with that name already exists!");
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("del")) {
            if (args.length == 2) {
                boolean delete = Impact.getInstance().getWaypointManager().deleteWaypoint(args[1]);
                if (delete) {
                    ClientUtils.message("Deleted Waypoint, " + TextFormatting.BLUE + args[1]);
                } else {
                    ClientUtils.error("Unable to find a Waypoint with that name!");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected String[] names() {
        return new String[]{ "waypoint" };
    }

    @Override
    protected String description() {
        return "Manage waypoints";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("add <name> r:[r] g:[g] b:[b] x:[x] y:[y] z:[z]", "Adds a waypoint"),
                new CommandUsage("del <name>", "Removes a waypoint")
        };
    }
}
