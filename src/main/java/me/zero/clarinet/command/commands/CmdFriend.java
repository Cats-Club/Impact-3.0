package me.zero.clarinet.command.commands;

import me.zero.clarinet.Impact;
import me.zero.clarinet.manager.manager.FriendManager.Friend;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.command.AbstractCommand;
import net.minecraft.util.text.TextFormatting;

public class CmdFriend extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length > 0) {
                if (args[1].length() > 0) {
                    if (args.length == 2) {
                        Impact.getInstance().getFriendManager().addFriend(new Friend(args[1]));
                    } else {
                        Impact.getInstance().getFriendManager().addFriend(new Friend(args[1], args[2]));
                    }
                    return true;
                } else {
                    ClientUtils.error("Name must be more than 0 characters long!");
                }
            }
        } else if (args[0].equalsIgnoreCase("del")) {
            if (args.length > 0) {
                Impact.getInstance().getFriendManager().removeFriend(args[1]);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 1) {
                ClientUtils.message(TextFormatting.GRAY + "List of Friends " + TextFormatting.BLUE + "(" + Impact.getInstance().getFriendManager().getFriends().size() + ")");
                String theList = "";
                for (Friend friend : Impact.getInstance().getFriendManager().getFriends()) {
                    TextFormatting col = TextFormatting.GRAY;
                    if (Impact.getInstance().getFriendManager().getFriends().indexOf(friend) != Impact.getInstance().getFriendManager().getFriends().size() - 1) {
                        theList += col + friend.getName() + TextFormatting.GRAY + ", ";
                    } else {
                        theList += col + friend.getName() + TextFormatting.GRAY + ".";
                    }
                }
                ClientUtils.message(theList);
                return true;
            }
        }
        return false;
    }

    @Override
    public String[] names() {
        return new String[] { "friend", "f" };
    }

    @Override
    public String description() {
        return "Manage your list of friends";
    }

    @Override
    public CommandUsage[] usage() {
        return new CommandUsage[] {
                new CommandUsage("del <name>", "Remove a friend"),
                new CommandUsage("add <name>", "Add a friend"),
                new CommandUsage("list", "List friends")
        };
    }
}
