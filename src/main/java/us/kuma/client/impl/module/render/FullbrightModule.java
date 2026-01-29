/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.module.render;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import us.kuma.client.api.event.Subscribe;
import us.kuma.client.api.manager.module.Module;
import us.kuma.client.api.manager.module.trait.ModuleCategory;
import us.kuma.client.api.manager.module.trait.ModuleManifest;
import us.kuma.client.api.setting.Setting;
import us.kuma.client.impl.event.render.LightTextureGammaEvent;
import us.kuma.client.impl.event.render.RenderNightVisonEffectEvent;

/**
 * @author xgraza
 * @since 1/26/26
 */
@ModuleManifest(name = "Fullbright",
        description = "Renders the world at full gamma",
        category = ModuleCategory.RENDER)
public final class FullbrightModule extends Module
{
    private static final float MAX_GAMMA_VALUE = 100.0F;
    private static final MobEffectInstance FAKE_NIGHT_VISION = new MobEffectInstance(
            MobEffects.NIGHT_VISION, -1, 1, false, false);

    private final Setting<Mode> modeSetting = builder("Mode", Mode.GAMMA)
            .setDescription("How to brighten up the world")
            .build();

    @Subscribe
    public void onLightTextureGamma(final LightTextureGammaEvent event)
    {
        if (modeSetting.getValue() == Mode.GAMMA)
        {
            event.setGamma(MAX_GAMMA_VALUE);
        }
    }

    @Subscribe
    public void onRenderNightVisonEffect(final RenderNightVisonEffectEvent event)
    {
        if (modeSetting.getValue() == Mode.POTION)
        {
            event.setEffect(FAKE_NIGHT_VISION);
        }
    }

    private enum Mode
    {
        GAMMA, POTION
    }
}
