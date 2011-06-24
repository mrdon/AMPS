package com.atlassian.plugins.codgen;

import com.atlassian.plugins.codgen.modules.PluginModuleCreator;

import java.util.Map;

/**
 * The entry point for generating plugin module code. Implementations of this class take care of registering
 * module creators and delegating the actual code generation to the module creator.
 */
public interface PluginModuleCreatorFactory {
    public static final String COMMON = "common";
    public static final String CONFLUENCE = "confluence";
    public static final String JIRA = "jira";
    public static final String BAMBOO = "bamboo";
    public static final String FECRU = "fecru";
    public static final String CROWD = "crowd";

    String getId();

    void registerModuleCreator(PluginModuleCreator moduleCreator);

    PluginModuleCreator getModuleCreator(String key);

    Map<String, PluginModuleCreator> getAllModuleCreators();

}
