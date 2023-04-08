package me.zero.clarinet.command.commands;

import me.zero.clarinet.command.AbstractCommand;
import net.minecraft.inventory.ClickType;

public class CmdDrop extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (args.length == 1) {
            String mode = args[0];
            if (mode.equalsIgnoreCase("hotbar")) {
                for (int i = 0; i < 9; i++) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 1, ClickType.THROW, mc.player);
                }
                return true;
            } else if (mode.equalsIgnoreCase("hand")) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, mc.player.inventory.currentItem, 1, ClickType.THROW, mc.player);
                return true;
            }
        }
        return false;
    }

    @Override
    protected String[] names() {
        return new String[] { "drop" };
    }

    @Override
    protected String description() {
        return "Drops Items";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[]{
                new CommandUsage("hotbar", "Drops all items on your hotbar"),
                new CommandUsage("hand", "Drops all items in your hand")
        };
    }
}
