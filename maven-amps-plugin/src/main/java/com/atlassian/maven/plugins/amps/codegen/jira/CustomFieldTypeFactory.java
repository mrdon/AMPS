package com.atlassian.maven.plugins.amps.codegen.jira;


import com.atlassian.maven.plugins.amps.codegen.annotations.asm.CustomFieldTypeLocator;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @since version
 */
public class CustomFieldTypeFactory {
    protected static Map<String, String> fields = new TreeMap<String, String>();

    public static void locateAvailableCustomFieldTypes(List<String> classpathElements) throws Exception {
        List<URL> fieldURLs = new ArrayList<URL>();
        for(String path : classpathElements) {
            File aFile = new File(path);
            fieldURLs.add(aFile.toURI().toURL());
        }

        URLClassLoader fieldLoader = URLClassLoader.newInstance(fieldURLs.toArray(new URL[]{}));
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(fieldLoader);

        CustomFieldTypeLocator locator = new CustomFieldTypeLocator(fields);
        locator.parse();

        Thread.currentThread().setContextClassLoader(oldLoader);
    }
    public static Map<String,String> getAvailableCustomFieldTypes() {
        return Collections.unmodifiableMap(fields);
    }
}
