/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.config;

import com.google.gson.JsonElement;

import java.io.File;

/**
 * @author xgraza
 * @since 1/26/26
 */
public interface JSONSerializable
{
    void fromJSON(final JsonElement element);

    JsonElement toJSON();

    default File getLocation()
    {
        return null;
    }
}
