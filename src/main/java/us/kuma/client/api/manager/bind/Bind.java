/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import us.kuma.client.api.manager.bind.trait.DeviceType;
import us.kuma.client.api.manager.config.JSONSerializable;
import us.kuma.client.api.trait.Nameable;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;

/**
 * @author xgraza
 * @since 1/26/26
 */
public abstract class Bind implements Nameable, JSONSerializable
{
    private final String name;
    private boolean persistent;
    private int key = GLFW_KEY_UNKNOWN, modifiers;
    private boolean state;
    private DeviceType deviceType = DeviceType.KEYBOARD;

    public Bind(final String name)
    {
        this.name = name;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    @Override
    public String getName()
    {
        return name;
    }

    public boolean isPersistent()
    {
        return persistent;
    }

    public void setPersistent(boolean persistent)
    {
        this.persistent = persistent;
    }

    public int getKey()
    {
        return key;
    }

    public void setKey(int key)
    {
        this.key = key;
    }

    public int getModifiers()
    {
        return modifiers;
    }

    public void setModifiers(int modifiers)
    {
        this.modifiers = modifiers;
    }

    public boolean isToggled()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
        if (state)
        {
            onEnable();
        } else
        {
            onDisable();
        }
    }

    public DeviceType getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType)
    {
        this.deviceType = deviceType;
    }

    @Override
    public void fromJSON(final JsonElement element)
    {
        if (!element.isJsonObject())
        {
            return;
        }
        final JsonObject object = element.getAsJsonObject();
        if (!name.equals(object.get("name").getAsString()))
        {
            return;
        }

        setKey(object.get("key").getAsInt());
        setModifiers(object.get("modifiers").getAsInt());
        setPersistent(object.get("persistent").getAsBoolean());
        setDeviceType(DeviceType.values()[object.get("device_type").getAsInt()]);
    }

    @Override
    public JsonElement toJSON()
    {
        final JsonObject object = new JsonObject();
        object.addProperty("name", name);
        object.addProperty("key", key);
        object.addProperty("modifiers", modifiers);
        object.addProperty("persistent", persistent);
        object.addProperty("device_type", deviceType.ordinal());
        return object;
    }
}
