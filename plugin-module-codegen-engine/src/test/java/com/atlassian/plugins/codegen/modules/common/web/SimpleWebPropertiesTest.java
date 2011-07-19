package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Condition;
import com.atlassian.plugins.codegen.modules.common.Conditions;
import com.atlassian.plugins.codegen.modules.common.Resource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @since version
 */
public class SimpleWebPropertiesTest extends AbstractWebFragmentTest<SimpleWebProperties> {
    public static final String MODULE_NAME = "Simple Web Module";


    @Before
    public void runGenerator() throws Exception {
        setCreator(new SimpleWebModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new SimpleWebProperties(MODULE_NAME));
        props.setIncludeExamples(false);

    }



}
