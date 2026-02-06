/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.render;

public abstract class Element implements GUIListener, ScreenElement
{
    protected double x, y, width, height;

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
