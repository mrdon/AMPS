package com.atlassian.maven.plugins.amps.codegen.prompter.common;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.DownloadablePluginResourceModuleCreator;
import com.atlassian.plugins.codegen.modules.common.DownloadablePluginResourceProperties;
import com.atlassian.plugins.codegen.modules.common.Resource;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(DownloadablePluginResourceModuleCreator.class)
public class DownloadablePluginResourcePrompter extends AbstractResourcePrompter<DownloadablePluginResourceProperties> {

    public DownloadablePluginResourcePrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public DownloadablePluginResourceProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        suppressAdvancedPrompt();
        return new DownloadablePluginResourceProperties(promptForResource());
    }

    @Override
    public void promptForAdvancedProperties(DownloadablePluginResourceProperties props, PluginModuleLocation moduleLocation) throws PrompterException {

    }
}