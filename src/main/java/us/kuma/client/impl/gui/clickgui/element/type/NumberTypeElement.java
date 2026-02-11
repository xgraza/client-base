/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.gui.clickgui.element.type;

import net.minecraft.client.gui.GuiGraphics;
import us.kuma.client.api.render.Element;
import us.kuma.client.api.setting.NumberSetting;
import us.kuma.client.util.math.MathUtil;

import java.awt.Color;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

/**
 * @author xgraza
 * @since 02/11/26
 */
public final class NumberTypeElement extends Element
{
    private static final int SLIDER_COLOR = new Color(167, 101, 248, 180).getRGB();

    private final NumberSetting<?> setting;
    private final double difference;
    private boolean dragging;

    public NumberTypeElement(final NumberSetting<?> setting)
    {
        this.setting = setting;
        this.difference = setting.getMax().doubleValue() - setting.getMin().doubleValue();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY)
    {
        if (dragging)
        {
            setValue(mouseX);
        }

        double part = (setting.getValue().doubleValue() - setting.getMin().doubleValue()) / difference;
        double barWidth = setting.getValue().doubleValue() < setting.getMin().doubleValue() ? 0.0 : (getWidth() - 2.0) * part;
        graphics.fill((int) x, (int) y, (int) (x + barWidth), (int) (y + height), SLIDER_COLOR);

        graphics.drawString(MC.font, setting.getName(), (int) (x + 3), (int) (y + 2), -1);

        String value = String.valueOf(setting.getValue());
        int textWidth = MC.font.width(value);
        graphics.drawString(MC.font, value, (int) (x + getWidth() - textWidth), (int) (y + 2), -1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, boolean doubleClick)
    {
        if (button == GLFW_MOUSE_BUTTON_1)
        {
            dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int button, int modifiers)
    {
        dragging = false;
        return false;
    }

    @SuppressWarnings("unchecked")
    private void setValue(final int mouseX)
    {
        double value = setting.getMin().doubleValue() + difference * (mouseX - getX()) / getWidth();
        final double precision = 1.0 / setting.getScale().doubleValue();
        value = Math.round(value * precision) / precision;
        value = MathUtil.round(value, 2);

        if (value > setting.getMax().doubleValue())
        {
            value = setting.getMax().doubleValue();
        }

        if (value < setting.getMin().doubleValue())
        {
            value = setting.getMin().doubleValue();
        }

        if (Integer.class.isAssignableFrom(setting.getType()))
        {
            ((NumberSetting<Integer>) setting).setValue((int) value);
        } else if (Double.class.isAssignableFrom(setting.getType()))
        {
            ((NumberSetting<Double>) setting).setValue(value);
        } else if (Float.class.isAssignableFrom(setting.getType()))
        {
            ((NumberSetting<Float>) setting).setValue((float) value);
        } else if (Long.class.isAssignableFrom(setting.getType()))
        {
            ((NumberSetting<Long>) setting).setValue((long) value);
        }
    }
}
