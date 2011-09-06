package com.atlassian.maven.plugins.amps.codegen.prompter.common;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.common.RESTProperties;
import com.sun.tools.javac.comp.Enter;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @since 3.5
 */
public class RESTPrompterTest extends AbstractPrompterTest {
    public static final String PACKAGE = "com.atlassian.plugins.rest";
    public static final String CLASSNAME = "MyRestResource";
    public static final String MODULE_NAME = "My Rest Resource";
    public static final String MODULE_KEY = "my-rest-resource";
    public static final String DESCRIPTION = "The My Rest Resource Plugin";
    public static final String I18N_NAME_KEY = "my-rest-resource.name";
    public static final String I18N_DESCRIPTION_KEY = "my-rest-resource.description";

    public static final String ADV_MODULE_NAME = "My Awesome Plugin";
    public static final String ADV_MODULE_KEY = "awesome-module";
    public static final String ADV_DESCRIPTION = "The Awesomest Plugin Ever";
    public static final String ADV_I18N_NAME_KEY = "awesome-plugin.name";
    public static final String ADV_I18N_DESCRIPTION_KEY = "pluginus-awesomeous.description";

    public static final String PATH = "/helloworld";
    public static final String VERSION = "1.1";

    Prompter prompter;

    @Before
    public void setup() {
        prompter = mock(Prompter.class);
    }

    @Test
    public void basicPropertiesAreValid() throws PrompterException {
        when(prompter.prompt("Enter New Classname", "MyRestResource")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".rest")).thenReturn(PACKAGE);
        when(prompter.prompt("Enter REST Path","/myrestresource")).thenReturn(PATH);
        when(prompter.prompt("Enter Version","1.0")).thenReturn(VERSION);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        RESTPrompter modulePrompter = new RESTPrompter(prompter);
        RESTProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong class", CLASSNAME, props.getClassname());
        assertEquals("wrong class package", PACKAGE, props.getPackage());
        assertEquals("wrong module name", MODULE_NAME, props.getModuleName());
        assertEquals("wrong module key", MODULE_KEY, props.getModuleKey());
        assertEquals("wrong description", DESCRIPTION, props.getDescription());
        assertEquals("wrong i18n name key", I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong i18n desc key", I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong path", PATH, props.getPath());
        assertEquals("wrong version", VERSION, props.getVersion());
    }

    @Test
    public void advancedPropertiesAreValid() throws PrompterException {
        String packageToScan = "com.atlassian.plugins.rest.helloworld";

        when(prompter.prompt("Enter New Classname", "MyRestResource")).thenReturn(CLASSNAME);
        when(prompter.prompt("Enter Package Name", AbstractModulePrompter.DEFAULT_BASE_PACKAGE + ".rest")).thenReturn(PACKAGE);
        when(prompter.prompt("Enter REST Path","/myrestresource")).thenReturn(PATH);
        when(prompter.prompt("Enter Version","1.0")).thenReturn(VERSION);
        when(prompter.prompt("Show Advanced Setup?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");

        when(prompter.prompt("Plugin Name", MODULE_NAME)).thenReturn(ADV_MODULE_NAME);
        when(prompter.prompt("Plugin Key", MODULE_KEY)).thenReturn(ADV_MODULE_KEY);
        when(prompter.prompt("Plugin Description", DESCRIPTION)).thenReturn(ADV_DESCRIPTION);
        when(prompter.prompt("i18n Name Key", I18N_NAME_KEY)).thenReturn(ADV_I18N_NAME_KEY);
        when(prompter.prompt("i18n Description Key", I18N_DESCRIPTION_KEY)).thenReturn(ADV_I18N_DESCRIPTION_KEY);
        when(prompter.prompt("Add Package To Scan?",PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("y");
        when(prompter.prompt("Enter Package")).thenReturn(packageToScan);
        when(prompter.prompt("values:\n" + packageToScan +"\nAdd Package To Scan?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        when(prompter.prompt("Add Dispatcher?",PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("y").thenReturn("N");
        when(prompter.prompt("Choose A Dispatcher\n1: REQUEST\n2: INCLUDE\n3: FORWARD\n4: ERROR\nChoose a number: ", Arrays.asList("1", "2", "3","4"),"1")).thenReturn("3");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        RESTPrompter modulePrompter = new RESTPrompter(prompter);
        RESTProperties props = modulePrompter.getModulePropertiesFromInput(moduleLocation);

        assertEquals("wrong adv class", CLASSNAME, props.getClassname());
        assertEquals("wrong adv package", PACKAGE, props.getPackage());
        assertEquals("wrong adv module name", ADV_MODULE_NAME, props.getModuleName());
        assertEquals("wrong adv module key", ADV_MODULE_KEY, props.getModuleKey());
        assertEquals("wrong adv description", ADV_DESCRIPTION, props.getDescription());
        assertEquals("wrong adv i18n name key", ADV_I18N_NAME_KEY, props.getNameI18nKey());
        assertEquals("wrong adv i18n desc key", ADV_I18N_DESCRIPTION_KEY, props.getDescriptionI18nKey());
        assertEquals("wrong path", PATH, props.getPath());
        assertEquals("wrong version", VERSION, props.getVersion());

        assertEquals("wrong number of packages",1,props.getPackagesToScan().size());
        assertEquals("wrong package",packageToScan,props.getPackagesToScan().get(0));

        assertEquals("wrong number of dispatcher",1,props.getDispatchers().size());
        assertEquals("wrong dispatcher","FORWARD",props.getDispatchers().get(0));
    }
}
