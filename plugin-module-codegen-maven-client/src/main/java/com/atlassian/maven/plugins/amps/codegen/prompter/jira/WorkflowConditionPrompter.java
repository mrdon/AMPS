package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.jira.WorkflowConditionModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.WorkflowElementProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(WorkflowConditionModuleCreator.class)
public class WorkflowConditionPrompter extends AbstractModulePrompter<WorkflowElementProperties> {

    public WorkflowConditionPrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public WorkflowElementProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String className = promptJavaClassname("Enter New Classname", "MyWorkflowCondition");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".jira.workflow");

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        return new WorkflowElementProperties(fqClass);
    }

    @Override
    public void promptForAdvancedProperties(WorkflowElementProperties props, PluginModuleLocation moduleLocation) throws PrompterException {

    }
}