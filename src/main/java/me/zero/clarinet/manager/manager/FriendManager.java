package me.zero.clarinet.manager.manager;

import java.util.List;
import java.util.stream.Collectors;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.render.EventNameprotect;
import me.zero.clarinet.event.render.EventNametag;
import me.zero.clarinet.util.io.FileUtil;
import me.zero.clarinet.manager.Manager;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.util.text.TextFormatting;

public class FriendManager extends Manager<FriendManager.Friend> {

	public FriendManager() {
		super("Friend");
	}

    @Override
    public void load() {
        List<String> data = FileUtil.read(Impact.getInstance().getFile("friends"));
        for (String piece : data) {
            String[] split = piece.split(":");
            if (split.length == 2) {
                String name = split[0];
                String alias = split[1];
                this.addFriend(new Friend(name, alias));
            }
        }
    }

    @Override
    public void save() {
        List<String> data = this.getData().stream().map(friend -> friend.name + ":" + friend.alias).collect(Collectors.toList());
        FileUtil.write(data, Impact.getInstance().getFile("friends"));
    }

    public void addFriend(Friend friend) {
        if (!isFriend(friend.name)) {
            this.addData(friend);
            ClientUtils.message("Added " + friend.name + " as a friend!");
            return;
        }
        ClientUtils.error("§cThat person is already your friend!");
    }

    public void removeFriend(String name) {
        if (isFriend(name)) {
            for (int i = 0; i < getFriends().size(); i++) {
                Friend friend = getFriends().get(i);
                if (friend.name.equalsIgnoreCase(name)) {
                    getData().remove(friend);
                    ClientUtils.message("Removed " + friend.name + " as a friend!");
                    return;
                }
            }
        }
        ClientUtils.error("§cThat person is not your friend!");
    }
	
	public boolean isFriend(String name) {
		for (Friend friend : getFriends()) {
            if (friend.name.equalsIgnoreCase(name)) {
                return true;
            }
        }
		return false;
	}

	@EventTarget
	public void onNameProtect(EventNameprotect event) {
		String message = event.getMessage();
		for (Friend friend : getFriends()) {
			if (message.contains(friend.getName())) {
				message = message.replaceAll(friend.getName(), friend.getAlias());
			}
		}
		event.setMessage(message);
	}
	
	@EventTarget
	public void onRenderName(EventNametag event) {
		String name = event.getRenderName();
		for (Friend friend : getFriends()) {
			name = name.replaceAll(friend.getAlias(), TextFormatting.AQUA + friend.getAlias());
		}
		event.setRenderName(name);
	}
	
	public String getAliasName(String name) {
		for (Friend friend : getFriends()) {
			if (friend.name.equalsIgnoreCase(name)) {
				return friend.alias;
			}
		}
		return name;
	}

	public List<Friend> getFriends() {
        return this.getData();
    }

    public static class Friend {

        public String name, alias;

        public Friend(String name) {
            this(name, name);
        }

        public Friend(String name, String alias) {
            this.name = name;
            this.alias = alias;
        }

        public String getName() {
            return name;
        }

        public String getAlias() {
            return alias;
        }
    }
}
