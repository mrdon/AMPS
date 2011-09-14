
package com.atlassian.maven.plugins.amps.product.studio;

import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_BAMBOO;
import static java.lang.String.format;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.product.BambooProductHandler;

public class StudioBambooProductHandler extends BambooProductHandler
{

    public StudioBambooProductHandler(MavenContext context, MavenGoals goals)
    {
        super(context, goals);
    }

    @Override
    public String getId()
    {
        return STUDIO_BAMBOO;
    }

    @Override
    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.studio", "studio-bamboo", "RELEASE");
    }

    @Override
    public void processHomeDirectory(Product ctx, File homeDir) throws MojoExecutionException
    {
        // Don't do anything, StudioBamboo starts with empty home

        StudioProductHandler.processProductsHomeDirectory(log, ctx, homeDir);
    }

    @Override
    protected void addProductHandlerOverrides(Product ctx, File homeDir, File explodedWarDir) throws MojoExecutionException
    {
        StudioProductHandler.addProductHandlerOverrides(log, ctx, homeDir, explodedWarDir);
    }

    @Override
    public List<ProductArtifact> getExtraContainerDependencies()
    {
        return Arrays.asList(
                new ProductArtifact("hsqldb", "hsqldb", "1.8.0.5"),
                new ProductArtifact("jta", "jta", "1.0.1"));
    }

    @Override
    public Map<String, String> getSystemProperties(final Product product)
    {
        Map<String, String> systemProperties = new HashMap<String, String>(super.getSystemProperties(product));

        // This datasource is only used by the Studio version of Confluence:
        final String dburl = System.getProperty("amps.datasource.url",
                format("jdbc:hsqldb:%s/database", StudioProductHandler.fixWindowsSlashes(getHomeDirectory(product).getAbsolutePath())));
        final String driverClass = System.getProperty("amps.datasource.driver", "org.hsqldb.jdbcDriver");
        final String username = System.getProperty("amps.datasource.username", "sa");
        final String password = System.getProperty("amps.datasource.password", "");
        final String datasourceTypeClass = "javax.sql.DataSource";

        final String datasource = format("cargo.datasource.url=%s", dburl);
        final String driver = format("cargo.datasource.driver=%s", driverClass);
        final String datasourceUsername = format("cargo.datasource.username=%s", username);
        final String datasourcePassword = format("cargo.datasource.password=%s", password);
        final String datasourceType = "cargo.datasource.type=" + datasourceTypeClass;
        final String jndi = "cargo.datasource.jndi=jdbc/DefaultDS";

        systemProperties.put("cargo.datasource.datasource",
                format("%s|%s|%s|%s|%s|%s", datasource, driver, datasourceUsername, datasourcePassword, datasourceType, jndi));

        // We also add common studio system properties
        systemProperties.putAll(product.getStudioProperties().getSystemProperties());

        return systemProperties;
    }

}