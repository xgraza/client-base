/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.render.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
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
    @Inject(method = "render", at = @At("TAIL"))
    private void hook$render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info)
    {
        Kuma.EVENT_BUS.post(new RenderHUDEvent(guiGraphics));
    }
}
