package fake.annotation.parser.modules.dependencies;

import com.atlassian.plugins.codegen.annotations.Dependencies;
import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

/**
 * @since 3.5
 */
@JiraPluginModuleCreator
@Dependencies({})
public class ValidJiraWithMissingNestedDependency implements PluginModuleCreator
{
    public static final String MODULE_NAME = "Valid Jira Module With Missing Nested Dependency";

    @Override
    public String getModuleName()
    {
        return MODULE_NAME;
    }

    @Override
    public void createModule(PluginModuleLocation location, PluginModuleProperties props) throws Exception
    {

    }
}
