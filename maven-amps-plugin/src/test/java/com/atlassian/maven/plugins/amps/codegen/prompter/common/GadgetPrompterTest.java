package com.atlassian.maven.plugins.amps.codegen.prompter.common;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.common.GadgetProperties;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since version
 */
public class GadgetPrompterTest extends AbstractPrompterTest {
    public static final String MODULE_NAME = "My Gadget";
    public static final String MODULE_KEY = "my-gadget";
    public static final String DESCRIPTION = "The My Gadget Plugin";
    public static final String I18N_NAME_KEY = "my-gadget.name";
    public static final String I18N_DESCRIPTION_KEY = "my-gadget.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    public static final String LOCATION = "gadgets/awesome/gadget.xml";

    Prompter prompter;

    @Before
    public void setup() {
        prompter = mock(Prompter.class);
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter Gadget Name", "My Gadget")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Gadget XML location", "gadgets/" + MODULE_KEY + "/gadget.xml")).thenReturn(LOCATION);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        GadgetPrompter modulePrompter = new GadgetPrompter(prompter);
        GadgetProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong module name", MODULE_NAME, props.getProperty(PluginModuleProperties.MODULE_NAME));
        assertEquals("wrong module key", MODULE_KEY, props.getProperty(PluginModuleProperties.MODULE_KEY));
        assertEquals("wrong description", DESCRIPTION, props.getProperty(PluginModuleProperties.DESCRIPTION));
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));
        assertEquals("wrong location", LOCATION, props.getLocation());
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter Gadget Name", "My Gadget")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Gadget XML location", "gadgets/" + MODULE_KEY + "/gadget.xml")).thenReturn(LOCATION);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Plugin Name", MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        GadgetPrompter modulePrompter = new GadgetPrompter(prompter);
        GadgetProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getProperty(PluginModuleProperties.MODULE_NAME));
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getProperty(PluginModuleProperties.MODULE_KEY));
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getProperty(PluginModuleProperties.DESCRIPTION));
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));
        assertEquals("wrong location", LOCATION, props.getLocation());
    }
}
