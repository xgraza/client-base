/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.event.network;

import us.kuma.client.api.event.Cancelable;

/**
 * @author xgraza
 * @since 1/29/26
 */
public final class ChatEvent extends Cancelable
{
    private String message;

    public ChatEvent(final String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
