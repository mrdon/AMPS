package com.atlassian.maven.plugins.amps.codegen.jira;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @since 3.5
 */
public class ActionTypeFactory
{
    protected static List<String> availableActionTypes;

    public static void locateAvailableActionTypes(List<String> classpathElements) throws MalformedURLException
    {
        List<URL> classpathURLs = new ArrayList<URL>();
        for (String path : classpathElements)
        {
            File aFile = new File(path);
            classpathURLs.add(aFile.toURI()
                    .toURL());
        }

        URLClassLoader productClassLoader = URLClassLoader.newInstance(classpathURLs.toArray(new URL[]{}));

        try
        {
            Class workflowClass = productClassLoader.loadClass("com.atlassian.jira.workflow.JiraWorkflow");
            Field allTypesField = workflowClass.getField("ACTION_TYPE_ALL");
            Collection<String> allTypes = (Collection<String>) allTypesField.get(null);
            availableActionTypes = new ArrayList<String>(allTypes);

        } catch (Exception e)
        {
            //don't care
        }

    }

    public static List<String> getAvailableActionTypes()
    {
        if (null == availableActionTypes)
        {
            availableActionTypes = new ArrayList<String>();
        }

        Collections.sort(availableActionTypes);

        return Collections.unmodifiableList(availableActionTypes);
    }
}
