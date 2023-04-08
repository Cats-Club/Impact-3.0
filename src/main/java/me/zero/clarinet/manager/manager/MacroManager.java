package me.zero.clarinet.manager.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.zero.clarinet.Impact;
import me.zero.clarinet.util.io.FileUtil;
import me.zero.clarinet.manager.Manager;

public class MacroManager extends Manager<MacroManager.Macro> {
	
	public MacroManager() {
		super("Macro");
	}

    @Override
    public void load() {
        List<String> data = FileUtil.read(Impact.getInstance().getFile("macros"));
        for (String piece : data) {
            String[] split = piece.split(":");
            if (split.length == 2) {
                try {
                    Integer key = Integer.valueOf(split[0]);
                    String[] values = split[1].split(";");
                    this.addMacro(new Macro(key, values));
                } catch(Exception e) {}
            }
        }
    }

    @Override
    public void save() {
        List<String> data = new ArrayList<>();
        for (Macro macro : this.getData()) {
            data.add(macro.toString());
        }
        FileUtil.write(data, Impact.getInstance().getFile("macros"));
    }

    private void addMacro(Macro macro) {
        addMacro(macro.getKey(), macro.getMessages());
	}

    public void addMacro(int key, String[] values) {
		if (getMacro(key) != null) {
			int i = 0;
			for (Macro macro : getMacros()) {
				if (macro.getKey() == key) {
					this.getMacros().set(i, new Macro(key, values));
                    return;
				}
				i++;
			}
		} else {
			this.getMacros().add(new Macro(key, values));
		}
	}
	
	private Macro getMacro(int key) {
		for (Macro macro : getMacros()) {
			if (macro.getKey() == key) {
                return macro;
            }
		}
		return null;
	}
	
	public void removeMacro(int key) {
		if (getMacro(key) != null) {
            for (int i = 0; i < getMacros().size(); i++) {
                Macro macro = getMacros().get(i);
                if (macro.getKey() == key) {
                    getMacros().remove(macro);
                    break;
                }
            }
		}
	}
	
	public List<Macro> getMacros() {
		return this.getData();
	}

    public static class Macro {

        private int key;
        private String[] messages;

        public Macro(int key, String[] messages) {
            this.key = key;
            List<String> fix = new ArrayList<>();
            for (int i = 0; i < messages.length; i++) {
                String msg = messages[i];
                if (!msg.trim().isEmpty()) {
                    fix.add(msg);
                }
            }
            this.messages = fix.toArray(new String[0]);
        }

        public int getKey() {
            return this.key;
        }

        public String[] getMessages() {
            return this.messages;
        }

        @Override
        public String toString() {
            String meme = "";
            for (int i = 0; i < this.messages.length; i++) {
                meme += this.messages[i] + (i >= this.messages.length - 1 ? "" : ";");
            }
            return this.key + ":" + meme;
        }
    }

}
