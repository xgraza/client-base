/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.server;

import us.kuma.client.Kuma;
import us.kuma.client.api.trait.Initializable;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class RotationManager implements Initializable
{
    @Override
    public void init()
    {
        Kuma.EVENT_BUS.register(this);
    }
}
