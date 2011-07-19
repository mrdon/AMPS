package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Icon;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Link;
import com.atlassian.plugins.codegen.modules.common.Tooltip;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @since version
 */
public class WebItemTest extends AbstractWebFragmentTest<WebItemProperties> {
    public static final String MODULE_NAME = "My Web Item";
    public static final String GLOBAL_SETTINGS_SECTION = "system.admin/globalsettings";

    @Before
    public void runGenerator() throws Exception {
        setCreator(new WebItemModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WebItemProperties(MODULE_NAME, GLOBAL_SETTINGS_SECTION));
        props.setIncludeExamples(false);

    }

    @Test
    public void moduleIsValid() throws Exception {
        String xpath = "/atlassian-plugin/web-item[@name='My Web Item' and @key='my-web-item' and @i18n-name-key='my-web-item.name' and @section='system.admin/globalsettings' and @weight='1000']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid web-item not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void moduleIsValidWithCustomWeight() throws Exception {
        props.setWeight(20);

        String xpath = "/atlassian-plugin/web-item[@name='My Web Item' and @key='my-web-item' and @i18n-name-key='my-web-item.name' and @section='system.admin/globalsettings' and @weight='20']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid web-item with custom weight not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void iconAdded() throws Exception {
        String path = "/images/myicon.png";
        Link link = new Link(path);
        Icon icon = new Icon(16, 16);
        icon.setLink(link);

        props.setIcon(icon);

        String xpath = "/atlassian-plugin/web-item/icon[@width='16' and @height='16']/link";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        Node linkNode = pluginDoc.selectSingleNode(xpath);

        assertNotNull("icon link not found",linkNode);
        assertEquals("wrong icon link",path,linkNode.getStringValue());
    }

    @Test
    public void labelAdded() throws Exception {
        String paramVal0 = "$helper.project.name";
        String paramVal1 = "$helper.project.description";
        Label label = new Label("common.concepts.create.new.issue", "create new issue");
        label.addParam(paramVal0);
        label.addParam(paramVal1);

        props.setLabel(label);

        String labelXpath = "/atlassian-plugin/web-item/label[@key='common.concepts.create.new.issue']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        Node labelNode = pluginDoc.selectSingleNode(labelXpath);

        assertNotNull("label not found",labelNode);

        Node param0 = labelNode.selectSingleNode("param[@name='param0' and @value='" + paramVal0 + "']");
        Node param1 = labelNode.selectSingleNode("param[@name='param1' and @value='" + paramVal1 + "']");

        assertNotNull("param 0 not found", param0);
        assertNotNull("param 1 not found", param1);

        Properties i18nprops = loadI18nProperties();
        assertTrue("label i18n not found", i18nprops.containsKey(label.getKey()));
        assertEquals("label i18n has wrong value",label.getValue(),i18nprops.getProperty(label.getKey()));

    }

    @Test
    public void linkAdded() throws Exception {
        String path = "/secure/CreateIssue!default.jspa";
        Link link = new Link(path);
        link.setLinkId("create link");

        props.setLink(link);

        String linkXpath = "/atlassian-plugin/web-item/link[@linkId='create link']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        Node linkNode = pluginDoc.selectSingleNode(linkXpath);

        assertNotNull("link not found",linkNode);
        assertEquals("wrong link value", path, linkNode.getStringValue());
    }

    @Test
    public void paramsAdded() throws Exception {
        props.addParam("isPopupLink","true");
        props.addParam("isSuperAwesome","false");

        String param1Xpath = "/atlassian-plugin/web-item/param[@name='isPopupLink' and @value='true']";
        String param2Xpath = "/atlassian-plugin/web-item/param[@name='isSuperAwesome' and @value='false']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("param 1 not found",pluginDoc.selectSingleNode(param1Xpath));
        assertNotNull("param 2 not found",pluginDoc.selectSingleNode(param2Xpath));
    }

    @Test
    public void tooltipAdded() throws Exception {
        Tooltip tooltip = new Tooltip("common.concepts.create.new.issue.tooltip","creates a new issue");
        props.setTooltip(tooltip);

        String xpath = "/atlassian-plugin/web-item/tooltip[@key='common.concepts.create.new.issue.tooltip']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("tooltip not found",pluginDoc.selectSingleNode(xpath));

        Properties i18nprops = loadI18nProperties();
        assertTrue("tooltip i18n not found", i18nprops.containsKey(tooltip.getKey()));
        assertEquals("tooltip i18n has wrong value",tooltip.getValue(),i18nprops.getProperty(tooltip.getKey()));
    }
}
