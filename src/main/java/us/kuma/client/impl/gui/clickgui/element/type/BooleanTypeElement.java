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
    private static final int PANEL_COLOR = new Color(167, 101, 248, 180).getRGB();

    private final Setting<Boolean> setting;

    public BooleanTypeElement(final Setting<Boolean> setting)
    {
        this.setting = setting;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY)
    {
        if (setting.getValue())
        {
            graphics.fill((int) x, (int) y, (int) (x + getWidth()), (int) (y + getHeight()), PANEL_COLOR);
        }
        graphics.drawString(MC.font, setting.getName(), (int) (x + 3), (int) (y + 2), -1);
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
