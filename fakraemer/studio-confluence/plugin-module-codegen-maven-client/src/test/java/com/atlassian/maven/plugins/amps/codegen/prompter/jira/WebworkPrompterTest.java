package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.jira.ActionProperties;
import com.atlassian.plugins.codegen.modules.jira.View;
import com.atlassian.plugins.codegen.modules.jira.WebworkProperties;

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
public class WebworkPrompterTest extends AbstractPrompterTest
{
    public static final String MODULE_NAME = "My Webwork";
    public static final String MODULE_KEY = "my-webwork";
    public static final String DESCRIPTION = "The My Webwork Plugin";
    public static final String I18N_NAME_KEY = "my-webwork.name";
    public static final String I18N_DESCRIPTION_KEY = "my-webwork.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    public static final String DEFAULT_ACTION_PACKAGE = "com.example.jira.webwork";
    public static final String DEFAULT_ACTION_NAME = "MyWebworkAction";
    public static final String DEFAULT_ACTION_ALIAS = "MyWebwork";
    public static final String DEFAULT_TEMPLATE_PATH = "/templates/" + MODULE_KEY + "/";

    public static final String CUSTOM_ACTION_PACKAGE = "my.custom.actions";
    public static final String CUSTOM_ACTION_ONE_NAME = "CustomActionOne";
    public static final String CUSTOM_ACTION_ONE_ALIAS = "ActionOne";

    public static final String CUSTOM_ACTION_TWO_NAME = "CustomActionTwo";
    public static final String CUSTOM_ACTION_TWO_ALIAS = "ActionTwo";

    public static final String SUCCESS_VIEW_NAME = "success";
    public static final String ACTION_ONE_VIEW_PATH_PREFIX = "/templates/actionone/";
    public static final String SUCCESS_VIEW_PATH = ACTION_ONE_VIEW_PATH_PREFIX + "success.vm";

    public static final String ERROR_VIEW_NAME = "error";
    public static final String ERROR_VIEW_PATH = ACTION_ONE_VIEW_PATH_PREFIX + "error.vm";

    public static final String CUSTOM_VIEW_NAME = "custom";
    public static final String CUSTOM_VIEW_PATH = "/templates/actiontwo/custom.vm";

    Prompter prompter;


    @Before
    public void setup()
    {
        prompter = mock(Prompter.class);
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Enter Plugin Module Name", "My Webwork Module")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebworkPrompter modulePrompter = new WebworkPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        WebworkProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong number of actions", 1, props.getActions()
                .size());

        ActionProperties action = props.getActions()
                .get(0);
        assertEquals("wrong action package", DEFAULT_ACTION_PACKAGE, action.getPackage());
        assertEquals("wrong action classname", DEFAULT_ACTION_NAME, action.getClassname());
        assertEquals("wrong action alias", DEFAULT_ACTION_ALIAS, action.getAlias());

        assertEquals("wrong number of views", 3, action.getViews()
                .size());
        View success = action.getViews()
                .get(0);
        View input = action.getViews()
                .get(1);
        View error = action.getViews()
                .get(2);

        assertEquals("wrong view name", SUCCESS_VIEW_NAME, success.getName());
        assertEquals("wrong view path", DEFAULT_TEMPLATE_PATH + "success.vm", success.getPath());

        assertEquals("wrong view name", "input", input.getName());
        assertEquals("wrong view path", DEFAULT_TEMPLATE_PATH + "input.vm", input.getPath());

        assertEquals("wrong view name", ERROR_VIEW_NAME, error.getName());
        assertEquals("wrong view path", DEFAULT_TEMPLATE_PATH + "error.vm", error.getPath());
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Enter Plugin Module Name", "My Webwork Module")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Enter Action Classname", "MyActionClass")).thenReturn(CUSTOM_ACTION_ONE_NAME)
                .thenReturn(CUSTOM_ACTION_TWO_NAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.webwork")).thenReturn(CUSTOM_ACTION_PACKAGE);
        when(prompter.prompt("Enter Package Name", CUSTOM_ACTION_PACKAGE)).thenReturn(CUSTOM_ACTION_PACKAGE);
        when(prompter.prompt("Enter Alias", CUSTOM_ACTION_ONE_NAME)).thenReturn(CUSTOM_ACTION_ONE_ALIAS);
        when(prompter.prompt("Enter Alias", CUSTOM_ACTION_TWO_NAME)).thenReturn(CUSTOM_ACTION_TWO_ALIAS);

        when(prompter.prompt("Enter View Name", "success")).thenReturn(SUCCESS_VIEW_NAME)
                .thenReturn(ERROR_VIEW_NAME)
                .thenReturn(CUSTOM_VIEW_NAME);

        String prompterViewPathOne = "/templates/" + ADV_MODULE_KEY + "/" + CUSTOM_ACTION_ONE_NAME.toLowerCase() + "/";
        String prompterViewPathTwo = "/templates/" + ADV_MODULE_KEY + "/" + CUSTOM_ACTION_TWO_NAME.toLowerCase() + "/";

        when(prompter.prompt("Enter Template Path", prompterViewPathOne + "success.vm")).thenReturn(SUCCESS_VIEW_PATH);
        when(prompter.prompt("Enter Template Path", ACTION_ONE_VIEW_PATH_PREFIX + "error.vm")).thenReturn(ERROR_VIEW_PATH);
        when(prompter.prompt("Enter Template Path", prompterViewPathTwo + "custom.vm")).thenReturn(CUSTOM_VIEW_PATH);

        when(prompter.prompt("Add Another Action?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");
        when(prompter.prompt("Add Another View?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebworkPrompter modulePrompter = new WebworkPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        WebworkProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());

        assertEquals("wrong number of actions", 2, props.getActions()
                .size());

        ActionProperties actionOne = props.getActions()
                .get(0);
        ActionProperties actionTwo = props.getActions()
                .get(1);

        assertEquals("wrong action package", CUSTOM_ACTION_PACKAGE, actionOne.getPackage());
        assertEquals("wrong action classname", CUSTOM_ACTION_ONE_NAME, actionOne.getClassname());
        assertEquals("wrong action alias", CUSTOM_ACTION_ONE_ALIAS, actionOne.getAlias());

        assertEquals("wrong action package", CUSTOM_ACTION_PACKAGE, actionOne.getPackage());
        assertEquals("wrong action classname", CUSTOM_ACTION_TWO_NAME, actionTwo.getClassname());
        assertEquals("wrong action alias", CUSTOM_ACTION_TWO_ALIAS, actionTwo.getAlias());

        assertEquals("wrong number of action one views", 2, actionOne.getViews()
                .size());
        View success = actionOne.getViews()
                .get(0);
        View error = actionOne.getViews()
                .get(1);

        assertEquals("wrong view name", SUCCESS_VIEW_NAME, success.getName());
        assertEquals("wrong view path", SUCCESS_VIEW_PATH, success.getPath());

        assertEquals("wrong view name", ERROR_VIEW_NAME, error.getName());
        assertEquals("wrong view path", ERROR_VIEW_PATH, error.getPath());

        assertEquals("wrong number of action one views", 1, actionTwo.getViews()
                .size());
        View custom = actionTwo.getViews()
                .get(0);

        assertEquals("wrong view name", CUSTOM_VIEW_NAME, custom.getName());
        assertEquals("wrong view path", CUSTOM_VIEW_PATH, custom.getPath());
    }
}
