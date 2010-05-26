package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractPluginProvider implements PluginProvider
{
    public final List<ProductArtifact> provide(Product product)
    {
        final List<ProductArtifact> artifacts = new ArrayList<ProductArtifact>();
        artifacts.addAll(product.getPluginArtifacts());

        if (product.getSalVersion() != null)
        {
            artifacts.addAll(getSalArtifacts(product.getSalVersion()));
        }

        if (product.getPdkVersion() != null)
        {
            artifacts.addAll(getPdkInstallArtifacts(product.getPdkVersion()));
        }

        if (product.getRestVersion() != null)
        {
            artifacts.addAll(getRestArtifacts(product.getRestVersion()));
        }

        if (product.getWebConsoleVersion() != null)
        {
            artifacts.addAll(getWebConsoleArtifacts(product.getWebConsoleVersion()));
        }

        return artifacts;
    }

    protected abstract Collection<ProductArtifact> getSalArtifacts(String salVersion);

    protected Collection<ProductArtifact> getPdkInstallArtifacts(String pdkInstallVersion)
    {
        return Collections.singletonList(new ProductArtifact("com.atlassian.pdkinstall", "pdkinstall-plugin", pdkInstallVersion));
    }

    protected Collection<ProductArtifact> getWebConsoleArtifacts(String webConsoleVersion)
    {
        return Arrays.asList(
                new ProductArtifact("org.apache.felix", "org.apache.felix.webconsole", webConsoleVersion),
                new ProductArtifact("org.apache.felix", "org.osgi.compendium", "1.2.0"),
                new ProductArtifact("com.atlassian.labs.httpservice", "httpservice-bridge", "0.6.1")
                );
    }

    protected Collection<ProductArtifact> getRestArtifacts(String restVersion)
    {
        return Collections.singletonList(new ProductArtifact("com.atlassian.plugins.rest", "atlassian-rest-module", restVersion));
    }
}
