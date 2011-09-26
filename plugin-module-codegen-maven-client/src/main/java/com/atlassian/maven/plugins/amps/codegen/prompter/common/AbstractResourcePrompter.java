package com.atlassian.maven.plugins.amps.codegen.prompter.common;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
public abstract class AbstractResourcePrompter<T extends PluginModuleProperties> extends AbstractModulePrompter<T>
{

    public AbstractResourcePrompter(Prompter prompter)
    {
        super(prompter);
    }

    protected List<Resource> promptForResources() throws PrompterException
    {
        List<Resource> resources = new ArrayList<Resource>();
        promptForResources(resources);
        return resources;
    }

    protected void promptForResources(List<Resource> resources) throws PrompterException
    {
        if (promptForBoolean("Add Resource", "N"))
        {
            resources.add(promptForResource());
            promptForResources(resources);
        }
    }

    protected Resource promptForResource() throws PrompterException
    {
        Resource resource = new Resource();
        promptForResourceNameOrPattern(resource);

        resource.setType(promptNotBlank("Enter Resource Type", "download"));
        resource.setLocation(promptNotBlank("Enter Location (path to resource file)"));

        resource.setParams(promptForParams("Add Resource Parameter?"));

        return resource;
    }

    protected void promptForResourceNameOrPattern(Resource resource) throws PrompterException
    {
        String name = prompt("Enter Resource Name (leave blank to use namePattern)");
        if (StringUtils.isNotBlank(name))
        {
            resource.setName(name);
        } else
        {
            String namePattern = prompt("Enter Resource Name Pattern");
            if (StringUtils.isNotBlank(namePattern))
            {
                resource.setNamePattern(namePattern);
            } else
            {
                promptForResourceNameOrPattern(resource);
            }
        }
    }

}
