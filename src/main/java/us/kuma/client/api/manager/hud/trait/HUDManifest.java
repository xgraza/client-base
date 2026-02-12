/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.hud.trait;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xgraza
 * @since 02/12/26
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HUDManifest
{
    String name();

    String description() default "No description provided for this HUD element";
}
