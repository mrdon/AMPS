package com.atlassian.maven.plugins.amps.codegen.registry;

import com.atlassian.plugins.codgen.CommonPluginModuleCreatorFactory;
import com.atlassian.plugins.codgen.JiraPluginModuleCreatorFactory;
import com.atlassian.plugins.codgen.PluginModuleCreatorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: jdoklovic
 */
public class ProductModuleCreatorRegistryImpl implements ProductModuleCreatorRegistry {
    private Map<String,PluginModuleCreatorFactory> moduleCreatorFactories;

    public ProductModuleCreatorRegistryImpl() {
        this.moduleCreatorFactories = new HashMap<String, PluginModuleCreatorFactory>();

        moduleCreatorFactories.put(PluginModuleCreatorFactory.COMMON, new CommonPluginModuleCreatorFactory());
        moduleCreatorFactories.put(PluginModuleCreatorFactory.JIRA, new JiraPluginModuleCreatorFactory());
    }

    @Override
    public PluginModuleCreatorFactory getProductModuleCreatorFactory(String productId) {
        return moduleCreatorFactories.get(productId);
    }
}
