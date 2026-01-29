/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client;

import net.fabricmc.api.ClientModInitializer;

/**
 * @author xgraza
 * @since 1/26/26
 */
public final class KumaMod implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        Kuma.INSTANCE.init();
    }
}
