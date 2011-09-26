package com.atlassian.maven.plugins.amps.codegen.prompter.common;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.RESTModuleCreator;
import com.atlassian.plugins.codegen.modules.common.RESTProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(RESTModuleCreator.class)
public class RESTPrompter extends AbstractModulePrompter<RESTProperties>
{

    public RESTPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public RESTProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        String className = promptJavaClassname("Enter New Classname", "MyRestResource");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".rest");

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        RESTProperties props = new RESTProperties(fqClass);

        props.setPath(promptNotBlank("Enter REST Path", props.getPath()));
        props.setVersion(promptNotBlank("Enter Version", props.getVersion()));

        return props;
    }

    @Override
    public void promptForAdvancedProperties(RESTProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {
        props.setPackagesToScan(promptForList("Add Package To Scan?", "Enter Package"));

        List<String> dispatchers = promptForDispatchers(props.allowedDispatchers());
        if (dispatchers.size() > 0)
        {
            props.setDispatchers(dispatchers);
        }
    }

    private List<String> promptForDispatchers(List<String> allowedDispatchers) throws PrompterException
    {
        List<String> dispatchers = new ArrayList<String>();
        List<String> mutableValues = new ArrayList<String>(allowedDispatchers);

        promptForDispatcher(dispatchers, mutableValues);

        return dispatchers;
    }

    private void promptForDispatcher(List<String> dispatchers, List<String> allowedDispatchers) throws PrompterException
    {
        boolean addDispatcher = promptForBoolean("Add Dispatcher?", "N");

        if (addDispatcher)
        {
            StringBuilder dispatcherQuery = new StringBuilder("Choose A Dispatcher\n");
            List<String> indexChoices = new ArrayList<String>(allowedDispatchers.size());
            int index = 1;
            for (String dispatcher : allowedDispatchers)
            {
                String strIndex = Integer.toString(index);
                dispatcherQuery.append(strIndex + ": " + dispatcher + "\n");
                indexChoices.add(strIndex);
                index++;
            }

            dispatcherQuery.append("Choose a number: ");
            String dispatcherAnswer = prompt(dispatcherQuery.toString(), indexChoices, "1");
            int selectedIndex = Integer.parseInt(dispatcherAnswer) - 1;

            String selectedDispatcher = allowedDispatchers.get(selectedIndex);

            dispatchers.add(selectedDispatcher);
            allowedDispatchers.remove(selectedIndex);

            promptForDispatcher(dispatchers, allowedDispatchers);
        }
    }
}