package com.atlassian.plugins.codegen.modules.common.component;

import com.atlassian.plugins.codegen.modules.*;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @since 3.5
 */
public class ComponentImportProperties extends ClassWithInterfaceProperties {
    public static final String FILTER = "FILTER";
    public static final String FQ_INTERFACE = "FQ_INTERFACE";

    public ComponentImportProperties() {
        this("MyComponentImportProperties");
    }

    public ComponentImportProperties(String fqClassName) {
        if(StringUtils.isNotBlank(fqClassName)) {
            setFullyQualifiedInterface(fqClassName);
            setModuleKey(StringUtils.uncapitalize(StringUtils.substringAfterLast(fqClassName,".")));
        }

        setFilter("");
    }

    public void setFilter(String filter) {
        setProperty(FILTER,filter);
    }

}
