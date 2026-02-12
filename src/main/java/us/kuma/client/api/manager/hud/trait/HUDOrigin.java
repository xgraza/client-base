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
public @interface HUDOrigin
{
    double x() default 0.0;

    double y() default 0.0;

    double width() default 0.0;

    double height() default 0.0;

    boolean state() default false;
}
