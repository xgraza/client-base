/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.module.trait;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xgraza
 * @since 1/26/26
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleManifest
{
    String name();

    String description() default "No description provided for this module";

    ModuleCategory category();
}
