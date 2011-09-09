
package com.atlassian.maven.plugins.amps.product.studio;

import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_FECRU;
import static org.apache.commons.io.FileUtils.copyDirectory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.tools.ant.taskdefs.Java;

import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.product.FeCruProductHandler;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;

public class StudioFeCruProductHandler extends FeCruProductHandler implements StudioComponentProductHandler
{

    public StudioFeCruProductHandler(MavenContext context, MavenGoals goals, Log log)
    {
        super(context, goals, log);
    }

    @Override
    public String getId()
    {
        return STUDIO_FECRU;
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

         // Note: this config.xml is in the Home directory.
            File configXml = new File(homeDir, "config.xml");
            if (StudioProductHandler.checkFileExists(configXml, log))
            {
                // Replace tokens in JIRA import
                ConfigFileUtils.replace(configXml, "%TEST-CONTROL-BIND%", String.valueOf(controlPort(ctx.getHttpPort())));
                ConfigFileUtils.replace(configXml, "%TEST-HTTP-BIND%", String.valueOf(this.getDefaultHttpPort()));
            }
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(String.format("Can't copy Fisheye's home directory from %s to %s", productHomeData.getAbsolutePath(),
                    homeDir.getAbsolutePath()));
        }
        
    }

    @Override
    protected void addProductHandlerOverrides(Product ctx, File homeDir, File explodedWarDir) throws MojoExecutionException
    {
        // Note: this config.xml is in the War.
        StudioProductHandler.addProductHandlerOverrides(log, ctx, homeDir, explodedWarDir, "config.xml");
    }
    
    @Override
    protected void addOverridesToJavaTask(final Product ctx, Java java)
    {
        java.setFork(true);
        java.setDir(getAppDirectory(ctx));
    }
}