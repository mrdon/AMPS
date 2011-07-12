package com.atlassian.maven.plugins.amps.codegen.prompter;

import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * Author: jdoklovic
 */
public interface PluginModulePrompter<T extends PluginModuleProperties> {

    PluginModuleProperties getModulePropertiesFromInput(PluginModuleLocation moduleLocation) throws PrompterException;
    <T extends PluginModuleProperties> T  promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException;
    void promptForAdvancedProperties(T props, PluginModuleLocation moduleLocation) throws PrompterException;
}
