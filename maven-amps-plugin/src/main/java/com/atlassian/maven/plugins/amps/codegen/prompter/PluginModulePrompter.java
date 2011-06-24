package com.atlassian.maven.plugins.amps.codegen.prompter;

import com.atlassian.plugins.codgen.modules.BasicModuleProperties;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * Author: jdoklovic
 */
public interface PluginModulePrompter {

    BasicModuleProperties getModulePropertiesFromInput() throws PrompterException;
}
