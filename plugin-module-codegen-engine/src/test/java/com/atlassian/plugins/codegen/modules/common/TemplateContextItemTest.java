package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleCreatorRegistry;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @since version
 */
public class TemplateContextItemTest extends AbstractCodegenTestCase<TemplateContextItemProperties> {

    public static final String MODULE_NAME = "My Template Context Item";
    public static final String COMPONENT_REF = "i18nResolver";
    public static final String CLASSNAME = "com.atlassian.component.SomeSingleton";
    public static final String CONTEXT_KEY = "i18n";

    @Before
    public void runGenerator() throws Exception {
        setCreator(new TemplateContextItemModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new TemplateContextItemProperties(MODULE_NAME,CONTEXT_KEY));

        props.setIncludeExamples(false);

    }

    @Test
    public void componentRefModuleIsValid() throws Exception {
        props.setComponentRef(COMPONENT_REF);
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        String xpath = "/atlassian-plugin/template-context-item[@name='My Template Context Item' and @key='my-template-context-item' and @i18n-name-key='my-template-context-item.name' and @component-ref='" + COMPONENT_REF + "' and @global='false' and @context-key='" + CONTEXT_KEY + "']";
        Node itemNode = pluginDoc.selectSingleNode(xpath);

        assertNotNull("component ref context item not found", itemNode);
    }

    @Test
    public void classModuleIsValid() throws Exception {
        props.setFullyQualifiedClassname(CLASSNAME);
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        String xpath = "/atlassian-plugin/template-context-item[@name='My Template Context Item' and @key='my-template-context-item' and @i18n-name-key='my-template-context-item.name' and @class='" + CLASSNAME + "' and @global='false' and @context-key='" + CONTEXT_KEY + "']";
        Node itemNode = pluginDoc.selectSingleNode(xpath);

        assertNotNull("class context item not found", itemNode);
    }

    @Test
    public void globalModuleIsValid() throws Exception {
        props.setComponentRef(COMPONENT_REF);
        props.setGlobal(true);
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        String xpath = "/atlassian-plugin/template-context-item[@name='My Template Context Item' and @key='my-template-context-item' and @i18n-name-key='my-template-context-item.name' and @component-ref='" + COMPONENT_REF + "' and @global='true' and @context-key='" + CONTEXT_KEY + "']";
        Node itemNode = pluginDoc.selectSingleNode(xpath);

        assertNotNull("component ref context item not found", itemNode);
    }
}
