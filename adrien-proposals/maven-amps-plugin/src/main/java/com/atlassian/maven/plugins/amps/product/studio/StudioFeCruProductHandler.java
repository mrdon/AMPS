
package com.atlassian.maven.plugins.amps.product.studio;

import static org.apache.commons.io.FileUtils.copyDirectory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.taskdefs.Java;

import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.StudioFecru;
import com.atlassian.maven.plugins.amps.product.FeCruProductHandler;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils.Replacement;

/**
 * Handler for Studio-FeCru
 * @since 3.6
 */
public class StudioFeCruProductHandler extends FeCruProductHandler implements StudioComponentProductHandler
{

    public StudioFeCruProductHandler(MavenContext context, MavenGoals goals)
    {
        super(context, goals);
    }

    @Override
    public String getId()
    {
        return StudioFecru.ID;
    }

    @Override
    protected ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.studio", "studio-fisheye", "RELEASE");
    }

    @Override
    public Map<String, String> getSystemProperties(Product product)
    {
        Map<String, String> properties = new HashMap<String, String>(super.getSystemProperties(product));

        properties.put("fisheye.dev.mode", "true");

        // We also add common studio system properties
        properties.putAll(product.getStudioProperties().getSystemProperties());


        return properties;
    }

    protected void extractProductHomeData(File productHomeData, File homeDir, Product ctx)
            throws MojoExecutionException
    {
        try
        {
            copyDirectory(productHomeData, homeDir);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(String.format("Can't copy Fisheye's home directory from %s to %s", productHomeData.getAbsolutePath(),
                    homeDir.getAbsolutePath()));
        }
    }

    @Override
    public List<Replacement> getReplacements(Product ctx)
    {
        List<Replacement> replacements = super.getReplacements(ctx);
        replacements.add(new Replacement("%TEST-CONTROL-BIND%", String.valueOf(controlPort(ctx.getHttpPort()))));
        replacements.add(new Replacement("%TEST-HTTP-BIND%", String.valueOf(this.getDefaultHttpPort())));
        return replacements;
    }

    @Override
    protected void customiseInstance(Product ctx, File homeDir, File explodedWarDir) throws MojoExecutionException
    {
        // Note: this config.xml is in the War.
        StudioProductHandler.addProductHandlerOverrides(log, ctx, homeDir, explodedWarDir, "config.xml");
    }

    @Override
    protected void addOverridesToJavaTask(final Product ctx, Java java)
    {
        // Note: FeCru doesn't support asynchronous startup, because setSpawn(true) returns an error
        // (output and timeout are forbidden).
        java.setFork(true);
        java.setDir(getAppDirectory(ctx));
    }
}