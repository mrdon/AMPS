package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.common.AbstractResourcePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.jira.SearchRequestViewModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.SearchRequestViewProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.6
 */
@ModuleCreatorClass(SearchRequestViewModuleCreator.class)
public class SearchRequestViewPrompter extends AbstractResourcePrompter<SearchRequestViewProperties>
{

    public SearchRequestViewPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public SearchRequestViewProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        String className = promptJavaClassname("Enter New Classname", "MySearchRequestView");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".jira.search");

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        SearchRequestViewProperties props = new SearchRequestViewProperties(fqClass);

        props.setFileExtension(promptNotBlank("Enter File Extension (i.e. html)"));
        props.setContentType(promptNotBlank("Enter Content Type (i.e. text/html)"));

        List<Resource> resources = new ArrayList<Resource>(3);

        String templatePath = "/templates/search/" + props.getModuleKey() + "/";

        Resource header = new Resource();
        header.setName("header");
        header.setType("velocity");
        header.setLocation(templatePath + "header.vm");

        Resource single = new Resource();
        single.setName("singleissue");
        single.setType("velocity");
        single.setLocation(templatePath + "singleissue.vm");

        Resource footer = new Resource();
        footer.setName("footer");
        footer.setType("velocity");
        footer.setLocation(templatePath + "footer.vm");

        resources.add(header);
        resources.add(single);
        resources.add(footer);

        props.setResources(resources);

        return props;
    }

    @Override
    public void promptForAdvancedProperties(SearchRequestViewProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {
        props.setOrder(promptForInt("Enter Order", 10));
        props.setResources(promptForResources());
    }

    @Override
    protected Resource promptForResource() throws PrompterException
    {
        Resource resource = new Resource();
        resource.setName(promptNotBlank("Enter Resource Name"));

        resource.setType("velocity");
        resource.setLocation(promptNotBlank("Enter Location (path to resource file)"));

        return resource;
    }
}