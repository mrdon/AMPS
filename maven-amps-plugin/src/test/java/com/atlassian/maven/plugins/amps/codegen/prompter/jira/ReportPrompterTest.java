package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.jira.ReportProperties;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since 3.5
 */
public class ReportPrompterTest extends AbstractPrompterTest {
    public static final String PACKAGE = "com.atlassian.plugins.jira.reports";
    public static final String CLASSNAME = "MyReport";
    public static final String MODULE_NAME = "My Report";
    public static final String MODULE_KEY = "my-report";
    public static final String DESCRIPTION = "The My Report Plugin";
    public static final String I18N_NAME_KEY = "my-report.name";
    public static final String I18N_DESCRIPTION_KEY = "my-report.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";
    public static final String RESOURCE_NAME = "view";
    public static final String RESOURCE_VM_PATH = "templates/resource.vm";
    public static final String LABEL_KEY = "item.label";
    public static final String LABEL_VALUE = "this is my label";
    public static final String LABEL_PARAM = "label param";

    Prompter prompter;

    @Before
    public void setup() {
        prompter = mock(Prompter.class);
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter New Classname", "MyReport")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.reports")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        ReportPrompter modulePrompter = new ReportPrompter(prompter);
        ReportProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong class", CLASSNAME, props.getClassname());
        assertEquals("wrong class package", PACKAGE, props.getPackage());
        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter New Classname", "MyReport")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.reports")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Plugin Name", MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Add Resource", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("Enter Resource Name")).thenReturn(RESOURCE_NAME).thenReturn("");
        when(prompter.prompt("Enter Location (path to resource file)")).thenReturn(RESOURCE_VM_PATH);

        when(prompter.prompt("Enter Label Key", "my-report.label")).thenReturn(LABEL_KEY);
        when(prompter.prompt("Enter Label Value", "My Report")).thenReturn(LABEL_VALUE);
        when(prompter.prompt("Add Label Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("values:\nlabel param\nAdd Label Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Enter Param Value")).thenReturn(LABEL_PARAM);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        ReportPrompter modulePrompter = new ReportPrompter(prompter);
        ReportProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv class", CLASSNAME, props.getClassname());
        assertEquals("wrong adv package", PACKAGE, props.getPackage());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());

        //resources
        List<Resource> resources = props.getResources();
        assertTrue("resources not found", !resources.isEmpty());
        assertEquals("wrong number of resources",2,resources.size());

        Resource viewResource = resources.get(0);

        assertEquals("wrong css resource name", RESOURCE_NAME, viewResource.getName());
        assertTrue("name pattern found when name is set", StringUtils.isBlank(viewResource.getNamePattern()));
        assertEquals("wrong resource type", "velocity", viewResource.getType());
        assertEquals("wrong resource location",RESOURCE_VM_PATH,viewResource.getLocation());

        Resource i18n = resources.get(1);

        assertEquals("wrong i18n resource name", "i18n", i18n.getName());
        assertTrue("name pattern found when name is set", StringUtils.isBlank(i18n.getNamePattern()));
        assertEquals("wrong resource type", "i18n", i18n.getType());
        assertEquals("wrong resource location",CLASSNAME,i18n.getLocation());

        assertEquals("wrong label key", LABEL_KEY, props.getLabel().getKey());
        assertEquals("wrong label value", LABEL_VALUE, props.getLabel().getValue());
    }
}
