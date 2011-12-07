package com.atlassian.plugins.codegen.modules.jira;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

/**
 * @since 3.6
 */
public class ActionProperties extends BasicClassModuleProperties
{

    public static final String VIEWS = "VIEWS";
    public static final String ALIAS = "ALIAS";

    public ActionProperties()
    {
        this("MyWebworkAction");
    }

    public ActionProperties(String fqClassName)
    {
        super(fqClassName);
        setViews(new ArrayList<View>());
        setAlias(getClassname());
    }

    public void setViews(List<View> views)
    {
        put(VIEWS, views);
    }

    public List<View> getViews()
    {
        return (List<View>) get(VIEWS);
    }

    public void addView(View view)
    {
        List<View> views = (List<View>) get(VIEWS);
        if (null == views)
        {
            views = new ArrayList<View>();
            put(VIEWS, views);
        }
        views.add(view);
    }

    public void setAlias(String alias)
    {
        setProperty(ALIAS, alias);
    }

    public String getAlias()
    {
        return getProperty(ALIAS);
    }
}
