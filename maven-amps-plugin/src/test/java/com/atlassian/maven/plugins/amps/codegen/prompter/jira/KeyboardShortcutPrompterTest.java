package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.jira.KeyboardShortcutProperties;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since version
 */
public class KeyboardShortcutPrompterTest extends AbstractPrompterTest {
    public static final String MODULE_NAME = "My KB Shortcut";
    public static final String MODULE_KEY = "my-kb-shortcut";
    public static final String DESCRIPTION = "The My KB Shortcut Plugin";
    public static final String I18N_NAME_KEY = "my-kb-shortcut.name";
    public static final String I18N_DESCRIPTION_KEY = "my-kb-shortcut.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    Prompter prompter;

    @Before
    public void setup() {
        prompter = mock(Prompter.class);
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter Keyboard Shortcut Name", "My Keyboard Shortcut")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Shortcut Character")).thenReturn("m");
        when(prompter.prompt("Choose A Context\n1: global\n2: issueaction\n3: issuenavigation\nChoose a number: ", Arrays.asList("1","2","3"),"")).thenReturn("2");
        when(prompter.prompt("Choose An Operation\n1: click\n2: evaluate\n3: execute\n4: followLink\n5: goTo\n6: moveToAndClick\n7: moveToAndFocus\n8: moveToNextItem\n9: moveToPrevItem\nChoose a number: ", Arrays.asList("1","2","3","4","5","6","7","8","9"),"")).thenReturn("2");
        when(prompter.prompt("Enter Operation Value")).thenReturn("some:selector");

        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        KeyboardShortcutPrompter modulePrompter = new KeyboardShortcutPrompter(prompter);
        KeyboardShortcutProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong shortcut key", "m", props.getShortcut());
        assertEquals("wrong order", "10", props.getOrder());
        assertFalse("wrong hidden value", props.isHidden());
        assertEquals("wrong operation type", "evaluate", props.getOperationType());
        assertEquals("wrong operation value", "some:selector", props.getOperationValue());
        assertEquals("wrong context", "issueaction", props.getContext());

    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter Keyboard Shortcut Name", "My Keyboard Shortcut")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Shortcut Character")).thenReturn("m");
        when(prompter.prompt("Choose A Context\n1: global\n2: issueaction\n3: issuenavigation\nChoose a number: ", Arrays.asList("1","2","3"),"")).thenReturn("2");
        when(prompter.prompt("Choose An Operation\n1: click\n2: evaluate\n3: execute\n4: followLink\n5: goTo\n6: moveToAndClick\n7: moveToAndFocus\n8: moveToNextItem\n9: moveToPrevItem\nChoose a number: ", Arrays.asList("1","2","3","4","5","6","7","8","9"),"")).thenReturn("2");
        when(prompter.prompt("Enter Operation Value")).thenReturn("some:selector");

        when(prompter.prompt("Hidden?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("y");
        when(prompter.prompt("Order","10")).thenReturn("50");
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);

        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        KeyboardShortcutPrompter modulePrompter = new KeyboardShortcutPrompter(prompter);
        KeyboardShortcutProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());

        assertEquals("wrong shortcut key", "m", props.getShortcut());
        assertEquals("wrong order", "50", props.getOrder());
        assertTrue("wrong hidden value", props.isHidden());
        assertEquals("wrong operation type", "evaluate", props.getOperationType());
        assertEquals("wrong operation value", "some:selector", props.getOperationValue());
        assertEquals("wrong context", "issueaction", props.getContext());
    }
}
