package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.ConditionFactory;
import com.atlassian.maven.plugins.amps.codegen.ContextProviderFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.common.Condition;
import com.atlassian.plugins.codegen.modules.common.Conditional;
import com.atlassian.plugins.codegen.modules.common.Conditions;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.common.web.AbstractWebFragmentProperties;
import com.atlassian.plugins.codegen.modules.common.web.WebItemProperties;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.After;
import org.junit.Before;

import java.util.List;
import java.util.SortedMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @since version
 */
public abstract class AbstractWebFragmentPrompterTest<T extends AbstractWebFragmentProperties> extends AbstractPrompterTest {

    public static final String RESOURCE_NAME = "resourceCSS";
    public static final String RESOURCE_NAME_PATTERN = "templates/*.vm";
    public static final String DOWNLOAD_TYPE = "download";
    public static final String VELOCITY_TYPE = "velocity";
    public static final String RESOURCE_CSS_PATH = "templates/resource.css";
    public static final String RESOURCE_VM_PATH = "templates/resource.vm";
    public static final String PARAM_KEY = "paramKey";
    public static final String PARAM_VAL = "paramVal";
    public static final String CUSTOM_CONTEXT_PROVIDER = "com.atlassian.plugins.web.CustomContextProvider";
    public static final String CONDITIONS_TYPE = "AND";
    public static final String CUSTOM_CONDITION = "com.atlassian.plugins.web.CustomCondition";

    protected TestingContextProviderFactory contextProviderFactory;
    protected TestingConditionFactory conditionFactory;
    protected T props;

    public void setProps(T props) {
        this.props = props;
    }

    @Before
    public void setupProviders() {
        contextProviderFactory = new TestingContextProviderFactory();
        conditionFactory = new TestingConditionFactory();
    }

    @After
    public void resetProviders() {
        contextProviderFactory.setProvidersMap(MapUtils.EMPTY_SORTED_MAP);
        conditionFactory.setConditions(MapUtils.EMPTY_SORTED_MAP);
    }

    @Before
    public void setupCommonPrompts() throws PrompterException {

        //resources
        when(prompter.prompt("Add Resource", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("Y").thenReturn("N");
        when(prompter.prompt("Enter Resource Name (leave blank to use namePattern)")).thenReturn(RESOURCE_NAME).thenReturn("");
        when(prompter.prompt("Enter Resource Name Pattern")).thenReturn(RESOURCE_NAME_PATTERN);
        when(prompter.prompt("Enter Resource Type", "download")).thenReturn(DOWNLOAD_TYPE).thenReturn(VELOCITY_TYPE).thenReturn(DOWNLOAD_TYPE);
        when(prompter.prompt("Enter Location (path to resource file)")).thenReturn(RESOURCE_CSS_PATH).thenReturn(RESOURCE_VM_PATH);
        when(prompter.prompt("Add Resource Parameter?",PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("params:\nparamKey->paramVal\nAdd Resource Parameter?",PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("param name")).thenReturn(PARAM_KEY);
        when(prompter.prompt("param value")).thenReturn(PARAM_VAL);

        //context provider
        when(prompter.prompt("Add Velocity Context Provider", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Enter Fully Qualified Context Provider Class", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".web.contextproviders.MyContextProvider")).thenReturn(CUSTOM_CONTEXT_PROVIDER);

        //conditions
        when(prompter.prompt("Add Conditions?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("Condition Type", PluginModulePrompter.ANDOR_ANSWERS, "AND")).thenReturn(CONDITIONS_TYPE);
        when(prompter.prompt("Enter Fully Qualified Condition Class", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".web.condition.MyCondition")).thenReturn(CUSTOM_CONDITION);
        when(prompter.prompt("Add Condition?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Add Condition Parameter?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("params:\nparamKey->paramVal\nAdd Condition Parameter?",PluginModulePrompter.YN_ANSWERS,"N")).thenReturn("N");
        when(prompter.prompt("Invert Condition?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

    }

    protected void assertAdvancedCommonProps() {
        //resources
        List<Resource> resources = props.getResources();
        assertTrue("resources not found", !resources.isEmpty());
        assertEquals("wrong number of resources",2,resources.size());

        Resource cssResource = resources.get(0);
        Resource vmResource = resources.get(1);

        assertEquals("wrong css resource name",RESOURCE_NAME,cssResource.getName());
        assertTrue("css name pattern found when name is set",StringUtils.isBlank(cssResource.getNamePattern()));
        assertEquals("wrong css resource type", DOWNLOAD_TYPE,cssResource.getType());
        assertEquals("wrong css resource location",RESOURCE_CSS_PATH,cssResource.getLocation());
        assertEquals("wrong number of css resource params",1,cssResource.getParams().size());
        assertTrue("css resource param key not found", cssResource.getParams().containsKey(PARAM_KEY));
        assertEquals("wrong css resource param value", PARAM_VAL, cssResource.getParams().get(PARAM_KEY));

        assertTrue("vm name found when name pattern is set",StringUtils.isBlank(vmResource.getName()));
        assertEquals("wrong vm resource name pattern",RESOURCE_NAME_PATTERN,vmResource.getNamePattern());
        assertEquals("wrong vm resource type", VELOCITY_TYPE,vmResource.getType());
        assertEquals("wrong vm resource location", RESOURCE_VM_PATH, vmResource.getLocation());
        assertEquals("wrong number of vm resource params",0,vmResource.getParams().size());

        //context provider (subclasses should check for actual value)
        assertTrue("context provider is blank", StringUtils.isNotBlank(props.getContextProvider()));

        //conditions
        List<Conditional> conditionals = props.getConditions();
        assertEquals("wrong number of conditionals",1,conditionals.size());

        Conditional conditional = conditionals.get(0);
        assertTrue("first conditional is not Conditions",(conditional instanceof Conditions));

        Conditions conditions = (Conditions) conditional;
        assertEquals("wrong conditions type",CONDITIONS_TYPE,conditions.getType());

        List<Conditional> nestedConditions = conditions.getConditions();
        assertEquals("wrong number of nested conditiomals",1,nestedConditions.size());

        Conditional nestedConditional = nestedConditions.get(0);
        assertTrue("nested conditional is not a Condition",(nestedConditional instanceof Condition));

        //subclasses should check actual condition class name

        Condition condition = (Condition) nestedConditional;
        assertEquals("wrong number of condition params",1,condition.getParams().size());
        assertTrue("condition param key not found", condition.getParams().containsKey(PARAM_KEY));
        assertEquals("wrong condition param value", PARAM_VAL, condition.getParams().get(PARAM_KEY));

        assertTrue("condition should be inverted",condition.isInvert());
    }

    protected class TestingContextProviderFactory extends ContextProviderFactory {
        public void setProvidersMap(SortedMap<String, String> map) {
            providers = map;
        }
    }

    protected class TestingConditionFactory extends ConditionFactory {
        public void setConditions(SortedMap<String,String> map) {
            conditions = map;
        }
    }
}
