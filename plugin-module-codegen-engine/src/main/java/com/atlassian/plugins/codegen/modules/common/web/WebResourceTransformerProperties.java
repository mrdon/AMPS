package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

/**
 * @since 3.6
 */
public class WebResourceTransformerProperties extends BasicClassModuleProperties
{

    public WebResourceTransformerProperties()
    {
        this("My Web Resource Transformer");
    }

    public WebResourceTransformerProperties(String fqClassName)
    {
        super(fqClassName);
    }
}
