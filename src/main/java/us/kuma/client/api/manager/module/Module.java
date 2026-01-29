/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import us.kuma.client.Kuma;
import us.kuma.client.api.manager.bind.Bind;
import us.kuma.client.api.manager.config.JSONSerializable;
import us.kuma.client.api.manager.module.trait.ModuleManifest;
import us.kuma.client.api.setting.Setting;
import us.kuma.client.api.setting.SettingProvider;
import us.kuma.client.api.trait.Nameable;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author xgraza
 * @since 1/26/26
 */
public class Module implements SettingProvider, JSONSerializable, Nameable
{
    protected static final Minecraft MC = Minecraft.getInstance();

    private final ModuleManifest manifest;
    private final Bind bind;

    private final Map<String, Setting<?>> settingToNameMap = new LinkedHashMap<>();
    private final List<Setting<?>> settingList = new LinkedList<>();

    private boolean drawn = true;

    public Module()
    {
        if (!getClass().isAnnotationPresent(ModuleManifest.class))
        {
            throw new RuntimeException("@ModuleManifest must be present on %s"
                    .formatted(getClass().getSimpleName()));
        }
        manifest = getClass().getDeclaredAnnotation(ModuleManifest.class);
        bind = new ModuleBind(this);
    }

    protected void onEnable()
    {
        Kuma.EVENT_BUS.register(this);
    }

    protected void onDisable()
    {
        Kuma.EVENT_BUS.deregister(this);
    }

    @Override
    public void discoverSettings()
    {
        for (final Field field : getClass().getDeclaredFields())
        {
            if (!Setting.class.isAssignableFrom(field.getType()) || !field.trySetAccessible())
            {
                continue;
            }
            try
            {
                registerSetting((Setting<?>) field.get(this));
            } catch (final IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public ModuleManifest getManifest()
    {
        return manifest;
    }

    @Override
    public String getName()
    {
        return manifest.name();
    }

    public Bind getBind()
    {
        return bind;
    }

    public void setState(final boolean state)
    {
        bind.setState(state);
    }

    public boolean isToggled()
    {
        return bind.isToggled();
    }

    public void setDrawn(boolean drawn)
    {
        this.drawn = drawn;
    }

    public boolean isDrawn()
    {
        return drawn;
    }

    @Override
    public void registerSetting(final Setting<?> setting)
    {
        settingToNameMap.put(setting.getName(), setting);
        settingList.add(setting);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Setting<T> getSetting(final String name)
    {
        return (Setting<T>) settingToNameMap.get(name);
    }

    @Override
    public List<Setting<?>> getSettings()
    {
        return settingList;
    }

    @Override
    public void fromJSON(final JsonElement element)
    {

    }

    @Override
    public JsonElement toJSON()
    {
        final JsonObject object = new JsonObject();
        object.addProperty("drawn", drawn);
        object.addProperty("state", isToggled());
        if (!settingList.isEmpty())
        {
            final JsonObject settingsObject = new JsonObject();
            for (final Setting<?> setting : settingList)
            {
                try
                {
                    final JsonElement element = setting.toJSON();
                    if (element == null)
                    {
                        Kuma.LOGGER.warn("{}.{} has no JSON save state",
                                getName(), setting.getName());
                        continue;
                    }
                    settingsObject.add(setting.getName(), element);
                } catch (final Throwable throwable)
                {
                    Kuma.LOGGER.error("Failed to save a setting", throwable);
                }
            }
            object.add("settings", settingsObject);
        }
        return object;
    }
}
