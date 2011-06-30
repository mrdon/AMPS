package com.atlassian.plugins.codgen.modules.common;

import com.atlassian.plugins.codgen.PluginModuleLocation;
import com.atlassian.plugins.codgen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codgen.modules.BasicModuleProperties;
import com.atlassian.plugins.codgen.util.CodeTemplateHelper;
import org.apache.commons.lang.StringUtils;

/**
 * Author: jdoklovic
 */
public class ServletFilterModuleCreator extends AbstractPluginModuleCreator {

    public static final String MODULE_ID = "Servlet Filter";
    private static final String TEMPLATE_PREFIX = "templates/common/servlet/filter/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "ServletFilter.java.vm";
    private static final String UNIT_TEST_TEMPLATE = TEMPLATE_PREFIX + "ServletFilterTest.java.vm";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "ServletFilterFuncTest.java.vm";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = "templates/common/servlet/filter/servlet-filter-plugin.xml.vm";

    @Override
    public void createModule(PluginModuleLocation location, BasicModuleProperties props) throws Exception {
        String packageName = props.getProperty(BasicModuleProperties.PACKAGE);

        String classname = props.getProperty(BasicModuleProperties.CLASSNAME);

        if (props.includeExamples()) {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else {
            //main class
            templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

            //unit test
            templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(classname), location.getTestDirectory(), packageName, props);

            //func test
            templateHelper.writeJavaClassFromTemplate(FUNC_TEST_TEMPLATE, funcTestClassname(classname), location.getTestDirectory(), funcTestPackageName(packageName), props);
        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getId() {
        return MODULE_ID;
    }
}
