package com.atlassian.maven.plugins.amps.codegen.registry;

import com.atlassian.plugins.codgen.PluginModuleCreatorFactory;

/**
 * Author: jdoklovic
 */
public interface ProductModuleCreatorRegistry {
    PluginModuleCreatorFactory getProductModuleCreatorFactory(String productId);
}
