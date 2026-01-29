/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.util.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.Strictness;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class JsonUtil
{
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setStrictness(Strictness.LENIENT)
            .serializeNulls()
            .create();

    public static boolean isNull(final JsonElement element)
    {
        return element == null || element.isJsonNull();
    }
}
