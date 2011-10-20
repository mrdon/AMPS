package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.jira.WorkflowElementProperties;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since 3.6
 */
public class WorkflowValidatorPrompterTest extends AbstractPrompterTest
{
    public static final String PACKAGE = "com.atlassian.plugins.jira.workflow";
    public static final String CLASSNAME = "MyWorkflowValidator";
    public static final String FACTORY_CLASSNAME = "MyWorkflowValidatorFactory";
    public static final String MODULE_NAME = "My Workflow Validator";
    public static final String MODULE_KEY = "my-workflow-validator";
    public static final String DESCRIPTION = "The My Workflow Validator Plugin";
    public static final String I18N_NAME_KEY = "my-workflow-validator.name";
    public static final String I18N_DESCRIPTION_KEY = "my-workflow-validator.description";

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
    public void basicPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Enter New Classname", "MyWorkflowValidator")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.workflow")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WorkflowValidatorPrompter modulePrompter = new WorkflowValidatorPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        WorkflowElementProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong class", CLASSNAME, props.getClassname());
        assertEquals("wrong factory class", FACTORY_CLASSNAME, props.getFactoryName());
        assertEquals("wrong class package", PACKAGE, props.getPackage());
        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Enter New Classname", "MyWorkflowValidator")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.workflow")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Plugin Name", MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WorkflowValidatorPrompter modulePrompter = new WorkflowValidatorPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        WorkflowElementProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv class", CLASSNAME, props.getClassname());
        assertEquals("wrong factory class", FACTORY_CLASSNAME, props.getFactoryName());
        assertEquals("wrong adv package", PACKAGE, props.getPackage());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
    }
}
