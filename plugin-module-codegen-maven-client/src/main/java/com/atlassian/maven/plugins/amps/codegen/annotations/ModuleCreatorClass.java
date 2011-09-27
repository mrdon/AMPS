package com.atlassian.maven.plugins.amps.codegen.annotations;

import java.lang.annotation.*;

/**
 * @since 3.6
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ModuleCreatorClass
{
    public Class value();
}