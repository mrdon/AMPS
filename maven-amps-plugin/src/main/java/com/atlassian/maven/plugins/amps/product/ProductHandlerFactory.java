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
    public static final String REFAPP = "refapp";
    public static final String CONFLUENCE = "confluence";
    public static final String JIRA = "jira";
    public static final String BAMBOO = "bamboo";

    public static ProductHandler create(String id, MavenProject project, MavenGoals goals)
    {
        if (REFAPP.equals(id))
        {
            return new RefappProductHandler(project, goals);
        }
        else if (CONFLUENCE.equals(id))
        {
            return new ConfluenceProductHandler(project, goals);
        }
        else if (JIRA.equals(id))
        {
            return new JiraProductHandler(project, goals);
        }
        else if (BAMBOO.equals(id))
        {
            return new BambooProductHandler(project, goals);
        }

        throw new IllegalArgumentException("Unknown product id:" + id);
    }

    public static Collection<String> getIds()
    {
        return Arrays.asList(REFAPP, CONFLUENCE, JIRA, BAMBOO);
    }
}
