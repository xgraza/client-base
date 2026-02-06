/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.render;

import java.util.List;

/**
 * @author xgraza
 * @since 1/29/26
 */
public interface Parent<T extends ScreenElement>
{
    void addChild(T child);

    List<T> getChildren();
}
