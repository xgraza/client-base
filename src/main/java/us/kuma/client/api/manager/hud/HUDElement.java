/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.hud;

import net.minecraft.client.gui.GuiGraphics;
import us.kuma.client.api.manager.hud.trait.HUDManifest;
import us.kuma.client.api.manager.hud.trait.HUDOrigin;
import us.kuma.client.api.render.ScreenElement;
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
 * @since 02/12/26
 */
public abstract class HUDElement implements Nameable, SettingProvider, ScreenElement
{
    private final HUDManifest manifest;

    private final Map<String, Setting<?>> settingToNameMap = new LinkedHashMap<>();
    private final List<Setting<?>> settingList = new LinkedList<>();

    private double x, y, width, height;
    private boolean state;

    public HUDElement()
    {
        if (!getClass().isAnnotationPresent(HUDManifest.class))
        {
            throw new RuntimeException("@HUDManifest must be present on %s"
                    .formatted(getClass().getSimpleName()));
        }
        manifest = getClass().getDeclaredAnnotation(HUDManifest.class);

        if (getClass().isAnnotationPresent(HUDOrigin.class))
        {
            final HUDOrigin dimensions = getClass().getDeclaredAnnotation(HUDOrigin.class);
            setX(dimensions.x());
            setY(dimensions.y());
            setWidth(dimensions.width());
            setHeight(dimensions.height());
            setState(state);
        }
    }

    public abstract void render(final GuiGraphics graphics);

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

    public HUDManifest getManifest()
    {
        return manifest;
    }

    @Override
    public String getName()
    {
        return manifest.name();
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isToggled()
    {
        return state;
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
    public void setHeight(double height)
    {
        this.height = height;
    }

    @Override
    public double getHeight()
    {
        return height;
    }

    @Override
    public void setWidth(double width)
    {
        this.width = width;
    }

    @Override
    public double getWidth()
    {
        return width;
    }

    @Override
    public void setY(double y)
    {
        this.y = y;
    }

    @Override
    public double getY()
    {
        return y;
    }

    @Override
    public void setX(double x)
    {
        this.x = x;
    }

    @Override
    public double getX()
    {
        return x;
    }
}
