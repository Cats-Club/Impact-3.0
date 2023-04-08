package me.zero.clarinet.ui.click.impact.component;

import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.BooleanValue;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ClickCheckbox extends ClickComponent {

    private BooleanValue value;

    private float hoverTime, enabledTime = 0F;

	public ClickCheckbox(BooleanValue value) {
		this.value = value;
        this.title = value.getName();
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
        float textColor = Math.min(enabledTime + 0.5F + (hoverTime * 0.3F), 1F);

        font.drawStringWithShadow(title, x + 30, y + 9, new Color(textColor, textColor, textColor, 1.0F).getRGB());

        RenderUtils.rectangle(x, y, x + width, y + height, new Color(0, 0, 0, hoverTime * 0.1F).getRGB());

        float padding = 6;
        float size = 38;
        int color = RenderUtils.blend(0xFF5BC94F, 0xFFC23030, enabledTime);

        int centerX = x + width - 23;
        int centerY = y + 17;

        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glLineWidth(1.0F);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glBegin(GL_LINE_STRIP);
        glVertex2d(centerX, centerY);

        if (enabledTime > 0.0F) {
            float meme1 = (Math.min(enabledTime, 0.5F)) * 16.0F;
            double stage1X = Math.cos(Math.toRadians(45)) * meme1 + centerX;
            double stage1Y = Math.sin(Math.toRadians(45)) * meme1 + centerY;
            glVertex2d(stage1X, stage1Y);
            if (enabledTime > 0.5F) {
                float meme2 = enabledTime * 16.0F;
                double stage2X = Math.cos(Math.toRadians(-65)) * meme2 + stage1X;
                double stage2Y = Math.sin(Math.toRadians(-65)) * meme2 + stage1Y;
                glVertex2d(stage2X, stage2Y);
            }
        }

        glEnd();
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
	}
	
	@Override
	public void onUpdate(int mouseX, int mouseY) {
        this.width = 220 - 4;
        this.height = 35;

        float change = 0.008F;
        hoverTime += this.isInside(mouseX, mouseY) ? change : -change;
        hoverTime = Math.max(0F, Math.min(1F, hoverTime));

        change = 0.005F;
        enabledTime += this.value.getValue() ? change : -change;
        enabledTime = Math.max(0F, Math.min(1F, enabledTime));
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.value.setValue(!this.value.getValue());
            }
        }
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {}
}
