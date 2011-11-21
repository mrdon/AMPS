package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.jira.RPCProperties;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Before;
import org.junit.Test;

import static com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter.MODULE_DESCRIP_PROMPT;
import static com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter.MODULE_KEY_PROMPT;
import static com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter.MODULE_NAME_PROMPT;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since 3.6
 */
public class RPCPrompterTest extends AbstractPrompterTest
{
    public static final String PACKAGE = "com.atlassian.plugins.jira.rpc";
    public static final String SOAP_CLASSNAME = "MySoapEndpointImpl";
    public static final String SOAP_INTERFACE = "MySoapEndpoint";
    public static final String SOAP_MODULE_NAME = "My Soap Endpoint";
    public static final String SOAP_MODULE_KEY = "my-soap-endpoint";
    public static final String SOAP_DESCRIPTION = "The My Soap Endpoint Plugin";
    public static final String SOAP_I18N_NAME_KEY = "my-soap-endpoint.name";
    public static final String SOAP_I18N_DESCRIPTION_KEY = "my-soap-endpoint.description";
    public static final String SOAP_PATH = "mysoapservice-v1";

    public static final String XML_CLASSNAME = "MyXmlEndpointImpl";
    public static final String XML_INTERFACE = "MyXmlEndpoint";
    public static final String XML_MODULE_NAME = "My Xml Endpoint";
    public static final String XML_MODULE_KEY = "my-xml-endpoint";
    public static final String XML_DESCRIPTION = "The My Xml Endpoint Plugin";
    public static final String XML_I18N_NAME_KEY = "my-xml-endpoint.name";
    public static final String XML_I18N_DESCRIPTION_KEY = "my-xml-endpoint.description";
    public static final String XML_PATH = "myxmlservice-v1";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    Prompter prompter;

    @Before
    public void setup()
    {
        prompter = mock(Prompter.class);
    }

    @Test
    public void basicSoapPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("[S]OAP or [X]ML-RPC?", RPCPrompter.RPC_ANSWERS, "S")).thenReturn("S");
        when(prompter.prompt("Enter Interface name", "MYSoapEndpoint")).thenReturn(SOAP_INTERFACE);
        when(prompter.prompt("Enter Interface package", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.rpc")).thenReturn(PACKAGE);
        when(prompter.prompt("Enter Class name", "MySoapEndpointImpl")).thenReturn(SOAP_CLASSNAME);
        when(prompter.prompt("Enter Package Name", PACKAGE)).thenReturn(PACKAGE);
        when(prompter.prompt("Enter Service Path", "mysoapendpoint-v1")).thenReturn(SOAP_PATH);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        RPCPrompter modulePrompter = new RPCPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        RPCProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong interface", SOAP_INTERFACE, props.getInterfaceClass());
        assertEquals("wrong interface package", PACKAGE, props.getInterfacePackage());
        assertEquals("wrong class", SOAP_CLASSNAME, props.getClassname());
        assertEquals("wrong class package", PACKAGE, props.getPackage());
        assertEquals("wrong service path", SOAP_PATH, props.getServicePath());
        assertEquals("wrong module name", SOAP_MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", SOAP_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", SOAP_DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", SOAP_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", SOAP_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertTrue("isSoap should be true", props.isSoap());
    }

    @Test
    public void advancedSoapPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("[S]OAP or [X]ML-RPC?", RPCPrompter.RPC_ANSWERS, "S")).thenReturn("S");
        when(prompter.prompt("Enter Interface name", "MYSoapEndpoint")).thenReturn(SOAP_INTERFACE);
        when(prompter.prompt("Enter Interface package", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.rpc")).thenReturn(PACKAGE);
        when(prompter.prompt("Enter Class name", "MySoapEndpointImpl")).thenReturn(SOAP_CLASSNAME);
        when(prompter.prompt("Enter Package Name", PACKAGE)).thenReturn(PACKAGE);
        when(prompter.prompt("Enter Service Path", "mysoapendpoint-v1")).thenReturn(SOAP_PATH);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt(MODULE_NAME_PROMPT, SOAP_MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt(MODULE_KEY_PROMPT, SOAP_MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt(MODULE_DESCRIP_PROMPT, SOAP_DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", SOAP_I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", SOAP_I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        RPCPrompter modulePrompter = new RPCPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        RPCProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv interface", SOAP_INTERFACE, props.getInterfaceClass());
        assertEquals("wrong adv interface package", PACKAGE, props.getInterfacePackage());
        assertEquals("wrong adv class", SOAP_CLASSNAME, props.getClassname());
        assertEquals("wrong adv package", PACKAGE, props.getPackage());
        assertEquals("wrong adv service path", SOAP_PATH, props.getServicePath());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertTrue("isSoap should be true", props.isSoap());
    }

    @Test
    public void basicXmlPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("[S]OAP or [X]ML-RPC?", RPCPrompter.RPC_ANSWERS, "S")).thenReturn("x");
        when(prompter.prompt("Enter Interface name", "MYXmlEndpoint")).thenReturn(XML_INTERFACE);
        when(prompter.prompt("Enter Interface package", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.rpc")).thenReturn(PACKAGE);
        when(prompter.prompt("Enter Class name", "MyXmlEndpointImpl")).thenReturn(XML_CLASSNAME);
        when(prompter.prompt("Enter Package Name", PACKAGE)).thenReturn(PACKAGE);
        when(prompter.prompt("Enter Service Path", "myxmlendpoint-v1")).thenReturn(XML_PATH);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        RPCPrompter modulePrompter = new RPCPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        RPCProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong interface", XML_INTERFACE, props.getInterfaceClass());
        assertEquals("wrong interface package", PACKAGE, props.getInterfacePackage());
        assertEquals("wrong class", XML_CLASSNAME, props.getClassname());
        assertEquals("wrong class package", PACKAGE, props.getPackage());
        assertEquals("wrong service path", XML_PATH, props.getServicePath());
        assertEquals("wrong module name", XML_MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", XML_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", XML_DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", XML_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", XML_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertFalse("isSoap should be false", props.isSoap());
    }

    @Test
    public void advancedXmlPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("[S]OAP or [X]ML-RPC?", RPCPrompter.RPC_ANSWERS, "S")).thenReturn("X");
        when(prompter.prompt("Enter Interface name", "MYXmlEndpoint")).thenReturn(XML_INTERFACE);
        when(prompter.prompt("Enter Interface package", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.rpc")).thenReturn(PACKAGE);
        when(prompter.prompt("Enter Class name", "MyXmlEndpointImpl")).thenReturn(XML_CLASSNAME);
        when(prompter.prompt("Enter Package Name", PACKAGE)).thenReturn(PACKAGE);
        when(prompter.prompt("Enter Service Path", "myxmlendpoint-v1")).thenReturn(XML_PATH);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt(MODULE_NAME_PROMPT, XML_MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt(MODULE_KEY_PROMPT, XML_MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt(MODULE_DESCRIP_PROMPT, XML_DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", XML_I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", XML_I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        RPCPrompter modulePrompter = new RPCPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        RPCProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv interface", XML_INTERFACE, props.getInterfaceClass());
        assertEquals("wrong adv interface package", PACKAGE, props.getInterfacePackage());
        assertEquals("wrong adv class", XML_CLASSNAME, props.getClassname());
        assertEquals("wrong adv package", PACKAGE, props.getPackage());
        assertEquals("wrong adv service path", XML_PATH, props.getServicePath());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertFalse("isSoap should be false", props.isSoap());
    }
}
