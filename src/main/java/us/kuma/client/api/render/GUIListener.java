/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * @author xgraza
 * @since 1/29/26
 */
public interface GUIListener
{
    void render(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY);

    default boolean mouseClicked(double x, double y, int button, int modifiers, boolean doubleClick)
    {
        return false;
    }

    default boolean mouseReleased(double x, double y, int button, int modifiers)
    {
        return false;
    }
}
