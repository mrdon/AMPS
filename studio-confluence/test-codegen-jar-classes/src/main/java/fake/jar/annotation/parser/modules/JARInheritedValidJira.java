package fake.jar.annotation.parser.modules;

import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;

/**
 * @since 3.5
 */
@JiraPluginModuleCreator
public class JARInheritedValidJira extends JARValidJiraModuleCreator {
    public static final String MODULE_NAME = "Inherited Valid Jira";

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
