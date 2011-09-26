package com.atlassian.plugins.codegen.modules.jira;

import java.io.File;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @since 3.5
 */
public class KeyboardShortcutTest extends AbstractCodegenTestCase<KeyboardShortcutProperties>
{

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new KeyboardShortcutModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new KeyboardShortcutProperties("My Keyboard Shortcut"));

        props.setIncludeExamples(false);


    }

    @Test
    public void allFilesAreGenerated() throws Exception
    {
        creator.createModule(moduleLocation, props);
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception
    {
        props.setOperationType("click");
        props.setOperationValue("do:something");
        props.setShortcut("m");
        props.setContext("issueaction");

        String xpath = "/atlassian-plugin/keyboard-shortcut[@name='My Keyboard Shortcut' and @key='my-keyboard-shortcut' and @i18n-name-key='my-keyboard-shortcut.name']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        Node rootNode = pluginDoc.selectSingleNode(xpath);
        assertNotNull("valid keyboard-shortcut not found", rootNode);

        Node order = rootNode.selectSingleNode("order[text() = '10']");
        Node shortcut = rootNode.selectSingleNode("shortcut[text() = 'm']");
        Node context = rootNode.selectSingleNode("context[text() = 'issueaction']");
        Node operation = rootNode.selectSingleNode("operation[@type='click' and text() = 'do:something']");

        assertNotNull("valid order not found", order);
        assertNotNull("valid shortcut not found", shortcut);
        assertNotNull("valid context not found", context);
        assertNotNull("valid operation not found", operation);
    }

}
