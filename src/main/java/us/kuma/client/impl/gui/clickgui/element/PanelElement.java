/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.gui.clickgui.element;

import net.minecraft.client.gui.GuiGraphics;
import us.kuma.client.api.manager.module.Module;
import us.kuma.client.api.manager.module.trait.ModuleCategory;
import us.kuma.client.api.render.Element;
import us.kuma.client.api.render.Parent;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xgraza
 * @since 1/29/26
 */
public final class PanelElement extends Element implements Parent<ModuleElement>
{
    private static final int PANEL_COLOR = new Color(167, 101, 248, 180).getRGB();
    private static final int BG_COLOR = new Color(63, 63, 63, 180).getRGB();

    private static final int PADDING = 1;
    private static final int PANEL_WIDTH = 120;
    private static final int PANEL_HEADER_HEIGHT = 15;
    private static final int PANEL_MAX_HEIGHT = 0;

    private final List<ModuleElement> children = new LinkedList<>();
    private final ModuleCategory category;

    public PanelElement(final ModuleCategory category, final List<Module> moduleList)
    {
        this.category = category;
        for (final Module module : moduleList)
        {
            addChild(new ModuleElement(module));
        }
        setWidth(PANEL_WIDTH);
        setHeight(PANEL_HEADER_HEIGHT);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY)
    {
        graphics.fill((int) x, (int) y, (int) (x + PANEL_WIDTH), (int) (y + PANEL_HEADER_HEIGHT), PANEL_COLOR);
        graphics.fill((int) x, (int) (y + PANEL_HEADER_HEIGHT), (int) (x + PANEL_WIDTH), (int) (y + getHeight()), BG_COLOR);
        graphics.renderOutline((int) x, (int) y, PANEL_WIDTH, (int) getHeight(), PANEL_COLOR);
        graphics.drawString(MC.font, category.toString(), (int) x + 2, (int) y + 4, -1);

        double elementY = getY() + PANEL_HEADER_HEIGHT + 1;
        for (ModuleElement child : getChildren())
        {
            child.setX(x + 2);
            child.setY(elementY);
            child.setWidth(PANEL_WIDTH - (PADDING * 4));
            child.render(graphics, mouseX, mouseY);
            elementY += child.getHeight() + PADDING;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, boolean doubleClick)
    {
        for (ModuleElement child : getChildren())
        {
            if (child.mouseInside(mouseX, mouseY)
                    && child.mouseClicked(mouseX, mouseY, button, modifiers, doubleClick))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public double getWidth()
    {
        return PANEL_WIDTH;
    }

    @Override
    public double getHeight()
    {
        double height = PANEL_HEADER_HEIGHT + PADDING;
        for (final ModuleElement element : getChildren())
        {
            height += element.getHeight() + 1;
        }
        return height + PADDING;
    }

    @Override
    public void addChild(ModuleElement child)
    {
        children.add(child);
    }

    @Override
    public List<ModuleElement> getChildren()
    {
        return children;
    }
}
