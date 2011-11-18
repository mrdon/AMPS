package fake.annotation.parser.prompters;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.codehaus.plexus.components.interactivity.PrompterException;

import fake.annotation.parser.creators.NoSuperFakeCreator;

/**
 * @since 3.6
 */
@ModuleCreatorClass(NoSuperFakeCreator.class)
public class NoSuperPrompter implements PluginModulePrompter<BasicClassModuleProperties>
{

    @Override
    public BasicClassModuleProperties getModulePropertiesFromInput(PluginModuleLocation moduleLocation) throws PrompterException
    {
        return null;
    }

    @Override
    public BasicClassModuleProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        return null;
    }

    @Override
    public void promptForAdvancedProperties(BasicClassModuleProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {

    }

    @Override
    public void setDefaultBasePackage(String basePackage)
    {

    }

    @Override
    public String getDefaultBasePackage()
    {
        return AbstractModulePrompter.DEFAULT_BASE_PACKAGE;
    }
}
