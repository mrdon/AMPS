package fake.jar.annotation.parser.modules;

import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;

/**
 * @since 3.5
 */
@JiraPluginModuleCreator
public class JARJiraAnnotatedWithoutInterface {
    public static final String MODULE_NAME = "Not A Plugin Module Creator";

    public String getModuleName() {
        return MODULE_NAME;
    }
}
