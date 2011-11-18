package fake.annotation.parser.modules.dependencies;

import com.atlassian.plugins.codegen.annotations.Dependencies;
import com.atlassian.plugins.codegen.annotations.Dependency;
import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

/**
 * @since 3.6
 */
@JiraPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "javax.servlet", artifactId = "servlet-api", version = "2.4")
})
public class ValidJiraWithMissingScopeDependency implements PluginModuleCreator
{
    public static final String MODULE_NAME = "Valid Jira Module With Missing Scope Dependency";

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
