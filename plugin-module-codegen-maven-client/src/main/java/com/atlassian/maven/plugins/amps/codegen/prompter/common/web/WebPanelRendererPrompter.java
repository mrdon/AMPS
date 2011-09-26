package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.web.WebPanelRendererModuleCreator;
import com.atlassian.plugins.codegen.modules.common.web.WebPanelRendererProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(WebPanelRendererModuleCreator.class)
public class WebPanelRendererPrompter extends AbstractModulePrompter<WebPanelRendererProperties>
{

    public WebPanelRendererPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public WebPanelRendererProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        String className = promptJavaClassname("Enter New Classname", "MyWebPanelRenderer");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".web");

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        return new WebPanelRendererProperties(fqClass);
    }

    @Override
    public void promptForAdvancedProperties(WebPanelRendererProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {

    }
}