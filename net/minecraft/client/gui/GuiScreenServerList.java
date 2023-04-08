package net.minecraft.client.gui;

import java.io.IOException;

import me.zero.clarinet.util.ServerHook;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiScreenServerList extends net.minecraft.client.gui.GuiScreen
{
    private final net.minecraft.client.gui.GuiScreen lastScreen;
    private final ServerData serverData;
    private net.minecraft.client.gui.GuiTextField ipEdit;

    public GuiScreenServerList(GuiScreen p_i1031_1_, ServerData p_i1031_2_)
    {
        this.lastScreen = p_i1031_1_;
        this.serverData = p_i1031_2_;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.ipEdit.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new net.minecraft.client.gui.GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new net.minecraft.client.gui.GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
        this.ipEdit = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 116, 200, 20);
        this.ipEdit.setMaxStringLength(128);
        this.ipEdit.setFocused(true);
        this.ipEdit.setText(this.mc.gameSettings.lastServer);
        ((net.minecraft.client.gui.GuiButton)this.buttonList.get(0)).enabled = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.mc.gameSettings.lastServer = this.ipEdit.getText();
        this.mc.gameSettings.saveOptions();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(net.minecraft.client.gui.GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                this.lastScreen.confirmClicked(false, 0);
            }
            else if (button.id == 0)
            {
                this.serverData.serverIP = this.ipEdit.getText();

                // TODON'T: Impact
                ServerHook.updateLastServerFromDirectConnect((GuiMultiplayer) lastScreen, serverData);

                this.lastScreen.confirmClicked(true, 0);
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.ipEdit.textboxKeyTyped(typedChar, keyCode))
        {
            ((net.minecraft.client.gui.GuiButton)this.buttonList.get(0)).enabled = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
        }
        else if (keyCode == 28 || keyCode == 156)
        {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.ipEdit.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("selectServer.direct", new Object[0]), this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), this.width / 2 - 100, 100, 10526880);
        this.ipEdit.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
