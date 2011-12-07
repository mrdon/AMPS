package com.atlassian.plugins.codegen.annotations;

import java.lang.annotation.*;

/**
 * @since 3.6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
public @interface Dependencies
{
    Dependency[] value();
}