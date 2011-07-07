package com.atlassian.plugins.codegen.modules;

import com.atlassian.plugins.codegen.annotations.DependencyDescriptor;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;

import java.util.List;
import java.util.Map;

/**
 * @since version
 */
public interface PluginModuleCreatorFactory {
    PluginModuleCreator getModuleCreator(String productId, String creatorKey);

    Map<String,PluginModuleCreator> getModuleCreatorsForProduct(String productId);

    List<DependencyDescriptor> getDependenciesForCreatorClass(Class creatorClass);
}
