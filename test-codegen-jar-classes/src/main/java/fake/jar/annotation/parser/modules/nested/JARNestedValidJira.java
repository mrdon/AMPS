package fake.jar.annotation.parser.modules.nested;

import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

/**
 * @since version
 */
@JiraPluginModuleCreator
public class JARNestedValidJira implements PluginModuleCreator {
    public static final String MODULE_NAME = "Nested Valid Jira Module";

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public void createModule(PluginModuleLocation location, PluginModuleProperties props) throws Exception {

    }
}
