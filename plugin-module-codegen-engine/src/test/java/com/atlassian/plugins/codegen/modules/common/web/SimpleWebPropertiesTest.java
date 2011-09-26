package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.junit.Before;

/**
 * @since 3.5
 */
public class SimpleWebPropertiesTest extends AbstractWebFragmentTest<SimpleWebProperties>
{
    public static final String MODULE_NAME = "Simple Web Module";


    @Before
    public void runGenerator() throws Exception
    {
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
