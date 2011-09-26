package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.atlassian.maven.plugins.amps.codegen.jira.ActionTypeFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.jira.WorkflowPostFunctionProperties;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since 3.5
 */
public class WorkflowPostFunctionPrompterTest extends AbstractPrompterTest
{
    public static final String PACKAGE = "com.atlassian.plugins.jira.workflow";
    public static final String CLASSNAME = "MyPostFunction";
    public static final String FACTORY_CLASSNAME = "MyPostFunctionFactory";
    public static final String MODULE_NAME = "My Post Function";
    public static final String MODULE_KEY = "my-post-function";
    public static final String DESCRIPTION = "The My Post Function Plugin";
    public static final String I18N_NAME_KEY = "my-post-function.name";
    public static final String I18N_DESCRIPTION_KEY = "my-post-function.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    Prompter prompter;
    TestingActionTypeFactory actionTypeFactory;

    @Before
    public void setup()
    {
        prompter = mock(Prompter.class);
        actionTypeFactory = new TestingActionTypeFactory();
        actionTypeFactory.setActionTypes(Arrays.asList("common", "initial", "global", "ordinary"));
    }

    @After
    public void resetActionTypes()
    {
        actionTypeFactory.setActionTypes(Collections.<String>emptyList());
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Enter New Classname", "MyPostFunction")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.workflow")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WorkflowPostFunctionPrompter modulePrompter = new WorkflowPostFunctionPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        WorkflowPostFunctionProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong function class", CLASSNAME, props.getClassname());
        assertEquals("wrong factory class", FACTORY_CLASSNAME, props.getFactoryName());
        assertEquals("wrong class package", PACKAGE, props.getPackage());
        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertTrue("orderable should be blank", StringUtils.isBlank(props.getOrderable()));
        assertTrue("unique should be blank", StringUtils.isBlank(props.getUnique()));
        assertTrue("deletable should be blank", StringUtils.isBlank(props.getDeletable()));
        assertTrue("addable should be blank", StringUtils.isBlank(props.getAddable()));
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Enter New Classname", "MyPostFunction")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.workflow")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Plugin Name", MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Is Function Orderable?", PluginModulePrompter.YN_ANSWERS, "Y")).thenReturn("y");
        when(prompter.prompt("Is Function Unique?", PluginModulePrompter.YN_ANSWERS, "Y")).thenReturn("n");
        when(prompter.prompt("Is Function Deletable?", PluginModulePrompter.YN_ANSWERS, "Y")).thenReturn("y");

        when(prompter.prompt("Define Action Types? (addable)", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("y");
        when(prompter.prompt("Choose A Type\n1: common\n2: global\n3: initial\n4: ordinary\nChoose a number: ", Arrays.asList("1", "2", "3", "4"), "1")).thenReturn("2");
        when(prompter.prompt("Add Action Type?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("y")
                .thenReturn("n");
        when(prompter.prompt("Choose A Type\n1: common\n2: initial\n3: ordinary\nChoose a number: ", Arrays.asList("1", "2", "3"), "1")).thenReturn("1");

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WorkflowPostFunctionPrompter modulePrompter = new WorkflowPostFunctionPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        WorkflowPostFunctionProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong function class", CLASSNAME, props.getClassname());
        assertEquals("wrong factory class", FACTORY_CLASSNAME, props.getFactoryName());
        assertEquals("wrong adv package", PACKAGE, props.getPackage());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong orderable", "true", props.getOrderable());
        assertEquals("wrong unique", "false", props.getUnique());
        assertEquals("wrong deletable", "true", props.getDeletable());
        assertEquals("wrong addable", "global,common", props.getAddable());
    }

    protected class TestingActionTypeFactory extends ActionTypeFactory
    {
        public void setActionTypes(List<String> types)
        {
            availableActionTypes = types;
        }
    }
}
