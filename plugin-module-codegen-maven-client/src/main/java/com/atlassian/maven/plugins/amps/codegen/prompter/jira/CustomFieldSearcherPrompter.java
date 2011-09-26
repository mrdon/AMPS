package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.jira.CustomFieldSearcherFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.common.AbstractResourcePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.jira.CustomFieldSearcherModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.CustomFieldSearcherProperties;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(CustomFieldSearcherModuleCreator.class)
public class CustomFieldSearcherPrompter extends AbstractResourcePrompter<CustomFieldSearcherProperties>
{

    public static final String CUSTOM_SEARCHER = "Custom Searcher Class";

    public CustomFieldSearcherPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public CustomFieldSearcherProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {

        CustomFieldSearcherProperties props = new CustomFieldSearcherProperties(promptForSearcherClass());
        if (props.getPackage()
                .equals("com.atlassian.jira.issue.customfields.searchers"))
        {
            props.setGenerateClass(false);
        } else
        {
            props.setGenerateClass(true);
        }

        props.setValidCustomFieldPackage(promptNotBlank("Enter Valid CustomField Package", getDefaultBasePackage()));
        props.setValidCustomFieldKey(promptNotBlank("Enter Valid CustomField Key"));

        List<Resource> resources = new ArrayList<Resource>(1);

        String templatePath = "/templates/customfields/" + props.getModuleKey() + "/";

        Resource search = new Resource();
        search.setName("search");
        search.setType("velocity");
        search.setLocation(templatePath + "search.vm");

        resources.add(search);

        props.setResources(resources);

        return props;
    }

    @Override
    public void promptForAdvancedProperties(CustomFieldSearcherProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {

        props.setResources(promptForResources());
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

    protected String promptForSearcherClass() throws PrompterException
    {
        String fqProvider = "";
        Map<String, String> searchers = CustomFieldSearcherFactory.getAvailableCustomFieldSearchers();
        if (searchers.isEmpty())
        {
            fqProvider = promptFullyQualifiedJavaClass("Enter Fully Qualified Searcher Class", getDefaultBasePackage() + ".jira.customfields.MyCustomFieldSearcher");
        } else
        {

            StringBuilder contextQuery = new StringBuilder("Choose A Searcher Class\n");
            List<String> indexChoices = new ArrayList<String>(searchers.size());
            Map<String, String> indexedValues = new HashMap<String, String>();
            int index = 1;
            String strIndex;
            for (Map.Entry<String, String> entry : searchers.entrySet())
            {
                strIndex = Integer.toString(index);
                contextQuery.append(strIndex + ": " + entry.getKey() + "\n");
                indexChoices.add(strIndex);
                indexedValues.put(strIndex, entry.getValue());
                index++;
            }

            strIndex = Integer.toString(index);
            contextQuery.append(strIndex + ": " + CUSTOM_SEARCHER + "\n");
            indexChoices.add(strIndex);
            indexedValues.put(strIndex, CUSTOM_SEARCHER);

            contextQuery.append("Choose a number: ");
            String contextAnswer = prompt(contextQuery.toString(), indexChoices, "");
            int answerInt = (Integer.parseInt(contextAnswer) - 1);

            if (answerInt < (searchers.size()))
            {
                fqProvider = indexedValues.get(contextAnswer);
            } else
            {
                fqProvider = promptFullyQualifiedJavaClass("Enter Fully Qualified Searcher Class", getDefaultBasePackage() + ".jira.customfields.MyCustomFieldSearcher");
            }
        }

        return fqProvider;
    }
}