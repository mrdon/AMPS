package com.atlassian.maven.plugins.amps.codegen.prompter;

import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;

/**
 *
 */
public interface PluginModulePrompterRegistry {
    void registerModulePrompter(Class creatorClass, PluginModulePrompter prompter);
    PluginModulePrompter getPrompterForCreatorClass(Class clazz);
}
