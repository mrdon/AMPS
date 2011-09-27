package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.dom4j.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @since 3.6
 */
public class WebPanelTest extends AbstractWebFragmentTest<WebPanelProperties>
{
    public static final String MODULE_NAME = "Awesome Web Panel";
    public static final String CUSTOM_LOCATION = "system.admin/mysection";

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new WebPanelModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WebPanelProperties(MODULE_NAME, CUSTOM_LOCATION));
        props.setIncludeExamples(false);
    }

    @Test
    public void moduleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/web-panel[@name='Awesome Web Panel' and @key='awesome-web-panel' and @i18n-name-key='awesome-web-panel.name' and @location='system.admin/mysection' and @weight='1000']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid web-panel not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void moduleIsValidWithCustomWeight() throws Exception
    {
        props.setWeight(20);

        String xpath = "/atlassian-plugin/web-panel[@name='Awesome Web Panel' and @key='awesome-web-panel' and @i18n-name-key='awesome-web-panel.name' and @location='system.admin/mysection' and @weight='20']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid web-panel with custom weight not found", pluginDoc.selectSingleNode(xpath));
    }

}
