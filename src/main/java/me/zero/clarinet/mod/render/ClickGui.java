package me.zero.clarinet.mod.render;

import me.zero.clarinet.ui.click.classic.ClickGuiManager;
import me.zero.clarinet.ui.click.impact.ImpactClickGuiManager;
import me.zero.clarinet.ui.click.impact.component.ClickFrame;
import me.zero.clarinet.ui.click.para.ParaClickGuiManager;
import me.zero.clarinet.util.io.FileUtil;
import me.zero.values.types.MultiValue;
import net.minecraft.util.math.Vec2f;
import org.apache.logging.log4j.core.appender.FileManager;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.ui.click.para.components.LFrame;
import me.zero.clarinet.ui.click.classic.elements.Frame;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickGui extends Mod {

    private MultiValue<String> mode = new MultiValue<>(this, "Mode", "mode", "New", new String[] { "New", "Para", "Classic" });

	public BooleanValue snap = new BooleanValue(this, "Snap", "snap", false);
	public BooleanValue blur = new BooleanValue(this, "Background Blur", "blur", false);
	
	public NumberValue blur_speed = new NumberValue(this, "Blur Speed", "blurSpeed", 3D, 1D, 5D, 0.1D);
	public NumberValue blur_amount = new NumberValue(this, "Blur", "blurAmount", 40D, 0D, 100D, 1D);

    public ClickGuiManager classicClick;
    private ParaClickGuiManager paraClick;
    private ImpactClickGuiManager impactClick;

    private Map<String, FrameState> classicFrames, paraFrames, impactFrames = new HashMap<>();
	
	public ClickGui() {
		super("ClickGui", "Clickable Gui, used to manage module options", Keyboard.KEY_RSHIFT, Category.RENDER);

        classicFrames = parse(FileUtil.read(Impact.getInstance().getFile("gui/classic")));
        paraFrames = parse(FileUtil.read(Impact.getInstance().getFile("gui/para")));
        impactFrames = parse(FileUtil.read(Impact.getInstance().getFile("gui/new")));
	}
	
	@Override
	public void onEnable() {
		saveGui();
		if (mc.player != null) {

            if (classicClick == null) classicClick = new ClickGuiManager();
            if (paraClick == null) paraClick = new ParaClickGuiManager();
            if (impactClick == null) impactClick = new ImpactClickGuiManager();

            switch(mode.getValue().toLowerCase()) {
                case "para": {
                    mc.displayGuiScreen(paraClick);
                    break;
                }
                case "classic": {
                    mc.displayGuiScreen(classicClick);
                    break;
                }
                case "new": {
                    mc.displayGuiScreen(impactClick);
                    break;
                }
            }
        }
		this.toggle();
	}
	
	private void saveGui() {
        new Thread("GUI Saving Thread") {
            @Override
            public void run() {
                if (classicClick != null) {
                    List<String> data = new ArrayList<>();
                    for (Frame frame : classicClick.frames) {
                        data.add(frame.getTitle() + ":" + frame.getX() + ":" + frame.getY() + ":" + frame.open);
                    }
                    FileUtil.write(data, Impact.getInstance().getFile("gui/classic"));
                }

                if (paraClick != null) {
                    List<String> data = new ArrayList<>();
                    for (LFrame frame : paraClick.getFrames()) {
                        data.add(frame.getTitle() + ":" + frame.getX() + ":" + frame.getY() + ":" + frame.isOpen());
                    }
                    FileUtil.write(data, Impact.getInstance().getFile("gui/para"));
                }

                if (impactClick != null) {
                    List<String> data = new ArrayList<>();
                    for (ClickFrame frame : impactClick.getFrames()) {
                        data.add(frame.getTitle() + ":" + frame.getX() + ":" + frame.getY() + ":" + frame.isOpen());
                    }
                    FileUtil.write(data, Impact.getInstance().getFile("gui/new"));
                }
            }
        };
	}

	private Map<String, FrameState> parse(List<String> list) {
        Map<String, FrameState> map = new HashMap<>();
        for (String s : list) {
            String[] split = s.split(":");
            if (split.length == 4) {
                try {
                    String title = split[0];
                    Integer x = Integer.valueOf(split[1]);
                    Integer y = Integer.valueOf(split[2]);
                    Boolean open = Boolean.valueOf(split[3]);
                    map.put(title, new FrameState(x, y, open));
                } catch (Exception e) {}
            }
        }
        return map;
    }

    public FrameState getState(Frame frame) {
        for (String title : classicFrames.keySet()) {
            if (title.equalsIgnoreCase(frame.getTitle())) {
                return classicFrames.get(title);
            }
        }
        return null;
    }

    public FrameState getState(LFrame frame) {
        for (String title : paraFrames.keySet()) {
            if (title.equalsIgnoreCase(frame.getTitle())) {
                return paraFrames.get(title);
            }
        }
        return null;
    }

    public FrameState getState(ClickFrame frame) {
        for (String title : impactFrames.keySet()) {
            if (title.equalsIgnoreCase(frame.getTitle())) {
                return impactFrames.get(title);
            }
        }
        return null;
    }

	public static class FrameState {

        public final int x, y;
        public final boolean open;

        public FrameState(int x, int y, boolean open) {
            this.x = x;
            this.y = y;
            this.open = open;
        }
    }
}
