package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.BasicNameModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Conditional;
import com.atlassian.plugins.codegen.modules.common.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @since version
 */
public abstract class AbstractConditionsProperties extends BasicNameModuleProperties {
    public static final String CONDITIONS = "CONDITIONS";

    public AbstractConditionsProperties() {
        this("My Web Module");
    }

    public AbstractConditionsProperties(String moduleName) {
        super(moduleName);
        setConditions(new ArrayList<Conditional>());
    }

    public void setConditions(List<Conditional> conditions) {
        put(CONDITIONS,conditions);
    }

    public List<Conditional> getConditions() {
        return (List<Conditional>)get(CONDITIONS);
    }
}
