/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.input;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.kuma.client.Kuma;
import us.kuma.client.impl.event.input.KeyInputEvent;

/**
 * @author xgraza
 * @since 1/26/26
 */
@Mixin(KeyboardHandler.class)
public final class KeyboardHandlerMixin
{
    @Inject(method = "keyPress",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/FramerateLimitTracker;onInputReceived()V",
                    shift = At.Shift.BEFORE))
    private void hook$keyPress(long handle, int action, KeyEvent event, CallbackInfo info)
    {
        Kuma.EVENT_BUS.post(new KeyInputEvent(action, event.key(), event.scancode(), event.modifiers()));
    }
}
