package com.atlassian.maven.plugins.amps.codegen.registry;

import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.common.ServletFilterModulePrompter;
import com.atlassian.plugins.codgen.modules.common.ServletFilterModuleCreator;
import org.codehaus.plexus.components.interactivity.Prompter;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: jdoklovic
 */
public class PluginModulePrompterRegistryImpl implements PluginModulePrompterRegistry {

    private Map<Class,PluginModulePrompter> modulePrompters;
    private Prompter prompter;

    public PluginModulePrompterRegistryImpl() {
        this.modulePrompters = new HashMap<Class, PluginModulePrompter>();

    }

    @Override
    public PluginModulePrompter getPrompterForCreatorClass(Class clazz) {
        return modulePrompters.get(clazz);
    }


    @Override
    public void initPrompters() {
        modulePrompters.put(ServletFilterModuleCreator.class,new ServletFilterModulePrompter(prompter));
    }
}
