package com.atlassian.maven.plugins.amps.product;

import java.util.Arrays;
import java.util.Collection;

import com.atlassian.maven.plugins.amps.Bamboo;
import com.atlassian.maven.plugins.amps.Confluence;
import com.atlassian.maven.plugins.amps.Crowd;
import com.atlassian.maven.plugins.amps.Fecru;
import com.atlassian.maven.plugins.amps.Jira;
import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Refapp;
import com.atlassian.maven.plugins.amps.Studio;
import com.atlassian.maven.plugins.amps.StudioBamboo;
import com.atlassian.maven.plugins.amps.StudioConfluence;
import com.atlassian.maven.plugins.amps.StudioCrowd;
import com.atlassian.maven.plugins.amps.StudioFecru;
import com.atlassian.maven.plugins.amps.StudioJira;
import com.atlassian.maven.plugins.amps.product.studio.StudioBambooProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioConfluenceProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioCrowdProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioFeCruProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioJiraProductHandler;
import com.atlassian.maven.plugins.amps.product.studio.StudioProductHandler;

public class ProductHandlerFactory
{
/*    public static final String REFAPP = "refapp";
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
    public static final String STUDIO_CROWD = "studio-crowd";*/



    public static ProductHandler create(String id, MavenContext context, MavenGoals goals)
    {
        if (Refapp.ID.equals(id))
        {
            return new RefappProductHandler(context, goals);
        }
        else if (Confluence.ID.equals(id))
        {
            return new ConfluenceProductHandler(context, goals);
        }
        else if (Jira.ID.equals(id))
        {
            return new JiraProductHandler(context, goals);
        }
        else if (Bamboo.ID.equals(id))
        {
            return new BambooProductHandler(context, goals);
        }
        else if (Fecru.ID.equals(id))
        {
            return new FeCruProductHandler(context, goals);
        }
        else if (Crowd.ID.equals(id))
        {
            return new CrowdProductHandler(context, goals);
        }

        // The Studio product itself
        else if (Studio.ID.equals(id))
        {
            return new StudioProductHandler(context, goals);
        }

        // The Studio products (products which are part of)
        else if (StudioConfluence.ID.equals(id))
        {
            return new StudioConfluenceProductHandler(context, goals);
        }
        else if (StudioJira.ID.equals(id))
        {
            return new StudioJiraProductHandler(context, goals);
        }
        else if (StudioBamboo.ID.equals(id))
        {
            return new StudioBambooProductHandler(context, goals);
        }
        else if (StudioFecru.ID.equals(id))
        {
            return new StudioFeCruProductHandler(context, goals);
        }
        else if (StudioCrowd.ID.equals(id))
        {
            return new StudioCrowdProductHandler(context, goals);
        }


        throw new IllegalArgumentException("Unknown product id: '" + id + "' Valid values: "
            + Arrays.toString(getIds().toArray()));
    }

    public static Collection<String> getIds()
    {
        return Arrays.asList(Refapp.ID, Confluence.ID, Jira.ID, Bamboo.ID, Fecru.ID, Crowd.ID,
                Studio.ID, StudioConfluence.ID, StudioJira.ID, StudioBamboo.ID, StudioFecru.ID, StudioCrowd.ID);
    }
}
