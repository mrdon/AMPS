package com.atlassian.plugins.codgen.modules;

import com.atlassian.plugins.codgen.PluginModuleLocation;

/**
 * Implementors of this class do the actual code generation for a specific plugin module
 */
public interface PluginModuleCreator {
    String getId();

    void createModule(PluginModuleLocation location, BasicModuleProperties props) throws Exception;
}
