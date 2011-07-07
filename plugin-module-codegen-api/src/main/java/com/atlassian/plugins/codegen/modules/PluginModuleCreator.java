package com.atlassian.plugins.codegen.modules;

/**
 * Implementors of this class do the actual code generation for a specific plugin module
 */
public interface PluginModuleCreator {
    String getModuleName();

    void createModule(PluginModuleLocation location, PluginModuleProperties props) throws Exception;
}
