package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jaxen.function.StringFunction;

import java.io.File;

/**
 * @since 3.5
 */
@RefAppPluginModuleCreator
@JiraPluginModuleCreator
@ConfluencePluginModuleCreator
@FeCruPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class GadgetModuleCreator extends AbstractPluginModuleCreator<GadgetProperties> {

    public static final String MODULE_NAME = "Gadget Plugin Module";
    private static final String TEMPLATE_PREFIX = "templates/common/gadget/";

    //stub
    private static final String GADGET_TEMPLATE = TEMPLATE_PREFIX + "gadget.xml.vtl";

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "gadget-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, GadgetProperties props) throws Exception {


        if (props.includeExamples()) {

        } else {
            String gadgetLocation = props.getLocation();
            String gadgetFilename = FilenameUtils.getName(gadgetLocation);
            String gadgetPath = FilenameUtils.getPath(gadgetLocation);
            File gadgetFolder;

            if(StringUtils.isNotBlank(gadgetPath)) {
                gadgetFolder = new File(location.getResourcesDir(),gadgetPath);
            } else {
                gadgetFolder = location.getResourcesDir();
            }

            //gadget
            templateHelper.writeFileFromTemplate(GADGET_TEMPLATE,gadgetFilename,gadgetFolder,props);

        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
