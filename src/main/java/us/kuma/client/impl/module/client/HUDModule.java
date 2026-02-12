/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.module.client;

import net.minecraft.client.gui.screens.LevelLoadingScreen;
import us.kuma.client.Kuma;
import us.kuma.client.api.event.Subscribe;
import us.kuma.client.api.manager.hud.HUDElement;
import us.kuma.client.api.manager.module.Module;
import us.kuma.client.api.manager.module.trait.ModuleCategory;
import us.kuma.client.api.manager.module.trait.ModuleInstance;
import us.kuma.client.api.manager.module.trait.ModuleManifest;
import us.kuma.client.impl.event.render.RenderHUDEvent;

/**
 * @author xgraza
 * @since 02/12/26
 */
@ModuleManifest(name = "HUD",
        description = "Displays client information",
        category = ModuleCategory.CLIENT)
public final class HUDModule extends Module
{
    @ModuleInstance
    public static final HUDModule INSTANCE = new HUDModule();

    public HUDModule()
    {
        super();

        for (HUDElement hudElement : Kuma.INSTANCE.getHUDManager().getEntities())
        {
            registerSetting(builder(hudElement.getName(), hudElement.isToggled())
                    .setDescription(hudElement.getManifest().description())
                    .onValueChanged(hudElement::setState)
                    .build());
        }
    }

    @Subscribe
    public void onRenderHUD(final RenderHUDEvent event)
    {
        if (MC.screen instanceof LevelLoadingScreen)
        {
            return;
        }
        for (HUDElement hudElement : Kuma.INSTANCE.getHUDManager().getEntities())
        {
            if (!hudElement.isToggled())
            {
                continue;
            }
            hudElement.render(event.graphics());
        }
    }
}
