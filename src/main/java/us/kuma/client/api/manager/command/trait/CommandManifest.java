/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.command.trait;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xgraza
 * @since 1/27/26
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandManifest
{
    String[] aliases();

    String description() default "No description provided for this command";
}
