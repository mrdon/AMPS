
package com.atlassian.maven.plugins.amps.product.studio;

import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_JIRA;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.product.JiraProductHandler;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;

public class StudioJiraProductHandler extends JiraProductHandler implements StudioComponentProductHandler
{
    private final Log log;

    public StudioJiraProductHandler(final MavenContext context, final MavenGoals goals, Log log)
    {
        super(context, goals, log);
        this.log = log;
    }
    

    @Override
    public String getId()
    {
        return STUDIO_JIRA;
    }
    
    @Override
    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.studio", "studio-jira", "RELEASE");
    }
    
    @Override
    protected void addProductHandlerOverrides(Product ctx, File homeDir, File explodedWarDir) throws MojoExecutionException
    {

        // change database to hsql
        File entityEngine = new File(explodedWarDir, "WEB-INF/classes/entityengine.xml");
        File webDotXml = new File(explodedWarDir, "WEB-INF/web.xml");
        if (StudioProductHandler.checkFileExists(entityEngine, log) && StudioProductHandler.checkFileExists(webDotXml, log))
        {
            // Replace tokens in JIRA import
            ConfigFileUtils.replace(entityEngine, "field-type-name=\"postgres72\"", "field-type-name=\"hsql\"");
            ConfigFileUtils.replace(entityEngine, "schema-name=\"public\"", "schema-name=\"PUBLIC\"");

            // Replace tokens in context.xml
            ConfigFileUtils.replace(webDotXml, "%JIRA-HOME%", homeDir.getAbsolutePath());

            // allow xml restore
            // Since the patch from JRA-21004 has been applied, we need to set jira.paths.set.allowed to true so that xml
            // restore is enabled.
            // TODO should check to see if the property exists and replace if it does

            /*
             * ArtifactVersion jiraArtifactVersion = new DefaultArtifactVersion(jiraVersion);
             * if (jiraArtifactVersion.compareTo(JIRA_4_1) < 0)
             * {
             * processGappsBundledPlugins(jiraWar, "WEB-INF/classes/com/atlassian/jira/plugin/atlassian-bundled-plugins.zip")
             * }
             * else
             * {
             * processGappsBundledPlugins(jiraWar, "WEB-INF/classes/atlassian-bundled-plugins.zip")
             * }
             */
        }
        StudioProductHandler.addProductHandlerOverrides(log, ctx, homeDir, explodedWarDir);
    }

    @Override
    public void processHomeDirectory(Product ctx, File homeDir) throws MojoExecutionException
    {
        // Do nothing, the home directory for JIRA in Studio is empty
    }

    @Override
    public Map<String, String> getSystemProperties(Product product)
    {
        Map<String, String> properties = new HashMap<String, String>(super.getSystemProperties(product));

        // We also add common studio system properties
        properties.putAll(product.getStudioProperties().getSystemProperties());

        return properties;
    }



}