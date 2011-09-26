package com.atlassian.maven.plugins.amps.codegen.prompter;

/**
 *
 */
public interface PluginModulePrompterRegistry
{
    void registerModulePrompter(Class creatorClass, PluginModulePrompter prompter);

    PluginModulePrompter getPrompterForCreatorClass(Class clazz);
}
