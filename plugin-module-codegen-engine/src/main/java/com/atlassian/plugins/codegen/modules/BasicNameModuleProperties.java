package com.atlassian.plugins.codegen.modules;

import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * @since version
 */
public class BasicNameModuleProperties extends AbstractNameBasedModuleProperties {

    public BasicNameModuleProperties() {
        this("MyPluginModule");
    }

    public BasicNameModuleProperties(String moduleName) {
        super();

        setModuleNameAndKey(moduleName);
    }


}