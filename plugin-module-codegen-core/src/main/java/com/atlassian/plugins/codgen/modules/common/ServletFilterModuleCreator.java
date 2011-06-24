package com.atlassian.plugins.codgen.modules.common;

import com.atlassian.plugins.codgen.PluginModuleLocation;
import com.atlassian.plugins.codgen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codgen.modules.BasicModuleProperties;
import com.atlassian.plugins.codgen.util.CodeTemplateHelper;

/**
 * Author: jdoklovic
 */
public class ServletFilterModuleCreator extends AbstractPluginModuleCreator {

    public static final String MODULE_ID = "Servlet Filter";
    private static final String TEMPLATE_PREFIX = "templates/common/servlet/filter/";
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "ServletFilter.java.vm";
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;
    private static final String PLUGIN_MODULE_TEMPLATE = "templates/common/servlet/filter/servlet-filter-plugin.xml.vm";

    @Override
    public void createModule(PluginModuleLocation location, BasicModuleProperties props) throws Exception {
        String packageName = props.getProperty(BasicModuleProperties.PACKAGE);
        String classname = props.getProperty(BasicModuleProperties.CLASSNAME);

        if (props.includeExamples()) {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else {
            templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getId() {
        return MODULE_ID;
    }
}
