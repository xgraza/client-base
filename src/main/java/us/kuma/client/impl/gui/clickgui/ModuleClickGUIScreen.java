/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.gui.clickgui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import us.kuma.client.Kuma;

import java.io.IOException;

/**
 * @author xgraza
 * @since 1/26/26
 */
public final class ModuleClickGUIScreen extends Screen
{
    private static final Component NARRATOR_COMPONENT = Component.literal("Module ClickGUI Screen");
    private static ModuleClickGUIScreen INSTANCE;

    private ModuleClickGUIScreen()
    {
        super(NARRATOR_COMPONENT);
    }

    @Override
    public void render(final GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {

    }

    @Override
    public void onClose()
    {
        super.onClose();

        try
        {
            Kuma.INSTANCE.getConfigManager().save(Kuma.INSTANCE.getModuleManager());
            Kuma.LOGGER.info("Saved module configurations");
        } catch (final IOException e)
        {
            Kuma.LOGGER.error("Failed to save module configurations", e);
        }
    }

    public static ModuleClickGUIScreen getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ModuleClickGUIScreen();
        }
        return INSTANCE;
    }
}
