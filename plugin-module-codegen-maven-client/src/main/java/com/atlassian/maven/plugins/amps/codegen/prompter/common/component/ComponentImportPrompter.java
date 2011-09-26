package com.atlassian.maven.plugins.amps.codegen.prompter.common.component;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.component.ComponentImportModuleCreator;
import com.atlassian.plugins.codegen.modules.common.component.ComponentImportProperties;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(ComponentImportModuleCreator.class)
public class ComponentImportPrompter extends AbstractModulePrompter<ComponentImportProperties>
{

    public ComponentImportPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public ComponentImportProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        String fqInterface = promptFullyQualifiedJavaClass("Enter Fully Qualified Interface", "");
        ComponentImportProperties props = new ComponentImportProperties(fqInterface);

        props.setModuleKey(prompt("Module Key", StringUtils.uncapitalize(StringUtils.substringAfterLast(fqInterface, "."))));
        props.setFilter(prompt("Filter (not required)"));

        suppressAdvancedPrompt();
        suppressExamplesPrompt();

        return props;
    }


}