package me.zero.clarinet.manager.manager;

import me.zero.clarinet.Impact;
import me.zero.clarinet.alt.AccountManagementException;
import me.zero.clarinet.alt.Alt;
import me.zero.clarinet.util.io.FileUtil;
import me.zero.clarinet.manager.Manager;

import java.util.ArrayList;
import java.util.List;

public class AltManager extends Manager<Alt> {
	
	public final int slotHeight = 25;
	
	public AltManager() {
		super("Alt");
	}

    @Override
    public void load() {
        List<String> data = FileUtil.read(Impact.getInstance().getFile("alts"));

        for (String piece : data) {
            String[] split = piece.split(":");
            if (split.length == 2) {
                String name = split[0];
                String password = split[1];
                this.addAlt(new Alt(name, password));
            } else if (split.length == 1) {
                this.addAlt(new Alt(split[0]));
            }
        }
    }

    @Override
    public void save() {
        List<String> data = new ArrayList<>();
        for (Alt alt : this.getAlts()) {
            if (alt.isPremium()) {
                try {
                    data.add(alt.getUsername() + ":" + alt.getPassword());
                } catch (AccountManagementException e){}
            } else {
                data.add(alt.getUsername());
            }
        }
        FileUtil.write(data, Impact.getInstance().getFile("alts"));
    }
	
	public String makePassChar(String regex) {
		return regex.replaceAll("(?s).", "*");
	}
	
	public Alt getAlt(int index) {
		return this.getAlts().get(index);
	}

	public void removeAlt(int index) {
        this.getAlts().remove(index);
    }

	public void addAlt(Alt alt) {
		try {
			if (alt.isPremium()) {
				this.addData(new Alt(alt.getUsername(), alt.getPassword()));
			} else {
				this.addData(new Alt(alt.getUsername()));
			}
		} catch (AccountManagementException e) {
		}
	}
	
	public boolean isAlt(Alt alt) {
		for (Alt data : this.getAlts()) {
            if (alt.getUsername().equalsIgnoreCase(alt.getUsername())) {
                return true;
            }
		}
		return false;
	}
	
	public int getPremiumAlts() {
        int alts = 0;
        for (Alt alt : getAlts()) {
            if (alt.isPremium()) {
                alts++;
            }
        }
        return alts;
	}
	
	public int getCrackedAlts() {
		int alts = 0;
		for (Alt alt : getAlts()) {
            if (!alt.isPremium()) {
                alts++;
            }
        }
		return alts;
	}
	
	public int getAltSize() {
		return this.getAlts().size();
	}

	public List<Alt> getAlts() {
        return this.getData();
    }
}
