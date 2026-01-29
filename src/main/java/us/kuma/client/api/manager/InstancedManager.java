/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager;

import us.kuma.client.api.trait.Initializable;

import java.util.List;

/**
 * @author xgraza
 * @since 1/26/26
 */
public interface InstancedManager<T> extends Initializable
{
    void register(T entity);

    void unregister(T entity);

    List<T> getEntities();
}
