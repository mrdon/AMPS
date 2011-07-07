package com.atlassian.maven.plugins.amps.codegen.annotations;

import java.lang.annotation.*;

/**
 * @since version
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ModuleCreatorClass {
    public Class value();
}