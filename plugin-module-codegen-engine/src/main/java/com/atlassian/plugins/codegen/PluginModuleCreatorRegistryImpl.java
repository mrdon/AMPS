package com.atlassian.plugins.codegen;

import com.atlassian.plugins.codegen.annotations.DependencyDescriptor;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleCreatorRegistry;

import java.util.*;

/**
 * @since version
 */
public class PluginModuleCreatorRegistryImpl implements PluginModuleCreatorRegistry {

    private final Map<String, SortedMap<String, PluginModuleCreator>> creatorRegistry;
    private final Map<Class, List<DependencyDescriptor>> creatorDependencyMap;

    public PluginModuleCreatorRegistryImpl() {
        this.creatorRegistry = new HashMap<String, SortedMap<String, PluginModuleCreator>>();
        creatorRegistry.put(PluginModuleCreatorRegistry.JIRA,new TreeMap<String, PluginModuleCreator>());
        creatorRegistry.put(PluginModuleCreatorRegistry.BAMBOO,new TreeMap<String, PluginModuleCreator>());
        creatorRegistry.put(PluginModuleCreatorRegistry.CONFLUENCE,new TreeMap<String, PluginModuleCreator>());
        creatorRegistry.put(PluginModuleCreatorRegistry.CROWD,new TreeMap<String, PluginModuleCreator>());
        creatorRegistry.put(PluginModuleCreatorRegistry.FECRU,new TreeMap<String, PluginModuleCreator>());
        creatorRegistry.put(PluginModuleCreatorRegistry.REFAPP,new TreeMap<String, PluginModuleCreator>());

        this.creatorDependencyMap = new HashMap<Class, List<DependencyDescriptor>>();
    }

    @Override
    public void registerModuleCreator(String productId, PluginModuleCreator moduleCreator) {
        Map<String,PluginModuleCreator> moduleMap = creatorRegistry.get(productId);
        if(null != moduleMap) {
            moduleMap.put(moduleCreator.getModuleName(), moduleCreator);
        }
    }

    @Override
    public PluginModuleCreator getModuleCreator(String productId, String creatorKey) {
        Map<String,PluginModuleCreator> moduleMap = creatorRegistry.get(productId);
        PluginModuleCreator creator = null;
        if(null != moduleMap) {
            creator = moduleMap.get(creatorKey);
        }

        return creator;
    }

    @Override
    public Map<String,PluginModuleCreator> getModuleCreatorsForProduct(String productId) {
        SortedMap<String,PluginModuleCreator> moduleMap = creatorRegistry.get(productId);
        if(null != moduleMap) {
            moduleMap = Collections.unmodifiableSortedMap(moduleMap);
        }

        return moduleMap;
    }

    @Override
    public void registerModuleCreatorDependencies(Class creatorClass, List<DependencyDescriptor> dependencies) {
        creatorDependencyMap.put(creatorClass,dependencies);
    }

    @Override
    public List<DependencyDescriptor> getDependenciesForCreatorClass(Class creatorClass) {
        if(!creatorDependencyMap.containsKey(creatorClass) || creatorDependencyMap.get(creatorClass).isEmpty()) {
            return new ArrayList<DependencyDescriptor>();
        }

        return creatorDependencyMap.get(creatorClass);
    }
}
