package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.ConditionFactory;
import com.atlassian.maven.plugins.amps.codegen.ContextProviderFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Condition;
import com.atlassian.plugins.codegen.modules.common.Conditions;
import com.atlassian.plugins.codegen.modules.common.web.AbstractWebFragmentProperties;
import com.atlassian.plugins.codegen.modules.common.web.WebPanelProperties;
import org.apache.commons.collections.MapUtils;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Test;

import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @since version
 */
public class WebPanelPrompterTest extends AbstractWebFragmentPrompterTest<WebPanelProperties> {
    public static final String MODULE_NAME = "My Web Panel";
    public static final String MODULE_KEY = "my-web-panel";
    public static final String DESCRIPTION = "The My Web Panel Plugin";
    public static final String I18N_NAME_KEY = "my-web-panel.name";
    public static final String I18N_DESCRIPTION_KEY = "my-web-panel.description";

    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";
    public static final String CUSTOM_SECTION = "system.admin/globalsettings";
    public static final String WEIGHT = "20";

    @Test
    public void basicPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter Plugin Module Name", "My Web Panel")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Location (e.g. system.admin/globalsettings)")).thenReturn(CUSTOM_SECTION);

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebPanelPrompter modulePrompter = new WebPanelPrompter(prompter);
        setProps((WebPanelProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation));

        assertEquals("wrong module name", MODULE_NAME, props.getProperty(PluginModuleProperties.MODULE_NAME));
        assertEquals("wrong module key", MODULE_KEY, props.getProperty(PluginModuleProperties.MODULE_KEY));
        assertEquals("wrong description", DESCRIPTION, props.getProperty(PluginModuleProperties.DESCRIPTION));
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));
        assertEquals("wrong location", CUSTOM_SECTION, props.getProperty(WebPanelProperties.LOCATION));
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter Plugin Module Name", "My Web Panel")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Location (e.g. system.admin/globalsettings)")).thenReturn(CUSTOM_SECTION);

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);
        when(prompter.prompt("Weight", "1000")).thenReturn(WEIGHT);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebPanelPrompter modulePrompter = new WebPanelPrompter(prompter);
        setProps((WebPanelProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation));

        assertEquals("wrong module name", MODULE_NAME, props.getProperty(PluginModuleProperties.MODULE_NAME));
        assertEquals("wrong module key", ADV_MODULE_KEY, props.getProperty(PluginModuleProperties.MODULE_KEY));
        assertEquals("wrong description", ADV_DESCRIPTION, props.getProperty(PluginModuleProperties.DESCRIPTION));
        assertEquals("wrong i18n name key", ADV_I18N_NAME_KEY, props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
        assertEquals("wrong i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));
        assertEquals("wrong location", CUSTOM_SECTION, props.getProperty(WebPanelProperties.LOCATION));
        assertEquals("wrong weight", WEIGHT, props.getProperty(AbstractWebFragmentProperties.WEIGHT));

        assertAdvancedCommonProps();

        //custom context-provider name check
        assertEquals("wrong context provider", CUSTOM_CONTEXT_PROVIDER, props.getProperty(WebPanelProperties.CONTEXT_PROVIDER));

        //custom condition name check
        Condition condition = (Condition) ((Conditions) props.getConditions().get(0)).getConditions().get(0);
        assertEquals("wrong condition name", CUSTOM_CONDITION, condition.getFullyQualifiedClassName());
    }

    @Test
    public void providerContextFromListIsValid() throws PrompterException {
        SortedMap<String, String> providersMap = new TreeMap<String, String>();
        providersMap.put("HeightContextProvider", "com.atlassian.test.HeightContextProvider");
        providersMap.put("WidthContextProvider", "com.atlassian.test.WidthContextProvider");

        ContextProviderFactory.setProvidersMapForTesting(providersMap);

        when(prompter.prompt("Enter Plugin Module Name", "My Web Panel")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Location (e.g. system.admin/globalsettings)")).thenReturn(CUSTOM_SECTION);

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);
        when(prompter.prompt("Weight", "1000")).thenReturn(WEIGHT);

        when(prompter.prompt("Choose A Context Provider\n1: HeightContextProvider\n2: WidthContextProvider\n3: Custom Context Provider\nChoose a number: ", Arrays.asList("1", "2", "3"), "")).thenReturn("2");

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebPanelPrompter modulePrompter = new WebPanelPrompter(prompter);
        setProps((WebPanelProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation));

        assertEquals("wrong context provider", "com.atlassian.test.WidthContextProvider", props.getProperty(WebPanelProperties.CONTEXT_PROVIDER));

        ContextProviderFactory.setProvidersMapForTesting(MapUtils.EMPTY_SORTED_MAP);
    }

    @Test
    public void conditionFromListIsValid() throws PrompterException {
        SortedMap<String, String> conditionMap = new TreeMap<String, String>();
        conditionMap.put("NoFacialHairCondition", "com.atlassian.test.NoFacialHairCondition");
        conditionMap.put("HasGlobalAdminPermissionCondition", "com.atlassian.test.HasGlobalAdminPermissionCondition");

        ConditionFactory.setConditionsMapForTesting(conditionMap);

        when(prompter.prompt("Enter Plugin Module Name", "My Web Panel")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Location (e.g. system.admin/globalsettings)")).thenReturn(CUSTOM_SECTION);

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);
        when(prompter.prompt("Weight", "1000")).thenReturn(WEIGHT);

        when(prompter.prompt("Choose A Condition\n1: HasGlobalAdminPermissionCondition\n2: NoFacialHairCondition\n3: Custom Condition\nChoose a number: ", Arrays.asList("1", "2", "3"), "")).thenReturn("2");

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebPanelPrompter modulePrompter = new WebPanelPrompter(prompter);
        setProps((WebPanelProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation));

        Condition condition = (Condition) ((Conditions) props.getConditions().get(0)).getConditions().get(0);
        assertEquals("wrong condition name", "com.atlassian.test.NoFacialHairCondition", condition.getFullyQualifiedClassName());

        ConditionFactory.setConditionsMapForTesting(MapUtils.EMPTY_SORTED_MAP);
    }
}
