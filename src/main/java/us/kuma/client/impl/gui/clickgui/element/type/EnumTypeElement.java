/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.gui.clickgui.element.type;

import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;
import us.kuma.client.api.render.Element;
import us.kuma.client.api.setting.EnumSetting;
import us.kuma.client.api.setting.Setting;

import java.util.StringJoiner;

/**
 * @author xgraza
 * @since 1/30/26
 */
public final class EnumTypeElement extends Element
{
    private final Setting<Enum<?>> setting;

    public EnumTypeElement(Setting<Enum<?>> setting)
    {
        this.setting = setting;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY)
    {
        graphics.drawString(MC.font, setting.getName(), (int) (x + 3), (int) (y + 2), -1);

        int textWidth = MC.font.width(getEnumName());
        graphics.drawString(MC.font, getEnumName(), (int) (x + getWidth() - textWidth), (int) (y + 2), -1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, boolean doubleClick)
    {
        if (button == GLFW.GLFW_MOUSE_BUTTON_1)
        {
            ((EnumSetting<?>) setting).nextValue();
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_2)
        {
            ((EnumSetting<?>) setting).previousValue();
        }
        return true;
    }

    private String getEnumName()
    {
        final String string = setting.getValue().toString();
        if (!string.equals(setting.getValue().name()))
        {
            return string;
        }

        final StringJoiner joiner = new StringJoiner(" ");
        for (String word : string.split("_"))
        {
            joiner.add(Character.toUpperCase(word.charAt(0))
                    + word.substring(1).toLowerCase());
        }
        return joiner.toString();
    }
}
