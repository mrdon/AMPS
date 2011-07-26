package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @since version
 */
public class WebResourceTransformerProperties extends BasicClassModuleProperties {

    public WebResourceTransformerProperties() {
        this("My Web Resource Transformer");
    }

    public WebResourceTransformerProperties(String fqClassName) {
        super(fqClassName);
    }
}
