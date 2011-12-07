package com.atlassian.plugins.codegen.modules.jira;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.plugins.codegen.modules.BasicNameModuleProperties;

/**
 * @since 3.6
 */
public class WebworkProperties extends BasicNameModuleProperties
{
    public static final String ACTIONS = "ACTIONS";

    public WebworkProperties()
    {
        this("My Webwork Action");
    }

    public WebworkProperties(String moduleName)
    {
        super(moduleName);
        setActions(new ArrayList<ActionProperties>());
    }

    public List<ActionProperties> getActions()
    {
        return (List<ActionProperties>) get(ACTIONS);
    }

    public void setActions(List<ActionProperties> actions)
    {
        put(ACTIONS, actions);
    }

    public void addAction(ActionProperties action)
    {
        List<ActionProperties> actions = getActions();

        if (null == actions)
        {
            actions = new ArrayList<ActionProperties>();
            put(ACTIONS, actions);
        }
        actions.add(action);
    }
}
