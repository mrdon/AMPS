package com.atlassian.maven.plugins.amps.codegen.prompter;

import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * Author: jdoklovic
 */
public interface PluginModulePrompter {

    PluginModuleProperties getModulePropertiesFromInput() throws PrompterException;
}
