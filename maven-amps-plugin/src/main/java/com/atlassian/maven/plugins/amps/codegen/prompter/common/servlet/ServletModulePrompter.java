package com.atlassian.maven.plugins.amps.codegen.prompter.common.servlet;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletModuleCreator;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletProperties;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.HashMap;
import java.util.Map;

/**
 * @since version
 */
@ModuleCreatorClass(ServletModuleCreator.class)
public class ServletModulePrompter extends AbstractModulePrompter {

    public ServletModulePrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public PluginModuleProperties getModulePropertiesFromInput() throws PrompterException {
        String className = promptJavaClassname("Enter New Classname", "MyServlet");
        String packageName = promptJavaPackagename("Enter Package Name", "com.atlassian.plugins.servlet");

        ServletProperties props = new ServletProperties(packageName + "." + className);

        boolean showAdvanced = promptForBoolean("Show Advanced Setup?", "N");

        if (showAdvanced) {
            props.setUrlPattern(getUrlPatternFromUser("/" + className.toLowerCase()));

            Map<String, String> initParams = promptForInitParams();
            if (initParams.size() > 0) {
                props.setInitParams(initParams);
            }
        }

        boolean includeExamples = promptForBoolean("Include Example Code?", "N");

        props.setIncludeExamples(includeExamples);

        return props;
    }

    private String getUrlPatternFromUser(String defaultVal) throws PrompterException {
        String pattern = promptNotBlank("URL Pattern", defaultVal);

        return pattern;
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