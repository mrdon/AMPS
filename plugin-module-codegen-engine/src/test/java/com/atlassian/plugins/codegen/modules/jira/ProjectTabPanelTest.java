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
 * @since 3.5
 */
public class ProjectTabPanelTest extends AbstractCodegenTestCase<TabPanelProperties>
{
    public static final String PACKAGE_NAME = "com.atlassian.plugins.jira.tabpanels";
    protected File templatePath;

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new ProjectTabPanelModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new TabPanelProperties(PACKAGE_NAME + ".MyProjectTabPanel"));

        props.setIncludeExamples(false);

        templatePath = new File(templateDir, "tabpanels");
    }

    @Test
    public void customFilesAreGenerated() throws Exception
    {
        props.setUseCustomClass(true);
        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyProjectTabPanel.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyProjectTabPanelTest.java").exists());
        assertTrue("view template not generated", new File(templatePath, "my-project-tab-panel.vm").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void genericFilesAreGenerated() throws Exception
    {
        setProps(new TabPanelProperties(ProjectTabPanelModuleCreator.FQ_GENERIC_CLASS));
        props.setUseCustomClass(false);
        props.setModuleNameAndKey("My Project Tab Panel");
        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertFalse("main class should not be generated", new File(srcDir, packagePath + File.separator + "MyProjectTabPanel.java").exists());
        assertFalse("test class should not be generated", new File(testDir, packagePath + File.separator + "MyProjectTabPanelTest.java").exists());
        assertTrue("view template not generated", new File(templatePath, "my-project-tab-panel.vm").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void customModuleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/project-tabpanel[@name='My Project Tab Panel' and @key='my-project-tab-panel' and @i18n-name-key='my-project-tab-panel.name' and @class='" + PACKAGE_NAME + ".MyProjectTabPanel']";
        props.setUseCustomClass(true);
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid project-tabpanel not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void genericModuleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/project-tabpanel[@name='My Project Tab Panel' and @key='my-project-tab-panel' and @i18n-name-key='my-project-tab-panel.name' and @class='" + ProjectTabPanelModuleCreator.FQ_GENERIC_CLASS + "']";

        setProps(new TabPanelProperties(ProjectTabPanelModuleCreator.FQ_GENERIC_CLASS));
        props.setModuleNameAndKey("My Project Tab Panel");
        props.setUseCustomClass(false);
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid generic project-tabpanel not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void labelIsAdded() throws Exception
    {
        String xpath = "/atlassian-plugin/project-tabpanel[@name='My Project Tab Panel' and @key='my-project-tab-panel' and @i18n-name-key='my-project-tab-panel.name' and @class='" + PACKAGE_NAME + ".MyProjectTabPanel']";

        Label label = new Label("common.concepts.project.tabpanel", "my project panel");
        props.setLabel(label);
        props.setUseCustomClass(true);
        creator.createModule(moduleLocation, props);

        String labelXpath = "/atlassian-plugin/project-tabpanel/label[@key='common.concepts.project.tabpanel']";
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid custom project-tabpanel not found", pluginDoc.selectSingleNode(xpath));

        Node labelNode = pluginDoc.selectSingleNode(labelXpath);
        assertNotNull("label not found", labelNode);

        Properties i18nprops = loadI18nProperties();
        assertTrue("label i18n not found", i18nprops.containsKey(label.getKey()));
        assertEquals("label i18n has wrong value", label.getValue(), i18nprops.getProperty(label.getKey()));

        Document viewDoc = getXmlDocument(new File(templatePath, "my-project-tab-panel.vm"));
        String viewLableXpath = "/div/h3[text() = \"$i18n.getText('" + label.getKey() + "')\"]";
        assertNotNull("label not found in view template", viewDoc.selectSingleNode(viewLableXpath));
    }

    @Test
    public void orderIsAdded() throws Exception
    {
        String xpath = "/atlassian-plugin/project-tabpanel[@name='My Project Tab Panel' and @key='my-project-tab-panel' and @i18n-name-key='my-project-tab-panel.name' and @class='" + PACKAGE_NAME + ".MyProjectTabPanel']";
        props.setOrder(10);
        props.setUseCustomClass(true);
        creator.createModule(moduleLocation, props);

        String orderXpath = "/atlassian-plugin/project-tabpanel/order[text() = '10']";
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid custom project-tabpanel not found", pluginDoc.selectSingleNode(xpath));

        Node orderNode = pluginDoc.selectSingleNode(orderXpath);
        assertNotNull("order not found", orderNode);

    }

}
