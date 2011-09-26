package com.atlassian.maven.plugins.amps.codegen.prompter;

import com.atlassian.maven.plugins.amps.codegen.annotations.asm.ModulePrompterAnnotationParser;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.components.interactivity.Prompter;

/**
 * @since 3.5
 */
public class PluginModulePrompterFactoryImpl implements PluginModulePrompterFactory
{
    private final PluginModulePrompterRegistry prompterRegistry;
    private final ModulePrompterAnnotationParser annotationParser;
    private String basePackage;

    //injected by plexus
    private Prompter prompter;

    private Log log;

    public PluginModulePrompterFactoryImpl() throws Exception
    {
        this("");
    }

    public PluginModulePrompterFactoryImpl(String basePackage) throws Exception
    {
        this.prompterRegistry = new PluginModulePrompterRegistryImpl();
        this.annotationParser = new ModulePrompterAnnotationParser(prompterRegistry);
        this.basePackage = basePackage;

    }

    @Override
    public void scanForPrompters() throws Exception
    {
        annotationParser.setLog(getLog());
        annotationParser.setMavenPrompter(prompter);
        doParse(basePackage);
    }

    @Override
    public PluginModulePrompter getPrompterForCreatorClass(Class clazz)
    {
        return prompterRegistry.getPrompterForCreatorClass(clazz);
    }

    private void doParse(String packageName) throws Exception
    {
        if (StringUtils.isBlank(packageName))
        {
            annotationParser.parse();
        } else
        {
            annotationParser.parse(packageName);
        }
    }

    public Log getLog()
    {
        return log;
    }

    public void setLog(Log log)
    {
        this.log = log;
    }
}
