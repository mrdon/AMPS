package com.atlassian.maven.plugins.amps.codegen.prompter.common.servlet;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletContextParameterModuleCreator;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletContextParameterProperties;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since version
 */
@ModuleCreatorClass(ServletContextParameterModuleCreator.class)
public class ServletContextParameterPrompter extends AbstractModulePrompter<ServletContextParameterProperties> {

    public ServletContextParameterPrompter(Prompter prompter) {
        super(prompter);

    }


    @Override
    public ServletContextParameterProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String moduleName = promptNotBlank("Enter Plugin Module Name", "MyContextParam");
        ServletContextParameterProperties props = new ServletContextParameterProperties(moduleName);

        props.setParamName(promptNotBlank("Parameter Name"));
        props.setParamValue(promptNotBlank("Parameter Value"));

        suppressAdvancedPrompt();
        suppressExamplesPrompt();

        return props;
    }
}