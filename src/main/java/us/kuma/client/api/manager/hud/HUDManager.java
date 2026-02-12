/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.hud;

import us.kuma.client.api.manager.InstancedManager;
import us.kuma.client.impl.hud.WatermarkHUDElement;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xgraza
 * @since 02/12/26
 */
public final class HUDManager implements InstancedManager<HUDElement>
{
    private final List<HUDElement> hudElementList = new LinkedList<>();

    @Override
    public void init()
    {
        register(new WatermarkHUDElement());
    }

    @Override
    public void register(HUDElement entity)
    {
        entity.discoverSettings();
        hudElementList.add(entity);
    }

    @Override
    public void unregister(HUDElement entity)
    {
        hudElementList.remove(entity);
    }

    @Override
    public List<HUDElement> getEntities()
    {
        return hudElementList;
    }
}
