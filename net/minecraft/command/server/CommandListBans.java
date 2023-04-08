package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListBans extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "banlist";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     */
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return (server.getPlayerList().getBannedIPs().isLanServer() || server.getPlayerList().getBannedPlayers().isLanServer()) && super.checkPermission(server, sender);
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.banlist.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length >= 1 && "ips".equalsIgnoreCase(args[0]))
        {
            sender.addChatMessage(new TextComponentTranslation("commands.banlist.ips", new Object[] {Integer.valueOf(server.getPlayerList().getBannedIPs().getKeys().length)}));
            sender.addChatMessage(new TextComponentString(joinNiceString(server.getPlayerList().getBannedIPs().getKeys())));
        }
        else
        {
            sender.addChatMessage(new TextComponentTranslation("commands.banlist.players", new Object[] {Integer.valueOf(server.getPlayerList().getBannedPlayers().getKeys().length)}));
            sender.addChatMessage(new TextComponentString(joinNiceString(server.getPlayerList().getBannedPlayers().getKeys())));
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"players", "ips"}): Collections.<String>emptyList();
    }
}
