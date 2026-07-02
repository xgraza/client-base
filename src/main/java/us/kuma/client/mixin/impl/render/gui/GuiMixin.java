/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.render.gui;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.kuma.client.Kuma;
import us.kuma.client.impl.event.render.RenderHUDEvent;

/**
 * @author xgraza
 * @since 02/12/26
 */
@Mixin(Gui.class)
public final class GuiMixin
{
    @Inject(method = "extractRenderState",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V",
                    shift = At.Shift.BEFORE,
                    ordinal = 3))
    private void a(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded, CallbackInfo info, @Local(name = "graphics") GuiGraphicsExtractor graphics)
    {
        Kuma.EVENT_BUS.post(new RenderHUDEvent(graphics));
    }
}
