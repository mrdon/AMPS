package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.web.WebPanelRendererProperties;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since version
 */
public class WebPanelRendererPrompterTest extends AbstractPrompterTest {
    public static final String PACKAGE = "com.atlassian.plugins.web";
    public static final String CLASSNAME = "AwesomeWebPanelRenderer";
    public static final String MODULE_NAME = "Awesome Web Panel Renderer";
    public static final String MODULE_KEY = "awesome-web-panel-renderer";
    public static final String DESCRIPTION = "The Awesome Web Panel Renderer Plugin";
    public static final String I18N_NAME_KEY = "awesome-web-panel-renderer.name";
    public static final String I18N_DESCRIPTION_KEY = "awesome-web-panel-renderer.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "Renders Panels Just Like Bacon!";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    Prompter prompter;

    @Before
    public void setup() {
        prompter = mock(Prompter.class);
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter New Classname", "MyWebPanelRenderer")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", "com.atlassian.plugins.web")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebPanelRendererPrompter modulePrompter = new WebPanelRendererPrompter(prompter);
        WebPanelRendererProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

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
        when(prompter.prompt("Enter New Classname", "MyWebPanelRenderer")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", "com.atlassian.plugins.web")).thenReturn(PACKAGE);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Plugin Name", MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        WebPanelRendererPrompter modulePrompter = new WebPanelRendererPrompter(prompter);
        WebPanelRendererProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv class", CLASSNAME, props.getClassname());
        assertEquals("wrong adv package", PACKAGE, props.getPackage());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
    }
}
