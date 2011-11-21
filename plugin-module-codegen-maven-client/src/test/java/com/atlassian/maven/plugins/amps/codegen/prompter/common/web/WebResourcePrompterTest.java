package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import java.util.Arrays;
import java.util.List;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.common.Condition;
import com.atlassian.plugins.codegen.modules.common.Conditional;
import com.atlassian.plugins.codegen.modules.common.Conditions;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.common.web.WebResourceProperties;
import com.atlassian.plugins.codegen.modules.common.web.WebResourceTransformation;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Before;
import org.junit.Test;

import static com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter.MODULE_DESCRIP_PROMPT;
import static com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter.MODULE_KEY_PROMPT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since 3.6
 */
public class WebResourcePrompterTest extends AbstractPrompterTest
{
    public static final String MODULE_NAME = "My Web Resource";
    public static final String MODULE_KEY = "my-web-resource";
    public static final String DESCRIPTION = "The My Web Resource Plugin";
    public static final String I18N_NAME_KEY = "my-web-resource.name";
    public static final String I18N_DESCRIPTION_KEY = "my-web-resource.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    public static final String RESOURCE_NAME = "resourceCSS";
    public static final String RESOURCE_NAME_PATTERN = "templates/*.vm";
    public static final String DOWNLOAD_TYPE = "download";
    public static final String VELOCITY_TYPE = "velocity";
    public static final String RESOURCE_CSS_PATH = "templates/resource.css";
    public static final String RESOURCE_VM_PATH = "templates/resource.vm";
    public static final String PARAM_KEY = "paramKey";
    public static final String PARAM_VAL = "paramVal";
    public static final String DEPENDENCY = "web.resources:ajs";

    public static final String CUSTOM_CONTEXT = "my.context";
    public static final String TRANS_EXTENSION = "css";
    public static final String TRANS_KEY = "template";

    public static final String CONDITIONS_TYPE = "AND";
    public static final String CUSTOM_CONDITION = "com.atlassian.plugins.web.CustomCondition";

    Prompter prompter;

    @Before
    public void setup()
    {
        prompter = mock(Prompter.class);
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Enter Plugin Module Name", "My Web Resource")).thenReturn(MODULE_NAME);

        when(prompter.prompt("Enter Resource Name (leave blank to use namePattern)")).thenReturn(RESOURCE_NAME)
                .thenReturn("");
        when(prompter.prompt("Enter Resource Name Pattern")).thenReturn(RESOURCE_NAME_PATTERN);
        when(prompter.prompt("Enter Resource Type", "download")).thenReturn(DOWNLOAD_TYPE)
                .thenReturn(VELOCITY_TYPE);
        when(prompter.prompt("Enter Location (path to resource file)")).thenReturn(RESOURCE_CSS_PATH)
                .thenReturn(RESOURCE_VM_PATH);
        when(prompter.prompt("Add Resource Parameter?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");
        when(prompter.prompt("params:\nparamKey->paramVal\nAdd Resource Parameter?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("param name")).thenReturn(PARAM_KEY);
        when(prompter.prompt("param value")).thenReturn(PARAM_VAL);
        when(prompter.prompt("Add Resource", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebResourcePrompter modulePrompter = new WebResourcePrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        WebResourceProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException
    {
        when(prompter.prompt("Enter Plugin Module Name", "My Web Resource")).thenReturn(MODULE_NAME);

        when(prompter.prompt("Enter Resource Name (leave blank to use namePattern)")).thenReturn(RESOURCE_NAME)
                .thenReturn("");
        when(prompter.prompt("Enter Resource Name Pattern")).thenReturn(RESOURCE_NAME_PATTERN);
        when(prompter.prompt("Enter Resource Type", "download")).thenReturn(DOWNLOAD_TYPE)
                .thenReturn(VELOCITY_TYPE);
        when(prompter.prompt("Enter Location (path to resource file)")).thenReturn(RESOURCE_CSS_PATH)
                .thenReturn(RESOURCE_VM_PATH);
        when(prompter.prompt("Add Resource Parameter?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");
        when(prompter.prompt("params:\nparamKey->paramVal\nAdd Resource Parameter?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("param name")).thenReturn(PARAM_KEY);
        when(prompter.prompt("param value")).thenReturn(PARAM_VAL);
        when(prompter.prompt("Add Resource", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt(MODULE_KEY_PROMPT, MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt(MODULE_DESCRIP_PROMPT, DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Add Dependency?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Enter Dependency")).thenReturn(DEPENDENCY);
        when(prompter.prompt("values:\nweb.resources:ajs\nAdd Dependency?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        when(prompter.prompt("Add Web Resource Context?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("Y")
                .thenReturn("N");
        when(prompter.prompt("Choose A Context\n1: atl.general\n2: atl.admin\n3: atl.userprofile\n4: Custom Context\nChoose a number: ", Arrays.asList("1", "2", "3", "4"), "1")).thenReturn("2");
        when(prompter.prompt("Choose A Context\n1: atl.general\n2: atl.userprofile\n3: Custom Context\nChoose a number: ", Arrays.asList("1", "2", "3"), "1")).thenReturn("3");
        when(prompter.prompt("Enter Context")).thenReturn(CUSTOM_CONTEXT);

        when(prompter.prompt("Add Web Resource Transformation?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");
        when(prompter.prompt("File Extension")).thenReturn(TRANS_EXTENSION);
        when(prompter.prompt("Transformer Key")).thenReturn(TRANS_KEY);
        when(prompter.prompt("Add Transformer Key?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        when(prompter.prompt("Add Conditions?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");
        when(prompter.prompt("Condition Type", PluginModulePrompter.ANDOR_ANSWERS, "AND")).thenReturn(CONDITIONS_TYPE);
        when(prompter.prompt("Enter Fully Qualified Condition Class", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".web.condition.MyCondition")).thenReturn(CUSTOM_CONDITION);
        when(prompter.prompt("Add Condition?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Add Condition Parameter?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y")
                .thenReturn("N");
        when(prompter.prompt("params:\nparamKey->paramVal\nAdd Condition Parameter?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Invert Condition?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebResourcePrompter modulePrompter = new WebResourcePrompter(prompter);
        modulePrompter.setUseAnsiColor(false);
        WebResourceProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());

        //resources
        List<Resource> resources = props.getResources();
        assertTrue("resources not found", !resources.isEmpty());
        assertEquals("wrong number of resources", 2, resources.size());

        Resource cssResource = resources.get(0);
        Resource vmResource = resources.get(1);

        assertEquals("wrong css resource name", RESOURCE_NAME, cssResource.getName());
        assertTrue("css name pattern found when name is set", StringUtils.isBlank(cssResource.getNamePattern()));
        assertEquals("wrong css resource type", DOWNLOAD_TYPE, cssResource.getType());
        assertEquals("wrong css resource location", RESOURCE_CSS_PATH, cssResource.getLocation());
        assertEquals("wrong number of css resource params", 1, cssResource.getParams()
                .size());
        assertTrue("css resource param key not found", cssResource.getParams()
                .containsKey(PARAM_KEY));
        assertEquals("wrong css resource param value", PARAM_VAL, cssResource.getParams()
                .get(PARAM_KEY));

        assertTrue("vm name found when name pattern is set", StringUtils.isBlank(vmResource.getName()));
        assertEquals("wrong vm resource name pattern", RESOURCE_NAME_PATTERN, vmResource.getNamePattern());
        assertEquals("wrong vm resource type", VELOCITY_TYPE, vmResource.getType());
        assertEquals("wrong vm resource location", RESOURCE_VM_PATH, vmResource.getLocation());
        assertEquals("wrong number of vm resource params", 0, vmResource.getParams()
                .size());

        //dependencies
        List<String> dependencies = props.getDependencies();
        assertEquals("wrong number of dependencies", 1, dependencies.size());
        assertEquals("wronf dependency value", DEPENDENCY, dependencies.get(0));

        //contexts
        List<String> contexts = props.getContexts();
        assertEquals("wrong number of contexts", 2, contexts.size());
        assertTrue("admin context not found", contexts.contains("atl.admin"));
        assertTrue("custom context not found", contexts.contains(CUSTOM_CONTEXT));

        //transformations
        List<WebResourceTransformation> transformations = props.getTransformations();
        assertEquals("wrong number of transformations", 1, transformations.size());

        WebResourceTransformation transformation = transformations.get(0);
        assertEquals("wrong transformation extension", TRANS_EXTENSION, transformation.getExtension());

        List<String> keys = transformation.getTransformerKeys();
        assertEquals("wrong number of transfromer keys", 1, keys.size());
        assertEquals("worng transformer key", TRANS_KEY, keys.get(0));

        //conditions
        List<Conditional> conditionals = props.getConditions();
        assertEquals("wrong number of conditionals", 1, conditionals.size());

        Conditional conditional = conditionals.get(0);
        assertTrue("first conditional is not Conditions", (conditional instanceof Conditions));

        Conditions conditions = (Conditions) conditional;
        assertEquals("wrong conditions type", CONDITIONS_TYPE, conditions.getType());

        List<Conditional> nestedConditions = conditions.getConditions();
        assertEquals("wrong number of nested conditiomals", 1, nestedConditions.size());

        Conditional nestedConditional = nestedConditions.get(0);
        assertTrue("nested conditional is not a Condition", (nestedConditional instanceof Condition));

        Condition condition = (Condition) nestedConditional;
        assertEquals("wrong number of condition params", 1, condition.getParams()
                .size());
        assertTrue("condition param key not found", condition.getParams()
                .containsKey(PARAM_KEY));
        assertEquals("wrong condition param value", PARAM_VAL, condition.getParams()
                .get(PARAM_KEY));

        assertTrue("condition should be inverted", condition.isInvert());


    }
}
