package com.atlassian.plugins.codegen.modules;

import com.atlassian.plugins.codegen.annotations.DependencyDescriptor;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;

import java.util.List;
import java.util.Map;

/**
 * @since version
 */
public interface PluginModuleCreatorRegistry {

    public static final String REFAPP = "refapp";
    public static final String CONFLUENCE = "confluence";
    public static final String JIRA = "jira";
    public static final String BAMBOO = "bamboo";
    public static final String FECRU = "fecru";
    public static final String CROWD = "crowd";

    void registerModuleCreator(String productId, PluginModuleCreator moduleCreator);
    PluginModuleCreator getModuleCreator(String productId, String creatorKey);
    Map<String,PluginModuleCreator> getModuleCreatorsForProduct(String productId);

    void registerModuleCreatorDependencies(Class creatorClass, List<DependencyDescriptor> dependencies);
    List<DependencyDescriptor> getDependenciesForCreatorClass(Class creatorClass);
}
