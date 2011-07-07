package fake.annotation.parser.prompters;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import fake.annotation.parser.creators.ValidFakeCreator;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since version
 */
@ModuleCreatorClass(ValidFakeCreator.class)
public class ValidPrompter extends AbstractModulePrompter {

    public ValidPrompter(Prompter prompter) {
        super(prompter);
    }

    @Override
    public PluginModuleProperties getModulePropertiesFromInput() throws PrompterException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
