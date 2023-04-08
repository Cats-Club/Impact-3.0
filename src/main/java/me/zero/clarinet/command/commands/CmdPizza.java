package me.zero.clarinet.command.commands;

import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.util.ClientUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CmdPizza extends AbstractCommand {

    @Override
    public boolean run(String message, String[] args) {
        if (args.length == 1) {
            if (Desktop.isDesktopSupported()) {
                String pizzaPlace = args[0];
                try {
                    if (pizzaPlace.equalsIgnoreCase("dominos")) {
                        Desktop.getDesktop().browse(new URI("https://www.dominos.com/en/pages/order/#/section/Food/category/AllEntrees/"));
                    } else if (pizzaPlace.equalsIgnoreCase("pizzahut")) {
                        Desktop.getDesktop().browse(new URI("https://www.pizzahut.com/#/menu/pizza/popular-pizzas"));
                    } else {
                        ClientUtils.error("Invalid Pizza Place!");
                    }
                } catch (IOException | URISyntaxException e) {
                    ClientUtils.error("Unable to order!");
                }
            } else {
                ClientUtils.error("Unable to order!");
            }
            return true;
        }
        return false;
    }

    @Override
    protected String[] names() {
        return new String[]{ "pizza" };
    }

    @Override
    protected String description() {
        return "Order a pizza!";
    }

    @Override
    protected CommandUsage[] usage() {
        return new CommandUsage[] {
                new CommandUsage("pizzahut", "Order a Pizza from Pizzahut"),
                new CommandUsage("dominos", "Order a Pizza from Domino's")
        };
    }
}
