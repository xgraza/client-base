/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.server;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.kuma.client.Kuma;
import us.kuma.client.api.event.Subscribe;
import us.kuma.client.api.trait.Initializable;
import us.kuma.client.impl.event.network.PacketEvent;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class InventoryManager implements Initializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryManager.class);
    private static final Minecraft MC = Minecraft.getInstance();

    private int serverSlot;

    @Override
    public void init()
    {
        Kuma.EVENT_BUS.register(this);
    }

    @Subscribe
    public void onPacketReceive(final PacketEvent.Receive event)
    {
        if (event.getPacket() instanceof ClientboundSetHeldSlotPacket(int slot))
        {
            LOGGER.warn("Server forced item slot to {} (previous: {})", slot, serverSlot);
            serverSlot = slot;
        }
    }

    @Subscribe
    public void onPacketSend(final PacketEvent.Send event)
    {
        if (event.getPacket() instanceof ServerboundSetCarriedItemPacket packet)
        {
            final int slot = packet.getSlot();
            if (slot < 0 || slot > 8)
            {
                LOGGER.warn("Attempted to set packet slot to invalid bounds ({})", slot);
                event.cancel();
                return;
            }

            serverSlot = packet.getSlot();
        }
    }

    public int getSlot()
    {
        return serverSlot;
    }
}
