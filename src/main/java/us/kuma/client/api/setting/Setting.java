/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import us.kuma.client.api.manager.config.JSONSerializable;
import us.kuma.client.api.trait.Nameable;

import java.util.function.Predicate;

/**
 * @param <T> the setting value type
 * @author xgraza
 * @since 1/27/27
 */
public class Setting<T> implements Nameable, JSONSerializable
{
    private final String name, description;
    private final T defaultValue;
    private final Predicate<T> visibility;
    private final Class<T> type;
    private T value;

    @SuppressWarnings("unchecked")
    public Setting(final String name, final String description, final Predicate<T> visibility, final T value)
    {
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.value = value;
        this.defaultValue = value;
        this.type = (Class<T>) value.getClass();
    }

    @Override
    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public T getDefaultValue()
    {
        return defaultValue;
    }

    public Class<T> getType()
    {
        return type;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    public T getValue()
    {
        return value;
    }

    public boolean isVisible()
    {
        return visibility == null || visibility.test(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fromJSON(final JsonElement element)
    {
        if (!element.isJsonPrimitive())
        {
            return;
        }
        final JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (String.class.isAssignableFrom(type) && primitive.isString())
        {
            setValue((T) primitive.getAsString());
        } else if (Boolean.class.isAssignableFrom(type) && primitive.isBoolean())
        {
            setValue((T) (Boolean) primitive.getAsBoolean());
        } else if (Number.class.isAssignableFrom(type) && primitive.isNumber())
        {
            if (Integer.class.isAssignableFrom(type))
            {
                setValue((T) (Integer) primitive.getAsInt());
            } else if (Float.class.isAssignableFrom(type))
            {
                setValue((T) (Float) primitive.getAsFloat());
            } else if (Double.class.isAssignableFrom(type))
            {
                setValue((T) (Double) primitive.getAsDouble());
            } else if (Long.class.isAssignableFrom(type))
            {
                setValue((T) (Long) primitive.getAsLong());
            }
        }
    }

    @Override
    public JsonElement toJSON()
    {
        if (String.class.isAssignableFrom(type))
        {
            return new JsonPrimitive((String) getValue());
        } else if (Boolean.class.isAssignableFrom(type))
        {
            return new JsonPrimitive((Boolean) getValue());
        } else if (Number.class.isAssignableFrom(type))
        {
            return new JsonPrimitive((Number) getValue());
        } else
        {
            return JsonNull.INSTANCE;
        }
    }

    public static class Builder<T>
    {
        protected final String name;
        protected T value;
        protected String description = "No description was provided for this setting";
        protected Predicate<T> visibility;

        public Builder(String name, T value)
        {
            this.name = name;
            this.value = value;
        }

        protected void setValue(T value)
        {
            this.value = value;
        }

        public Builder<T> setDescription(String description)
        {
            this.description = description;
            return this;
        }

        public Builder<T> setVisibility(final Predicate<T> visibility)
        {
            this.visibility = visibility;
            return this;
        }

        public Setting<T> build()
        {
            return new Setting<>(name, description, visibility, value);
        }
    }
}
