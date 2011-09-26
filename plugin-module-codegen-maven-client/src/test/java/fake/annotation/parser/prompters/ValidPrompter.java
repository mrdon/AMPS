package fake.annotation.parser.prompters;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import fake.annotation.parser.creators.ValidFakeCreator;

/**
 * @since 3.5
 */
@ModuleCreatorClass(ValidFakeCreator.class)
public class ValidPrompter extends AbstractModulePrompter<BasicClassModuleProperties>
{

    public ValidPrompter(Prompter prompter)
    {
        super(prompter);
    }

    @Override
    public BasicClassModuleProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
