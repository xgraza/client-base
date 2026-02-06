/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.module.trait;

/**
 * @author xgraza
 * @since 1/26/26
 */
public enum ModuleCategory
{
    MOVEMENT("Movement"),
    RENDER("Render"),
    CLIENT("Client");

    private final String displayName;

    ModuleCategory(String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public String toString()
    {
        return displayName;
    }
}
