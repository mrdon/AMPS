package com.atlassian.plugins.codegen.modules;

import java.util.List;
import java.util.Map;

import com.atlassian.plugins.codegen.annotations.DependencyDescriptor;

/**
 * @since 3.5
 */
public interface PluginModuleCreatorFactory
{
    PluginModuleCreator getModuleCreator(String productId, Class creatorClass);

    Map<Class, PluginModuleCreator> getModuleCreatorsForProduct(String productId);

    List<DependencyDescriptor> getDependenciesForCreatorClass(Class creatorClass);
}
