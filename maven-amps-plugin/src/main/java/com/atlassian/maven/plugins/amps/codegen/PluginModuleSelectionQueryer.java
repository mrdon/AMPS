package com.atlassian.maven.plugins.amps.codegen;

import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.Map;

/**
 * @since 3.5
 */
public interface PluginModuleSelectionQueryer {
    public PluginModuleCreator selectModule(Map<Class,PluginModuleCreator> map) throws PrompterException;
    public boolean addAnotherModule() throws PrompterException;
}
