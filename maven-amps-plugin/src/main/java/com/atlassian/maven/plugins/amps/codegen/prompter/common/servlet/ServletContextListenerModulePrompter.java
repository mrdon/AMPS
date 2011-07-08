package com.atlassian.maven.plugins.amps.codegen.prompter.common.servlet;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletContextListenerModuleCreator;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletContextListenerProperties;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since version
 */
@ModuleCreatorClass(ServletContextListenerModuleCreator.class)
public class ServletContextListenerModulePrompter extends AbstractModulePrompter {

    public ServletContextListenerModulePrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public PluginModuleProperties getModulePropertiesFromInput() throws PrompterException {
        String className = promptJavaClassname("Enter New Classname", "MyServletContextListener");
        String packageName = promptJavaPackagename("Enter Package Name", "com.atlassian.plugins.servlet.listener");

        ServletContextListenerProperties props = new ServletContextListenerProperties(packageName + "." + className);

        boolean includeExamples = promptForBoolean("Include Example Code?", "N");

        props.setIncludeExamples(includeExamples);

        return props;
    }
}