package me.zero.clarinet.mod.movement;

import me.zero.clarinet.ui.click.impact.ImpactClickGuiManager;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.ui.click.para.ParaClickGuiManager;
import me.zero.clarinet.ui.click.classic.ClickGuiManager;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;

public class InventoryMove extends Mod {
	
	private BooleanValue look = new BooleanValue(this, "Look Around", "look", true);
	
	private NumberValue yawSpeed = new NumberValue(this, "Yaw Speed", "yaw", 6D, 2D, 12D, 0.5D);
	
	private NumberValue pitchSpeed = new NumberValue(this, "Pitch Speed", "pitch", 4D, 2D, 12D, 0.5D);
	
	public InventoryMove() {
		super("InventoryMove", "Move whilst in your inventory", Keyboard.KEY_NONE, Category.MOVEMENT);
	}

	// Doogie13 - originally an EventMotionUpdate but that made no sense and didn't work
	@SubscribeEvent
	public void call(InputUpdateEvent event) {
		if (mc.currentScreen instanceof GuiContainer || mc.currentScreen instanceof ClickGuiManager || mc.currentScreen instanceof ParaClickGuiManager || mc.currentScreen instanceof ImpactClickGuiManager) {
			if (look.getValue()) {
				if (Keyboard.isKeyDown(200)) {
					mc.player.rotationPitch -= pitchSpeed.getValue().floatValue();
				}
				if (Keyboard.isKeyDown(208)) {
					mc.player.rotationPitch += pitchSpeed.getValue().floatValue();
				}
				if (Keyboard.isKeyDown(203)) {
					mc.player.rotationYaw -= yawSpeed.getValue().floatValue();
				}
				if (Keyboard.isKeyDown(205)) {
					mc.player.rotationYaw += yawSpeed.getValue().floatValue();
				}
			}

            KeyBinding[] moveKeys = new KeyBinding[] { mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };
            KeyBinding[] array = moveKeys;
            int length = array.length;
            int i2 = 0;
            while (i2 < length) {
                KeyBinding key = array[i2];
                KeyBinding.setKeyBindState(key.getKeyCode(), Keyboard.isKeyDown(key.getKeyCode()));
                ++i2;
            }
            return;
		}
	}
}
