package com.atlassian.maven.plugins.amps.codegen.registry;

import org.apache.maven.model.Dependency;

import java.util.List;

/**
 * Author: jdoklovic
 */
public interface PluginModuleDependencyRegistry {
    List<Dependency> getDependenciesForCreatorClass(Class clazz);
}
