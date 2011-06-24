package com.atlassian.plugins.codgen;

import com.atlassian.plugins.codgen.modules.PluginModuleCreator;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Author: jdoklovic
 */
public abstract class AbstractPluginModuleCreatorFactory implements PluginModuleCreatorFactory {

    private SortedMap<String, PluginModuleCreator> creatorRegistry;

    protected AbstractPluginModuleCreatorFactory() {
        this.creatorRegistry = new TreeMap<String, PluginModuleCreator>();
    }

    @Override
    public void registerModuleCreator(PluginModuleCreator moduleCreator) {
        creatorRegistry.put(moduleCreator.getId(), moduleCreator);
    }

    @Override
    public PluginModuleCreator getModuleCreator(String moduleId) {
        return creatorRegistry.get(moduleId);
    }

    @Override
    public Map<String, PluginModuleCreator> getAllModuleCreators() {
        return Collections.unmodifiableSortedMap(creatorRegistry);
    }
}
