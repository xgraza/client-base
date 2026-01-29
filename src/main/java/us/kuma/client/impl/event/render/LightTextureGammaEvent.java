/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.event.render;

/**
 * @author xgraza
 * @since 1/26/26
 */
public final class LightTextureGammaEvent
{
    private float gamma;

    public LightTextureGammaEvent(float gamma)
    {
        this.gamma = gamma;
    }

    public float getGamma()
    {
        return gamma;
    }

    public void setGamma(float gamma)
    {
        this.gamma = gamma;
    }
}
