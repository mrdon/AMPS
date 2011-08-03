package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @since version
 */
public class TabPanelProperties extends BasicClassModuleProperties {

    public static final String ORDER = "ORDER";
    public static final String LABEL = "LABEL";

    private boolean useCustomClass;

    public TabPanelProperties() {
        this("MyTabPanel");
    }

    public TabPanelProperties(String fqClassName) {
        super(fqClassName);
        setOrder(10);
        useCustomClass = false;
    }

    public void setModuleNameAndKey(String moduleName) {
        if (StringUtils.isNotBlank(moduleName)) {
            setModuleName(moduleName);
            setModuleKey(ClassnameUtil.camelCaseOrSpaceToDashed(moduleName).toLowerCase());
            setDescription("The " + getProperty(MODULE_NAME) + " Plugin");
            setNameI18nKey(getProperty(MODULE_KEY) + ".name");
            setDescriptionI18nKey(getProperty(MODULE_KEY) + ".description");
        }
    }

    public void setLabel(Label label) {
        put(LABEL,label);
        addI18nProperty(label.getKey(),label.getValue());
    }

    public Label getLabel() {
        Label label = null;
        if(keySet().contains(LABEL)) {
            label = (Label)get(LABEL);
        }
        return label;
    }

    public void setOrder(int order) {
        setProperty(ORDER, Integer.toString(order));
    }

    public String getOrder() {
        return getProperty(ORDER);
    }

    public int getOrderAsInt() {
        return Integer.parseInt(getProperty(ORDER));
    }

    public boolean isUseCustomClass() {
        return useCustomClass;
    }

    public void setUseCustomClass(boolean useCustomClass) {
        this.useCustomClass = useCustomClass;
    }
}
