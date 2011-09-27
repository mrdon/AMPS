package com.atlassian.maven.plugins.amps.codegen.annotations.asm;

import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompterRegistry;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompterRegistryImpl;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.junit.Before;
import org.junit.Test;

import fake.annotation.parser.creators.NoSuperFakeCreator;
import fake.annotation.parser.creators.ValidFakeCreator;
import fake.annotation.parser.prompters.ValidPrompter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;


/**
 * @since 3.6
 */
public class PrompterAnnotationParserTest
{
    private static final String PROMPTERS_PACKAGE = "fake.annotation.parser.prompters";
    private Log log;
    private Prompter mavenPrompter;
    private PluginModulePrompterRegistry prompterRegistry;
    private ModulePrompterAnnotationParser annotationParser;

    @Before
    public void setup()
    {
        mavenPrompter = mock(Prompter.class);
        log = new SystemStreamLog();

        prompterRegistry = new PluginModulePrompterRegistryImpl();
        annotationParser = new ModulePrompterAnnotationParser(prompterRegistry);
        annotationParser.setLog(log);
        annotationParser.setMavenPrompter(mavenPrompter);
    }

    @Test
    public void validPrompterRegistered() throws Exception
    {
        annotationParser.parse(PROMPTERS_PACKAGE);
        PluginModulePrompter modulePrompter = prompterRegistry.getPrompterForCreatorClass(ValidFakeCreator.class);

        assertEquals("module prompter does not match valid prompter", ValidPrompter.class, modulePrompter.getClass());
    }

    @Test
    public void prompterWithoutSuperclassNotRegistered() throws Exception
    {
        annotationParser.parse(PROMPTERS_PACKAGE);
        PluginModulePrompter modulePrompter = prompterRegistry.getPrompterForCreatorClass(NoSuperFakeCreator.class);

        assertNull("prompter retured but doesn't extend AbstractPrompter", modulePrompter);
    }
}
