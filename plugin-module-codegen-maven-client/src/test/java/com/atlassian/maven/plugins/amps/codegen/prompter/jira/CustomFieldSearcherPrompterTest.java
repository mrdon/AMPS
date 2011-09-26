package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atlassian.core.util.map.EasyMap;
import com.atlassian.maven.plugins.amps.codegen.jira.CustomFieldSearcherFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.jira.CustomFieldSearcherProperties;

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
public class CustomFieldSearcherPrompterTest extends AbstractPrompterTest
{
    public static final String PACKAGE = "com.atlassian.plugins.jira.customfields";
    public static final String CLASSNAME = "MyCustomFieldSearcher";
    public static final String MODULE_NAME = "My Custom Field Searcher";
    public static final String MODULE_KEY = "my-custom-field-searcher";
    public static final String DESCRIPTION = "The My Custom Field Searcher Plugin";
    public static final String I18N_NAME_KEY = "my-custom-field-searcher.name";
    public static final String I18N_DESCRIPTION_KEY = "my-custom-field-searcher.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    public static final String RESOURCE_NAME = "view";
    public static final String RESOURCE_VM_PATH = "templates/resource.vm";

    Prompter prompter;
    TestingCustomFieldSearcherFactory searcherFactory;

    @Before
    public void setup()
    {
        prompter = mock(Prompter.class);
        searcherFactory = new TestingCustomFieldSearcherFactory();
        searcherFactory.setSearchers(EasyMap.build("TextSearcher", "com.atlassian.jira.issue.customfields.searchers.TextSearcher", "NumberSearcher", "com.atlassian.jira.issue.customfields.searchers.NumberSearcher"));
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException
    {

        when(prompter.prompt("Choose A Searcher Class\n1: NumberSearcher\n2: TextSearcher\n3: Custom Searcher Class\nChoose a number: ", Arrays.asList("1", "2", "3"), "")).thenReturn("2");
        when(prompter.prompt("Enter Valid CustomField Package", AbstractModulePrompter.DEFAULT_BASE_PACKAGE)).thenReturn(AbstractModulePrompter.DEFAULT_BASE_PACKAGE);
        when(prompter.prompt("Enter Valid CustomField Key")).thenReturn("cf-key");

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        CustomFieldSearcherPrompter modulePrompter = new CustomFieldSearcherPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        CustomFieldSearcherProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong class", "TextSearcher", props.getClassname());
        assertEquals("wrong class package", "com.atlassian.jira.issue.customfields.searchers", props.getPackage());
        assertEquals("wrong module name", "Text Searcher", props.getModuleName());
        assertEquals("wrong module key", "text-searcher", props.getModuleKey());
        assertEquals("wrong i18n name key", "text-searcher.name", props.getNameI18nKey());
        assertEquals("wrong i18n desc key", "text-searcher.description", props.getDescriptionI18nKey());
        assertEquals("wrong customfield package", AbstractModulePrompter.DEFAULT_BASE_PACKAGE, props.getValidCustomFieldPackage());
        assertEquals("wrong customfield key", "cf-key", props.getValidCustomFieldKey());
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Choose A Searcher Class\n1: NumberSearcher\n2: TextSearcher\n3: Custom Searcher Class\nChoose a number: ", Arrays.asList("1", "2", "3"), "")).thenReturn("2");
        when(prompter.prompt("Enter Valid CustomField Package", AbstractModulePrompter.DEFAULT_BASE_PACKAGE)).thenReturn(AbstractModulePrompter.DEFAULT_BASE_PACKAGE);
        when(prompter.prompt("Enter Valid CustomField Key")).thenReturn("cf-key");
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Plugin Name", "Text Searcher")).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Plugin Key", "text-searcher")).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", "The Text Searcher Plugin")).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", "text-searcher.name")).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", "text-searcher.description")).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Add Resource", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");
        when(prompter.prompt("Enter Resource Name")).thenReturn(RESOURCE_NAME)
                .thenReturn("");
        when(prompter.prompt("Enter Location (path to resource file)")).thenReturn(RESOURCE_VM_PATH);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        CustomFieldSearcherPrompter modulePrompter = new CustomFieldSearcherPrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        CustomFieldSearcherProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong class", "TextSearcher", props.getClassname());
        assertEquals("wrong class package", "com.atlassian.jira.issue.customfields.searchers", props.getPackage());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong customfield package", AbstractModulePrompter.DEFAULT_BASE_PACKAGE, props.getValidCustomFieldPackage());
        assertEquals("wrong customfield key", "cf-key", props.getValidCustomFieldKey());
        //resources
        List<Resource> resources = props.getResources();
        assertTrue("resources not found", !resources.isEmpty());
        assertEquals("wrong number of resources", 1, resources.size());

        Resource viewResource = resources.get(0);

        assertEquals("wrong css resource name", RESOURCE_NAME, viewResource.getName());
        assertTrue("name pattern found when name is set", StringUtils.isBlank(viewResource.getNamePattern()));
        assertEquals("wrong resource type", "velocity", viewResource.getType());
        assertEquals("wrong resource location", RESOURCE_VM_PATH, viewResource.getLocation());

    }

    protected class TestingCustomFieldSearcherFactory extends CustomFieldSearcherFactory
    {
        public void setSearchers(Map<String, String> searcherMap)
        {
            searchers = searcherMap;
        }
    }
}
