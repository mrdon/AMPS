package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;

import java.util.List;

/**
 * Determines which plugins to provide to the application.  Should take into account default plugins, plugin version
 * overrides, and plugins specified in the configuration.
 */
public interface PluginProvider
{
    List<ProductArtifact> provide(Product product);
}
