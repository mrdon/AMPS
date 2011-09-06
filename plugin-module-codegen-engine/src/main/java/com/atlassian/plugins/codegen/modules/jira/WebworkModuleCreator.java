package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * @since 3.5
 */
@JiraPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
        , @Dependency(groupId = "org.apache.httpcomponents", artifactId = "httpclient", version = "4.1.1", scope = "test")
})
public class WebworkModuleCreator extends AbstractPluginModuleCreator<WebworkProperties> {

    public static final String MODULE_NAME = "Webwork Plugin";
    private static final String TEMPLATE_PREFIX = "templates/jira/webwork/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "WebworkAction.java.vtl";
    //private static final String UNIT_TEST_TEMPLATE = TEMPLATE_PREFIX + "WebworkActionTest.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = "templates/generic/GenericTest.java.vtl";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "WebworkActionFuncTest.java.vtl";
     private static final String VIEW_TEMPLATE = "templates/common/actionview.vm.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "webwork-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, WebworkProperties props) throws Exception {

        if (props.includeExamples()) {

        } else {
            for(ActionProperties action : props.getActions()) {
                //main class
                templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, action.getClassname(), location.getSourceDirectory(), action.getPackage(), action);

                //unit test
                templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(action.getClassname()), location.getTestDirectory(), action.getPackage(), action);

                //write view templates
                for(View view : action.getViews()) {

                    String viewPath = FilenameUtils.separatorsToSystem(view.getPath());

                    if(viewPath.startsWith("templates" + File.separator) || viewPath.startsWith(File.separator + "templates" + File.separator)) {
                        viewPath = StringUtils.substringAfter(viewPath, "templates" + File.separator);
                    }

                    File viewFolder = new File(location.getTemplateDirectory(),FilenameUtils.getPath(viewPath));
                    String viewFile = FilenameUtils.getName(viewPath);

                    action.setProperty("CURRENT_VIEW",viewFile);

                    templateHelper.writeFileFromTemplate(VIEW_TEMPLATE,FilenameUtils.getName(viewPath),viewFolder,action);
                }
            }
        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
