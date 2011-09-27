package com.atlassian.maven.plugins.amps.codegen.prompter.common.servlet;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletContextListenerModuleCreator;
import com.atlassian.plugins.codegen.modules.common.servlet.ServletContextListenerProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.6
 */
@ModuleCreatorClass(ServletContextListenerModuleCreator.class)
public class ServletContextListenerModulePrompter extends AbstractModulePrompter<ServletContextListenerProperties>
{

    public ServletContextListenerModulePrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public ServletContextListenerProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        String className = promptJavaClassname("Enter New Classname", "MyServletContextListener");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".servlet.listener");

        return new ServletContextListenerProperties(ClassnameUtil.fullyQualifiedName(packageName, className));
    }

}