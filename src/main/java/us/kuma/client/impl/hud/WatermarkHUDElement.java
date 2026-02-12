/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.hud;

import net.minecraft.client.gui.GuiGraphics;
import us.kuma.client.api.manager.hud.HUDElement;
import us.kuma.client.api.manager.hud.trait.HUDManifest;
import us.kuma.client.api.manager.hud.trait.HUDOrigin;
import us.kuma.client.util.BuildConfig;

/**
 * @author xgraza
 * @since 02/12/26
 */
@HUDManifest(name = "Watermark", description = "Shows the client watermark")
@HUDOrigin(x = 2.0, y = 2.0)
public final class WatermarkHUDElement extends HUDElement
{
    @Override
    public void render(final GuiGraphics graphics)
    {
        String text = BuildConfig.NAME + " v" + BuildConfig.VERSION;
        int width = MC.font.width(text);
        setWidth(width);
        setHeight(MC.font.lineHeight);
        graphics.drawString(MC.font, text, (int) getX(), (int) getY(), -1);
    }
}
