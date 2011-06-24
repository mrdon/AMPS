package com.atlassian.maven.plugins.amps.codegen.registry;

import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;

/**
 * Author: jdoklovic
 */
public interface PluginModulePrompterRegistry {
    PluginModulePrompter getPrompterForCreatorClass(Class clazz);

    void initPrompters();
}
