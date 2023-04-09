package me.zero.clarinet.ui.screen;

import me.zero.clarinet.Impact;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

/**
 * @author Doogie13
 * @since 09/04/2023
 */
public class ImpactGuiNameprotect extends GuiScreen {

    private final GuiScreen old;
    private GuiTextField textField;
    private GuiButton button;

    public ImpactGuiNameprotect(GuiImpact guiImpact) {
        this.old = guiImpact;
    }

    @Override
    public void initGui() {
        this.textField = new GuiTextField(0, mc.fontRenderer, width / 2 - 100, height / 2, 200, 12);
        textField.setFocused(true);
        textField.setText(Impact.getInstance().getNameprotectManager().nameProtect);
        addButton(this.button = new GuiButton(1, width / 2 - 80, height / 2 - 12, 160, 12, "Confirm"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        textField.drawTextBox();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        textField.setFocused(true);
        textField.textboxKeyTyped(typedChar, keyCode);
        Impact.getInstance().getNameprotectManager().nameProtect = textField.getText();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 1)
            mc.displayGuiScreen(old);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Impact.getInstance().getNameprotectManager().save();
    }
}
