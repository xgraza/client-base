/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.module;

import us.kuma.client.api.manager.bind.Bind;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class ModuleBind extends Bind
{
    private final Module module;

    public ModuleBind(final Module module)
    {
        super(module.getManifest().name());
        this.module = module;
    }

    @Override
    public void onEnable()
    {
        module.onEnable();
    }

    @Override
    public void onDisable()
    {
        module.onDisable();
    }
}
