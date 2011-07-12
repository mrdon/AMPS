package com.atlassian.plugins.codegen.modules;

/**
 * Implementors of this class do the actual code generation for a specific plugin module
 */
public interface PluginModuleCreator<T extends PluginModuleProperties> {
    String getModuleName();

    void createModule(PluginModuleLocation location, T props) throws Exception;
}
