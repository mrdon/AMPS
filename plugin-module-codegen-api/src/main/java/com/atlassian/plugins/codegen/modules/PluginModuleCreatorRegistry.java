package com.atlassian.plugins.codegen.modules;

import java.util.List;
import java.util.Map;

import com.atlassian.plugins.codegen.annotations.DependencyDescriptor;

/**
 * @since 3.5
 */
public interface PluginModuleCreatorRegistry
{

    public static final String REFAPP = "refapp";
    public static final String CONFLUENCE = "confluence";
    public static final String JIRA = "jira";
    public static final String BAMBOO = "bamboo";
    public static final String FECRU = "fecru";
    public static final String CROWD = "crowd";

    void registerModuleCreator(String productId, PluginModuleCreator moduleCreator);

    <T extends PluginModuleCreator> T getModuleCreator(String productId, Class<T> type);

    Map<Class, PluginModuleCreator> getModuleCreatorsForProduct(String productId);

    void registerModuleCreatorDependencies(Class creatorClass, List<DependencyDescriptor> dependencies);

    List<DependencyDescriptor> getDependenciesForCreatorClass(Class creatorClass);
}
