package me.zero.clarinet.command.commands;

import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;

import java.util.Random;

public class CmdMeme extends AbstractCommand {

    private final String[] memes = {
            "{ boolean, kill yourself.hey;",
            "Raise your pepsi",
            "Boy I'm stanced up",
            "how2dab",
            "nyaPvp",
            "0x22"
    };

    @Override
    public boolean run(String message, String[] args) {
        ClientUtils.message(memes[new Random().nextInt(memes.length - 1)]);
        return true;
    }

    @Override
    protected String[] names() {
        return new String[]{ "meme" };
    }

    @Override
    protected String description() {
        return "Gives you a spicy meme";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[0];
    }
}
