/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.gui.clickgui.element;

import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;
import us.kuma.client.api.manager.module.Module;
import us.kuma.client.api.render.Element;
import us.kuma.client.api.render.Parent;
import us.kuma.client.api.setting.Setting;
import us.kuma.client.impl.gui.clickgui.element.type.BooleanTypeElement;
import us.kuma.client.impl.gui.clickgui.element.type.EnumTypeElement;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public final class ModuleElement extends Element implements Parent<Element>
{
    private static final int PANEL_COLOR = new Color(167, 101, 248, 180).getRGB();

    private static final int PADDING = 1;
    private static final int ELEMENT_BASE_HEIGHT = 12;

    private final List<Element> children = new LinkedList<>();
    private final Module module;
    private boolean opened;

    @SuppressWarnings("unchecked")
    public ModuleElement(final Module module)
    {
        this.module = module;
        setHeight(ELEMENT_BASE_HEIGHT);

        for (Setting<?> setting : module.getSettings())
        {
            if (Boolean.class.isAssignableFrom(setting.getType()))
            {
                addChild(new BooleanTypeElement((Setting<Boolean>) setting));
            } else if (Enum.class.isAssignableFrom(setting.getType()))
            {
                addChild(new EnumTypeElement((Setting<Enum<?>>) setting));
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY)
    {
        if (module.isToggled())
        {
            graphics.fill((int) x, (int) y, (int) (x + getWidth()), (int) (y + ELEMENT_BASE_HEIGHT), PANEL_COLOR);
        }

        graphics.drawString(MC.font, module.getName(), (int) (x + 3), (int) (y + 2), -1);
       
        String symbol = opened ? "-" : "+";
        int textWidth = MC.font.width(symbol) + 3;
        graphics.drawString(MC.font, symbol, (int) (x + getWidth() - textWidth), (int) (y + 2), -1);

        if (!opened)
        {
            return;
        }

        graphics.renderOutline((int) x, (int) y, (int) getWidth(), (int) getHeight(), PANEL_COLOR);

        double elementY = getY() + ELEMENT_BASE_HEIGHT + PADDING;
        for (Element child : getChildren())
        {
            child.setX(getX() + (PADDING * 2));
            child.setY(elementY);
            child.setWidth(getWidth() - (PADDING * 4));
            child.setHeight(ELEMENT_BASE_HEIGHT);
            child.render(graphics, mouseX, mouseY);
            elementY += child.getHeight() + PADDING;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, boolean doubleClick)
    {
        if (!mouseInside(mouseX, mouseY))
        {
            return false;
        }

        if (mouseInside(mouseX, mouseY, getWidth(), ELEMENT_BASE_HEIGHT))
        {
            if (button == GLFW.GLFW_MOUSE_BUTTON_1)
            {
                module.setState(!module.isToggled());
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_2)
            {
                opened = !opened;
            }

            return true;
        } else
        {
            if (!opened)
            {
                return false;
            }
            for (Element child : getChildren())
            {
                if (child.mouseInside(mouseX, mouseY) && child.mouseClicked(mouseX, mouseY, button, modifiers, doubleClick))
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public double getHeight()
    {
        if (!opened)
        {
            return ELEMENT_BASE_HEIGHT;
        }
        double height = ELEMENT_BASE_HEIGHT + PADDING;
        for (Element child : getChildren())
        {
            height += child.getHeight() + PADDING;
        }
        return height + PADDING;
    }

    @Override
    public void addChild(Element child)
    {
        children.add(child);
    }

    @Override
    public List<Element> getChildren()
    {
        return children;
    }
}
