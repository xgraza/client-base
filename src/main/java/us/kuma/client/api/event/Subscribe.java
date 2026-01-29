/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xgraza
 * @since 1/26/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe
{
    int eventPriority() default 0;

    boolean receiveCanceled() default false;
}