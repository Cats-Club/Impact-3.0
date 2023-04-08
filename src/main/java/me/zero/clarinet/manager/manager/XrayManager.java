package me.zero.clarinet.manager.manager;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.Impact;
import me.zero.clarinet.util.Helper;
import me.zero.clarinet.util.io.FileUtil;
import me.zero.clarinet.manager.Manager;
import me.zero.clarinet.mod.world.Xray;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.block.Block;
import net.minecraft.util.text.TextFormatting;

public class XrayManager extends Manager<Integer> implements Helper {

	public XrayManager() {
		super("Xray");
	}

    @Override
    public void load() {
        List<String> data = FileUtil.read(Impact.getInstance().getFile("xray"));
        for (String piece : data) {
            try {
                Integer id = Integer.valueOf(piece);
                this.addData(id);
            } catch (Exception e){}
        }
    }

    @Override
    public void save() {
        List<String> data = new ArrayList<>();
        for (Integer id : this.getData()) {
            data.add(String.valueOf(id));
        }
        FileUtil.write(data, Impact.getInstance().getFile("xray"));
    }

    public void addBlock(Block block) {
		this.addBlock(Block.getIdFromBlock(block));
	}
	
	public void addBlock(int id) {
		if (!isBlock(id)) {
			this.addData(id);
			if (Impact.getInstance().getModManager().get(Xray.class).isToggled()) {
				mc.renderGlobal.loadRenderers();
			}
			ClientUtils.message("Added " + TextFormatting.BLUE + Block.getBlockById(id).getLocalizedName() + TextFormatting.GRAY + " to the Xray Block list!");
		} else {
			ClientUtils.error("That block is already a Xray Block!");
		}
	}
	
	public void removeBlock(Block block) {
		this.removeBlock(Block.getIdFromBlock(block));
	}
	
	public void removeBlock(int id) {
		if (isBlock(id)) {
			this.removeData(id);
			ClientUtils.message("Removed " + TextFormatting.BLUE + Block.getBlockById(id).getLocalizedName() + TextFormatting.GRAY + " from the Xray Block list!");
            if (Impact.getInstance().getModManager().get(Xray.class).isToggled()) {
                mc.renderGlobal.loadRenderers();
            }
		} else {
			ClientUtils.error("That block isn't a Xray Block!");
		}
	}
	
	public boolean isBlock(int id) {
		for (int blockid : getBlocks()) {
			if (blockid == id) {
				return true;
			}
		}
		return false;
	}
	
	public List<Integer> getBlocks() {
		return this.getData();
	}
}
