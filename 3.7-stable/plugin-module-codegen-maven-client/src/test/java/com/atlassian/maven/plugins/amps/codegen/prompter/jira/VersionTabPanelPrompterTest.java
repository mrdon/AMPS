package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.jira.TabPanelProperties;
import com.atlassian.plugins.codegen.modules.jira.VersionTabPanelModuleCreator;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since 3.6
 */
public class VersionTabPanelPrompterTest extends AbstractPrompterTest
{
    public static final String PACKAGE = "com.atlassian.plugins.jira.tabpanels";
    public static final String CLASSNAME = "MyVersionTabPanel";
    public static final String MODULE_NAME = "My Version Tab Panel";
    public static final String MODULE_KEY = "my-version-tab-panel";
    public static final String DESCRIPTION = "The My Version Tab Panel Plugin";
    public static final String I18N_NAME_KEY = "my-version-tab-panel.name";
    public static final String I18N_DESCRIPTION_KEY = "my-version-tab-panel.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    public static final String ORDER = "22";
    public static final String LABEL_KEY = "item.label";
    public static final String LABEL_VALUE = "this is my label";

    Prompter prompter;

    @Before
    public void setup()
    {
        prompter = mock(Prompter.class);
    }

    @Test
    public void basicCustomPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Use " + VersionTabPanelModuleCreator.GENERIC_CLASS + "?", PluginModulePrompter.YN_ANSWERS, "Y")).thenReturn("n");
        when(prompter.prompt("Enter New Classname", "MyVersionTabPanel")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.tabpanels")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        VersionTabPanelPrompter modulePrompter = new VersionTabPanelPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        TabPanelProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong class", CLASSNAME, props.getClassname());
        assertEquals("wrong class package", PACKAGE, props.getPackage());
        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong order", "10", props.getOrder());
        assertEquals("wrong label key", MODULE_KEY + ".label", props.getLabel()
                .getKey());
        assertEquals("wrong label value", MODULE_NAME, props.getLabel()
                .getValue());
        assertTrue("use custom class should be true", props.isUseCustomClass());
    }

    @Test
    public void basicGenericPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Use " + VersionTabPanelModuleCreator.GENERIC_CLASS + "?", PluginModulePrompter.YN_ANSWERS, "Y")).thenReturn("y");
        when(prompter.prompt("Enter Plugin Module Name", "My Version Tab Panel")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        VersionTabPanelPrompter modulePrompter = new VersionTabPanelPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        TabPanelProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong class", VersionTabPanelModuleCreator.GENERIC_CLASS, props.getClassname());
        assertEquals("wrong class package", VersionTabPanelModuleCreator.GENERIC_PACKAGE, props.getPackage());
        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong order", "10", props.getOrder());
        assertEquals("wrong label key", MODULE_KEY + ".label", props.getLabel()
                .getKey());
        assertEquals("wrong label value", MODULE_NAME, props.getLabel()
                .getValue());
        assertFalse("use custom class should be false", props.isUseCustomClass());
    }

    @Test
    public void advancedCustomPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Use " + VersionTabPanelModuleCreator.GENERIC_CLASS + "?", PluginModulePrompter.YN_ANSWERS, "Y")).thenReturn("n");
        when(prompter.prompt("Enter New Classname", "MyVersionTabPanel")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.tabpanels")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("y");

        when(prompter.prompt("Plugin Name", MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Order", "10")).thenReturn(ORDER);
        when(prompter.prompt("Enter Label Key", MODULE_KEY + ".label")).thenReturn(LABEL_KEY);
        when(prompter.prompt("Enter Label Value", MODULE_NAME)).thenReturn(LABEL_VALUE);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        VersionTabPanelPrompter modulePrompter = new VersionTabPanelPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        TabPanelProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv class", CLASSNAME, props.getClassname());
        assertEquals("wrong adv package", PACKAGE, props.getPackage());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong order", ORDER, props.getOrder());
        assertEquals("wrong label key", LABEL_KEY, props.getLabel()
                .getKey());
        assertEquals("wrong label value", LABEL_VALUE, props.getLabel()
                .getValue());
        assertTrue("use custom class should be true", props.isUseCustomClass());
    }

    @Test
    public void advancedGenericPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Use " + VersionTabPanelModuleCreator.GENERIC_CLASS + "?", PluginModulePrompter.YN_ANSWERS, "Y")).thenReturn("y");
        when(prompter.prompt("Enter Plugin Module Name", "My Version Tab Panel")).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("y");

        when(prompter.prompt("Plugin Key", "my-awesome-plugin")).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", "The My Awesome Plugin Plugin")).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", "my-awesome-plugin.name")).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", "my-awesome-plugin.description")).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Order", "10")).thenReturn(ORDER);
        when(prompter.prompt("Enter Label Key", "my-awesome-plugin.label")).thenReturn(LABEL_KEY);
        when(prompter.prompt("Enter Label Value", ADV_MODULE_NAME)).thenReturn(LABEL_VALUE);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        VersionTabPanelPrompter modulePrompter = new VersionTabPanelPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        TabPanelProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv class", VersionTabPanelModuleCreator.GENERIC_CLASS, props.getClassname());
        assertEquals("wrong adv class package", VersionTabPanelModuleCreator.GENERIC_PACKAGE, props.getPackage());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong order", ORDER, props.getOrder());
        assertEquals("wrong label key", LABEL_KEY, props.getLabel()
                .getKey());
        assertEquals("wrong label value", LABEL_VALUE, props.getLabel()
                .getValue());
        assertFalse("use custom class should be false", props.isUseCustomClass());
    }
}
