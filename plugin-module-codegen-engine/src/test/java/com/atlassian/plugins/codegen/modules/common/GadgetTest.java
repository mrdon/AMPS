package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @since 3.5
 */
public class GadgetTest extends AbstractCodegenTestCase<GadgetProperties> {

    @Before
    public void runGenerator() throws Exception {
        setCreator(new GadgetModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new GadgetProperties("My Gadget", "gadgets/mygadget/gadget.xml"));

        props.setIncludeExamples(false);

        creator.createModule(moduleLocation, props);
    }

    @Test
    public void allFilesAreGenerated() throws Exception {

        File gadgetFolder = new File(resourcesDir, FilenameUtils.getPath(props.getLocation()));

        assertTrue("main gadget not generated", new File(gadgetFolder, FilenameUtils.getName(props.getLocation())).exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception {
        String xpath = "/atlassian-plugin/gadget[@name='My Gadget' and @key='my-gadget' and @i18n-name-key='my-gadget.name' and @location='" + props.getLocation() + "']";


        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid gadget not found", pluginDoc.selectSingleNode(xpath));
    }

}
