/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.module.client;

import us.kuma.client.api.manager.module.Module;
import us.kuma.client.api.manager.module.trait.ModuleCategory;
import us.kuma.client.api.manager.module.trait.ModuleManifest;
import us.kuma.client.impl.gui.clickgui.ModuleClickGUIScreen;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;

/**
 * @author xgraza
 * @since 1/27/26
 */
@ModuleManifest(name = "ClickGUI", category = ModuleCategory.CLIENT)
public final class ClickGUIModule extends Module
{
    public ClickGUIModule()
    {
        getBind().setKey(GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    protected void onEnable()
    {
        super.onEnable();
        if (MC.player == null || MC.level == null)
        {
            setState(false);
            return;
        }
        MC.setScreen(ModuleClickGUIScreen.getInstance());
        setState(false);
    }
}
