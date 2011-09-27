package com.atlassian.plugins.codegen.modules.jira;

import java.io.File;

import com.atlassian.plugins.codegen.annotations.Dependencies;
import com.atlassian.plugins.codegen.annotations.Dependency;
import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @since 3.6
 */
@JiraPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class CustomFieldSearcherModuleCreator extends AbstractPluginModuleCreator<CustomFieldSearcherProperties>
{

    public static final String MODULE_NAME = "Custom Field Searcher";
    private static final String TEMPLATE_PREFIX = "templates/jira/customfield/searcher/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "CustomFieldSearcher.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = "templates/generic/GenericTest.java.vtl";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "CustomFieldSearcherFuncTest.java.vtl";
    private static final String VIEW_TEMPLATE = "templates/common/actionview.vm.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "customfield-searcher-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, CustomFieldSearcherProperties props) throws Exception
    {
        String packageName = props.getPackage();
        String classname = props.getClassname();

        if (props.includeExamples())
        {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else
        {
            if (props.generateClass())
            {
                //main class
                templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

                //unit test
                templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(classname), location.getTestDirectory(), packageName, props);

                //func test
                //templateHelper.writeJavaClassFromTemplate(FUNC_TEST_TEMPLATE, funcTestClassname(classname), location.getTestDirectory(), funcTestPackageName(packageName), props);
            }
        }

        //since we know resources are velocity templates, let's create them
        for (Resource resource : props.getResources())
        {
            String resourcePath = FilenameUtils.separatorsToSystem(resource.getLocation());

            if (resourcePath.startsWith("templates" + File.separator) || resourcePath.startsWith(File.separator + "templates" + File.separator))
            {
                resourcePath = StringUtils.substringAfter(resourcePath, "templates" + File.separator);
            }

            File resourceFolder = new File(location.getTemplateDirectory(), FilenameUtils.getPath(resourcePath));
            String resourceFile = FilenameUtils.getName(resourcePath);

            props.setProperty("CURRENT_VIEW", resourceFile);

            templateHelper.writeFileFromTemplate(VIEW_TEMPLATE, FilenameUtils.getName(resourcePath), resourceFolder, props);
        }

        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName()
    {
        return MODULE_NAME;
    }
}
