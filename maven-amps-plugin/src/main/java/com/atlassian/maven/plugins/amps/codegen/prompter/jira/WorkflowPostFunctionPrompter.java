package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.jira.ActionTypeFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.jira.WorkflowPostFunctionModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.WorkflowPostFunctionProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 3.5
 */
@ModuleCreatorClass(WorkflowPostFunctionModuleCreator.class)
public class WorkflowPostFunctionPrompter extends AbstractModulePrompter<WorkflowPostFunctionProperties> {

    public WorkflowPostFunctionPrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public WorkflowPostFunctionProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String className = promptJavaClassname("Enter New Classname", "MyPostFunction");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".jira.workflow");

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        return new WorkflowPostFunctionProperties(fqClass);
    }

    @Override
    public void promptForAdvancedProperties(WorkflowPostFunctionProperties props, PluginModuleLocation moduleLocation) throws PrompterException {
        props.setOrderable(promptForBoolean("Is Function Orderable?","Y"));
        props.setUnique(promptForBoolean("Is Function Unique?","Y"));
        props.setDeletable(promptForBoolean("Is Function Deletable?","Y"));

        String addable = "";
        if(promptForBoolean("Define Action Types? (addable)","N")) {
            addable = promptForAddables();
        }

        props.setAddable(addable);
    }

    private String promptForAddables() throws PrompterException {
        List<String> addables = new ArrayList<String>();
        List<String> mutableValues = new ArrayList<String>(ActionTypeFactory.getAvailableActionTypes());

        promptForAddable(addables, mutableValues);

        StringBuffer addableBuffer = new StringBuffer();
        for(String addable : addables) {
            if(addableBuffer.length() > 0) {
                addableBuffer.append(",");
            }

            addableBuffer.append(addable);
        }

        return addableBuffer.toString();
    }

    private void promptForAddable(List<String> addables, List<String> allowedAddables) throws PrompterException {
        boolean addAddable;

        if(addables.isEmpty()) {
            addAddable = true;
        } else {
            addAddable = promptForBoolean("Add Action Type?", "N");
        }

        if (addAddable) {
            StringBuilder addableQuery = new StringBuilder("Choose A Type\n");
            List<String> indexChoices = new ArrayList<String>(allowedAddables.size());
            int index = 1;
            for (String addable : allowedAddables) {
                String strIndex = Integer.toString(index);
                addableQuery.append(strIndex + ": " + addable + "\n");
                indexChoices.add(strIndex);
                index++;
            }

            addableQuery.append("Choose a number: ");
            String addableAnswer = prompt(addableQuery.toString(), indexChoices, "1");
            int selectedIndex = Integer.parseInt(addableAnswer) - 1;

            String selectedDispatcher = allowedAddables.get(selectedIndex);

            addables.add(selectedDispatcher);
            allowedAddables.remove(selectedIndex);

            if(!allowedAddables.isEmpty()) {
                promptForAddable(addables, allowedAddables);
            }
        }
    }
}