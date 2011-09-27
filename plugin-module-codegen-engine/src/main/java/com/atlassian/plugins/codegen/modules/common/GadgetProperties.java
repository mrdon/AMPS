package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.modules.BasicNameModuleProperties;

/**
 * @since 3.6
 */
public class GadgetProperties extends BasicNameModuleProperties
{

    public static final String LOCATION = "LOCATION";


    public GadgetProperties()
    {
        this("My Gadget");
    }

    public GadgetProperties(String moduleName)
    {
        super(moduleName);
    }

    public GadgetProperties(String moduleName, String location)
    {
        this(moduleName);
        setLocation(location);
    }

    public String getLocation()
    {
        return getProperty(LOCATION);
    }

    public void setLocation(String location)
    {
        setProperty(LOCATION, location);
    }
}
