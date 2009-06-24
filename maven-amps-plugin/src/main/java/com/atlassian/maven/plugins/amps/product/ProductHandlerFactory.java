package com.atlassian.maven.plugins.amps.product;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import com.atlassian.maven.plugins.amps.MavenGoals;

import java.util.Set;
import java.util.Collections;
import java.util.Arrays;
import java.util.Collection;

public class ProductHandlerFactory
{
    public static ProductHandler create(String id, MavenProject project, MavenGoals goals)
    {
        if ("refapp".equals(id))
        {
            return new RefappProductHandler(project, goals);
        }
        else if ("confluence".equals(id))
        {
            return new ConfluenceProductHandler(project, goals);
        }
        else if ("jira".equals(id))
        {
            return new JiraProductHandler(project, goals);
        }
        else if ("bamboo".equals(id))
        {
            return new BambooProductHandler(project, goals);
        }

        throw new IllegalArgumentException("Unknown product id:" + id);
    }

    public static Collection<String> getIds()
    {
        return Arrays.asList("refapp", "confluence", "jira", "bamboo");
    }
}
