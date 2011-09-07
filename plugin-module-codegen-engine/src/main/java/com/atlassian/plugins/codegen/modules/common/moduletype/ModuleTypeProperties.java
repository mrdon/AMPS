package com.atlassian.plugins.codegen.modules.common.moduletype;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.ClassWithInterfaceProperties;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.5
 */
public class ModuleTypeProperties extends ClassWithInterfaceProperties {

    public ModuleTypeProperties() {
        this("MyModuleTypeProperties");
    }

    public ModuleTypeProperties(String fqClassName) {
        super(fqClassName);
    }
}
