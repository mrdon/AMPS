
package com.atlassian.maven.plugins.amps.product.studio;

import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_CROWD;
import static java.lang.String.format;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.product.CrowdProductHandler;

public class StudioCrowdProductHandler extends CrowdProductHandler implements StudioComponentProductHandler
{
    public StudioCrowdProductHandler(final MavenContext context, final MavenGoals goals)
    {
        super(context, goals);
    }

    @Override
    public String getId()
    {
        return STUDIO_CROWD;
    }

    @Override
    public ProductArtifact getArtifact()
    {
        // We return the default artifact. May be overridden by the pom.
        return new ProductArtifact("com.atlassian.studio", "studio-crowd", "RELEASE");
    }

    @Override
    public void processHomeDirectory(Product ctx, File homeDir) throws MojoExecutionException
    {
        super.processHomeDirectory(ctx, homeDir);
        StudioProductHandler.processProductsHomeDirectory(log, ctx, homeDir);

    }

    @Override
    protected void addProductHandlerOverrides(Product ctx, File homeDir, File explodedWarDir) throws MojoExecutionException
    {
        StudioProductHandler.addProductHandlerOverrides(log, ctx, homeDir, explodedWarDir);
    }

    @Override
    public Map<String, String> getSystemProperties(Product product)
    {
        Map<String, String> systemProperties = new HashMap<String, String>(super.getSystemProperties(product));
        final String dbUrl = format("jdbc:hsqldb:%s/database", getHomeDirectory(product).getPath());
        final String driverClass = "org.hsqldb.jdbcDriver";
        final String username = "sa";
        final String password = "";
        final String datasourceTypeClass = "javax.sql.DataSource";

        final String datasource = format("cargo.datasource.url=%s", dbUrl);
        final String driver = format("cargo.datasource.driver=%s", driverClass);
        final String datasourceUsername = format("cargo.datasource.username=%s", username);
        final String datasourcePassword = format("cargo.datasource.password=%s", password);
        final String datasourceType = format("cargo.datasource.type=%s", datasourceTypeClass);
        final String jndi = "cargo.datasource.jndi=jdbc/DefaultDS";

        systemProperties.put("cargo.datasource.datasource",
                format("%s|%s|%s|%s|%s|%s", datasource, driver, datasourceUsername, datasourcePassword, datasourceType, jndi));

        // We also add common studio system properties
        systemProperties.putAll(product.getStudioProperties().getSystemProperties());

        return systemProperties;
    }

}