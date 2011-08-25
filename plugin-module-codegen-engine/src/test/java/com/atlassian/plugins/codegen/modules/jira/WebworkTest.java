package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @since version
 */
public class WebworkTest extends AbstractCodegenTestCase<WebworkProperties> {
    public static final String PACKAGE_NAME = "com.atlassian.plugins.jira.webwork";

    @Before
    public void runGenerator() throws Exception {
        setCreator(new WebworkModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WebworkProperties("My Webwork"));

        props.setIncludeExamples(false);

    }

    @Test
    public void singleActionFilesAreGenerated() throws Exception {
        ActionProperties action = new ActionProperties(PACKAGE_NAME + ".ActionOne");
        props.addAction(action);
        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertTrue("action class not generated", new File(srcDir, packagePath + File.separator + "ActionOne.java").exists());
        assertTrue("action test class not generated", new File(testDir, packagePath + File.separator + "ActionOneTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void multipleActionFilesAreGenerated() throws Exception {
        ActionProperties action1 = new ActionProperties(PACKAGE_NAME + ".ActionOne");
        ActionProperties action2 = new ActionProperties(PACKAGE_NAME + ".ActionTwo");
        props.addAction(action1);
        props.addAction(action2);
        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertTrue("action one class not generated", new File(srcDir, packagePath + File.separator + "ActionOne.java").exists());
        assertTrue("action one test class not generated", new File(testDir, packagePath + File.separator + "ActionOneTest.java").exists());
        assertTrue("action two class not generated", new File(srcDir, packagePath + File.separator + "ActionTwo.java").exists());
        assertTrue("action two test class not generated", new File(testDir, packagePath + File.separator + "ActionTwoTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void singleActionSingleViewModuleIsValid() throws Exception {
        String actionOneFQName = PACKAGE_NAME + ".ActionOne";
        ActionProperties action = new ActionProperties(actionOneFQName);
        View successView = new View("success","templates/success.vm");
        action.addView(successView);
        props.addAction(action);
        creator.createModule(moduleLocation, props);

        String webworkXpath = "/atlassian-plugin/webwork1[@name='My Webwork' and @key='my-webwork' and @i18n-name-key='my-webwork.name']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node webworkNode = pluginDoc.selectSingleNode(webworkXpath);

        assertNotNull("valid webwork not found", webworkNode);

        List<Node> actions = webworkNode.selectNodes("actions/action");
        assertEquals("wrong number of actions found",1,actions.size());

        Node actionOneNode = webworkNode.selectSingleNode("actions/action[@name='" + actionOneFQName + "' and @alias='ActionOne']");
        assertNotNull("action node not found",actionOneNode);

        List<Node> views = actionOneNode.selectNodes("view");
        assertEquals("wrong number of views",1,views.size());

        Node successNode = actionOneNode.selectSingleNode("view[@name='success' and text()='templates/success.vm']");
        assertNotNull("success view not found",successNode);

        File successFile = new File(moduleLocation.getTemplateDirectory(),"success.vm");
        assertTrue("view file does not exist!",successFile.exists());
    }

    @Test
    public void singleActionMultipleViewModuleIsValid() throws Exception {
        String actionOneFQName = PACKAGE_NAME + ".ActionOne";
        ActionProperties action = new ActionProperties(actionOneFQName);
        View successView = new View("success","templates/success.vm");
        View errorView = new View("error","templates/error.vm");
        action.addView(successView);
        action.addView(errorView);
        props.addAction(action);
        creator.createModule(moduleLocation, props);

        String webworkXpath = "/atlassian-plugin/webwork1[@name='My Webwork' and @key='my-webwork' and @i18n-name-key='my-webwork.name']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node webworkNode = pluginDoc.selectSingleNode(webworkXpath);

        assertNotNull("valid webwork not found", webworkNode);

        List<Node> actions = webworkNode.selectNodes("actions/action");
        assertEquals("wrong number of actions found",1,actions.size());

        Node actionOneNode = webworkNode.selectSingleNode("actions/action[@name='" + actionOneFQName + "' and @alias='ActionOne']");
        assertNotNull("action node not found",actionOneNode);

        List<Node> views = actionOneNode.selectNodes("view");
        assertEquals("wrong number of views",2,views.size());

        Node successNode = actionOneNode.selectSingleNode("view[@name='success' and text()='templates/success.vm']");
        assertNotNull("success view not found",successNode);

        File successFile = new File(moduleLocation.getTemplateDirectory(),"success.vm");
        assertTrue("success file does not exist!",successFile.exists());

        Node errorNode = actionOneNode.selectSingleNode("view[@name='error' and text()='templates/error.vm']");
        assertNotNull("error view not found",errorNode);

        File errorFile = new File(moduleLocation.getTemplateDirectory(),"error.vm");
        assertTrue("error file does not exist!",errorFile.exists());
    }

    @Test
    public void multipleActionMultipleViewModuleIsValid() throws Exception {
        String actionOneFQName = PACKAGE_NAME + ".ActionOne";
        String actionTwoFQName = PACKAGE_NAME + ".ActionTwo";

        ActionProperties actionOne = new ActionProperties(actionOneFQName);
        View successView = new View("success","/templates/subdir/success.vm");
        View errorView = new View("error","templates/subdir/error.vm");
        actionOne.addView(successView);
        actionOne.addView(errorView);

        ActionProperties actionTwo = new ActionProperties(actionTwoFQName);
        View inputView = new View("input","templates/input.vm");
        actionTwo.addView(inputView);

        props.addAction(actionOne);
        props.addAction(actionTwo);
        creator.createModule(moduleLocation, props);

        String webworkXpath = "/atlassian-plugin/webwork1[@name='My Webwork' and @key='my-webwork' and @i18n-name-key='my-webwork.name']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node webworkNode = pluginDoc.selectSingleNode(webworkXpath);

        assertNotNull("valid webwork not found", webworkNode);

        List<Node> actions = webworkNode.selectNodes("actions/action");
        assertEquals("wrong number of actions found",2,actions.size());

        Node actionOneNode = webworkNode.selectSingleNode("actions/action[@name='" + actionOneFQName + "' and @alias='ActionOne']");
        Node actionTwoNode = webworkNode.selectSingleNode("actions/action[@name='" + actionTwoFQName + "' and @alias='ActionTwo']");

        assertNotNull("action one node not found",actionOneNode);
        assertNotNull("action two node not found",actionTwoNode);

        List<Node> viewsOne = actionOneNode.selectNodes("view");
        assertEquals("actionOne - wrong number of views",2,viewsOne.size());

        List<Node> viewsTwo = actionTwoNode.selectNodes("view");
        assertEquals("actionTwo - wrong number of views",1,viewsTwo.size());

        Node successNode = actionOneNode.selectSingleNode("view[@name='success' and text()='/templates/subdir/success.vm']");
        assertNotNull("success view not found",successNode);

        File successFile = new File(moduleLocation.getTemplateDirectory(),"subdir/success.vm");
        assertTrue("success file does not exist!",successFile.exists());

        Node errorNode = actionOneNode.selectSingleNode("view[@name='error' and text()='templates/subdir/error.vm']");
        assertNotNull("error view not found",errorNode);

        File errorFile = new File(moduleLocation.getTemplateDirectory(),"subdir/error.vm");
        assertTrue("error file does not exist!",errorFile.exists());

        Node inputNode = actionTwoNode.selectSingleNode("view[@name='input' and text()='templates/input.vm']");
        assertNotNull("input view not found",inputNode);

        File inputFile = new File(moduleLocation.getTemplateDirectory(),"input.vm");
        assertTrue("input file does not exist!",inputFile.exists());
    }

}
