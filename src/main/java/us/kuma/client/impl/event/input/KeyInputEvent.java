/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.event.input;

/**
 * @author xgraza
 * @since 1/26/26
 */
public record KeyInputEvent(int action, int keyCode, int scanCode, int modifiers)
{
}
