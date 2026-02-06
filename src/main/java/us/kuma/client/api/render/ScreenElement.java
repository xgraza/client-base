/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.render;

import net.minecraft.client.Minecraft;

/**
 * @author xgraza
 * @since 1/29/26
 */
public interface ScreenElement
{
    Minecraft MC = Minecraft.getInstance();

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getWidth();

    void setWidth(double width);

    double getHeight();

    void setHeight(double height);

    default boolean mouseInside(double mouseX, double mouseY)
    {
        return getX() <= mouseX && getX() + getWidth() >= mouseX && getY() <= mouseY && getY() + getHeight() >= mouseY;
    }

    default boolean mouseInside(double mouseX, double mouseY, double width, double height)
    {
        return getX() <= mouseX && getX() + width >= mouseX && getY() <= mouseY && getY() + height >= mouseY;
    }
}
