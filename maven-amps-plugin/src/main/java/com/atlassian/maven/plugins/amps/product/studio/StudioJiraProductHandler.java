
package com.atlassian.maven.plugins.amps.product.studio;

import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_JIRA;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.product.JiraProductHandler;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils.Replacement;
import com.google.common.collect.Lists;


public class StudioJiraProductHandler extends JiraProductHandler implements StudioComponentProductHandler
{
    public StudioJiraProductHandler(final MavenContext context, final MavenGoals goals)
    {
        super(context, goals);
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
        List<File> configFiles = Lists.newArrayList();
        configFiles.add(new File(explodedWarDir, "WEB-INF/classes/entityengine.xml"));
        configFiles.add(new File(explodedWarDir, "WEB-INF/web.xml"));

        List<Replacement> replacements = Lists.newArrayList();
        replacements.add(new Replacement("field-type-name=\"postgres72\"", "field-type-name=\"hsql\"", false));
        replacements.add(new Replacement("schema-name=\"public\"", "schema-name=\"PUBLIC\"", false));
        replacements.add(new Replacement("%JIRA-HOME%", homeDir.getAbsolutePath()));

        ConfigFileUtils.replace(configFiles, replacements, false, log);

        StudioProductHandler.addProductHandlerOverrides(log, ctx, homeDir, explodedWarDir);

        // JIRA needs a bit more PermGen - default is -Xmx512m -XX:MaxPermSize=160m
        if (ctx.getJvmArgs() == null)
        {
            ctx.setJvmArgs("-Xms256m -Xmx768m -XX:MaxPermSize=512m");
        }
        else
        {
            ctx.setJvmArgs(ctx.getJvmArgs() + " -Xms256m -Xmx768m -XX:MaxPermSize=512m");
        }
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