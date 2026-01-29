/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.event.render;

import net.minecraft.world.effect.MobEffectInstance;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class RenderNightVisonEffectEvent
{
    private MobEffectInstance effect;

    public RenderNightVisonEffectEvent(MobEffectInstance effect)
    {
        this.effect = effect;
    }

    public void setEffect(MobEffectInstance effect)
    {
        this.effect = effect;
    }

    public MobEffectInstance getEffect()
    {
        return effect;
    }
}
