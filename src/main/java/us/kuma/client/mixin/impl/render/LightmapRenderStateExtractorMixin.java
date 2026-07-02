/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.render;

import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import us.kuma.client.Kuma;
import us.kuma.client.impl.event.render.LightTextureGammaEvent;

/**
 * @author xgraza
 * @since 1/26/26
 */
@Mixin(LightmapRenderStateExtractor.class)
public final class LightmapRenderStateExtractorMixin
{
    @Redirect(method = "extract",
            at = @At(value = "INVOKE", target = "Ljava/lang/Float;floatValue()F", ordinal = 0))
    private float hook$updateLightTexture$floatValue(Float gamma)
    {
        final LightTextureGammaEvent event = new LightTextureGammaEvent(gamma);
        Kuma.EVENT_BUS.post(event);
        return event.getGamma();
    }
}
