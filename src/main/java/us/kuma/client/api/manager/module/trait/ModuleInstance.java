/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.module.trait;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xgraza
 * @since 02/12/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModuleInstance
{
}
