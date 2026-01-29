/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.server;

import us.kuma.client.Kuma;
import us.kuma.client.api.event.Subscribe;
import us.kuma.client.api.trait.Initializable;
import us.kuma.client.impl.event.network.PacketEvent;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class CombatManager implements Initializable
{
    @Override
    public void init()
    {
        Kuma.EVENT_BUS.register(this);
    }

    @Subscribe
    public void onPacketReceive(final PacketEvent.Receive event)
    {

    }
}
