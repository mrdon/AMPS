package fake.annotation.parser.modules;

import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;

/**
 * @since version
 */
@JiraPluginModuleCreator
public class InheritedValidJira extends ValidJiraModuleCreator {
    public static final String MODULE_NAME = "Inherited Valid Jira";

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
