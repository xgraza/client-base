/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.event.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * @param graphics
 * @author xgraza
 * @since 02/12/26
 */
public record RenderHUDEvent(GuiGraphicsExtractor graphics)
{
}
