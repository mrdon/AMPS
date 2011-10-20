package com.atlassian.maven.plugins.amps.codegen.prompter;

import org.apache.maven.plugin.logging.Log;

/**
 * @since 3.6
 */
public interface PluginModulePrompterFactory
{
    void scanForPrompters() throws Exception;

    PluginModulePrompter getPrompterForCreatorClass(Class clazz);

    void setLog(Log log);

    Log getLog();
}
