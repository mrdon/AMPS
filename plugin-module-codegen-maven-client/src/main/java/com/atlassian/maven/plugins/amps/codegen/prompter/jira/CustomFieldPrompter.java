package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.jira.CustomFieldTypeFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.common.AbstractResourcePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.jira.CustomFieldModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.CustomFieldProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(CustomFieldModuleCreator.class)
public class CustomFieldPrompter extends AbstractResourcePrompter<CustomFieldProperties>
{

    public CustomFieldPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public CustomFieldProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        String className = promptJavaClassname("Enter New Classname", "MyCustomField");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".jira.customfields");

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        CustomFieldProperties props = new CustomFieldProperties(fqClass);
        List<Resource> resources = new ArrayList<Resource>(3);

        String templatePath = "/templates/customfields/" + props.getModuleKey() + "/";

        Resource view = new Resource();
        view.setName("view");
        view.setType("velocity");
        view.setLocation(templatePath + "view.vm");

        Resource edit = new Resource();
        edit.setName("edit");
        edit.setType("velocity");
        edit.setLocation(templatePath + "edit.vm");

        resources.add(view);
        resources.add(edit);

        props.setResources(resources);

        return props;
    }

    @Override
    public void promptForAdvancedProperties(CustomFieldProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {
        String fqSuperClass = promptForSuperClass();
        if (StringUtils.isNotBlank(fqSuperClass))
        {
            props.setFullyQualifiedClassToExtend(fqSuperClass);
        }

        props.setResources(promptForResources());
    }

    protected String promptForSuperClass() throws PrompterException
    {
        String fqSuperClass = "";

        Map<String, String> customFieldTypes = CustomFieldTypeFactory.getAvailableCustomFieldTypes();
        if (!customFieldTypes.isEmpty())
        {

            StringBuilder superQuery = new StringBuilder("Choose A Custom Field Type To Extend\n");
            List<String> indexChoices = new ArrayList<String>(customFieldTypes.size());
            Map<String, String> indexedValues = new HashMap<String, String>();
            int index = 1;
            String strIndex;
            for (Map.Entry<String, String> entry : customFieldTypes.entrySet())
            {
                strIndex = Integer.toString(index);
                superQuery.append(strIndex + ": " + entry.getKey() + "\n");
                indexChoices.add(strIndex);
                indexedValues.put(strIndex, entry.getValue());
                index++;
            }

            strIndex = Integer.toString(index);

            superQuery.append("Choose a number: ");
            String superAnswer = prompt(superQuery.toString(), indexChoices, "");
            int answerInt = (Integer.parseInt(superAnswer) - 1);

            if (answerInt < (customFieldTypes.size()))
            {
                fqSuperClass = indexedValues.get(superAnswer);
            }
        }

        return fqSuperClass;
    }

    @Override
    protected Resource promptForResource() throws PrompterException
    {
        Resource resource = new Resource();
        resource.setName(promptNotBlank("Enter Resource Name"));

        resource.setType("velocity");
        resource.setLocation(promptNotBlank("Enter Location (path to resource file)"));

        return resource;
    }

}