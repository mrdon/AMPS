package com.atlassian.plugins.codegen.modules.jira;

import java.io.File;
import java.util.Properties;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Label;

import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @since 3.6
 */
public class ComponentTabPanelTest extends AbstractCodegenTestCase<TabPanelProperties>
{
    public static final String PACKAGE_NAME = "com.atlassian.plugins.jira.tabpanels";
    protected File templatePath;

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new ComponentTabPanelModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new TabPanelProperties(PACKAGE_NAME + ".MyComponentTabPanel"));

        props.setIncludeExamples(false);

        templatePath = new File(templateDir, "tabpanels");
    }

    @Test
    public void customFilesAreGenerated() throws Exception
    {
        props.setUseCustomClass(true);
        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyComponentTabPanel.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyComponentTabPanelTest.java").exists());
        assertTrue("view template not generated", new File(templatePath, "my-component-tab-panel.vm").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void genericFilesAreGenerated() throws Exception
    {
        setProps(new TabPanelProperties(ComponentTabPanelModuleCreator.FQ_GENERIC_CLASS));
        props.setUseCustomClass(false);
        props.setModuleNameAndKey("My Component Tab Panel");
        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertFalse("main class should not be generated", new File(srcDir, packagePath + File.separator + "MyComponentTabPanel.java").exists());
        assertFalse("test class should not be generated", new File(testDir, packagePath + File.separator + "MyComponentTabPanelTest.java").exists());
        assertTrue("view template not generated", new File(templatePath, "my-component-tab-panel.vm").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void customModuleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/component-tabpanel[@name='My Component Tab Panel' and @key='my-component-tab-panel' and @i18n-name-key='my-component-tab-panel.name' and @class='" + PACKAGE_NAME + ".MyComponentTabPanel']";
        props.setUseCustomClass(true);
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid custom component-tabpanel not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void genericModuleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/component-tabpanel[@name='My Component Tab Panel' and @key='my-component-tab-panel' and @i18n-name-key='my-component-tab-panel.name' and @class='" + ComponentTabPanelModuleCreator.FQ_GENERIC_CLASS + "']";

        setProps(new TabPanelProperties(ComponentTabPanelModuleCreator.FQ_GENERIC_CLASS));
        props.setModuleNameAndKey("My Component Tab Panel");
        props.setUseCustomClass(false);
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid generic component-tabpanel not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void labelIsAdded() throws Exception
    {
        String xpath = "/atlassian-plugin/component-tabpanel[@name='My Component Tab Panel' and @key='my-component-tab-panel' and @i18n-name-key='my-component-tab-panel.name' and @class='" + PACKAGE_NAME + ".MyComponentTabPanel']";

        Label label = new Label("common.concepts.component.tabpanel", "my component panel");
        props.setLabel(label);
        props.setUseCustomClass(true);
        creator.createModule(moduleLocation, props);

        String labelXpath = "/atlassian-plugin/component-tabpanel/label[@key='common.concepts.component.tabpanel']";
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid custom component-tabpanel not found", pluginDoc.selectSingleNode(xpath));

        Node labelNode = pluginDoc.selectSingleNode(labelXpath);
        assertNotNull("label not found", labelNode);

        Properties i18nprops = loadI18nProperties();
        assertTrue("label i18n not found", i18nprops.containsKey(label.getKey()));
        assertEquals("label i18n has wrong value", label.getValue(), i18nprops.getProperty(label.getKey()));

        Document viewDoc = getXmlDocument(new File(templatePath, "my-component-tab-panel.vm"));
        String viewLableXpath = "/div/h3[text() = \"$i18n.getText('" + label.getKey() + "')\"]";
        assertNotNull("label not found in view template", viewDoc.selectSingleNode(viewLableXpath));
    }

    @Test
    public void orderIsAdded() throws Exception
    {
        String xpath = "/atlassian-plugin/component-tabpanel[@name='My Component Tab Panel' and @key='my-component-tab-panel' and @i18n-name-key='my-component-tab-panel.name' and @class='" + PACKAGE_NAME + ".MyComponentTabPanel']";
        props.setOrder(10);
        props.setUseCustomClass(true);
        creator.createModule(moduleLocation, props);

        String orderXpath = "/atlassian-plugin/component-tabpanel/order[text() = '10']";
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid custom component-tabpanel not found", pluginDoc.selectSingleNode(xpath));

        Node orderNode = pluginDoc.selectSingleNode(orderXpath);
        assertNotNull("order not found", orderNode);

    }

}
