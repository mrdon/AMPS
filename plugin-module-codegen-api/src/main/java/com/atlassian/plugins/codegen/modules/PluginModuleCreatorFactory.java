package com.atlassian.plugins.codegen.modules;

import com.atlassian.plugins.codegen.annotations.DependencyDescriptor;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;

import java.util.List;
import java.util.Map;

/**
 * @since 3.5
 */
public interface PluginModuleCreatorFactory {
    PluginModuleCreator getModuleCreator(String productId, Class creatorClass);

    Map<Class,PluginModuleCreator> getModuleCreatorsForProduct(String productId);

    List<DependencyDescriptor> getDependenciesForCreatorClass(Class creatorClass);
}
