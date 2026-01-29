/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.network;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.kuma.client.Kuma;
import us.kuma.client.impl.event.network.ChatEvent;

/**
 * @author xgraza
 * @since 1/29/26
 */
@Mixin(ClientPacketListener.class)
public final class ClientPacketListenerMixin
{
    @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
    private void hook$sendChat(final String message, final CallbackInfo info)
    {
        if (Kuma.EVENT_BUS.post(new ChatEvent(message)))
        {
            info.cancel();
        }
    }
}
