/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.network;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.kuma.client.Kuma;
import us.kuma.client.impl.event.network.PacketEvent;

/**
 * @author xgraza
 * @since 1/27/26
 */
@Mixin(Connection.class)
public final class ConnectionMixin
{
    @Inject(method = "sendPacket",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/Connection;doSendPacket(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V",
                    shift = At.Shift.BEFORE),
            cancellable = true)
    private void hook$sendPacket(Packet<?> packet, @Nullable ChannelFutureListener channelFutureListener, boolean bl, CallbackInfo info)
    {
        if (Kuma.EVENT_BUS.post(new PacketEvent.Send(packet)))
        {
            info.cancel();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/PacketListener;shouldHandleMessage(Lnet/minecraft/network/protocol/Packet;)Z",
                    shift = At.Shift.BEFORE),
            cancellable = true)
    private void hook$channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info)
    {
        if (Kuma.EVENT_BUS.post(new PacketEvent.Receive(packet)))
        {
            info.cancel();
        }
    }
}
