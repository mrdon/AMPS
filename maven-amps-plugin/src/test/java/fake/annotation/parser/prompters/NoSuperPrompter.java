package fake.annotation.parser.prompters;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import fake.annotation.parser.creators.NoSuperFakeCreator;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since version
 */
@ModuleCreatorClass(NoSuperFakeCreator.class)
public class NoSuperPrompter implements PluginModulePrompter {
    @Override
    public PluginModuleProperties getModulePropertiesFromInput() throws PrompterException {
        return null;
    }
}
