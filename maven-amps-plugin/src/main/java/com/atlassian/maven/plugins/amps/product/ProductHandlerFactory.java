package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.product.studio.StudioBambooProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioConfluenceProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioCrowdProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioFeCruProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioJiraProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioProductHandler;

import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.logging.Log;

import java.util.Arrays;
import java.util.Collection;

public class ProductHandlerFactory
{
    public static final String REFAPP = "refapp";
    public static final String CONFLUENCE = "confluence";
    public static final String JIRA = "jira";
    public static final String BAMBOO = "bamboo";
    public static final String FECRU = "fecru";
    public static final String CROWD = "crowd";

    public static final String STUDIO = "studio";
    public static final String STUDIO_CONFLUENCE = "studio-confluence";
    public static final String STUDIO_JIRA = "studio-jira";
    public static final String STUDIO_BAMBOO = "studio-bamboo";
    public static final String STUDIO_FECRU = "studio-fecru";
    public static final String STUDIO_CROWD = "studio-crowd";



    public static ProductHandler create(String id, MavenContext context, MavenGoals goals)
    {
        if (REFAPP.equals(id))
        {
            return new RefappProductHandler(context, goals);
        }
        else if (CONFLUENCE.equals(id))
        {
            return new ConfluenceProductHandler(context, goals);
        }
        else if (JIRA.equals(id))
        {
            return new JiraProductHandler(context, goals);
        }
        else if (BAMBOO.equals(id))
        {
            return new BambooProductHandler(context, goals);
        }
        else if (FECRU.equals(id))
        {
            return new FeCruProductHandler(context, goals);
        }
        else if (CROWD.equals(id))
        {
            return new CrowdProductHandler(context, goals);
        }

        // The Studio product itself
        else if (STUDIO.equals(id))
        {
            return new StudioProductHandler(context, goals);
        }

        // The Studio products (products which are part of)
        else if (STUDIO_CONFLUENCE.equals(id))
        {
            return new StudioConfluenceProductHandler(context, goals);
        }
        else if (STUDIO_JIRA.equals(id))
        {
            return new StudioJiraProductHandler(context, goals);
        }
        else if (STUDIO_BAMBOO.equals(id))
        {
            return new StudioBambooProductHandler(context, goals);
        }
        else if (STUDIO_FECRU.equals(id))
        {
            return new StudioFeCruProductHandler(context, goals);
        }
        else if (STUDIO_CROWD.equals(id))
        {
            return new StudioCrowdProductHandler(context, goals);
        }


        throw new IllegalArgumentException("Unknown product id: '" + id + "' Valid values: "
            + Arrays.toString(getIds().toArray()));
    }

    public static Collection<String> getIds()
    {
        return Arrays.asList(REFAPP, CONFLUENCE, JIRA, BAMBOO, FECRU, CROWD,
                STUDIO, STUDIO_CONFLUENCE, STUDIO_JIRA, STUDIO_BAMBOO, STUDIO_FECRU, STUDIO_CROWD);
    }
}
