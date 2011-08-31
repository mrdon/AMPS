package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.common.AbstractResourcePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.jira.UserFormatModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.UserFormatProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.ArrayList;
import java.util.List;

/**
 * @since version
 */
@ModuleCreatorClass(UserFormatModuleCreator.class)
public class UserFormatPrompter extends AbstractResourcePrompter<UserFormatProperties> {

    public UserFormatPrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public UserFormatProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String className = promptJavaClassname("Enter New Classname", "MyUserFormat");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".jira.userformat");

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        UserFormatProperties props =  new UserFormatProperties(fqClass);

        props.setTypeName(promptNotBlank("Enter Type Name"));
        props.setTypeKey(promptNotBlank("Enter Type i18n Key"));

        List<Resource> resources = new ArrayList<Resource>(1);

        String templatePath = "/templates/userformat/" + props.getModuleKey() + "/";

        Resource view = new Resource();
        view.setName("view");
        view.setType("velocity");
        view.setLocation(templatePath + "view.vm");
        resources.add(view);
        props.setResources(resources);

        return props;
    }

    @Override
    public void promptForAdvancedProperties(UserFormatProperties props, PluginModuleLocation moduleLocation) throws PrompterException {
        props.setResources(promptForResources());
    }

    @Override
    protected Resource promptForResource() throws PrompterException {
        Resource resource = new Resource();
        resource.setName(promptNotBlank("Enter Resource Name"));

        resource.setType("velocity");
        resource.setLocation(promptNotBlank("Enter Location (path to resource file)"));

        return resource;
    }
}