package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.5
 */
public class WebPanelRendererProperties extends BasicClassModuleProperties {

    public WebPanelRendererProperties() {
        this("MyWebPanelRenderer");
    }

    public WebPanelRendererProperties(String fqClassName) {
        super(fqClassName);
    }
}
