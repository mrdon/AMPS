package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.ClassWithInterfaceProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @since version
 */
public class RPCProperties extends ClassWithInterfaceProperties {

    public static final String SERVICE_PATH = "SERVICE_PATH";
    private boolean soap;

    public RPCProperties() {
        this("My RPC");
    }

    public RPCProperties(String fqClassName) {
        super();
        setSoap(true);

        setFullyQualifiedInterface(fqClassName);
        setFullyQualifiedClassname(fqClassName + "Impl");

        String classname = getProperty(INTERFACE_CLASS);
        setModuleName(ClassnameUtil.camelCaseToSpaced(classname));
        setModuleKey(ClassnameUtil.camelCaseToDashed(classname).toLowerCase());
        setDescription("The " + getProperty(MODULE_NAME) + " Plugin");
        setNameI18nKey(getProperty(MODULE_KEY) + ".name");
        setDescriptionI18nKey(getProperty(MODULE_KEY) + ".description");

        setServicePath(getInterfaceClass().toLowerCase() + "-v1");
    }

    public RPCProperties(String fqClassName, String servicePath) {
        this(fqClassName);
        setServicePath(servicePath);
    }

    public String getServicePath() {
        return getProperty(SERVICE_PATH);
    }

    public void setServicePath(String path) {
        setProperty(SERVICE_PATH,path);
    }

    public boolean isSoap() {
        return soap;
    }

    public void setSoap(boolean soap) {
        this.soap = soap;
    }
}
