package com.atlassian.plugins.codegen.modules.common.web;

import java.util.Properties;

import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Tooltip;

import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @since 3.5
 */
public class WebSectionTest extends AbstractWebFragmentTest<WebSectionProperties>
{
    public static final String MODULE_NAME = "Awesome Web Section";
    public static final String CUSTOM_LOCATION = "system.admin/mysection";

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new WebSectionModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WebSectionProperties(MODULE_NAME, CUSTOM_LOCATION));
        props.setIncludeExamples(false);
    }

    @Test
    public void moduleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/web-section[@name='Awesome Web Section' and @key='awesome-web-section' and @i18n-name-key='awesome-web-section.name' and @location='system.admin/mysection' and @weight='1000']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid web-section not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void moduleIsValidWithCustomWeight() throws Exception
    {
        props.setWeight(20);

        String xpath = "/atlassian-plugin/web-section[@name='Awesome Web Section' and @key='awesome-web-section' and @i18n-name-key='awesome-web-section.name' and @location='system.admin/mysection' and @weight='20']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid web-section with custom weight not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void labelAdded() throws Exception
    {
        String paramVal0 = "$helper.project.name";
        String paramVal1 = "$helper.project.description";
        Label label = new Label("web.section.mysection", "awesome web section");
        label.addParam(paramVal0);
        label.addParam(paramVal1);

        props.setLabel(label);

        String labelXpath = "/atlassian-plugin/web-section/label[@key='web.section.mysection']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        Node labelNode = pluginDoc.selectSingleNode(labelXpath);

        assertNotNull("label not found", labelNode);

        Node param0 = labelNode.selectSingleNode("param[@name='param0' and @value='" + paramVal0 + "']");
        Node param1 = labelNode.selectSingleNode("param[@name='param1' and @value='" + paramVal1 + "']");

        assertNotNull("param 0 not found", param0);
        assertNotNull("param 1 not found", param1);

        Properties i18nprops = loadI18nProperties();
        assertTrue("label i18n not found", i18nprops.containsKey(label.getKey()));
        assertEquals("label i18n has wrong value", label.getValue(), i18nprops.getProperty(label.getKey()));

    }

    @Test
    public void paramsAdded() throws Exception
    {
        props.addParam("isAwesomeSection", "true");
        props.addParam("isSuperAwesome", "false");

        String param1Xpath = "/atlassian-plugin/web-section/param[@name='isAwesomeSection' and @value='true']";
        String param2Xpath = "/atlassian-plugin/web-section/param[@name='isSuperAwesome' and @value='false']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("param 1 not found", pluginDoc.selectSingleNode(param1Xpath));
        assertNotNull("param 2 not found", pluginDoc.selectSingleNode(param2Xpath));
    }

    @Test
    public void tooltipAdded() throws Exception
    {
        Tooltip tooltip = new Tooltip("web.section.mysection.tooltip", "this is an awesome section");
        props.setTooltip(tooltip);

        String xpath = "/atlassian-plugin/web-section/tooltip[@key='web.section.mysection.tooltip']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("tooltip not found", pluginDoc.selectSingleNode(xpath));

        Properties i18nprops = loadI18nProperties();
        assertTrue("tooltip i18n not found", i18nprops.containsKey(tooltip.getKey()));
        assertEquals("tooltip i18n has wrong value", tooltip.getValue(), i18nprops.getProperty(tooltip.getKey()));
    }

}
