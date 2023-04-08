package me.zero.clarinet.ui.screen.override.screen;

import me.zero.clarinet.Impact;
import me.zero.clarinet.command.AbstractCommand;
import me.zero.clarinet.command.CommandOptions;
import me.zero.clarinet.util.render.ColorUtils;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;

public class OverrideGuiChat extends GuiChat {
	
	public OverrideGuiChat(String defaultText) {
		super(defaultText);
        this.mc = Minecraft.getMinecraft();
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        String prefix = String.valueOf(CommandOptions.getPrefix());
		if (this.inputField.getText().startsWith(prefix)) {
			int cl = ColorUtils.getColorCode('9');
			RenderUtils.rectangleBordered(2, this.height - 14, this.width - 2, this.height - 2, 1, cl, 0xCF000000);
			for (AbstractCommand cmd : Impact.getInstance().getCommandManager().getCommands()) {
                /**
				if ((AbstractCommand.getPrefix() + cmd.getName()).startsWith(inputField.getText()) || (Command.getPrefix() + cmd.getUsage()).startsWith(inputField.getText()) || (Command.getPrefix() + cmd.getName()).equalsIgnoreCase(inputField.getText())) {
					String msg = Command.getPrefix() + cmd.getUsage() + " - " + cmd.getDescription();
                    mc.fontRenderer.drawString(msg, 4, sr.getScaledHeight() - 12, -1);
					break;
				} else if (cmd.getSubCommands().size() > 0) {
					for (SubCommand subcmd : cmd.getSubCommands()) {
						String total = Command.getPrefix() + cmd.getName() + " " + subcmd.getName();
						if (total.startsWith(inputField.getText())) {
							String msg = total + " - " + subcmd.getDescription();
                            mc.fontRenderer.drawString(msg, 4, sr.getScaledHeight() - 12, -1);
							break;
						}
					}
				}
                 */
			}
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
