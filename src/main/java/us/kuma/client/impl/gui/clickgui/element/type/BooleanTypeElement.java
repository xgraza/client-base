/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.gui.clickgui.element.type;

import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;
import us.kuma.client.api.render.Element;
import us.kuma.client.api.setting.Setting;

import java.awt.Color;

/**
 * @author xgraza
 * @since 1/30/26
 */
public final class BooleanTypeElement extends Element
{
    private static final int UNTOGGLED_COLOR = new Color(42, 42, 42, 180).getRGB();
    private static final int TOGGLE_COLOR = new Color(167, 101, 248, 180).getRGB();
    private static final int PADDING = 1;

    private final Setting<Boolean> setting;

    public BooleanTypeElement(final Setting<Boolean> setting)
    {
        this.setting = setting;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY)
    {
        drawCheckbox(graphics);
        graphics.drawString(MC.font, setting.getName(), (int) (x + 3), (int) (y + 2), -1);
    }

    private void drawCheckbox(final GuiGraphics graphics)
    {
        final double checkboxSize = height - (PADDING * 2);
        final double checkboxX = x + width - checkboxSize - PADDING;
        final double checkboxY = y + PADDING;
        graphics.fill((int) checkboxX,
                (int) checkboxY,
                (int) (checkboxX + checkboxSize),
                (int) (checkboxY + checkboxSize),
                setting.getValue() ? TOGGLE_COLOR : UNTOGGLED_COLOR);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, boolean doubleClick)
    {
        if (button == GLFW.GLFW_MOUSE_BUTTON_1)
        {
            setting.setValue(!setting.getValue());
            return true;
        }
        return false;
    }
}
