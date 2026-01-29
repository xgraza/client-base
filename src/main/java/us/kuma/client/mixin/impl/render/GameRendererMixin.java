/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.render;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import us.kuma.client.Kuma;
import us.kuma.client.impl.event.render.RenderNightVisonEffectEvent;

/**
 * @author xgraza
 * @since 1/27/26
 */
@Mixin(GameRenderer.class)
public final class GameRendererMixin
{
    @Redirect(method = "getNightVisionScale",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getEffect(Lnet/minecraft/core/Holder;)Lnet/minecraft/world/effect/MobEffectInstance;"))
    private static @Nullable MobEffectInstance hook$getNightVisionScale$getEffect(final LivingEntity livingEntity, final Holder<MobEffect> holder)
    {
        final RenderNightVisonEffectEvent event = new RenderNightVisonEffectEvent(
                livingEntity.getEffect(MobEffects.NIGHT_VISION));
        Kuma.EVENT_BUS.post(event);
        return event.getEffect();
    }
}
