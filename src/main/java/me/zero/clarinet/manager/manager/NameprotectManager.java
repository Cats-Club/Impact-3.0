package me.zero.clarinet.manager.manager;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.render.EventNameprotect;
import me.zero.clarinet.manager.Manager;
import me.zero.clarinet.util.Helper;
import me.zero.clarinet.util.io.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Doogie13
 * @since 09/04/2023
 */
public class NameprotectManager extends Manager<String> implements Helper {

    public String nameProtect = "";

    public NameprotectManager() {
        super("Nameprotect");
        EventManager.register(this);
    }

    @Override
    public void load() {
        List<String> data = FileUtil.read(Impact.getInstance().getFile("alts"));

        for (String piece : data) {
            nameProtect = piece;
            return;
        }
    }

    @Override
    public void save() {
        List<String> data = new ArrayList<>();
        data.add(nameProtect);
        FileUtil.write(data, Impact.getInstance().getFile("alts"));
    }

    @EventTarget
    public void onNameprotect(EventNameprotect event) {

        if (!nameProtect.equalsIgnoreCase("")) {
            event.setMessage(event.getMessage().replace(mc.getSession().getUsername(), nameProtect));
        }

    }

}
