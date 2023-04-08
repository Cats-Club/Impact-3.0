package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.command.*;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class CommandHandler implements ICommandManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<String, net.minecraft.command.ICommand> commandMap = Maps.<String, net.minecraft.command.ICommand>newHashMap();
    private final Set<net.minecraft.command.ICommand> commandSet = Sets.<net.minecraft.command.ICommand>newHashSet();

    /**
     * Attempt to execute a command. This method should return the number of times that the command was executed. If the
     * command does not exist or if the player does not have permission, 0 will be returned. A number greater than 1 can
     * be returned if a player selector is used.
     */
    public int executeCommand(ICommandSender sender, String rawCommand)
    {
        rawCommand = rawCommand.trim();

        if (rawCommand.startsWith("/"))
        {
            rawCommand = rawCommand.substring(1);
        }

        String[] astring = rawCommand.split(" ");
        String s = astring[0];
        astring = dropFirstString(astring);
        net.minecraft.command.ICommand icommand = (net.minecraft.command.ICommand)this.commandMap.get(s);
        int i = this.getUsernameIndex(icommand, astring);
        int j = 0;

        if (icommand == null)
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.generic.notFound", new Object[0]);
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation);
        }
        else if (icommand.checkPermission(this.getServer(), sender))
        {
            if (i > -1)
            {
                List<Entity> list = EntitySelector.<Entity>matchEntities(sender, astring[i], Entity.class);
                String s1 = astring[i];
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());

                for (Entity entity : list)
                {
                    astring[i] = entity.getCachedUniqueIdString();

                    if (this.tryExecute(sender, astring, icommand, rawCommand))
                    {
                        ++j;
                    }
                }

                astring[i] = s1;
            }
            else
            {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);

                if (this.tryExecute(sender, astring, icommand, rawCommand))
                {
                    ++j;
                }
            }
        }
        else
        {
            TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.generic.permission", new Object[0]);
            textcomponenttranslation1.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation1);
        }

        sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
        return j;
    }

    protected boolean tryExecute(ICommandSender sender, String[] args, net.minecraft.command.ICommand command, String input)
    {
        try
        {
            command.execute(this.getServer(), sender, args);
            return true;
        }
        catch (WrongUsageException wrongusageexception)
        {
            TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.generic.usage", new Object[] {new TextComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorObjects())});
            textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation2);
        }
        catch (CommandException commandexception)
        {
            TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            textcomponenttranslation1.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation1);
        }
        catch (Throwable var9)
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.generic.exception", new Object[0]);
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation);
            LOGGER.warn("Couldn\'t process command: \'{}\'", new Object[] {input});
        }

        return false;
    }

    protected abstract MinecraftServer getServer();

    /**
     * adds the command and any aliases it has to the internal map of available commands
     */
    public net.minecraft.command.ICommand registerCommand(net.minecraft.command.ICommand command)
    {
        this.commandMap.put(command.getCommandName(), command);
        this.commandSet.add(command);

        for (String s : command.getCommandAliases())
        {
            net.minecraft.command.ICommand icommand = (net.minecraft.command.ICommand)this.commandMap.get(s);

            if (icommand == null || !icommand.getCommandName().equals(s))
            {
                this.commandMap.put(s, command);
            }
        }

        return command;
    }

    /**
     * creates a new array and sets elements 0..n-2 to be 0..n-1 of the input (n elements)
     */
    private static String[] dropFirstString(String[] input)
    {
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }

    public List<String> getTabCompletionOptions(ICommandSender sender, String input, @Nullable BlockPos pos)
    {
        String[] astring = input.split(" ", -1);
        String s = astring[0];

        if (astring.length == 1)
        {
            List<String> list = Lists.<String>newArrayList();

            for (Entry<String, net.minecraft.command.ICommand> entry : this.commandMap.entrySet())
            {
                if (CommandBase.doesStringStartWith(s, (String)entry.getKey()) && ((net.minecraft.command.ICommand)entry.getValue()).checkPermission(this.getServer(), sender))
                {
                    list.add(entry.getKey());
                }
            }

            return list;
        }
        else
        {
            if (astring.length > 1)
            {
                net.minecraft.command.ICommand icommand = (net.minecraft.command.ICommand)this.commandMap.get(s);

                if (icommand != null && icommand.checkPermission(this.getServer(), sender))
                {
                    return icommand.getTabCompletionOptions(this.getServer(), sender, dropFirstString(astring), pos);
                }
            }

            return Collections.<String>emptyList();
        }
    }

    public List<net.minecraft.command.ICommand> getPossibleCommands(ICommandSender sender)
    {
        List<net.minecraft.command.ICommand> list = Lists.<net.minecraft.command.ICommand>newArrayList();

        for (net.minecraft.command.ICommand icommand : this.commandSet)
        {
            if (icommand.checkPermission(this.getServer(), sender))
            {
                list.add(icommand);
            }
        }

        return list;
    }

    public Map<String, net.minecraft.command.ICommand> getCommands()
    {
        return this.commandMap;
    }

    /**
     * Return a command's first parameter index containing a valid username.
     */
    private int getUsernameIndex(ICommand command, String[] args)
    {
        if (command == null)
        {
            return -1;
        }
        else
        {
            for (int i = 0; i < args.length; ++i)
            {
                if (command.isUsernameIndex(args, i) && EntitySelector.matchesMultiplePlayers(args[i]))
                {
                    return i;
                }
            }

            return -1;
        }
    }
}
