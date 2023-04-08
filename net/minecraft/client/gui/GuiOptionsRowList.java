package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionsRowList extends net.minecraft.client.gui.GuiListExtended
{
    private final List<Row> options = Lists.<Row>newArrayList();

    public GuiOptionsRowList(Minecraft mcIn, int p_i45015_2_, int p_i45015_3_, int p_i45015_4_, int p_i45015_5_, int p_i45015_6_, GameSettings.Options... p_i45015_7_)
    {
        super(mcIn, p_i45015_2_, p_i45015_3_, p_i45015_4_, p_i45015_5_, p_i45015_6_);
        this.centerListVertically = false;

        for (int i = 0; i < p_i45015_7_.length; i += 2)
        {
            GameSettings.Options gamesettings$options = p_i45015_7_[i];
            GameSettings.Options gamesettings$options1 = i < p_i45015_7_.length - 1 ? p_i45015_7_[i + 1] : null;
            net.minecraft.client.gui.GuiButton guibutton = this.createButton(mcIn, p_i45015_2_ / 2 - 155, 0, gamesettings$options);
            net.minecraft.client.gui.GuiButton guibutton1 = this.createButton(mcIn, p_i45015_2_ / 2 - 155 + 160, 0, gamesettings$options1);
            this.options.add(new Row(guibutton, guibutton1));
        }
    }

    private net.minecraft.client.gui.GuiButton createButton(Minecraft mcIn, int p_148182_2_, int p_148182_3_, GameSettings.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.returnEnumOrdinal();
            return (net.minecraft.client.gui.GuiButton)(options.getEnumFloat() ? new GuiOptionSlider(i, p_148182_2_, p_148182_3_, options) : new net.minecraft.client.gui.GuiOptionButton(i, p_148182_2_, p_148182_3_, options, mcIn.gameSettings.getKeyBinding(options)));
        }
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public Row getListEntry(int index)
    {
        return (Row)this.options.get(index);
    }

    protected int getSize()
    {
        return this.options.size();
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return 400;
    }

    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 32;
    }

    public static class Row implements IGuiListEntry
    {
        private final Minecraft client = Minecraft.getMinecraft();
        private final net.minecraft.client.gui.GuiButton buttonA;
        private final net.minecraft.client.gui.GuiButton buttonB;

        public Row(net.minecraft.client.gui.GuiButton buttonAIn, GuiButton buttonBIn)
        {
            this.buttonA = buttonAIn;
            this.buttonB = buttonBIn;
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
        {
            if (this.buttonA != null)
            {
                this.buttonA.yPosition = y;
                this.buttonA.drawButton(this.client, mouseX, mouseY);
            }

            if (this.buttonB != null)
            {
                this.buttonB.yPosition = y;
                this.buttonB.drawButton(this.client, mouseX, mouseY);
            }
        }

        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.buttonA.mousePressed(this.client, mouseX, mouseY))
            {
                if (this.buttonA instanceof net.minecraft.client.gui.GuiOptionButton)
                {
                    this.client.gameSettings.setOptionValue(((net.minecraft.client.gui.GuiOptionButton)this.buttonA).returnEnumOptions(), 1);
                    this.buttonA.displayString = this.client.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(this.buttonA.id));
                }

                return true;
            }
            else if (this.buttonB != null && this.buttonB.mousePressed(this.client, mouseX, mouseY))
            {
                if (this.buttonB instanceof net.minecraft.client.gui.GuiOptionButton)
                {
                    this.client.gameSettings.setOptionValue(((GuiOptionButton)this.buttonB).returnEnumOptions(), 1);
                    this.buttonB.displayString = this.client.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(this.buttonB.id));
                }

                return true;
            }
            else
            {
                return false;
            }
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.buttonA != null)
            {
                this.buttonA.mouseReleased(x, y);
            }

            if (this.buttonB != null)
            {
                this.buttonB.mouseReleased(x, y);
            }
        }

        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
        {
        }
    }
}
