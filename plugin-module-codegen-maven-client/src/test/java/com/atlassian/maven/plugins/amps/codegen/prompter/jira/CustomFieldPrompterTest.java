package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atlassian.core.util.map.EasyMap;
import com.atlassian.maven.plugins.amps.codegen.jira.CustomFieldTypeFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.jira.CustomFieldProperties;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since 3.5
 */
public class CustomFieldPrompterTest extends AbstractPrompterTest
{
    public static final String PACKAGE = "com.atlassian.plugins.jira.customfields";
    public static final String CLASSNAME = "MyCustomField";
    public static final String MODULE_NAME = "My Custom Field";
    public static final String MODULE_KEY = "my-custom-field";
    public static final String DESCRIPTION = "The My Custom Field Plugin";
    public static final String I18N_NAME_KEY = "my-custom-field.name";
    public static final String I18N_DESCRIPTION_KEY = "my-custom-field.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    public static final String RESOURCE_NAME = "view";
    public static final String RESOURCE_VM_PATH = "templates/resource.vm";

    Prompter prompter;
    TestingCustomFieldTypeFactory fieldTypeFactory;

    @Before
    public void setup()
    {
        prompter = mock(Prompter.class);
        fieldTypeFactory = new TestingCustomFieldTypeFactory();
        fieldTypeFactory.setFieldTypes(EasyMap.build("AbstractCustomField", "com.example.AbstractCustomField", "AnotherCustomField", "com.example.AnotherCustomField"));
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Enter New Classname", "MyCustomField")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.customfields")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        CustomFieldPrompter modulePrompter = new CustomFieldPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        CustomFieldProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong class", CLASSNAME, props.getClassname());
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
        when(prompter.prompt("Enter New Classname", "MyCustomField")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".jira.customfields")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Plugin Name", MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Choose A Custom Field Type To Extend\n1: AbstractCustomField\n2: AnotherCustomField\nChoose a number: ", Arrays.asList("1", "2"), "")).thenReturn("1");

        when(prompter.prompt("Add Resource", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");
        when(prompter.prompt("Enter Resource Name")).thenReturn(RESOURCE_NAME)
                .thenReturn("");
        when(prompter.prompt("Enter Location (path to resource file)")).thenReturn(RESOURCE_VM_PATH);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        CustomFieldPrompter modulePrompter = new CustomFieldPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        CustomFieldProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

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
        assertEquals("wrong number of resources", 1, resources.size());

        Resource viewResource = resources.get(0);

        assertEquals("wrong css resource name", RESOURCE_NAME, viewResource.getName());
        assertTrue("name pattern found when name is set", StringUtils.isBlank(viewResource.getNamePattern()));
        assertEquals("wrong resource type", "velocity", viewResource.getType());
        assertEquals("wrong resource location", RESOURCE_VM_PATH, viewResource.getLocation());

        assertEquals("wrong super class", "com.example.AbstractCustomField", props.getFullyQualifiedClassToExtend());
    }

    protected class TestingCustomFieldTypeFactory extends CustomFieldTypeFactory
    {
        public void setFieldTypes(Map<String, String> types)
        {
            fields = types;
        }
    }
}
