package com.atlassian.maven.plugins.amps.codegen.prompter.common.servlet;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletFilterModuleCreator;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletFilterProperties;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since version
 */
@ModuleCreatorClass(ServletFilterModuleCreator.class)
public class ServletFilterModulePrompter extends AbstractModulePrompter {

    public ServletFilterModulePrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public PluginModuleProperties getModulePropertiesFromInput() throws PrompterException {
        String className = promptJavaClassname("Enter New Classname", "MyServletFilter");
        String packageName = promptJavaPackagename("Enter Package Name", "com.atlassian.plugins.servlet.filter");

        ServletFilterProperties props = new ServletFilterProperties(packageName + "." + className);

        boolean showAdvanced = promptForBoolean("Show Advanced Setup?", "N");

        if (showAdvanced) {
            props.setUrlPattern(getUrlPatternFromUser());
            props.setLocation(getLocationFromUser(props.allowedLocations()));
            props.setWeight(Integer.parseInt(getWeightFromUser()));

            List<String> dispatchers = promptForDispatchers(props.allowedDispatchers());
            if (dispatchers.size() > 0) {
                props.setDispatchers(dispatchers);
            }

            Map<String, String> initParams = promptForInitParams();
            if (initParams.size() > 0) {
                props.setInitParams(initParams);
            }
        }

        boolean includeExamples = promptForBoolean("Include Example Code?", "N");

        props.setIncludeExamples(includeExamples);

        return props;
    }


    private String getUrlPatternFromUser() throws PrompterException {
        String pattern = promptNotBlank("URL Pattern", "/*");

        return pattern;
    }

    private String getWeightFromUser() throws PrompterException {
        String weight = prompter.prompt("Location Weight", "100");
        if (StringUtils.isBlank(weight) || !StringUtils.isNumeric(weight)) {
            weight = getWeightFromUser();
        }
        return weight;
    }

    private String getLocationFromUser(List<String> allowedLocations) throws PrompterException {
        StringBuilder locationQuery = new StringBuilder("Choose Filter Chain Location\n");
        List<String> indexChoices = new ArrayList<String>(allowedLocations.size());
        int index = 1;
        for (String location : allowedLocations) {
            String strIndex = Integer.toString(index);
            locationQuery.append(strIndex + ": " + location + "\n");
            indexChoices.add(strIndex);
            index++;
        }

        locationQuery.append("Choose a number: ");
        String locationAnswer = prompter.prompt(locationQuery.toString(), indexChoices, "4");

        return allowedLocations.get(Integer.parseInt(locationAnswer) - 1);
    }

    private List<String> promptForDispatchers(List<String> allowedDispatchers) throws PrompterException {
        List<String> dispatchers = new ArrayList<String>();
        List<String> mutableValues = new ArrayList<String>(allowedDispatchers);

        promptForDispatcher(dispatchers, mutableValues);

        return dispatchers;
    }

    private void promptForDispatcher(List<String> dispatchers, List<String> allowedDispatchers) throws PrompterException {
        boolean addDispatcher = promptForBoolean("Add Dispatcher?", "N");

        if (addDispatcher) {
            StringBuilder dispatcherQuery = new StringBuilder("Choose A Dispatcher\n");
            List<String> indexChoices = new ArrayList<String>(allowedDispatchers.size());
            int index = 1;
            for (String dispatcher : allowedDispatchers) {
                String strIndex = Integer.toString(index);
                dispatcherQuery.append(strIndex + ": " + dispatcher + "\n");
                indexChoices.add(strIndex);
                index++;
            }

            dispatcherQuery.append("Choose a number: ");
            String dispatcherAnswer = prompter.prompt(dispatcherQuery.toString(), indexChoices, "1");
            int selectedIndex = Integer.parseInt(dispatcherAnswer) - 1;

            String selectedDispatcher = allowedDispatchers.get(selectedIndex);

            dispatchers.add(selectedDispatcher);
            allowedDispatchers.remove(selectedIndex);

            promptForDispatcher(dispatchers, allowedDispatchers);
        }
    }

    private Map<String, String> promptForInitParams() throws PrompterException {
        Map<String, String> params = new HashMap<String, String>();
        promptForInitParam(params);

        return params;
    }

    private void promptForInitParam(Map<String, String> params) throws PrompterException {
        StringBuffer addBuffer = new StringBuffer();
        if (params.size() > 0) {
            addBuffer.append("init-params:\n");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                addBuffer.append(entry.getKey()).append("->").append(entry.getValue()).append("\n");
            }
        }
        addBuffer.append("Add Init-Param?");
        boolean addParam = promptForBoolean(addBuffer.toString(), "N");

        if (addParam) {
            String key = promptNotBlank("param key");
            String value = promptNotBlank("param value");
            params.put(key, value);
            promptForInitParam(params);
        }
    }
}
