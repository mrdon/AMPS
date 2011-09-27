package com.atlassian.maven.plugins.amps.codegen.jira;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import com.atlassian.maven.plugins.amps.codegen.annotations.asm.CustomFieldSearcherLocator;

/**
 * @since 3.6
 */
public class CustomFieldSearcherFactory
{
    protected static Map<String, String> searchers = new TreeMap<String, String>();

    public static void locateAvailableCustomFieldSearchers(List<String> classpathElements) throws Exception
    {
        List<URL> fieldURLs = new ArrayList<URL>();
        for (String path : classpathElements)
        {
            File aFile = new File(path);
            fieldURLs.add(aFile.toURI()
                    .toURL());
        }

        URLClassLoader fieldLoader = URLClassLoader.newInstance(fieldURLs.toArray(new URL[]{}));
        ClassLoader oldLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread()
                .setContextClassLoader(fieldLoader);

        CustomFieldSearcherLocator locator = new CustomFieldSearcherLocator(searchers);
        locator.parse();

        Thread.currentThread()
                .setContextClassLoader(oldLoader);
    }

    public static Map<String, String> getAvailableCustomFieldSearchers()
    {
        return Collections.unmodifiableMap(searchers);
    }
}
