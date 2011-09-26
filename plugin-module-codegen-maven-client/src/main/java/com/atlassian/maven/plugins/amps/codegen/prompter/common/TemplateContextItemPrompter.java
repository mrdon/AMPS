package com.atlassian.maven.plugins.amps.codegen.prompter.common;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.TemplateContextItemModuleCreator;
import com.atlassian.plugins.codegen.modules.common.TemplateContextItemProperties;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(TemplateContextItemModuleCreator.class)
public class TemplateContextItemPrompter extends AbstractModulePrompter<TemplateContextItemProperties>
{

    public TemplateContextItemPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public TemplateContextItemProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        String moduleName = promptNotBlank("Enter Plugin Module Name", "My Context Item");
        String contextKey = promptNotBlank("Enter Context Key");

        TemplateContextItemProperties props = new TemplateContextItemProperties(moduleName, contextKey);
        promptForRefOrClass(props);

        props.setGlobal(promptForBoolean("Global Access?", "N"));

        suppressAdvancedNamePrompt();
        return props;
    }

    @Override
    public void promptForAdvancedProperties(TemplateContextItemProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {

    }

    protected void promptForRefOrClass(TemplateContextItemProperties props) throws PrompterException
    {
        String componentRef = prompt("Enter Component-Ref Key (leave blank to specify class)");
        if (StringUtils.isBlank(componentRef))
        {
            String className = promptFullyQualifiedJavaClassBlankOK("Enter Fully Qualified Class Name", "");
            if (StringUtils.isBlank(className))
            {
                promptForRefOrClass(props);
            } else
            {
                props.setFullyQualifiedClassname(className);
            }
        } else
        {
            props.setComponentRef(componentRef);
        }
    }
}