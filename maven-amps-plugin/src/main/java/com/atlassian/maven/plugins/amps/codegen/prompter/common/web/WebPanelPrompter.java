package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.web.WebPanelModuleCreator;
import com.atlassian.plugins.codegen.modules.common.web.WebPanelProperties;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(WebPanelModuleCreator.class)
public class WebPanelPrompter extends AbstractWebFragmentPrompter<WebPanelProperties> {

    public WebPanelPrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public WebPanelProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String moduleName = promptNotBlank("Enter Plugin Module Name", "My Web Panel");
        String location = promptNotBlank("Enter Location (e.g. system.admin/globalsettings)");

        WebPanelProperties props = new WebPanelProperties(moduleName, location);

        suppressAdvancedNamePrompt();

        return props;
    }

    @Override
    public void promptForAdvancedProperties(WebPanelProperties props, PluginModuleLocation moduleLocation) throws PrompterException {
        //WEIGHT
        props.setWeight(promptForInt("Weight", 1000));

        //RESOURCES
        props.setResources(promptForResources());

        //CONTEXT PROVIDER
        props.setContextProvider(promptForContextProvider());

        //CONDITIONS
        props.setConditions(promptForConditions());
    }
}