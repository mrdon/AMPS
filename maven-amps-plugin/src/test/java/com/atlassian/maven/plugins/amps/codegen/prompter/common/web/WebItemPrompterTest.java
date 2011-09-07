package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.ConditionFactory;
import com.atlassian.maven.plugins.amps.codegen.ContextProviderFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.common.Condition;
import com.atlassian.plugins.codegen.modules.common.Conditions;
import com.atlassian.plugins.codegen.modules.common.web.WebItemProperties;
import org.apache.commons.collections.MapUtils;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @since 3.5
 */
public class WebItemPrompterTest extends AbstractWebFragmentPrompterTest<WebItemProperties> {
    public static final String MODULE_NAME = "My Web Item";
    public static final String MODULE_KEY = "my-web-item";
    public static final String LINK_ID = "item-link";
    public static final String DESCRIPTION = "The My Web Item Plugin";
    public static final String I18N_NAME_KEY = "my-web-item.name";
    public static final String I18N_DESCRIPTION_KEY = "my-web-item.description";

    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";
    public static final String SYSTEM_ADMIN_SETTINGS = "system.admin/settings";
    public static final String LINK_URL = "/secure/MyAction.jspa";
    public static final String WEIGHT = "20";
    public static final String LABEL_KEY = "item.label";
    public static final String LABEL_VALUE = "this is my label";
    public static final String LABEL_PARAM = "label param";
    public static final String ICON_PATH = "/images/icon.png";
    public static final String ICON_WIDTH = "32";
    public static final String ICON_HEIGHT = "20";
    public static final String TOOLTIP_KEY = "item.toolip";
    public static final String TOOLTIP_VALUE = "this is a tooltip";

    @Test
    public void basicPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter Plugin Module Name", "My Web Item")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Section (e.g. system.admin/globalsettings)")).thenReturn(SYSTEM_ADMIN_SETTINGS);
        when(prompter.prompt("Enter Link URL (e.g. /secure/CreateIssue!default.jspa)")).thenReturn(LINK_URL);

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebItemPrompter modulePrompter = new WebItemPrompter(prompter);
        setProps((WebItemProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation));

        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong section", SYSTEM_ADMIN_SETTINGS, props.getSection());
        assertEquals("wrong link", LINK_URL, props.getLink().getValue());
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter Plugin Module Name", "My Web Item")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Section (e.g. system.admin/globalsettings)")).thenReturn(SYSTEM_ADMIN_SETTINGS);
        when(prompter.prompt("Enter Link URL (e.g. /secure/CreateIssue!default.jspa)")).thenReturn(LINK_URL);

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);
        when(prompter.prompt("Weight", "1000")).thenReturn(WEIGHT);
        when(prompter.prompt("Link Id", "my-web-item-link")).thenReturn(LINK_ID);
        when(prompter.prompt("Enter Label Key", "my-web-item.label")).thenReturn(LABEL_KEY);
        when(prompter.prompt("Enter Label Value", "My Web Item")).thenReturn(LABEL_VALUE);
        when(prompter.prompt("Add Label Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("values:\nlabel param\nAdd Label Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Enter Param Value")).thenReturn(LABEL_PARAM);
        when(prompter.prompt("Add Icon?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("Icon Location (e.g. /images/icons/print.gif)")).thenReturn(ICON_PATH);
        when(prompter.prompt("Icon Width", "16")).thenReturn(ICON_WIDTH);
        when(prompter.prompt("Icon Height", "16")).thenReturn(ICON_HEIGHT);
        when(prompter.prompt("Add Tooltip?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("Enter Tooltip Key", "awesome-module.tooltip")).thenReturn(TOOLTIP_KEY);
        when(prompter.prompt("Enter Tooltip Value", "My Web Item Tooltip")).thenReturn(TOOLTIP_VALUE);
        when(prompter.prompt("Add Tooltip Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Add Plugin Module Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("params:\nparamKey->paramVal\nAdd Plugin Module Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");


        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebItemPrompter modulePrompter = new WebItemPrompter(prompter);
        setProps((WebItemProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation));

        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong section", SYSTEM_ADMIN_SETTINGS, props.getSection());
        assertEquals("wrong link", LINK_URL, props.getLink().getValue());
        assertEquals("wrong weight", WEIGHT, props.getWeight());

        assertAdvancedCommonProps();

        //custom context-provider name check
        assertEquals("wrong context provider", CUSTOM_CONTEXT_PROVIDER, props.getContextProvider());

        //custom condition name check
        Condition condition = (Condition) ((Conditions) props.getConditions().get(0)).getConditions().get(0);
        assertEquals("wrong condition name", CUSTOM_CONDITION, condition.getFullyQualifiedClassName());
    }

    @Test
    public void providerContextFromListIsValid() throws PrompterException {
        SortedMap<String, String> providersMap = new TreeMap<String, String>();
        providersMap.put("HeightContextProvider", "com.atlassian.test.HeightContextPRovider");
        providersMap.put("WidthContextProvider", "com.atlassian.test.WidthContextProvider");

        contextProviderFactory.setProvidersMap(providersMap);

        when(prompter.prompt("Enter Plugin Module Name", "My Web Item")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Section (e.g. system.admin/globalsettings)")).thenReturn(SYSTEM_ADMIN_SETTINGS);
        when(prompter.prompt("Enter Link URL (e.g. /secure/CreateIssue!default.jspa)")).thenReturn(LINK_URL);

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);
        when(prompter.prompt("Weight", "1000")).thenReturn(WEIGHT);
        when(prompter.prompt("Link Id", "my-web-item-link")).thenReturn(LINK_ID);
        when(prompter.prompt("Enter Label Key", "my-web-item.label")).thenReturn(LABEL_KEY);
        when(prompter.prompt("Enter Label Value", "My Web Item")).thenReturn(LABEL_VALUE);
        when(prompter.prompt("Add Label Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("values:\nlabel param\nAdd Label Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Enter Param Value")).thenReturn(LABEL_PARAM);
        when(prompter.prompt("Add Icon?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("Icon Location (e.g. /images/icons/print.gif)")).thenReturn(ICON_PATH);
        when(prompter.prompt("Icon Width", "16")).thenReturn(ICON_WIDTH);
        when(prompter.prompt("Icon Height", "16")).thenReturn(ICON_HEIGHT);
        when(prompter.prompt("Add Tooltip?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("Enter Tooltip Key", "awesome-module.tooltip")).thenReturn(TOOLTIP_KEY);
        when(prompter.prompt("Enter Tooltip Value", "My Web Item Tooltip")).thenReturn(TOOLTIP_VALUE);
        when(prompter.prompt("Add Tooltip Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Add Plugin Module Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("params:\nparamKey->paramVal\nAdd Plugin Module Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        when(prompter.prompt("Choose A Context Provider\n1: HeightContextProvider\n2: WidthContextProvider\n3: Custom Context Provider\nChoose a number: ", Arrays.asList("1", "2", "3"), "")).thenReturn("2");

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebItemPrompter modulePrompter = new WebItemPrompter(prompter);
        setProps((WebItemProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation));

        assertEquals("wrong context provider", "com.atlassian.test.WidthContextProvider", props.getContextProvider());
    }

    @Test
    public void conditionFromListIsValid() throws PrompterException {
        SortedMap<String, String> conditionMap = new TreeMap<String, String>();
        conditionMap.put("NoFacialHairCondition", "com.atlassian.test.NoFacialHairCondition");
        conditionMap.put("HasGlobalAdminPermissionCondition", "com.atlassian.test.HasGlobalAdminPermissionCondition");

        conditionFactory.setConditions(conditionMap);

        when(prompter.prompt("Enter Plugin Module Name", "My Web Item")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Section (e.g. system.admin/globalsettings)")).thenReturn(SYSTEM_ADMIN_SETTINGS);
        when(prompter.prompt("Enter Link URL (e.g. /secure/CreateIssue!default.jspa)")).thenReturn(LINK_URL);

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);
        when(prompter.prompt("Weight", "1000")).thenReturn(WEIGHT);
        when(prompter.prompt("Link Id", "my-web-item-link")).thenReturn(LINK_ID);
        when(prompter.prompt("Enter Label Key", "my-web-item.label")).thenReturn(LABEL_KEY);
        when(prompter.prompt("Enter Label Value", "My Web Item")).thenReturn(LABEL_VALUE);
        when(prompter.prompt("Add Label Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("values:\nlabel param\nAdd Label Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Enter Param Value")).thenReturn(LABEL_PARAM);
        when(prompter.prompt("Add Icon?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("Icon Location (e.g. /images/icons/print.gif)")).thenReturn(ICON_PATH);
        when(prompter.prompt("Icon Width", "16")).thenReturn(ICON_WIDTH);
        when(prompter.prompt("Icon Height", "16")).thenReturn(ICON_HEIGHT);
        when(prompter.prompt("Add Tooltip?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("Enter Tooltip Key", "awesome-module.tooltip")).thenReturn(TOOLTIP_KEY);
        when(prompter.prompt("Enter Tooltip Value", "My Web Item Tooltip")).thenReturn(TOOLTIP_VALUE);
        when(prompter.prompt("Add Tooltip Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Add Plugin Module Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y").thenReturn("N");
        when(prompter.prompt("params:\nparamKey->paramVal\nAdd Plugin Module Param?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        when(prompter.prompt("Choose A Condition\n1: HasGlobalAdminPermissionCondition\n2: NoFacialHairCondition\n3: Custom Condition\nChoose a number: ", Arrays.asList("1", "2", "3"), "")).thenReturn("2");

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebItemPrompter modulePrompter = new WebItemPrompter(prompter);
        setProps((WebItemProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation));

        Condition condition = (Condition) ((Conditions) props.getConditions().get(0)).getConditions().get(0);
        assertEquals("wrong condition name", "com.atlassian.test.NoFacialHairCondition", condition.getFullyQualifiedClassName());

    }

}
