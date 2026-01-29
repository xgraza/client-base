/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.event.entity;

import us.kuma.client.api.event.Cancelable;
import us.kuma.client.api.event.Stage;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class PlayerUpdateMovementEvent extends Cancelable
{
    private final Stage stage;
    private double x, y, z;
    private float xRot, yRot;
    private boolean onGround;

    public PlayerUpdateMovementEvent(Stage stage, double x, double y, double z, float xRot, float yRot, boolean onGround)
    {
        this.stage = stage;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
        this.onGround = onGround;
    }

    public Stage getStage()
    {
        return stage;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
    }

    public float getXRot()
    {
        return xRot;
    }

    public void setxRot(float xRot)
    {
        this.xRot = xRot;
    }

    public float getYRot()
    {
        return yRot;
    }

    public void setyRot(float yRot)
    {
        this.yRot = yRot;
    }

    public boolean isOnGround()
    {
        return onGround;
    }

    public void setOnGround(boolean onGround)
    {
        this.onGround = onGround;
    }
}
