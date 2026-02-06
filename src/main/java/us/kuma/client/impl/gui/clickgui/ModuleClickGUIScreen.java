/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.gui.clickgui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import us.kuma.client.Kuma;
import us.kuma.client.api.manager.module.Module;
import us.kuma.client.api.manager.module.trait.ModuleCategory;
import us.kuma.client.impl.gui.clickgui.element.PanelElement;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xgraza
 * @since 1/26/26
 */
public final class ModuleClickGUIScreen extends Screen
{
    private static ModuleClickGUIScreen INSTANCE;

    private static final Component NARRATOR_COMPONENT = Component.literal("Module ClickGUI Screen");
    private static final int PANEL_Y = 26;

    private final List<PanelElement> panelElementList = new LinkedList<>();

    private ModuleClickGUIScreen()
    {
        super(NARRATOR_COMPONENT);
        double panelX = 10;
        for (final ModuleCategory category : ModuleCategory.values())
        {
            final List<Module> moduleList = Kuma.INSTANCE.getModuleManager().getEntities()
                    .stream()
                    .filter((module) -> module.getManifest().category() == category)
                    .toList();
            PanelElement panelElement = new PanelElement(category, moduleList);
            panelElement.setY(PANEL_Y);
            panelElement.setX(panelX);
            panelElementList.add(panelElement);
            panelX += panelElement.getWidth() + 5;
        }
    }

    @Override
    public void render(final GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        for (PanelElement panelElement : panelElementList)
        {
            panelElement.render(graphics, mouseX, mouseY);
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f)
    {
        // intentionally blank
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl)
    {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();
        for (PanelElement panelElement : panelElementList)
        {
            if (panelElement.mouseInside(mouseX, mouseY)
                    && panelElement.mouseClicked(mouseX, mouseY, mouseButtonEvent.button(), mouseButtonEvent.modifiers(), bl))
            {
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g)
    {
        return super.mouseScrolled(d, e, f, g);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e)
    {
        return super.mouseDragged(mouseButtonEvent, d, e);
    }

    @Override
    public void mouseMoved(double d, double e)
    {

    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
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
