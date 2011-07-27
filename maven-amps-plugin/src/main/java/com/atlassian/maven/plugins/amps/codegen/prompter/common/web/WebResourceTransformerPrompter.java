package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.web.WebResourceTransformerModuleCreator;
import com.atlassian.plugins.codegen.modules.common.web.WebResourceTransformerProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since version
 */
@ModuleCreatorClass(WebResourceTransformerModuleCreator.class)
public class WebResourceTransformerPrompter extends AbstractModulePrompter<WebResourceTransformerProperties> {

    public WebResourceTransformerPrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public WebResourceTransformerProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String className = promptJavaClassname("Enter New Classname", "MyWebResourceTransformer");

        /* Due to PLUG-779 the package MUST be com.atlassian.plugin.webresource for the tests to work! */
        //String packageName = promptJavaPackagename("Enter Package Name", "com.atlassian.plugins.webresource");

        String packageName = "com.atlassian.plugin.webresource";
        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        return new WebResourceTransformerProperties(fqClass);
    }

    @Override
    public void promptForAdvancedProperties(WebResourceTransformerProperties props, PluginModuleLocation moduleLocation) throws PrompterException {

    }
}