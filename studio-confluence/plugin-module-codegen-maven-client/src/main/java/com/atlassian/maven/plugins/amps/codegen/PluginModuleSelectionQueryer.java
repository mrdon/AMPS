package com.atlassian.maven.plugins.amps.codegen;

import java.util.Map;

import com.atlassian.plugins.codegen.modules.PluginModuleCreator;

import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.6
 */
public interface PluginModuleSelectionQueryer
{
    public PluginModuleCreator selectModule(Map<Class, PluginModuleCreator> map) throws PrompterException;

    public boolean addAnotherModule() throws PrompterException;
}
