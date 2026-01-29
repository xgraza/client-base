/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.kuma.client.Kuma;
import us.kuma.client.api.event.Subscribe;
import us.kuma.client.api.manager.InstancedManager;
import us.kuma.client.api.manager.bind.trait.DeviceType;
import us.kuma.client.api.manager.config.JSONSerializable;
import us.kuma.client.impl.event.input.KeyInputEvent;
import us.kuma.client.util.io.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author xgraza
 * @since 1/26/26
 */
public final class BindManager implements InstancedManager<Bind>, JSONSerializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BindManager.class);
    private static final Minecraft MC = Minecraft.getInstance();

    private final Map<String, Bind> bindToIdMap = new LinkedHashMap<>();
    private final List<Bind> bindList = new ArrayList<>();

    @Override
    public void init()
    {
        Kuma.EVENT_BUS.register(this);
        Kuma.INSTANCE.getConfigManager().register(this);
    }

    @Subscribe
    public void onKeyInput(final KeyInputEvent event)
    {
        if (MC.screen != null || event.keyCode() <= GLFW_KEY_UNKNOWN)
        {
            return;
        }

        for (final Bind bind : bindList)
        {
            if (bind.getDeviceType() != DeviceType.KEYBOARD)
            {
                continue;
            }

            if (bind.getKey() == event.keyCode())
            {
                if (event.action() == GLFW_PRESS)
                {
                    bind.setState(!bind.isToggled());
                } else if (event.action() == GLFW_RELEASE && !bind.isPersistent())
                {
                    bind.setState(false);
                }
            }
        }
    }

    @Override
    public void register(Bind entity)
    {
        bindList.add(entity);
        bindToIdMap.put(entity.getName(), entity);
    }

    @Override
    public void unregister(Bind entity)
    {

    }

    @Override
    public List<Bind> getEntities()
    {
        return bindList;
    }

    @Override
    public void fromJSON(final JsonElement element)
    {
        if (!element.isJsonArray())
        {
            return;
        }
        final JsonArray array = element.getAsJsonArray();
        for (final JsonElement jsonElement : array)
        {
            if (!jsonElement.isJsonObject())
            {
                continue;
            }
            final JsonObject object = jsonElement.getAsJsonObject();
            if (!object.has("name"))
            {
                LOGGER.warn("Bind has no name property");
                continue;
            }
            final String name = object.get("name").getAsString();
            final Bind bind = bindToIdMap.get(name);
            if (bind == null)
            {
                LOGGER.warn("Unknown key bind \"{}\"", name);
                continue;
            }
            bind.fromJSON(object);
        }
    }

    @Override
    public JsonElement toJSON()
    {
        final JsonArray array = new JsonArray();
        for (final Bind bind : bindList)
        {
            final JsonElement element = bind.toJSON();
            if (element != null)
            {
                array.add(element);
            }
        }
        return array;
    }

    @Override
    public File getLocation()
    {
        return FileUtil.resolve("binds.json");
    }
}
