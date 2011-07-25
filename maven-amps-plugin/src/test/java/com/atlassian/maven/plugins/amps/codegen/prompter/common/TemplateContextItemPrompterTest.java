package com.atlassian.maven.plugins.amps.codegen.prompter.common;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.TemplateContextItemProperties;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since version
 */
public class TemplateContextItemPrompterTest extends AbstractPrompterTest {
    public static final String CLASSNAME = "com.atlassian.plugins.MyContextItem";
    public static final String MODULE_NAME = "My Context Item";
    public static final String MODULE_KEY = "my-context-item";
    public static final String DESCRIPTION = "The My Context Item Plugin";
    public static final String I18N_NAME_KEY = "my-context-item.name";
    public static final String I18N_DESCRIPTION_KEY = "my-context-item.description";

    public static final String CONTEXT_KEY = "i18n";
    public static final String COMPONENT_REF = "i18nResolver";

    Prompter prompter;

    @Before
    public void setup() {
        prompter = mock(Prompter.class);
    }

    @Test
    public void nonGlobalComponentRefIsValid() throws PrompterException {
        when(prompter.prompt("Enter Plugin Module Name", "My Context Item")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Context Key")).thenReturn(CONTEXT_KEY);
        when(prompter.prompt("Enter Component-Ref Key (leave blank to specify class)")).thenReturn(COMPONENT_REF);
        when(prompter.prompt("Global Access?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        TemplateContextItemPrompter modulePrompter = new TemplateContextItemPrompter(prompter);
        TemplateContextItemProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong module name", MODULE_NAME, props.getProperty(PluginModuleProperties.MODULE_NAME));
        assertEquals("wrong module key", MODULE_KEY, props.getProperty(PluginModuleProperties.MODULE_KEY));
        assertEquals("wrong description", DESCRIPTION, props.getProperty(PluginModuleProperties.DESCRIPTION));
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));

        assertEquals("wrong context key",CONTEXT_KEY,props.getContextKey());
        assertEquals("wrong component-ref",COMPONENT_REF,props.getComponentRef());
        assertFalse("wong global access", props.isGlobal());
    }

    @Test
    public void globalComponentRefIsValid() throws PrompterException {
        when(prompter.prompt("Enter Plugin Module Name", "My Context Item")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Context Key")).thenReturn(CONTEXT_KEY);
        when(prompter.prompt("Enter Component-Ref Key (leave blank to specify class)")).thenReturn(COMPONENT_REF);
        when(prompter.prompt("Global Access?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        TemplateContextItemPrompter modulePrompter = new TemplateContextItemPrompter(prompter);
        TemplateContextItemProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong module name", MODULE_NAME, props.getProperty(PluginModuleProperties.MODULE_NAME));
        assertEquals("wrong module key", MODULE_KEY, props.getProperty(PluginModuleProperties.MODULE_KEY));
        assertEquals("wrong description", DESCRIPTION, props.getProperty(PluginModuleProperties.DESCRIPTION));
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));

        assertEquals("wrong context key",CONTEXT_KEY,props.getContextKey());
        assertEquals("wrong component-ref",COMPONENT_REF,props.getComponentRef());
        assertTrue("wong global access", props.isGlobal());
    }

    @Test
    public void nonGlobalClassIsValid() throws PrompterException {
        when(prompter.prompt("Enter Plugin Module Name", "My Context Item")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Context Key")).thenReturn(CONTEXT_KEY);
        when(prompter.prompt("Enter Component-Ref Key (leave blank to specify class)")).thenReturn("");
        when(prompter.prompt("Enter Fully Qualified Class Name")).thenReturn(CLASSNAME);
        when(prompter.prompt("Global Access?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        TemplateContextItemPrompter modulePrompter = new TemplateContextItemPrompter(prompter);
        TemplateContextItemProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong module name", MODULE_NAME, props.getProperty(PluginModuleProperties.MODULE_NAME));
        assertEquals("wrong module key", MODULE_KEY, props.getProperty(PluginModuleProperties.MODULE_KEY));
        assertEquals("wrong description", DESCRIPTION, props.getProperty(PluginModuleProperties.DESCRIPTION));
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));

        assertEquals("wrong context key",CONTEXT_KEY,props.getContextKey());
        assertEquals("wrong class",CLASSNAME,props.getFullyQualifiedClassname());
        assertFalse("wong global access", props.isGlobal());
    }

    @Test
    public void globalClassIsValid() throws PrompterException {
        when(prompter.prompt("Enter Plugin Module Name", "My Context Item")).thenReturn(MODULE_NAME);
        when(prompter.prompt("Enter Context Key")).thenReturn(CONTEXT_KEY);
        when(prompter.prompt("Enter Component-Ref Key (leave blank to specify class)")).thenReturn("");
        when(prompter.prompt("Enter Fully Qualified Class Name")).thenReturn(CLASSNAME);
        when(prompter.prompt("Global Access?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        TemplateContextItemPrompter modulePrompter = new TemplateContextItemPrompter(prompter);
        TemplateContextItemProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong module name", MODULE_NAME, props.getProperty(PluginModuleProperties.MODULE_NAME));
        assertEquals("wrong module key", MODULE_KEY, props.getProperty(PluginModuleProperties.MODULE_KEY));
        assertEquals("wrong description", DESCRIPTION, props.getProperty(PluginModuleProperties.DESCRIPTION));
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));

        assertEquals("wrong context key",CONTEXT_KEY,props.getContextKey());
        assertEquals("wrong class",CLASSNAME,props.getFullyQualifiedClassname());
        assertTrue("wong global access", props.isGlobal());
    }
}
