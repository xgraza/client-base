/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.event.network;

import net.minecraft.network.protocol.Packet;
import us.kuma.client.api.event.Cancelable;

/**
 * @author xgraza
 * @since 1/27/26
 */
public class PacketEvent extends Cancelable
{
    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet)
    {
        this.packet = packet;
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet<?>> T getPacket()
    {
        return (T) packet;
    }

    public static final class Send extends PacketEvent
    {
        public Send(Packet<?> packet)
        {
            super(packet);
        }
    }

    public static final class Receive extends PacketEvent
    {
        public Receive(Packet<?> packet)
        {
            super(packet);
        }
    }
}
