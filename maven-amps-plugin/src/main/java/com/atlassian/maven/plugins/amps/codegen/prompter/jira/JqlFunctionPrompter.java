package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.jira.JqlFunctionModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.JqlFunctionProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(JqlFunctionModuleCreator.class)
public class JqlFunctionPrompter extends AbstractModulePrompter<JqlFunctionProperties> {

    public JqlFunctionPrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public JqlFunctionProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String className = promptJavaClassname("Enter New Classname", "MyJqlFunction");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".jira.jql");

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        return new JqlFunctionProperties(fqClass);
    }

    @Override
    public void promptForAdvancedProperties(JqlFunctionProperties props, PluginModuleLocation moduleLocation) throws PrompterException {

    }
}