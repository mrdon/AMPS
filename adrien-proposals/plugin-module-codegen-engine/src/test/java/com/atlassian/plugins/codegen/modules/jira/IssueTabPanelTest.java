package com.atlassian.plugins.codegen.modules.jira;

import java.io.File;
import java.util.Properties;
import java.util.regex.Matcher;

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
public class IssueTabPanelTest extends AbstractCodegenTestCase<TabPanelProperties>
{
    public static final String PACKAGE_NAME = "com.atlassian.plugins.jira.tabpanels";
    protected File templatePath;

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new IssueTabPanelModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new TabPanelProperties(PACKAGE_NAME + ".MyIssueTabPanel"));

        props.setIncludeExamples(false);
        props.setUseCustomClass(true);

        templatePath = new File(templateDir, "tabpanels");

    }

    @Test
    public void allFilesAreGenerated() throws Exception
    {
        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyIssueTabPanel.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyIssueTabPanelTest.java").exists());
        assertTrue("view template not generated", new File(templatePath, "my-issue-tab-panel.vm").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/issue-tabpanel[@name='My Issue Tab Panel' and @key='my-issue-tab-panel' and @i18n-name-key='my-issue-tab-panel.name' and @class='" + PACKAGE_NAME + ".MyIssueTabPanel']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid issue-tabpanel not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void labelIsAdded() throws Exception
    {
        String xpath = "/atlassian-plugin/issue-tabpanel[@name='My Issue Tab Panel' and @key='my-issue-tab-panel' and @i18n-name-key='my-issue-tab-panel.name' and @class='" + PACKAGE_NAME + ".MyIssueTabPanel']";

        Label label = new Label("common.concepts.issue.tabpanel", "my issue panel");
        props.setLabel(label);
        creator.createModule(moduleLocation, props);

        String labelXpath = "/atlassian-plugin/issue-tabpanel/label[@key='common.concepts.issue.tabpanel']";
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid custom issue-tabpanel not found", pluginDoc.selectSingleNode(xpath));

        Node labelNode = pluginDoc.selectSingleNode(labelXpath);
        assertNotNull("label not found", labelNode);

        Properties i18nprops = loadI18nProperties();
        assertTrue("label i18n not found", i18nprops.containsKey(label.getKey()));
        assertEquals("label i18n has wrong value", label.getValue(), i18nprops.getProperty(label.getKey()));

        Document viewDoc = getXmlDocument(new File(templatePath, "my-issue-tab-panel.vm"));
        String viewLableXpath = "/div/h3[text() = \"$i18n.getText('" + label.getKey() + "')\"]";
        assertNotNull("label not found in view template", viewDoc.selectSingleNode(viewLableXpath));
    }

    @Test
    public void orderIsAdded() throws Exception
    {
        String xpath = "/atlassian-plugin/issue-tabpanel[@name='My Issue Tab Panel' and @key='my-issue-tab-panel' and @i18n-name-key='my-issue-tab-panel.name' and @class='" + PACKAGE_NAME + ".MyIssueTabPanel']";
        props.setOrder(10);
        creator.createModule(moduleLocation, props);

        String orderXpath = "/atlassian-plugin/issue-tabpanel/order[text() = '10']";
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid custom issue-tabpanel not found", pluginDoc.selectSingleNode(xpath));

        Node orderNode = pluginDoc.selectSingleNode(orderXpath);
        assertNotNull("order not found", orderNode);

    }

}
