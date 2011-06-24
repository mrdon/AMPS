package com.atlassian.maven.plugins.amps.codegen;

import com.atlassian.plugins.codgen.modules.PluginModuleCreator;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.Map;

/**
 * Author: jdoklovic
 */
public interface PluginModuleSelectionQueryer {
    public PluginModuleCreator selectModule(Map<String,PluginModuleCreator> map) throws PrompterException;
}
