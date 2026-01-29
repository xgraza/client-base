/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.Color;
import java.util.function.Predicate;

/**
 * @author xgraza
 * @since 1/29/26
 */
public final class ColorSetting extends Setting<Color>
{
    public ColorSetting(String name, String description, Predicate<Color> visibility, Color value)
    {
        super(name, description, visibility, value);
    }

    public int getValueInt()
    {
        return getValue().getRGB();
    }

    public Color getWithTransparency(int alpha)
    {
        final Color color = getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public void setHSB(float hue, float saturation, float brightness)
    {
        setValue(Color.getHSBColor(hue, saturation, brightness));
    }

    public void setTransparency(int alpha)
    {
        final Color color = getValue();
        setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    }

    @Override
    public void fromJSON(final JsonElement element)
    {
        if (!element.isJsonObject())
        {
            return;
        }
        final JsonObject object = element.getAsJsonObject();

        int red = 255, green = 255, blue = 255, alpha = 255;
        if (object.has("red"))
        {
            red = object.get("red").getAsInt();
        }
        if (object.has("green"))
        {
            green = object.get("green").getAsInt();
        }
        if (object.has("blue"))
        {
            blue = object.get("blue").getAsInt();
        }
        if (object.has("alpha"))
        {
            alpha = object.get("alpha").getAsInt();
        }
        setValue(new Color(red, green, blue, alpha));
    }

    @Override
    public JsonElement toJSON()
    {
        final Color color = getValue();
        final JsonObject object = new JsonObject();
        object.addProperty("red", color.getRed());
        object.addProperty("green", color.getGreen());
        object.addProperty("blue", color.getBlue());
        object.addProperty("alpha", color.getAlpha());
        return object;
    }

    public static final class Builder extends Setting.Builder<Color>
    {
        private int red = 255, green = 255, blue = 255, alpha = 255;

        public Builder(String name, Color value)
        {
            super(name, value);
        }

        public Builder setRed(int red)
        {
            checkColorBounds("red", red);
            this.red = red;
            return this;
        }

        public Builder setGreen(int green)
        {
            checkColorBounds("green", green);
            this.green = green;
            return this;
        }

        public Builder setBlue(int blue)
        {
            checkColorBounds("blue", blue);
            this.blue = blue;
            return this;
        }

        public Builder setAlpha(int alpha)
        {
            checkColorBounds("alpha", alpha);
            this.alpha = alpha;
            return this;
        }

        private void checkColorBounds(String element, int value)
        {
            if (value > 255 || value < 0)
            {
                throw new RuntimeException("%s value must be less than 255 and greater than 0"
                        .formatted(element));
            }
        }

        @Override
        public Setting<Color> build()
        {
            setValue(new Color(red, green, blue, alpha));
            return super.build();
        }
    }
}
