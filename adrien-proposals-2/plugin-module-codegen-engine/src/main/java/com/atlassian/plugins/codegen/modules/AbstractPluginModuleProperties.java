package com.atlassian.plugins.codegen.modules;

import java.util.Properties;

/**
 * @since 3.6
 */
public abstract class AbstractPluginModuleProperties extends Properties implements PluginModuleProperties
{

    protected boolean includeExamples;
    protected Properties i18nProperties;

    protected AbstractPluginModuleProperties()
    {
        super();
        i18nProperties = new Properties();
        includeExamples = false;
        setProductId("RefApp");
    }

    @Override
    public void setProductId(String id)
    {
        setProperty(PRODUCT_ID, id);
    }

    @Override
    public String getProductId()
    {
        return getProperty(PRODUCT_ID);
    }

    @Override
    public void setIncludeExamples(boolean includeExamples)
    {
        this.includeExamples = includeExamples;
    }

    @Override
    public boolean includeExamples()
    {
        return includeExamples;
    }

    @Override
    public void addI18nProperty(String name, String value)
    {
        i18nProperties.setProperty(name, value);
    }

    @Override
    public Properties getI18nProperties()
    {
        return i18nProperties;
    }

}
