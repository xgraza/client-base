/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.event;

/**
 * @author xgraza
 * @since 1/26/26
 */
public class Cancelable
{
    private boolean canceled;

    public void cancel()
    {
        canceled = true;
    }

    public void setCanceled(boolean canceled)
    {
        this.canceled = canceled;
    }

    public boolean isCanceled()
    {
        return canceled;
    }
}
