package com.atlassian.plugins.codgen;

import com.atlassian.plugins.codgen.modules.jira.WorkflowPostFunctionModuleCreator;

/**
 * Author: jdoklovic
 */
public class JiraPluginModuleCreatorFactory extends CommonPluginModuleCreatorFactory {

    public JiraPluginModuleCreatorFactory() {
        super();
        registerModuleCreator(new WorkflowPostFunctionModuleCreator());
    }

    @Override
    public String getId() {
        return JIRA;
    }
}
