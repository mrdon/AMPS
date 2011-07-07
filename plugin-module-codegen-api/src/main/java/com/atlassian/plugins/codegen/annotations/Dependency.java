package com.atlassian.plugins.codegen.annotations;

import java.lang.annotation.*;

/**
 * @since version
 */
@Documented
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Dependency {
    String groupId();
    String artifactId();
    String version();
    String scope() default "";
}