package com.atlassian.plugins.codegen.annotations;

import java.lang.annotation.*;

/**
 * @since 3.6
 */
@Documented
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Dependency
{
    String groupId();

    String artifactId();

    String version();

    String scope() default "";
}