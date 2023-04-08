package net.minecraft.command;

import net.minecraft.command.CommandException;

public class CommandNotFoundException extends CommandException
{
    public CommandNotFoundException()
    {
        this("commands.generic.notFound", new Object[0]);
    }

    public CommandNotFoundException(String message, Object... args)
    {
        super(message, args);
    }
}
