package me.zero.clarinet.manager.manager;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.Impact;
import me.zero.clarinet.util.io.FileUtil;
import me.zero.clarinet.manager.Manager;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.block.Block;
import net.minecraft.util.text.TextFormatting;

public class NukerManager extends Manager<Integer> {

    public NukerManager() {
        super("Nuker");
    }

    @Override
    public void load() {
        List<String> data = FileUtil.read(Impact.getInstance().getFile("nuker"));
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
        FileUtil.write(data, Impact.getInstance().getFile("nuker"));
    }

    public void addBlock(Block block) {
        this.addBlock(Block.getIdFromBlock(block));
    }

    public void addBlock(int id) {
        if (!isBlock(id)) {
            this.addData(id);
            ClientUtils.message("Added " + TextFormatting.BLUE + Block.getBlockById(id).getLocalizedName() + TextFormatting.GRAY + " to the Nuker Block list!");
        } else {
            ClientUtils.error("That block is already a Nuker Block!");
        }
    }

    public void removeBlock(Block block) {
        this.removeBlock(Block.getIdFromBlock(block));
    }

    public void removeBlock(int id) {
        if (isBlock(id)) {
            this.removeData(id);
            ClientUtils.message("Removed " + TextFormatting.BLUE + Block.getBlockById(id).getLocalizedName() + TextFormatting.GRAY + " from the Xray Block list!");
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
