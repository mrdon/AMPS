package com.atlassian.plugins.codegen.modules.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

/**
 * @since 3.5
 */
public class RESTProperties extends BasicClassModuleProperties
{

    public static final String PATH = "PATH";
    public static final String VERSION = "VERSION";
    public static final String PACKAGES_TO_SCAN = "PACKAGES_TO_SCAN";
    public static final String DISPATCHERS = "DISPATCHERS";

    public static final List<String> ALLOWED_DISPATCHERS = initDispatchers();

    private static List<String> initDispatchers()
    {
        List<String> dispatchers = new ArrayList<String>(4);
        dispatchers.add("REQUEST");
        dispatchers.add("INCLUDE");
        dispatchers.add("FORWARD");
        dispatchers.add("ERROR");

        return Collections.unmodifiableList(dispatchers);
    }

    public RESTProperties()
    {
        this("MyRESTResource");
    }

    public RESTProperties(String fqClassname)
    {
        super(fqClassname);

        put(DISPATCHERS, new ArrayList<String>());
        put(PACKAGES_TO_SCAN, new ArrayList<String>());

        setPath("/" + getProperty(CLASSNAME).toLowerCase());
        setVersion("1.0");
    }

    public void setPath(String path)
    {
        if (!path.startsWith("/"))
        {
            path = "/" + path;
        }

        setProperty(PATH, path);
    }

    public String getPath()
    {
        return getProperty(PATH);
    }

    public void setVersion(String version)
    {
        setProperty(VERSION, version);
    }

    public String getVersion()
    {
        return getProperty(VERSION);
    }

    public void setDispatchers(List<String> dispatchers)
    {
        put(DISPATCHERS, dispatchers);
    }

    @SuppressWarnings(value = "unchecked")
    public void addDispatcher(String dispatcher)
    {
        List<String> dispatchers = (List<String>) get(DISPATCHERS);
        if (dispatchers == null)
        {
            dispatchers = new ArrayList<String>();
            setDispatchers(dispatchers);
        }

        dispatchers.add(dispatcher);
    }

    public void setPackagesToScan(List<String> packages)
    {
        put(PACKAGES_TO_SCAN, packages);
    }

    @SuppressWarnings(value = "unchecked")
    public void addPackageToScan(String packageToScan)
    {
        List<String> packages = (List<String>) get(PACKAGES_TO_SCAN);
        if (packages == null)
        {
            packages = new ArrayList<String>();
            setPackagesToScan(packages);
        }

        packages.add(packageToScan);
    }

    public List<String> getPackagesToScan()
    {
        return (List<String>) get(PACKAGES_TO_SCAN);
    }

    public List<String> getDispatchers()
    {
        return (List<String>) get(DISPATCHERS);
    }

    public List<String> allowedDispatchers()
    {
        return ALLOWED_DISPATCHERS;
    }
}
