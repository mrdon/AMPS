package com.atlassian.plugins.codegen.modules.common.moduletype;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @since version
 */
public class ModuleTypeProperties extends BasicClassModuleProperties {
    public static final String INTERFACE_CLASS = "INTERFACE_CLASS";
    public static final String FQ_INTERFACE = "FQ_INTERFACE";
    public static final String INTERFACE_PACKAGE = "INTERFACE_PACKAGE";

    public ModuleTypeProperties() {
        this("MyModuleTypeProperties");
    }

    public ModuleTypeProperties(String fqClassName) {
        super(fqClassName);
    }

    public void setFullyQualifiedInterface(String fqName) {
        String classname;
        if (StringUtils.isNotBlank(fqName)) {

            if (fqName.lastIndexOf(".") > 0) {
                classname = StringUtils.substringAfterLast(fqName, ".");
                String packageName = StringUtils.substringBeforeLast(fqName, ".");
                setProperty(INTERFACE_CLASS, classname);
                setProperty(INTERFACE_PACKAGE, packageName);
            } else {
                classname = fqName;
                setProperty(INTERFACE_CLASS, classname);
                setProperty(INTERFACE_PACKAGE, "");
            }

            setProperty(FQ_INTERFACE, fqName);
        }
    }
}
