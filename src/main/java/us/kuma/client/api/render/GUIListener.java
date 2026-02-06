/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.render;

import net.minecraft.client.gui.GuiGraphics;

/**
 * @author xgraza
 * @since 1/29/26
 */
public interface GUIListener
{
    void render(final GuiGraphics graphics, final int mouseX, final int mouseY);

    default boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, boolean doubleClick)
    {
        return true;
    }
}
